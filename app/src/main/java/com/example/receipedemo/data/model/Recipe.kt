package com.example.receipedemo.data.model

data class Recipe(val image_url:String,
                  val title:String,
                  val _id:String?,
                  val source_url:String?,
                  val recipe_id:String?,
                  val publisher_url:String?,
                  val social_rank:Double,
                  val publisher:String)

data class RecipeResp(val recipes:ArrayList<RecipeItem>)