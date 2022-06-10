package com.bangkit.anticede.api

import android.content.Context
import com.bangkit.anticede.BuildConfig
import com.bangkit.anticede.utilities.SaveReceivedCookiesInterceptorUser
import com.bangkit.anticede.utilities.SendSavedCookiesInterceptorUser
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfigUser {
    companion object {
        fun getApiService(context: Context): ApiService {
            val loggingInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            val client = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES) // read timeout
                .addInterceptor(loggingInterceptor)
                .addInterceptor(SaveReceivedCookiesInterceptorUser(context))
                .addInterceptor(SendSavedCookiesInterceptorUser(context))
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