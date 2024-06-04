package com.fransbudikashira.storyapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fransbudikashira.storyapp.data.local.entity.StoryEntity
import com.fransbudikashira.storyapp.data.local.room.StoryDatabase
import com.fransbudikashira.storyapp.data.remote.response.AddStoryResponse
import com.fransbudikashira.storyapp.data.remote.response.ListStoryItem
import com.fransbudikashira.storyapp.data.remote.response.LoginResponse
import com.fransbudikashira.storyapp.data.remote.response.RegisterResponse
import com.fransbudikashira.storyapp.data.remote.response.StoriesResponse
import com.fransbudikashira.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest {

    private var mockApi: ApiService = FakeApiService()
    private var mockDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoryDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = StoryRemoteMediator(
            mockDb,
            mockApi
        )
        val pagingState = PagingState<Int, StoryEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}

class FakeApiService : ApiService {
    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Response<RegisterResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun login(name: String, email: String): Response<LoginResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getStories(page: Int, size: Int): Response<StoriesResponse> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story =  ListStoryItem(
                id =  i.toString(),
                photoUrl = "photoUrl $i",
                createdAt = "createdAt $i",
                name = "name $i",
                description = "description $i",
                lat = null,
                lon = null,
            )
            items.add(story)
        }
        return Response.success(StoriesResponse(items, false, "Stories fetched successfully"))
    }

    override suspend fun addStory(
        file: MultipartBody.Part,
        description: RequestBody
    ): Response<AddStoryResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getStoriesWithLocation(location: Int): Response<StoriesResponse> {
        TODO("Not yet implemented")
    }

}