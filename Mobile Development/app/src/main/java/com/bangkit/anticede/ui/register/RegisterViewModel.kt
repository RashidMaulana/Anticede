package com.bangkit.anticede.ui.register

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.anticede.R
import com.bangkit.anticede.api.ApiConfig
import com.bangkit.anticede.api.response.RegisterResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun registerUser(context: Context, username: String, age: String, Password: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().register(username, age, Password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    AlertDialog.Builder(context).apply {
                        setTitle(context.resources.getString(R.string.register_success))
                        setMessage(context.resources.getString(R.string.register_success2))
                        setPositiveButton(context.resources.getString(R.string.yes)) { _: DialogInterface, _: Int ->
                        }
                        create()
                        show()
                    }
                } else {
                    _isLoading.value = false
                    val jsonObj = JSONObject(response.errorBody()?.charStream()!!.readText())
                    val responseMessage = jsonObj.getString("message")
                    Log.d(TAG, "onResponse: ${response.body()?.message}")
                    Toast.makeText(context, responseMessage, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}