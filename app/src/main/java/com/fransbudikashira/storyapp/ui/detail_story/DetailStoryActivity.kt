package com.fransbudikashira.storyapp.ui.detail_story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.fransbudikashira.storyapp.R
import com.fransbudikashira.storyapp.data.remote.response.ListStoryItem
import com.fransbudikashira.storyapp.databinding.ActivityDetailStoryBinding
import com.fransbudikashira.storyapp.utils.loadImage
import com.fransbudikashira.storyapp.utils.withDateFormat

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // BackButton Action
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        val storyItem: ListStoryItem? = intent.getParcelableExtra(EXTRA_STORY)
        setUpView(storyItem)
    }

    private fun setUpView(storyItem: ListStoryItem?) {
        with(binding) {
            titleText.text = storyItem?.name ?: ""
            timeText.text = storyItem?.createdAt?.withDateFormat() ?: ""
            descriptionText.text = storyItem?.description ?: ""
            imageView.loadImage(url = storyItem?.photoUrl)
        }
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}