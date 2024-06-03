package com.fransbudikashira.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.fransbudikashira.storyapp.data.remote.response.AddStoryResponse
import com.fransbudikashira.storyapp.data.remote.response.LoginResponse
import com.fransbudikashira.storyapp.data.remote.response.StoriesResponse
import com.fransbudikashira.storyapp.data.remote.retrofit.ApiService
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository(private val apiService: ApiService) {

    fun getStories(): LiveData<Result<StoriesResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStories()
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
                Log.d(TAG, "getStories: $errorResponse")
                emit(Result.Error(errorResponse.message))
            }
        } catch (e: Exception) {
            Log.d(TAG, "userLogin: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStoriesWithLocation(): LiveData<Result<StoriesResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoriesWithLocation()
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
                Log.d(TAG, "getStories: $errorResponse")
                emit(Result.Error(errorResponse.message))
            }
        } catch (e: Exception) {
            Log.d(TAG, "userLogin: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun addStory(
        imageFile: File,
        description: String
    ): LiveData<Result<AddStoryResponse>> = liveData {
        emit(Result.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData("photo", imageFile.name, requestImageFile)

        try {
            val response = apiService.addStory(multipartBody, requestBody)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, AddStoryResponse::class.java)
                emit(Result.Error(errorResponse.message))
            }
        } catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AddStoryResponse::class.java)
            Log.d(TAG, "addStory: $errorResponse")
            emit(Result.Error(errorResponse.message))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService)
            }.also { instance = it }

        private const val TAG = "StoryRepository"
    }
}