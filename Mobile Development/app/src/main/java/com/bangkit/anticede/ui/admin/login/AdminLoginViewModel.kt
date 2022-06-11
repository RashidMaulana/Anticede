package com.bangkit.anticede.ui.admin.login

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.anticede.api.ApiConfig
import com.bangkit.anticede.api.response.AdminLoginResponse
import com.bangkit.anticede.ui.admin.home.AdminHomeActivity
import org.json.JSONObject

class AdminLoginViewModel : ViewModel() {

    private val adminLogin = MutableLiveData<AdminLoginResponse>()
    private var responseMessage: String? = null
    val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loginAdmin(context: Context, username: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(context).loginAdmin(username, password)
        client.enqueue(object : retrofit2.Callback<AdminLoginResponse> {
            override fun onFailure(call: retrofit2.Call<AdminLoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: retrofit2.Call<AdminLoginResponse>,
                response: retrofit2.Response<AdminLoginResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    adminLogin.value = response.body()
                    responseMessage = response.body()?.message

                    Toast.makeText(context, responseMessage, Toast.LENGTH_LONG).show()
                    val intent = Intent(context, AdminHomeActivity::class.java)
                    context.startActivity(intent)
                } else {
                    _isLoading.value = false
                    val jsonObj = JSONObject(response.errorBody()?.charStream()!!.readText())
                    responseMessage = jsonObj.getString("message")
                    Toast.makeText(context, responseMessage, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun getAdmin(): MutableLiveData<AdminLoginResponse> {
        return adminLogin
    }

    companion object {
        private const val TAG = "AdminLoginViewModel"
    }
}