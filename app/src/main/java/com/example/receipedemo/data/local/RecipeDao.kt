package com.example.receipedemo.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.receipedemo.data.model.Recipe
import com.example.receipedemo.data.model.RecipeItem


@Dao
interface RecipeDao{

    @androidx.room.Query("SELECT * FROM recipes")
    fun loadAll(): LiveData<List<RecipeItem>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(recipes: List<RecipeItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: RecipeItem)

    @Delete
    fun delete(recipes:ArrayList<RecipeItem>):Int

    @Update
    fun update(recipes:ArrayList<RecipeItem>):Int

//    @Query("SELECT * FROM recipes WHERE title LIKE :title")
//    fun getRecipeWithQuery(title :String):ArrayList<RecipeItem>


}