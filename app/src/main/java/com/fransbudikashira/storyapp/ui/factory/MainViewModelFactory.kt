package com.fransbudikashira.storyapp.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fransbudikashira.storyapp.data.StoryRepository
import com.fransbudikashira.storyapp.data.UserRepository
import com.fransbudikashira.storyapp.di.Injection
import com.fransbudikashira.storyapp.ui.add_story.AddStoryViewModel
import com.fransbudikashira.storyapp.ui.main.MainViewModel

class MainViewModelFactory(
    private val storyRepository: StoryRepository,
    private val userRepository: UserRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(storyRepository, userRepository) as T
            }

            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(storyRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: MainViewModelFactory? = null
        fun getInstance(context: Context): MainViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: MainViewModelFactory(
                    Injection.provideRepository(context),
                    Injection.provideUserRepository(context)
                )
            }.also { instance = it }
    }
}