package com.bangkit.anticede.preferences.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AdminPreferenceViewModel (private val pref: AdminPreference) : ViewModel() {

    fun getTokenUserSession(): LiveData<String> {
        return pref.getTokenPreference().asLiveData()
    }

    fun saveUserSession(userToken: String) {
        viewModelScope.launch {
            pref.setPreferences(userToken)
        }
    }
}