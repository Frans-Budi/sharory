package com.fransbudikashira.storyapp.ui.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.fransbudikashira.storyapp.databinding.ActivityWelcomeBinding
import com.fransbudikashira.storyapp.ui.factory.AuthViewModelFactory
import com.fransbudikashira.storyapp.ui.login.LoginActivity
import com.fransbudikashira.storyapp.ui.main.MainActivity
import com.fransbudikashira.storyapp.ui.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: AuthViewModelFactory = AuthViewModelFactory.getInstance(this)
        val viewModel: WelcomeViewModel by viewModels { factory }

        viewModel.getToken().observe(this) { token ->
            if (!token.isNullOrEmpty()) {
                val intent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
        }

        setupAction()
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(300)
        val register = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(300)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(300)
        val desc = ObjectAnimator.ofFloat(binding.descTextView, View.ALPHA, 1f).setDuration(300)

        val together = AnimatorSet().apply {
            playTogether(login, register)
        }

        AnimatorSet().apply {
            playSequentially(title, desc, together)
            start()
        }
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}