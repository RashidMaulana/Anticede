package com.bangkit.anticede.api

import com.bangkit.anticede.api.response.LoginResponse
import com.bangkit.anticede.api.response.RegisterResponse
import com.bangkit.anticede.api.response.UploadResponse
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
}