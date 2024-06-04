package com.fransbudikashira.storyapp.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.fransbudikashira.storyapp.data.local.entity.RemoteKeys
import com.fransbudikashira.storyapp.data.local.entity.StoryEntity
import com.fransbudikashira.storyapp.data.local.room.StoryDatabase
import com.fransbudikashira.storyapp.data.remote.response.toEntity
import com.fransbudikashira.storyapp.data.remote.response.toEntityList
import com.fransbudikashira.storyapp.data.remote.retrofit.ApiService

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryDatabase,
    private val apiService: ApiService
) : RemoteMediator<Int, StoryEntity>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData = apiService.getStories(page, state.config.pageSize)

            if (responseData.isSuccessful) {
                val data = responseData.body()?.listStory ?: emptyList()
                val endOfPaginationReached = data.isEmpty()

                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.remoteKeysDao().deleteRemoteKeys()
                        database.storyDao().deleteAll()
                    }
                    val prevKeys = if (page == 1) null else page - 1
                    val nextKeys = if (endOfPaginationReached) null else page + 1
                    val keys = data.map {
                        RemoteKeys(id = it.id, prevKey = prevKeys, nextKey = nextKeys)
                    }
                    database.remoteKeysDao().insertAll(keys)
                    database.storyDao().insertStory(data.map { it.toEntity() })

                    Log.d("HALO", "Prev: $prevKeys, Next: $nextKeys, Keys: $keys")
                }
                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } else {
                Log.d("Halo", "GAGAL")
                return MediatorResult.Error(Exception("Failed to load data"))
            }
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }
}