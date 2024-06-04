package com.fransbudikashira.storyapp.ui.detail_story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fransbudikashira.storyapp.data.local.entity.StoryEntity
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

        val storyItem: StoryEntity? = intent.getParcelableExtra(EXTRA_STORY)
        setUpView(storyItem)
    }

    private fun setUpView(storyItem: StoryEntity?) {
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