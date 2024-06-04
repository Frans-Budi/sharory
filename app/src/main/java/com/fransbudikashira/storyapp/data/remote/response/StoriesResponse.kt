package com.fransbudikashira.storyapp.data.remote.response

import android.os.Parcelable
import com.fransbudikashira.storyapp.data.local.entity.StoryEntity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class StoriesResponse(

    @field:SerializedName("listStory")
	val listStory: List<ListStoryItem>? = null,

    @field:SerializedName("error")
	val error: Boolean,

    @field:SerializedName("message")
	val message: String
)

@Parcelize
data class ListStoryItem(

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("lon")
	val lon: Double? = null,

	@field:SerializedName("lat")
	val lat: Double? = null
): Parcelable

fun ListStoryItem.toEntity(): StoryEntity {
	return StoryEntity(
		id = this.id,
		name = this.name,
		description = this.description,
		photoUrl = this.photoUrl,
		createdAt = this.createdAt,
		lat = this.lat,
		lon = this.lon
	)
}

fun List<ListStoryItem>.toEntityList(): List<StoryEntity> {
	return this.map { it.toEntity() }
}
