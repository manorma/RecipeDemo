package com.example.receipedemo.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.receipedemo.data.local.RecipeRepository
import com.example.receipedemo.data.model.RecipeResp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RecipeViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {
    var recipeList = MutableLiveData<ArrayList<RecipeResp.RecipeItem>>()
    private var disposable: CompositeDisposable = CompositeDisposable()

    companion object {
        const val TAG = "RecipeViewModel"
    }

    fun fetchRecipe(query: String) {
        if (!recipeRepository.isRecipeEmpty()) {
            Log.d(TAG, "fetchRecipe :" + recipeRepository.getRecipeItems().size)
            recipeList.value = ArrayList(recipeRepository.getRecipeItems())
        }
        disposable.add(recipeRepository.fetchRecipeFromServer(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    recipeList.value = ArrayList(it.recipes)
                    saveRecipe()
                },
                { Log.d(TAG, "error fetchRecipe :" + it.message) }
            )
        )
    }

    private fun saveRecipe() {
        Log.d(TAG, "saveRecipe :")
        disposable.add(recipeRepository.saveRecipes(recipeList.value!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { Log.d(TAG, "onSuccess saveRecipe ") },
                { Log.d(TAG, "onError :" + it.message) }
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}