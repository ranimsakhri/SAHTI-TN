package tn.sahti.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SahtiApp() }
    }
}

@Composable
private fun SahtiApp() {
    val context = LocalContext.current.applicationContext
    val api = remember { ApiFactory.create(context) }
    var token by remember { mutableStateOf(ApiFactory.token(context)) }
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            if (token.isNullOrBlank()) {
                SignInScreen(api) { auth ->
                    ApiFactory.saveToken(context, auth.token)
                    token = auth.token
                }
            } else {
                JournalScreen(api) {
                    ApiFactory.clearToken(context)
                    token = null
                }
            }
        }
    }
}

@Composable
private fun SignInScreen(api: SahtiApi, onAuthenticated: (AuthResponse) -> Unit) {
    val scope = rememberCoroutineScope()
    var registering by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text("SA7TI", style = MaterialTheme.typography.headlineLarge)
        Text("Votre suivi nutritionnel et sportif")
        Spacer(Modifier.height(20.dp))
        if (registering) {
            OutlinedTextField(firstName, { firstName = it }, label = { Text("Prénom") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(lastName, { lastName = it }, label = { Text("Nom") }, modifier = Modifier.fillMaxWidth())
        }
        OutlinedTextField(email, { email = it }, label = { Text("E-mail") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            password,
            { password = it },
            label = { Text("Mot de passe") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
        )
        if (error.isNotBlank()) Text(error, color = MaterialTheme.colorScheme.error)
        Button(
            enabled = !loading,
            onClick = {
                loading = true
                error = ""
                scope.launch {
                    try {
                        val auth = if (registering) {
                            api.register(RegisterRequest(lastName.trim(), firstName.trim(), email.trim(), password, password))
                        } else {
                            api.login(AuthRequest(email.trim(), password))
                        }
                        onAuthenticated(auth)
                    } catch (exception: Exception) {
                        error = exception.userMessage()
                    } finally {
                        loading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (loading) CircularProgressIndicator()
            else Text(if (registering) "Créer mon compte" else "Se connecter")
        }
        Button(onClick = { registering = !registering; error = "" }, modifier = Modifier.fillMaxWidth()) {
            Text(if (registering) "J’ai déjà un compte" else "Créer un compte")
        }
        Text("Les données sont associées à votre compte SA7TI.")
    }
}

@Composable
private fun JournalScreen(api: SahtiApi, onLogout: () -> Unit) {
    val scope = rememberCoroutineScope()
    var date by remember { mutableStateOf(java.time.LocalDate.now().toString()) }
    var foods by remember { mutableStateOf(emptyList<Food>()) }
    var meals by remember { mutableStateOf(emptyList<MealEntry>()) }
    var activities by remember { mutableStateOf(emptyList<ActivityEntry>()) }
    var progress by remember { mutableStateOf<DailyProgress?>(null) }
    var profile by remember { mutableStateOf<ProfileResponse?>(null) }
    var selectedFood by remember { mutableStateOf<Food?>(null) }
    var quantity by remember { mutableStateOf("100") }
    var mealType by remember { mutableStateOf("DEJEUNER") }
    var activityType by remember { mutableStateOf("Marche") }
    var duration by remember { mutableStateOf("30") }
    var intensity by remember { mutableStateOf("MODEREE") }
    var search by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(true) }
    var showProfile by remember { mutableStateOf(false) }
    var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("FEMME") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("MAINTENIR") }
    var activityLevel by remember { mutableStateOf("SEDENTAIRE") }
    var preferences by remember { mutableStateOf("") }
    var allergies by remember { mutableStateOf("") }

    suspend fun refresh() {
        loading = true
        try {
            foods = api.foods(search.takeIf { it.isNotBlank() })
            meals = api.meals(date)
            activities = api.activities(date)
            progress = api.progress(date)
            if (profile == null) {
                profile = api.getProfile()
                profile?.let {
                    birthDate = it.birthDate.orEmpty()
                    gender = it.gender ?: gender
                    height = it.heightCm?.toString().orEmpty()
                    weight = it.weightKg?.toString().orEmpty()
                    goal = it.goal ?: goal
                    activityLevel = it.activityLevel ?: activityLevel
                    preferences = it.dietaryPreferences.orEmpty()
                    allergies = it.allergies.orEmpty()
                }
            }
            error = ""
        } catch (exception: Exception) {
            error = exception.userMessage()
        } finally {
            loading = false
        }
    }

    LaunchedEffect(date) { refresh() }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Mon suivi SA7TI", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = onLogout) { Text("Quitter") }
        }
        OutlinedTextField(date, { date = it }, label = { Text("Date (AAAA-MM-JJ)") }, modifier = Modifier.fillMaxWidth())
        if (loading) CircularProgressIndicator()
        progress?.let {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Progression · ${it.date}", style = MaterialTheme.typography.titleMedium)
                    Text("${it.caloriesEstimated.toInt()} kcal estimées · ${it.mealEntries} aliments")
                    Text("${it.activeMinutes} minutes d’activité · ${it.activities} séances")
                }
            }
        }
        Button(onClick = { showProfile = !showProfile }, modifier = Modifier.fillMaxWidth()) {
            Text(if (showProfile) "Masquer mon profil" else "Mon profil et mes objectifs")
        }
        if (showProfile) {
            OutlinedTextField(birthDate, { birthDate = it }, label = { Text("Date de naissance (AAAA-MM-JJ)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(gender, { gender = it }, label = { Text("Genre (FEMME, HOMME, AUTRE)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(height, { height = it }, label = { Text("Taille en cm") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(weight, { weight = it }, label = { Text("Poids en kg") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(goal, { goal = it }, label = { Text("Objectif (PERDRE, MAINTENIR, PRENDRE)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(activityLevel, { activityLevel = it }, label = { Text("Activité (SEDENTAIRE, LEGER, MODERE, INTENSE, TRES_INTENSE)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(preferences, { preferences = it }, label = { Text("Préférences alimentaires") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(allergies, { allergies = it }, label = { Text("Allergies / intolérances") }, modifier = Modifier.fillMaxWidth())
            Button(onClick = {
                scope.launch {
                    try {
                        profile = api.saveProfile(
                            ProfileRequest(
                                gender,
                                birthDate,
                                height.toDouble(),
                                weight.toDouble(),
                                goal,
                                activityLevel,
                                preferences,
                                allergies,
                            ),
                        )
                        error = "Profil enregistré."
                    } catch (exception: Exception) {
                        error = exception.userMessage()
                    }
                }
            }, modifier = Modifier.fillMaxWidth()) { Text("Enregistrer le profil") }
        }

        Text("Aliments et recettes", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(search, { search = it }, label = { Text("Rechercher un aliment tunisien") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = { scope.launch { refresh() } }, modifier = Modifier.fillMaxWidth()) { Text("Rechercher") }
        foods.take(30).forEach { food ->
            Card(
                modifier = Modifier.fillMaxWidth().clickable { selectedFood = food },
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(food.name, style = MaterialTheme.typography.titleMedium)
                    Text("${food.category} · ≈ ${food.caloriesPer100g.toInt()} kcal / 100 g")
                    if (selectedFood?.id == food.id) Text("Sélectionné")
                }
            }
        }
        OutlinedTextField(quantity, { quantity = it }, label = { Text("Quantité en grammes") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(mealType, { mealType = it }, label = { Text("Repas (PETIT_DEJEUNER, DEJEUNER, DINER, COLLATION)") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            val food = selectedFood
            val amount = quantity.toDoubleOrNull()
            if (food == null || amount == null || amount <= 0) {
                error = "Choisissez un aliment et saisissez une quantité valide."
            } else {
                scope.launch {
                    try {
                        api.addMeal(MealRequest(food.id, mealType, date, amount))
                        refresh()
                    } catch (exception: Exception) {
                        error = exception.userMessage()
                    }
                }
            }
        }, modifier = Modifier.fillMaxWidth()) { Text("Ajouter au journal") }

        Text("Repas du jour", style = MaterialTheme.typography.titleLarge)
        meals.forEach { meal ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${meal.foodName} · ${meal.quantityGrams.toInt()} g · ${meal.caloriesEstimated.toInt()} kcal")
                Button(onClick = {
                    scope.launch {
                        try {
                            api.deleteMeal(meal.id)
                            refresh()
                        } catch (exception: Exception) {
                            error = exception.userMessage()
                        }
                    }
                }) { Text("Retirer") }
            }
        }

        Text("Activité physique", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(activityType, { activityType = it }, label = { Text("Type d’exercice") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(duration, { duration = it }, label = { Text("Durée (minutes)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(intensity, { intensity = it }, label = { Text("Intensité (FAIBLE, MODEREE, ELEVEE)") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            val minutes = duration.toIntOrNull()
            if (activityType.isBlank() || minutes == null || minutes !in 1..1440) {
                error = "Saisissez un exercice et une durée comprise entre 1 et 1 440 minutes."
            } else {
                scope.launch {
                    try {
                        api.addActivity(ActivityRequest(activityType.trim(), date, minutes, intensity, ""))
                        refresh()
                    } catch (exception: Exception) {
                        error = exception.userMessage()
                    }
                }
            }
        }, modifier = Modifier.fillMaxWidth()) { Text("Ajouter l’activité") }
        activities.forEach { activity ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${activity.type} · ${activity.durationMinutes} min · ${activity.intensity}")
                Button(onClick = {
                    scope.launch {
                        try {
                            api.deleteActivity(activity.id)
                            refresh()
                        } catch (exception: Exception) {
                            error = exception.userMessage()
                        }
                    }
                }) { Text("Retirer") }
            }
        }
        if (error.isNotBlank()) Text(error, color = MaterialTheme.colorScheme.error)
        Text("Calories indicatives, sans valeur de diagnostic médical.")
    }
}

private fun Exception.userMessage(): String = when (this) {
    is HttpException -> when (code()) {
        401, 403 -> "Session expirée ou accès non autorisé. Reconnectez-vous."
        400 -> "Certaines informations sont invalides. Vérifiez les champs."
        else -> "Le serveur a répondu avec une erreur (${code()})."
    }
    else -> "Serveur injoignable. Vérifiez le backend et la connexion de l’émulateur."
}
