package com.example.receipedemo.ui.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.receipedemo.R
import com.example.receipedemo.ui.adapter.RecipeAdapter
import com.example.receipedemo.ui.viewmodel.RecipeViewModel
import com.example.receipedemo.ui.viewmodel.ViewModelFactory
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_recipe.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

class RecipeActivity :AppCompatActivity(),RecipeAdapter.OnRecipelickListener{
    override fun onRecipeClick() {

    }

    lateinit var viewModel:RecipeViewModel
    lateinit var recipeAdapter:RecipeAdapter
    val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        val factory = ViewModelFactory(this)
        viewModel = ViewModelProviders.of(this,factory).get(RecipeViewModel::class.java)
        initRecyclerView()
        subscribeToSearch(getSearchObservable())
        initSubscriber()
    }

    private fun getSearchObservable() :Observable<String>{

        return Observable.create(object :ObservableOnSubscribe<String>{
            override fun subscribe(emitter: ObservableEmitter<String>) {
                search_view.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if(!emitter.isDisposed){
                            newText?.let {
                                emitter.onNext(newText!!)
                            }
                        }
                        return false
                    }

                })
            }

        }).debounce(1000,TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())

    }

    private fun subscribeToSearch(observable: Observable<String>) {
        disposable.add(observable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribeWith(object :
            DisposableObserver<String>() {
            override fun onComplete() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onNext(t: String) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(e: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }));





        observable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : io.reactivex.Observer<String> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onNext(t: String) {
                    Log.d(RecipeViewModel.TAG,"fetchRecipe :"+t)
                    viewModel.fetchRecipe(t)
                }

                override fun onError(e: Throwable) {
                    Log.d(RecipeViewModel.TAG,"onError :"+e.message)
                }

                override fun onComplete(){
                    Log.d(RecipeViewModel.TAG,"onComplete :")
                }

            })

    }
    private fun initSubscriber() {
        viewModel.recipeList.observe(this, Observer {
            Log.d("RecipeActivity","initSubscriber " )
            recipeAdapter.setRecipe(it)
        })
    }

    private fun initRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recipeAdapter = RecipeAdapter()
        recipeAdapter.setLisener(this)
        recycler_view.adapter = recipeAdapter
    }

}