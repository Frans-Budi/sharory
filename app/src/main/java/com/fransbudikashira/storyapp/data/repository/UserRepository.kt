package com.fransbudikashira.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.fransbudikashira.storyapp.data.Result
import com.fransbudikashira.storyapp.data.local.data_store.TokenPreferences
import com.fransbudikashira.storyapp.data.remote.response.LoginResponse
import com.fransbudikashira.storyapp.data.remote.response.RegisterResponse
import com.fransbudikashira.storyapp.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRepository(
    private val apiService: ApiService,
    private val tokenPreferences: TokenPreferences
) {

    fun userRegister(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                Log.d(TAG, "userRegister: $errorResponse")
                emit(Result.Error(errorResponse.message))
            }
        } catch (e: Exception) {
            Log.d(TAG, "userRegister: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun userLogin(
        email: String,
        password: String
    ): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            if (response.isSuccessful) {
                response.body()?.loginResult?.let { data ->
                    tokenPreferences.saveToken(data.token)
                    Log.d(TAG, "userLogin: ${data.token}")
                    emit(Result.Success(response.body()!!))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
                Log.d(TAG, "userLogin error response: $errorResponse")
                emit(Result.Error(errorResponse.message))
            }

        } catch (e: Exception) {
            Log.d(TAG, "userLogin: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getToken(): LiveData<String> {
        return tokenPreferences.getToken().asLiveData()
    }

    fun deleteToken() = CoroutineScope(Dispatchers.IO).launch {
        tokenPreferences.deleteToken()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            tokenPreferences: TokenPreferences
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, tokenPreferences)
            }.also { instance = it }

        private const val TAG = "UserRepository"
    }
}