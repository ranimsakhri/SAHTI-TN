package tn.sahti.mobile

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

data class AuthRequest(val email: String, val password: String)
data class RegisterRequest(
    val nom: String,
    val prenom: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
)
data class AuthResponse(
    val token: String,
    val role: String,
    val nom: String,
    val prenom: String,
    val email: String,
)
data class ProfileRequest(
    val gender: String,
    val birthDate: String,
    val heightCm: Double,
    val weightKg: Double,
    val goal: String,
    val activityLevel: String,
    val dietaryPreferences: String,
    val allergies: String,
)
data class ProfileResponse(
    val email: String,
    val firstName: String,
    val lastName: String,
    val roles: List<String>,
    val gender: String?,
    val birthDate: String?,
    val heightCm: Double?,
    val weightKg: Double?,
    val goal: String?,
    val activityLevel: String?,
    val dietaryPreferences: String?,
    val allergies: String?,
)
data class Food(
    val id: Long,
    val name: String,
    val category: String,
    val origin: String,
    val serving: String,
    val caloriesPer100g: Double,
    val proteinPer100g: Double?,
    val carbohydratesPer100g: Double?,
    val fatPer100g: Double?,
)
data class MealRequest(val foodId: Long, val type: String, val date: String, val quantityGrams: Double)
data class MealEntry(
    val id: Long,
    val foodId: Long,
    val foodName: String,
    val type: String,
    val date: String,
    val quantityGrams: Double,
    val caloriesEstimated: Double,
)
data class ActivityRequest(
    val type: String,
    val date: String,
    val durationMinutes: Int,
    val intensity: String,
    val notes: String,
)
data class ActivityEntry(
    val id: Long,
    val type: String,
    val date: String,
    val durationMinutes: Int,
    val intensity: String,
    val notes: String?,
)
data class DailyProgress(
    val date: String,
    val caloriesEstimated: Double,
    val mealEntries: Int,
    val activeMinutes: Int,
    val activities: Int,
)

interface SahtiApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: AuthRequest): AuthResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @GET("api/profile")
    suspend fun getProfile(): ProfileResponse

    @PUT("api/profile")
    suspend fun saveProfile(@Body request: ProfileRequest): ProfileResponse

    @GET("api/foods")
    suspend fun foods(@Query("q") query: String? = null): List<Food>

    @GET("api/meals")
    suspend fun meals(@Query("date") date: String): List<MealEntry>

    @POST("api/meals")
    suspend fun addMeal(@Body request: MealRequest): MealEntry

    @DELETE("api/meals/{id}")
    suspend fun deleteMeal(@Path("id") id: Long)

    @GET("api/activities")
    suspend fun activities(@Query("date") date: String): List<ActivityEntry>

    @POST("api/activities")
    suspend fun addActivity(@Body request: ActivityRequest): ActivityEntry

    @DELETE("api/activities/{id}")
    suspend fun deleteActivity(@Path("id") id: Long)

    @GET("api/progress")
    suspend fun progress(@Query("date") date: String): DailyProgress
}

object ApiFactory {
    private const val PREFS = "sahti-session"

    fun create(context: Context): SahtiApi {
        val appContext = context.applicationContext
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                    .getString("token", null)
                val request = chain.request().newBuilder()
                    .apply { if (!token.isNullOrBlank()) header("Authorization", "Bearer $token") }
                    .build()
                chain.proceed(request)
            }
            .build()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SahtiApi::class.java)
    }

    fun saveToken(context: Context, token: String) {
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString("token", token)
            .apply()
    }

    fun clearToken(context: Context) {
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .remove("token")
            .apply()
    }

    fun token(context: Context): String? =
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString("token", null)
}
