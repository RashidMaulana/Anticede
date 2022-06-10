package com.bangkit.anticede.ui.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.anticede.OnBoardingActivity
import com.bangkit.anticede.R
import com.bangkit.anticede.api.ApiConfigUser
import com.bangkit.anticede.api.response.LogoutResponse
import com.bangkit.anticede.api.response.UploadResponse
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    val responseMessage = MutableLiveData<String>()
    val voiceTranscription = MutableLiveData<String>()

    val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun uploadVoice(
        context: Context,
        file: MultipartBody.Part,
    ) {
        _isLoading.value = true
        val client = ApiConfigUser.getApiService(context).UploadVoice(file)
        client.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    responseMessage.value = response.body()?.message
                    voiceTranscription.value = response.body()?.transcription
                } else {
                    _isLoading.value = false
                    val jsonObj = JSONObject(response.errorBody()?.charStream()!!.readText())
                    responseMessage.value = jsonObj.getString("message") + context.getString(R.string.warning_expired_cookie)
                    Toast.makeText(context, responseMessage.value, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                _isLoading.value = false
                responseMessage.value = t.message + context.getString(R.string.warning_expired_cookie)
                Log.e(TAG, "onFailure: ${responseMessage.value}")
                Toast.makeText(context, responseMessage.value, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun logout(context: Context){
        _isLoading.value = true
        val client = ApiConfigUser.getApiService(context).logout()
        client.enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(
                call: Call<LogoutResponse>,
                response: Response<LogoutResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    responseMessage.value = response.body()?.message
                    Toast.makeText(context, responseMessage.value, Toast.LENGTH_LONG).show()
                    val intentToOnboard = Intent(context, OnBoardingActivity::class.java)
                    intentToOnboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intentToOnboard.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intentToOnboard)
                } else {
                    _isLoading.value = false
                    val jsonObj = JSONObject(response.errorBody()?.charStream()!!.readText())
                    responseMessage.value = jsonObj.getString("message") +
                            context.getString(R.string.warning_expired_cookie)
                    Log.d(TAG, "onResponse: ${responseMessage.value}")
                    Toast.makeText(context, responseMessage.value, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                Toast.makeText(context, t.message.toString() +
                        context.getString(R.string.warning_expired_cookie),
                        Toast.LENGTH_SHORT).show()
            }
        })
    }



    companion object {
        private const val TAG = "HomeViewModel"
    }
}