package com.example.receipedemo.ui.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.receipedemo.R
import com.example.receipedemo.api.NetworkManager
import com.example.receipedemo.data.Comment
import com.example.receipedemo.data.Post
import com.example.receipedemo.ui.adapter.RecyclerAdapter
import com.example.receipedemo.ui.viewmodel.PostViewModel
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity() {

    companion object {
        const val TAG: String = "SearchActivity"
    }

    lateinit var adapter: RecyclerAdapter
    lateinit var disposable: CompositeDisposable
    lateinit var viewModel: PostViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)
        viewModel = ViewModelProviders.of(this).get(PostViewModel::class.java)
        disposable = CompositeDisposable()
        initRecyclerView()
        initSearchObservable()
        setObserver()


    }

    private fun setObserver() {
        viewModel.posts.observe(this, Observer {
            adapter.setPost(it)
        })
    }


    private fun initSearchObservable() {
        var timeSinceLastRequest = System.currentTimeMillis()
        val observable = Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String>) {

                search_view.setOnQueryTextListener(object :
                    androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (!emitter.isDisposed) {
                            newText?.let {
                                emitter.onNext(newText)
                            }

                        }
                        return false
                    }

                })
            }
        }).debounce(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())

        observable.subscribe(object : io.reactivex.Observer<String> {
            override fun onComplete() {
                Log.d(TAG, "onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, "onSubscribe")
            }

            override fun onNext(t: String) {
                Log.d(
                    TAG,
                    "onNext: time  since last request: " + (System.currentTimeMillis() - timeSinceLastRequest)
                );
                Log.d(TAG, "onNext: search query: " + t);
                timeSinceLastRequest = System.currentTimeMillis();

                // method for sending a request to the server


            }

            override fun onError(e: Throwable) {
                Log.d(TAG, "onError")
            }

        })
    }


    private fun initRecyclerView() {
        adapter = RecyclerAdapter()
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter
    }


    override fun onDestroy() {
        super.onDestroy()

    }
}
