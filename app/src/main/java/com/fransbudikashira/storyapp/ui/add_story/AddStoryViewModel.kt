package com.fransbudikashira.storyapp.ui.add_story

import androidx.lifecycle.ViewModel
import com.fransbudikashira.storyapp.data.StoryRepository
import java.io.File

class AddStoryViewModel(private val storyRepository: StoryRepository): ViewModel() {

    fun addStory(
        imageFile: File,
        description: String
    ) = storyRepository.addStory(imageFile, description)
}