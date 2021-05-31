package com.example.receipedemo.data.local

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.receipedemo.api.NetworkManager
import com.example.receipedemo.data.model.RecipeResp
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class RecipeRepository(private val activity: AppCompatActivity) {

    private val recipeDatabase by lazy { RecipeDatabase.getInstance(context = activity) }
    private var recipeItemList = ArrayList<RecipeResp.RecipeItem>()
    private val compositeDisposable = CompositeDisposable()

    companion object {
        const val TAG = "RecipeRepository"
    }

    init {
        fetchRecipeFromDb()
    }

    private fun fetchRecipeFromDb() {
        Log.d(TAG, "retrieveRecipe:")
        compositeDisposable.add(recipeDatabase.recipeDao().loadAllRecipes().subscribe(
            {
                if (recipeItemList.size > 0) {
                    recipeItemList.clear()
                }
                recipeItemList = ArrayList(it)
            },
            {
                Log.d("error :", it.message)
            }
        ))
    }

    fun getRecipeItems(): List<RecipeResp.RecipeItem> {
        return recipeItemList
    }

    fun fetchRecipeFromServer(query: String): Single<RecipeResp> {
        return NetworkManager.getRecipeApi().getRecipe(query).map {
            if (it.isSuccessful) {
                it.body()
            } else {
                throw Exception("Api error:" + it.message())
            }
        }
    }

    fun insertRecipe(recipeItem: RecipeResp.RecipeItem) {
        recipeDatabase.recipeDao().insert(recipe = recipeItem)
    }

//    fun convertToRecipeItem(recipeList: List<Recipe>): List<RecipeItem> {
//        Log.d("RecipeRepository", "insertAll:" + recipeItemList.size)
//        if (recipeItemList.size > 0 && recipeList.size > 0) {
//            recipeItemList.clear()
//        }
//        Observable.fromIterable(recipeList)
//            .flatMap(object : Function<Recipe, ObservableSource<RecipeItem>> {
//                override fun apply(t: Recipe): ObservableSource<RecipeItem> {
//                    return Observable.just(
//                        RecipeItem(
//                            t.title,
//                            t.publisher,
//                            t.imageUrl,
//                            t.socialRank
//                        )
//                    )
//                }
//
//            }).observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe(object : io.reactivex.Observer<RecipeItem> {
//                override fun onComplete() {
//                    Log.d("RecipeRepository", "onComplete:" + recipeItemList.size)
//
//                }
//
//                override fun onSubscribe(d: Disposable) {
//                    compositeDisposable.add(d)
//
//                }
//
//                override fun onNext(t: RecipeItem) {
//                    Log.d("RecipeRepository", "OnNext:" + t)
//                    recipeItemList.add(t)
//                }
//
//                override fun onError(e: Throwable) {
//                    Log.d("RecipeRepository", "OnError:" + e.message)
//                }
//
//            })
//        return recipeItemList
//    }

    fun saveRecipes(recipeList: ArrayList<RecipeResp.RecipeItem>): Observable<Boolean> {
        Log.d(TAG, "saveRecipe:")
        return Observable.fromCallable {
            recipeDatabase.recipeDao().insertAll(recipeList)
            true
        }
    }

    fun isRecipeEmpty(): Boolean {
        Log.d(TAG, "isRecipeEmpty:" + recipeItemList.size)
        return recipeItemList.isEmpty()
    }
}