package com.fransbudikashira.storyapp.ui.main

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fransbudikashira.storyapp.R
import com.fransbudikashira.storyapp.data.Result
import com.fransbudikashira.storyapp.data.remote.response.ListStoryItem
import com.fransbudikashira.storyapp.databinding.ActivityMainBinding
import com.fransbudikashira.storyapp.ui.adapter.StoryItemAdapter
import com.fransbudikashira.storyapp.ui.add_story.AddStoryActivity
import com.fransbudikashira.storyapp.ui.factory.MainViewModelFactory
import com.fransbudikashira.storyapp.ui.maps.MapsActivity
import com.fransbudikashira.storyapp.ui.welcome.WelcomeActivity
import com.jakewharton.threetenabp.AndroidThreeTen

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initial for convert date
        AndroidThreeTen.init(this)

        // Initial for Factory and ViewModel
        val factory: MainViewModelFactory = MainViewModelFactory.getInstance(this)
        val viewModel: MainViewModel by viewModels { factory }

        // Handle AppBar Menu Button -> Logout
        binding.toAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.maps -> {
                    startActivity(Intent(this, MapsActivity::class.java))
                    true
                }
                R.id.logout -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(getString(R.string.logout))
                        .setMessage(getString(R.string.ask_for_logout))
                        .setPositiveButton(getString(R.string.yes)) { _, _ ->
                            viewModel.deleteToken()
                            val intent = Intent(this, WelcomeActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            startActivity(intent)
                        }
                        .setNegativeButton(getString(R.string.no)) { _, _ ->}.create().show()
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                else -> false
            }
        }

        // Handle Floating Action Button
        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

        // Perform Optimize Recycle View
        binding.rvStory.setHasFixedSize(true)

        // Initial for Recycle View Layout
        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        // Initial for Adapter List Items
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)

        viewModel.getStories().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.linearProgress.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.linearProgress.visibility = View.GONE
                        val stories = result.data.listStory
                        if (!stories.isNullOrEmpty()) {
                            binding.statusText.text = ""
                            setStoryItemData(result.data.listStory)
                        } else {
                            binding.statusText.text = getString(R.string.empty_stories)
                        }
                    }
                    is Result.Error -> {
                        binding.linearProgress.visibility = View.GONE
                        binding.statusText.text = getString(R.string.empty_stories)
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                        Log.d("MainActivity", "onCreate: ${result.error}")
                    }
                }
            }
        }

    }

    private fun setStoryItemData(listStoryItems: List<ListStoryItem?>) {
        val adapter = StoryItemAdapter()
        adapter.submitList(listStoryItems)
        binding.rvStory.adapter = adapter
    }
}