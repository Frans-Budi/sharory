package com.fransbudikashira.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fransbudikashira.storyapp.data.local.entity.StoryEntity
import com.fransbudikashira.storyapp.data.remote.response.ListStoryItem
import com.fransbudikashira.storyapp.data.repository.StoryRepository
import com.fransbudikashira.storyapp.data.repository.UserRepository

class MainViewModel(
    private val storyRepository: StoryRepository,
    private val userRepository: UserRepository
): ViewModel() {

    fun deleteToken() = userRepository.deleteToken()

    val stories: LiveData<PagingData<StoryEntity>> =
        storyRepository.getStories().cachedIn(viewModelScope)
}