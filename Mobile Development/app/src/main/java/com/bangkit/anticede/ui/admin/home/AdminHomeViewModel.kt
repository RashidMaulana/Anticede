package com.bangkit.anticede.ui.admin.home

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.anticede.R
import com.bangkit.anticede.api.ApiConfig
import com.bangkit.anticede.api.response.DeleteResponse
import com.bangkit.anticede.api.response.GetAllUserResponseItem
import com.bangkit.anticede.api.response.LogoutResponse
import org.json.JSONObject

class AdminHomeViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var logoutMessage: String? = null
    private var deleteMessage: String? = null

    val users = MutableLiveData<List<GetAllUserResponseItem>>()

    fun getUsers(context: Context) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(context).getMembers()
        client.enqueue(object : retrofit2.Callback<List<GetAllUserResponseItem>> {
            override fun onFailure(
                call: retrofit2.Call<List<GetAllUserResponseItem>>,
                t: Throwable
            ) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                Toast.makeText(
                    context, t.message.toString() +
                            context.getString(R.string.warning_expired_cookie),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: retrofit2.Call<List<GetAllUserResponseItem>>,
                response: retrofit2.Response<List<GetAllUserResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _isLoading.value = false
                    users.value = responseBody!!
                    Log.d("response", users.value.toString())
                } else {
                    _isLoading.value = false
                    val jsonObj = JSONObject(response.errorBody()?.charStream()!!.readText())
                    val responseMessage = jsonObj.getString("message") +
                            context.getString(R.string.warning_expired_cookie)
                    Toast.makeText(context, responseMessage, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun getListUser(): LiveData<List<GetAllUserResponseItem>> {
        return users
    }

    fun deleteUser(context: Context, id: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(context).deleteUser(id)
        client.enqueue(object : retrofit2.Callback<DeleteResponse> {
            override fun onFailure(call: retrofit2.Call<DeleteResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                Toast.makeText(
                    context, t.message.toString() +
                            context.getString(R.string.warning_expired_cookie),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: retrofit2.Call<DeleteResponse>,
                response: retrofit2.Response<DeleteResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _isLoading.value = false
                    deleteMessage = responseBody?.message
                    Toast.makeText(context, deleteMessage, Toast.LENGTH_SHORT).show()
                } else {
                    _isLoading.value = false
                    val jsonObj = JSONObject(response.errorBody()?.charStream()!!.readText())
                    val responseMessage = jsonObj.getString("message") +
                            context.getString(R.string.warning_expired_cookie)
                    Toast.makeText(context, responseMessage, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun logoutAdmin(context: Context) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(context).logout()
        client.enqueue(object : retrofit2.Callback<LogoutResponse> {
            override fun onFailure(call: retrofit2.Call<LogoutResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                Toast.makeText(
                    context, t.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: retrofit2.Call<LogoutResponse>,
                response: retrofit2.Response<LogoutResponse>
            ) {
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

    companion object {
        private const val TAG = "AdminHomeViewModel"
    }
}