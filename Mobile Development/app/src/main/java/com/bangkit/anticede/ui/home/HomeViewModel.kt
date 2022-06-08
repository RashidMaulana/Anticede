package com.bangkit.anticede.ui.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.anticede.BottomNavigationActivity
import com.bangkit.anticede.api.ApiConfig
import com.bangkit.anticede.api.response.LoginResponse
import com.bangkit.anticede.api.response.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    var responseMessage: String? = null

    val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun uploadVoice(
        context: Context,
        file: MultipartBody.Part,
    ) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(context).UploadVoice(file)
        client.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    responseMessage = response.body()?.message
                    Toast.makeText(context, responseMessage, Toast.LENGTH_LONG).show()
                } else {
                    _isLoading.value = false
                    responseMessage = response.body()?.message
                    Toast.makeText(context, responseMessage, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }



    companion object {
        private const val TAG = "HomeViewModel"
    }
}