package com.fransbudikashira.storyapp

import com.fransbudikashira.storyapp.data.local.entity.StoryEntity

object DataDummy {

    fun generateDummyStoryResponse(): List<StoryEntity> {
        val items: MutableList<StoryEntity> = arrayListOf()
        for (i in 0..100) {
            val story = StoryEntity(
                id =  i.toString(),
                photoUrl = "photoUrl $i",
                createdAt = "createdAt $i",
                name = "name $i",
                description = "description $i",
                lat = null,
                lon = null,
            )
            items.add(story)
        }
        return items
    }
}