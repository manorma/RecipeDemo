package com.example.receipedemo.data.local

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.receipedemo.data.model.Recipe
import com.example.receipedemo.data.model.RecipeItem
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable

class RecipeRepository(val activity: AppCompatActivity) {

    private var recipeDatabase: RecipeDatabase
//    private var recipeList = ArrayList<Recipe>()
    private  var recipeItemList= ArrayList<RecipeItem>()
    private val compositeDisposable = CompositeDisposable()

    init {
        recipeDatabase = RecipeDatabase.getInstance(context = activity)
        retrieveReceipe()
    }

    fun retrieveReceipe() :List<RecipeItem>{
        Log.d("RecipeRepository","retrieveRecipe:")
        fetchRecipe().observe(activity, Observer {
            if(recipeItemList.size > 0){
                recipeItemList.clear()
            }
            recipeItemList = ArrayList(it)
            Log.d("RecipeRepository","retrieveRecipe: size"+it.size+" "+recipeItemList.size)
        })
        return recipeItemList
    }

    fun insertRecipe(recipe:Recipe){
        val recipeItem = RecipeItem(recipe.title,recipe.publisher,recipe.image_url,recipe.social_rank)
        recipeDatabase.recipeDao().insert(recipe = recipeItem)

    }

    fun updateRecipe(recipe: Recipe){

    }

    fun convertToRecipeItem(recipeList: List<Recipe>):List<RecipeItem>{
        Log.d("RecipeRepository","insertAll:"+recipeItemList.size)
        if(recipeItemList.size > 0 && recipeList.size > 0){
            recipeItemList.clear()
        }
        Observable.fromIterable(recipeList)
            .flatMap(object :Function<Recipe,ObservableSource<RecipeItem>>{
                override fun apply(t: Recipe): ObservableSource<RecipeItem> {
                    return Observable.just(RecipeItem(t.title,t.publisher,t.image_url,t.social_rank))
                }

            }).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object :io.reactivex.Observer<RecipeItem>{
                override fun onComplete() {
                    Log.d("RecipeRepository","onComplete:" +recipeItemList.size)

                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)

                }

                override fun onNext(t: RecipeItem) {
                    Log.d("RecipeRepository","OnNext:"+t)
                    recipeItemList.add(t)
                }

                override fun onError(e: Throwable) {
                    Log.d("RecipeRepository","OnError:"+e.message)
                }

            })

        return recipeItemList
    }

    fun saveRecipes(recipeList: ArrayList<RecipeItem>):Observable<Boolean>{
        Log.d("RecipeRepository","saveRecipe:")
        return Observable.fromCallable(object :Callable<Boolean>{
            override fun call(): Boolean {
                recipeDatabase.recipeDao().insertAll(recipeList)
                return true
            }

        })


    }

    fun fetchRecipe(): LiveData<List<RecipeItem>> {
        return recipeDatabase.recipeDao().loadAll()
    }

    fun isRecipeEmpty():Boolean{
        val recipeList = retrieveReceipe()
        Log.d("RecipeRepository","isRecipeEmpty:" +recipeList.size)

        return recipeList.isEmpty()
    }
    fun loadAllRecipe():List<RecipeItem>{
//        return Observable.fromCallable(object :Callable<List<Recipe>>{
//            override fun call(): List<Recipe> {
//                return recipeList
//            }
//
//        })
        Log.d("RecipeRepository","loadAllRecipe:" +recipeItemList.size)
        return recipeItemList
    }




}