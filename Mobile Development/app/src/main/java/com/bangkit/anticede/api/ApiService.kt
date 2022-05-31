package com.bangkit.anticede.api

import com.bangkit.anticede.api.response.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @Multipart
    @POST("upload_audio")
    fun UploadVoice(
        @Part file: MultipartBody.Part,
    ): Call<UploadResponse>


}