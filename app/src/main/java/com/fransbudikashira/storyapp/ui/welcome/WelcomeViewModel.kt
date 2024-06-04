package com.fransbudikashira.storyapp.ui.welcome

import androidx.lifecycle.ViewModel
import com.fransbudikashira.storyapp.data.repository.UserRepository

class WelcomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getToken() = userRepository.getToken()
}