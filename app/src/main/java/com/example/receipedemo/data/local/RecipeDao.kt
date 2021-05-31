package com.example.receipedemo.data.local

import androidx.room.*
import com.example.receipedemo.data.model.RecipeResp
import io.reactivex.Single


@Dao
interface RecipeDao{

    @Transaction
    @androidx.room.Query("SELECT * FROM recipes")
    fun loadAllRecipes(): Single<List<RecipeResp.RecipeItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(recipes: List<RecipeResp.RecipeItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: RecipeResp.RecipeItem)

    @Delete
    fun delete(recipes:ArrayList<RecipeResp.RecipeItem>):Int

    @Update
    fun update(recipes:ArrayList<RecipeResp.RecipeItem>):Int

//    @Query("SELECT * FROM recipes WHERE title LIKE :title")
//    fun getRecipeWithQuery(title :String):ArrayList<RecipeItem>


}