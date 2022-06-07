package com.bangkit.anticede.preferences.admin

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AdminPreference private constructor(private val dataStore: DataStore<Preferences>) {
    private val token = stringPreferencesKey("token")

    fun getTokenPreference(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[token].toString()
        }
    }


    suspend fun setPreferences(tokenUser: String) {
        dataStore.edit { preferences ->
            preferences[token] = tokenUser
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: AdminPreference? = null
        fun getInstance(dataStore: DataStore<Preferences>): AdminPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = AdminPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}