package com.bangkit.anticede.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {
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
        private var INSTANCE: UserPreferences? = null
        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}