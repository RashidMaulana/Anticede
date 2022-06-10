package com.bangkit.anticede.ui.login

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.anticede.BottomNavigationActivity
import com.bangkit.anticede.R
import com.bangkit.anticede.api.ApiConfig
import com.bangkit.anticede.api.ApiConfigUser
import com.bangkit.anticede.api.response.LoginResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel : ViewModel() {
    private val userLogin = MutableLiveData<LoginResponse>()
    var responseMessage: String? = null

    val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loginUser(context: Context, username : String, password : String){
        _isLoading.value = true
        val client = ApiConfigUser.getApiService(context).loginUser(username, password)
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
                    val jsonObj = JSONObject(response.errorBody()?.charStream()!!.readText())
                    responseMessage = jsonObj.getString("message") +
                            context.getString(R.string.warning_expired_cookie)
                    Toast.makeText(context, responseMessage, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                Toast.makeText(context, t.message.toString() +
                        context.getString(R.string.warning_expired_cookie),
                        Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getUser(): MutableLiveData<LoginResponse> {
        return userLogin
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}