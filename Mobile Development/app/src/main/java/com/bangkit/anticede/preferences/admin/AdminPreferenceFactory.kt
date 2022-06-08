package com.bangkit.anticede.preferences.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AdminPreferenceFactory(private val pref: AdminPreference) :
    ViewModelProvider.NewInstanceFactory() {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminPreferenceViewModel::class.java)) {
            return AdminPreferenceViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}