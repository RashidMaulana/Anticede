package com.bangkit.anticede.api

import android.content.Context
import com.bangkit.anticede.BuildConfig
import com.bangkit.anticede.utilities.SaveReceivedCookiesInterceptor
import com.bangkit.anticede.utilities.SendSavedCookiesInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {

    companion object {
        fun getApiService(context : Context): ApiService {
            val loggingInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(SaveReceivedCookiesInterceptor(context))
                .addInterceptor(SendSavedCookiesInterceptor(context))
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.ANTICEDE_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}