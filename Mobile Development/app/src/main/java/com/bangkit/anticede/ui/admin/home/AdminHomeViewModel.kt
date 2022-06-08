package com.bangkit.anticede.ui.admin.home

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.anticede.api.ApiConfig
import com.bangkit.anticede.api.response.GetAllUserResponse
import com.bangkit.anticede.api.response.GetAllUserResponseItem
import com.bangkit.anticede.api.response.LogoutResponse
import org.json.JSONObject

class AdminHomeViewModel  : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var logoutMessage : String? = null

    private val users = MutableLiveData<List<GetAllUserResponseItem>>()
    val usersList: LiveData<List<GetAllUserResponseItem>> = users

    fun getUsers(context : Context){
        _isLoading.value = true
        val client = ApiConfig.getApiService(context).getMembers()
        client.enqueue(object : retrofit2.Callback<GetAllUserResponse>{
            override fun onFailure(call: retrofit2.Call<GetAllUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: retrofit2.Call<GetAllUserResponse>, response: retrofit2.Response<GetAllUserResponse>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    users.value = response.body()?.getAllUserResponse
                    Log.d("response", usersList.value.toString())
                } else {
                    _isLoading.value = false
                    val jsonObj = JSONObject(response.errorBody()?.charStream()!!.readText())
                    val responseMessage = jsonObj.getString("message")
                    Toast.makeText(context, responseMessage, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun logoutAdmin(context : Context){
        _isLoading.value = true
        val client = ApiConfig.getApiService(context).logoutAdmin()
        client.enqueue(object : retrofit2.Callback<LogoutResponse>{
            override fun onFailure(call: retrofit2.Call<LogoutResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: retrofit2.Call<LogoutResponse>, response: retrofit2.Response<LogoutResponse>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    logoutMessage = response.body()?.message
                    Toast.makeText(context, logoutMessage, Toast.LENGTH_SHORT).show()
                } else {
                    _isLoading.value = false
                    val jsonObj = JSONObject(response.errorBody()?.charStream()!!.readText())
                    val responseMessage = jsonObj.getString("message")
                    Toast.makeText(context, responseMessage, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    companion object{
        private const val TAG = "AdminHomeViewModel"
    }
}