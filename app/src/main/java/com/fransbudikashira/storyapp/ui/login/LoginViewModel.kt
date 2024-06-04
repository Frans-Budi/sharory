package com.fransbudikashira.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fransbudikashira.storyapp.data.repository.UserRepository
import com.fransbudikashira.storyapp.utils.Event

class LoginViewModel(private val storyRepository: UserRepository): ViewModel() {
    private val _isEnableButton = MutableLiveData<Event<Boolean>>()
    val isEnableButton: LiveData<Event<Boolean>> = _isEnableButton

    init {
        setEnabledButton(false)
    }

    fun setEnabledButton(isEnabled: Boolean) {
        _isEnableButton.value = Event(isEnabled)
    }

    fun userLogin(
        email: String,
        password: String
    ) = storyRepository.userLogin(email, password)
}