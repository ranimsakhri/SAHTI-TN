package tn.rnu.isetr.sahtitn_mobile.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import tn.rnu.isetr.sahtitn_mobile.models.AuthResponse
import tn.rnu.isetr.sahtitn_mobile.models.RegisterRequest
import tn.rnu.isetr.sahtitn_mobile.models.LoginRequest
import tn.rnu.isetr.sahtitn_mobile.models.ChangePasswordRequest
import tn.rnu.isetr.sahtitn_mobile.models.ForgotPasswordRequest
import tn.rnu.isetr.sahtitn_mobile.models.ResetPasswordRequest

interface AuthApi {

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("api/auth/change-password")
    suspend fun changePassword(
        @Body request: ChangePasswordRequest
    ): retrofit2.Response<Map<String, String>>

    @POST("api/auth/reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): retrofit2.Response<Map<String, String>>

    @POST("api/auth/forgot-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): retrofit2.Response<Map<String, String>>


}