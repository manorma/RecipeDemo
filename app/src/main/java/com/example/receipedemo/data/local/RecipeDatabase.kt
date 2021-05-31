package com.example.receipedemo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.receipedemo.data.model.RecipeResp
import java.util.*

@Database(entities = arrayOf(RecipeResp.RecipeItem::class),version = 1)
abstract class RecipeDatabase:RoomDatabase(){
    companion object{
        private var mInstance: RecipeDatabase? = null
        const val DATABASE_NAME ="recipe_db"

        fun getInstance(context: Context):RecipeDatabase{
            if(mInstance == null){
                mInstance = Room.databaseBuilder(context.applicationContext,RecipeDatabase::class.java,
                    DATABASE_NAME).build()
            }
            return mInstance as RecipeDatabase
        }
    }

    abstract fun recipeDao():RecipeDao
}