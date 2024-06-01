package com.fransbudikashira.storyapp.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fransbudikashira.storyapp.data.UserRepository
import com.fransbudikashira.storyapp.di.Injection
import com.fransbudikashira.storyapp.ui.login.LoginViewModel
import com.fransbudikashira.storyapp.ui.register.RegisterViewModel
import com.fransbudikashira.storyapp.ui.welcome.WelcomeViewModel

class AuthViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(WelcomeViewModel::class.java) -> {
                WelcomeViewModel(userRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: AuthViewModelFactory? = null
        fun getInstance(context: Context): AuthViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: AuthViewModelFactory(Injection.provideUserRepository(context))
            }.also { instance = it }
    }
}