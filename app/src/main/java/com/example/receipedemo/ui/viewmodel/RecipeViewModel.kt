package com.example.receipedemo.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.receipedemo.api.NetworkManager
import com.example.receipedemo.data.local.RecipeRepository
import com.example.receipedemo.data.model.Recipe
import com.example.receipedemo.data.model.RecipeItem
import com.example.receipedemo.data.model.RecipeResp
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class RecipeViewModel(val recipeRepository: RecipeRepository) : ViewModel() {
    var recipeList = MutableLiveData<ArrayList<RecipeItem>>()
    private var disposable:CompositeDisposable = CompositeDisposable()

    companion object{
        const val TAG = "RecipeViewModel"
    }


    private fun subscribeToSearch(observable: Observable<String>) {
        observable
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(object : io.reactivex.Observer<String> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onNext(t: String) {
                    Log.d(TAG,"fetchRecipe :"+t)
                    fetchRecipe(t)
                }

                override fun onError(e: Throwable) {
                    Log.d(TAG,"onError :"+e.message)
                }

                override fun onComplete(){
                    Log.d(TAG,"onComplete :")
                }

            })

    }



    fun fetchRecipe(query:String) {
        if(!recipeRepository.isRecipeEmpty()){
            Log.d(TAG,"fetchRecipe :"+recipeRepository.loadAllRecipe().size)
            recipeList.value = ArrayList(recipeRepository.loadAllRecipe())
        }
        NetworkManager.getRecipeApi().getRecipe(query)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : io.reactivex.Observer<RecipeResp> {
                override fun onComplete() {
                    Log.d(TAG,"onComplete :")
                }

                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onNext(t: RecipeResp) {
                    Log.d(TAG,"onNext :"+t)
                    recipeList.value = t.recipes
                    saveRecipe()


                }

                override fun onError(e: Throwable) {
                    Log.d(TAG,"onError :"+e.message)
                }

            })
    }

    private fun saveRecipe() {
        Log.d(TAG,"saveRecipe :")
        recipeRepository.saveRecipes(recipeList.value!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object :Observer<Boolean>{
                override fun onComplete() {
                    Log.d(TAG,"onComplete :")
                }

                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onNext(t: Boolean) {
                    Log.d(TAG,"onNext :"+t)
                }

                override fun onError(e: Throwable) {
                    Log.d(TAG,"onError :"+e.message)
                }

            })
    }


}