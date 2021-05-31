package com.example.receipedemo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class RecipeResp(
    val recipes: List<RecipeItem>
) {
    @Entity(tableName = "Recipes")
    @JsonClass(generateAdapter = true)
    data class RecipeItem(
        val title: String,
        val publisher: String,
        @Json(name = "image_url") val imageUrl: String,
        @Json(name = "social_rank")val socialRank: Double
    ) {
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0
    }
}

