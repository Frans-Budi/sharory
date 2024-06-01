package com.fransbudikashira.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import com.fransbudikashira.storyapp.R
import com.fransbudikashira.storyapp.data.Result
import com.fransbudikashira.storyapp.databinding.ActivityLoginBinding
import com.fransbudikashira.storyapp.ui.factory.AuthViewModelFactory
import com.fransbudikashira.storyapp.ui.main.MainActivity
import com.fransbudikashira.storyapp.utils.isValidEmail

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var email: String
    private lateinit var password: String
    private var checkEmail:Boolean = false
    private var checkPassword:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: AuthViewModelFactory = AuthViewModelFactory.getInstance(this)
        val viewModel: LoginViewModel by viewModels { factory }

        viewModel.isEnableButton.observe(this) {
            it.getContentIfNotHandled()?.let { isEnabled ->
                binding.loginButton.isEnabled = isEnabled
            }
        }

        binding.loginButton.setOnClickListener {
            viewModel.userLogin(email, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.overlayProgress.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.overlayProgress.visibility = View.GONE
                            val intent = Intent(this, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            startActivity(intent)
                        }
                        is Result.Error -> {
                            binding.overlayProgress.visibility = View.GONE
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle(getString(R.string.failed))
                                .setMessage(result.error)
                                .setPositiveButton(getString(R.string.try_again)) { _, _ -> }.create().show()
                        }
                    }
                }
            }
        }

        setupEditText(viewModel)
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(300)
        val edtEmail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(300)
        val edtPassword = ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(300)
        val button = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(title, edtEmail, edtPassword, button)
            start()
        }
    }

    private fun setupEditText(viewModel: LoginViewModel) {
        // Email
        binding.edtEmail.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!binding.edtEmail.error.isNullOrEmpty()) {
                    checkEmail = false
                    isEnabledButton(viewModel)
                } else {
                    email = s.toString()
                    checkEmail = true
                    isEnabledButton(viewModel)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        // Password
        binding.edtPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!binding.edtPassword.error.isNullOrEmpty()) {
                    checkPassword = false
                    isEnabledButton(viewModel)
                } else {
                    password = s.toString()
                    checkPassword = true
                    isEnabledButton(viewModel)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun isEnabledButton(viewModel: LoginViewModel) {
        if (checkEmail && checkPassword)
            viewModel.setEnabledButton(true)
        else
            viewModel.setEnabledButton(false)
    }
}