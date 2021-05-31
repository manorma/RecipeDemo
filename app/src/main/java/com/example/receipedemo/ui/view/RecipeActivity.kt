package com.example.receipedemo.ui.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.receipedemo.R
import com.example.receipedemo.ui.adapter.RecipeAdapter
import com.example.receipedemo.ui.viewmodel.RecipeViewModel
import com.example.receipedemo.ui.viewmodel.ViewModelFactory
import com.jakewharton.rxbinding4.widget.textChangeEvents
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_recipe.*
import java.util.concurrent.TimeUnit

class RecipeActivity : AppCompatActivity(), RecipeAdapter.OnRecipelickListener {

    lateinit var viewModel: RecipeViewModel
    lateinit var recipeAdapter: RecipeAdapter
    private lateinit var disposable: io.reactivex.rxjava3.disposables.Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        val factory = ViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, factory).get(RecipeViewModel::class.java)
        initRecyclerView()
        setRxTextView()
        initSubscriber()
    }

    private fun setRxTextView() {
        disposable = search_view.textChangeEvents()
            .skipInitialValue()
            .map { it.text.toString() }
            .debounce(800, TimeUnit.MILLISECONDS)
            .filter { it.isNotEmpty() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.computation())
            .subscribe { viewModel.fetchRecipe(it) }
    }

    private fun initSubscriber() {
        viewModel.recipeList.observe(this, {
            recipeAdapter.setRecipe(it)
        })
    }

    private fun initRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recipeAdapter = RecipeAdapter()
        recipeAdapter.setListener(this)
        recycler_view.adapter = recipeAdapter
    }

    override fun onRecipeClick() {

    }
}