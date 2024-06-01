package com.fransbudikashira.storyapp.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fransbudikashira.storyapp.R
import com.fransbudikashira.storyapp.data.remote.response.ListStoryItem
import com.fransbudikashira.storyapp.databinding.ItemStoryBinding
import com.fransbudikashira.storyapp.ui.detail_story.DetailStoryActivity
import com.fransbudikashira.storyapp.utils.loadImage
import com.fransbudikashira.storyapp.utils.withDateFormat

class StoryItemAdapter: ListAdapter<ListStoryItem, StoryItemAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(
        private val binding: ItemStoryBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(resultItem: ListStoryItem) {
            binding.apply {
                titleText.text = resultItem.name
                timeText.text = resultItem.createdAt.withDateFormat()
                imageView.loadImage(url = resultItem.photoUrl)

                // Handle Click each item
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                    intent.putExtra(DetailStoryActivity.EXTRA_STORY, resultItem)

                    // Shared Element Animation
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(imageView, "photo"),
                            Pair(titleText, "title"),
                            Pair(timeText, "dateTime"),
                        )
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val result = getItem(position)
        holder.bind(result)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}