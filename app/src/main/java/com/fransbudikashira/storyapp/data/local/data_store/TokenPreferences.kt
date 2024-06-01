package com.fransbudikashira.storyapp.data.local.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

class TokenPreferences private constructor(private val dataStore: DataStore<Preferences>){
    private val tokenPrefences = stringPreferencesKey("token")

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[tokenPrefences] ?: ""
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[tokenPrefences] = token
        }
    }

    suspend fun deleteToken(){
        dataStore.edit { preferences ->
            preferences.remove(tokenPrefences)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: TokenPreferences ? = null

        fun getInstance(dataStore: DataStore<Preferences>): TokenPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = TokenPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}