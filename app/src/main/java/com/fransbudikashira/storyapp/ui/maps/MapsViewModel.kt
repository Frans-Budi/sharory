package com.fransbudikashira.storyapp.ui.maps

import androidx.lifecycle.ViewModel
import com.fransbudikashira.storyapp.data.repository.StoryRepository

class MapsViewModel(
    private val storyRepository: StoryRepository
): ViewModel() {

    fun getStoriesWithLocation() = storyRepository.getStoriesWithLocation()
}