package com.bangkit.anticede.api

import com.bangkit.anticede.api.response.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Multipart
    @POST("upload_audio")
    fun UploadVoice(
        @Part file: MultipartBody.Part,
    ): Call<UploadResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("members")
    fun register(
        @Field("username") username: String,
        @Field("age") age: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login_admin")
    fun loginAdmin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<AdminLoginResponse>

    @GET("members")
    fun getMembers() : Call<GetAllUserResponse>

    @POST("logout")
    fun logoutAdmin() : Call<LogoutResponse>
}