package com.fransbudikashira.storyapp.ui.main

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fransbudikashira.storyapp.R
import com.fransbudikashira.storyapp.databinding.ActivityMainBinding
import com.fransbudikashira.storyapp.ui.adapter.LoadingStateAdapter
import com.fransbudikashira.storyapp.ui.adapter.StoryItemAdapter
import com.fransbudikashira.storyapp.ui.add_story.AddStoryActivity
import com.fransbudikashira.storyapp.ui.factory.MainViewModelFactory
import com.fransbudikashira.storyapp.ui.maps.MapsActivity
import com.fransbudikashira.storyapp.ui.welcome.WelcomeActivity
import com.jakewharton.threetenabp.AndroidThreeTen

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the factory with application context in onCreate
        viewModel = obtainViewModel(this@MainActivity)

        // Initial for convert date
        AndroidThreeTen.init(this)

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

        setStoryItemData()
    }

    private fun setStoryItemData() {
        val adapter = StoryItemAdapter()
        binding.rvStory.adapter = adapter
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { adapter.retry() }
        )
        viewModel.stories.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = MainViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
    }
}