package com.example.receipedemo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Recipes")
class RecipeItem (val title:String,
                  val publisher:String,
                  val image_url:String,
                  val social_rank:Double){
    @PrimaryKey(autoGenerate = true)
    var id: Int=0

}