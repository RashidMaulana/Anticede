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

    private val userLogin = MutableLiveData<LoginResponse>()


    fun uploadVoice(
        context: Context,
        file: MultipartBody.Part,
    ) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().UploadVoice(file)
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

    fun loginUser(context: Context, username : String, password : String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().loginUser(username, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    userLogin.value = response.body()
                    responseMessage = response.body()?.message

                    Toast.makeText(context, responseMessage, Toast.LENGTH_LONG).show()
                    val intent = Intent(context, BottomNavigationActivity::class.java)
                    context.startActivity(intent)
                } else {
                    _isLoading.value = false
                    responseMessage = response.body()?.message
                    Log.d(TAG, "onResponse: ${response.body()?.message}")
                    Toast.makeText(context, responseMessage, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getUser(): MutableLiveData<LoginResponse> {
        return userLogin
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}