package com.fransbudikashira.storyapp.ui.add_story

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.fransbudikashira.storyapp.R
import com.fransbudikashira.storyapp.data.Result
import com.fransbudikashira.storyapp.databinding.ActivityAddStoryBinding
import com.fransbudikashira.storyapp.ui.factory.MainViewModelFactory
import com.fransbudikashira.storyapp.ui.main.MainActivity
import com.fransbudikashira.storyapp.utils.getImageUri
import com.fransbudikashira.storyapp.utils.reduceFileImage
import com.fransbudikashira.storyapp.utils.uriToFile

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
        }
    }

    private fun allPermissionGranted() =
        ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: MainViewModelFactory = MainViewModelFactory.getInstance(this)
        val viewModel: AddStoryViewModel by viewModels { factory }

        if (!allPermissionGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        // BackButton Action
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.uploadButton.setOnClickListener {
            if (currentImageUri == null) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.failed))
                    .setMessage(getString(R.string.invalid_empty_image))
                    .setPositiveButton(getString(R.string.back)) { _, _ -> }.create().show()
            }

            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, this).reduceFileImage()
                val description = binding.edtDescription.text.toString()

                viewModel.addStory(imageFile, description).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }
                            is Result.Success -> {
                                showLoading(false)
                                val intent = Intent(this, MainActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                                startActivity(intent)

                                showToast(result.data.message)
                            }
                            is Result.Error -> {
                                showLoading(false)
                                val builder = AlertDialog.Builder(this)
                                builder.setTitle(getString(R.string.failed))
                                    .setMessage(result.error)
                                    .setPositiveButton(getString(R.string.back)) { _, _ -> }.create().show()
                            }
                        }
                    }
                }
            }
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener{ startCamera() }

        playAnimation()
    }

    private fun startGallery() {
        launcherGallery.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        }
        else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imageView.setImageURI(it)
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.overlayProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun playAnimation() {
        val photo = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(300)
        val camera = ObjectAnimator.ofFloat(binding.cameraButton, View.ALPHA, 1f).setDuration(300)
        val gallery = ObjectAnimator.ofFloat(binding.galleryButton, View.ALPHA, 1f).setDuration(300)
        val edtDescription = ObjectAnimator.ofFloat(binding.edtDescriptionLayout, View.ALPHA, 1f).setDuration(300)
        val upload = ObjectAnimator.ofFloat(binding.uploadButton, View.ALPHA, 1f).setDuration(300)

        val togetherCamGal = AnimatorSet().apply {
            playTogether(camera, gallery)
        }

        AnimatorSet().apply {
            playSequentially(photo, togetherCamGal, edtDescription, upload)
            start()
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}