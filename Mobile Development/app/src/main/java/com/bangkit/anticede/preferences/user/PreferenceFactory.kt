package com.bangkit.anticede.preferences.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PreferenceFactory(private val pref: UserPreferences) :
    ViewModelProvider.NewInstanceFactory() {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PreferenceViewModel::class.java)) {
            return PreferenceViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

}