package com.fransbudikashira.storyapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fransbudikashira.storyapp.data.remote.response.ListStoryItem
import com.fransbudikashira.storyapp.data.remote.retrofit.ApiService

class StoryPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStories(position, params.loadSize)

            if (responseData.isSuccessful){
                val data = responseData.body()?.listStory ?: emptyList()

                LoadResult.Page(
                    data = data,
                    prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                    nextKey = if (data.isEmpty()) null else position + 1
                )
            } else {
                LoadResult.Error(Exception("Failed to load data"))
            }
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}