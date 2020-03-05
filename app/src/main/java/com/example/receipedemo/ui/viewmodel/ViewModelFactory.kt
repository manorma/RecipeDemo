package com.example.receipedemo.ui.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.receipedemo.data.local.RecipeRepository

class ViewModelFactory(val activity: AppCompatActivity) :ViewModelProvider.NewInstanceFactory(){


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RecipeViewModel::class.java)){

            return RecipeViewModel(RecipeRepository(activity)) as T
        }
        return super.create(modelClass)
    }
}