package com.fransbudikashira.storyapp.di

import android.content.Context
import com.fransbudikashira.storyapp.data.StoryRepository
import com.fransbudikashira.storyapp.data.UserRepository
import com.fransbudikashira.storyapp.data.local.data_store.TokenPreferences
import com.fransbudikashira.storyapp.data.local.data_store.dataStore
import com.fransbudikashira.storyapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val tokenPreferences = TokenPreferences.getInstance(context.dataStore)
        val user = runBlocking { tokenPreferences.getToken().first() }
        val apiService = ApiConfig.getApiService(user)

        return StoryRepository.getInstance(apiService)
    }

    fun provideUserRepository(context: Context): UserRepository {
        val tokenPreferences = TokenPreferences.getInstance(context.dataStore)
        val user = runBlocking { tokenPreferences.getToken().first() }
        val apiService = ApiConfig.getApiService(user)

        return UserRepository.getInstance(apiService, tokenPreferences)
    }
}