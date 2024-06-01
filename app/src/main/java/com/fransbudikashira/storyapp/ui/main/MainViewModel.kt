package com.fransbudikashira.storyapp.ui.main

import androidx.lifecycle.ViewModel
import com.fransbudikashira.storyapp.data.StoryRepository
import com.fransbudikashira.storyapp.data.UserRepository

class MainViewModel(
    private val storyRepository: StoryRepository,
    private val userRepository: UserRepository
): ViewModel() {

    fun deleteToken() = userRepository.deleteToken()

    fun getStories() = storyRepository.getStories()
}