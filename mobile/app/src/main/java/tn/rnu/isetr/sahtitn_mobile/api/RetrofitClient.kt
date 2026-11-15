package tn.rnu.isetr.sahtitn_mobile.api

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tn.rnu.isetr.sahtitn_mobile.utils.SessionManager

object RetrofitClient {

    // Adresse du backend
    // home
    private const val BASE_URL = "http://192.168.8.101:8081/"

    // iset_réseau Etudiant
    // private const val BASE_URL = "http://172.16.18.164:8081/"

    // ih
    // private const val BASE_URL = "http://10.30.22.8:8081/"

    private lateinit var retrofit: Retrofit

    private var _authApi: AuthApi? = null
    val authApi: AuthApi
        get() = _authApi
            ?: throw IllegalStateException(
                "RetrofitClient n'est pas encore initialisé"
            )

    private var _profilApi: ProfilApi? = null
    val profilApi: ProfilApi
        get() = _profilApi
            ?: throw IllegalStateException(
                "RetrofitClient n'est pas encore initialisé"
            )

    private var _foodApi: FoodApi? = null
    val foodApi: FoodApi
        get() = _foodApi
            ?: throw IllegalStateException(
                "RetrofitClient n'est pas encore initialisé"
            )

    private var _mealApi: MealApi? = null
    val mealApi: MealApi
        get() = _mealApi
            ?: throw IllegalStateException(
                "RetrofitClient n'est pas encore initialisé"
            )

    private var _activiteApi: ActiviteApi? = null
    val activiteApi: ActiviteApi
        get() = _activiteApi
            ?: throw IllegalStateException(
                "RetrofitClient n'est pas encore initialisé"
            )

    fun init(context: Context, sessionManager: SessionManager) {

        // Ajouter automatiquement le JWT aux requêtes
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .build()

        // Configuration Retrofit
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Initialisation des API
        _authApi = retrofit.create(AuthApi::class.java)
        _profilApi = retrofit.create(ProfilApi::class.java)
        _foodApi = retrofit.create(FoodApi::class.java)
        _mealApi = retrofit.create(MealApi::class.java)
        _activiteApi = retrofit.create(ActiviteApi::class.java)
    }
}