package com.fransbudikashira.storyapp.data.remote.retrofit

import com.fransbudikashira.storyapp.data.remote.response.AddStoryResponse
import com.fransbudikashira.storyapp.data.remote.response.LoginResponse
import com.fransbudikashira.storyapp.data.remote.response.RegisterResponse
import com.fransbudikashira.storyapp.data.remote.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") name: String,
        @Field("password") email: String
    ): Response<LoginResponse>

    @GET("stories")
    suspend fun getStories(): Response<StoriesResponse>

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Response<AddStoryResponse>
}