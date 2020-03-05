package com.example.receipedemo.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.receipedemo.api.NetworkManager
import com.example.receipedemo.data.Comment
import com.example.receipedemo.data.Post
import com.example.receipedemo.ui.view.SearchActivity
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers

class PostViewModel :ViewModel(){
    var posts = MutableLiveData<ArrayList<Post>>()
    var disposable:CompositeDisposable
    init {
        disposable = CompositeDisposable()
    }
    companion object{
        const val TAG= "PostViewModel"
    }

    internal fun fetchPost(){
        getPostObservable().subscribeOn(Schedulers.io())
            .concatMap(object: Function<Post, ObservableSource<Post>>{ //if you want to load comment in order up - down
                override fun apply(t: Post): ObservableSource<Post> {
                    return getCommentObservable(t)
                }

            }
            ).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Post> {
                override fun onComplete() {
                    Log.d(TAG,"onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    Log.d(TAG,"onSubscribe")
                    disposable.add(d)
                }

                override fun onNext(t: Post) {
                    updatePost(t)
                }

                override fun onError(e: Throwable) {
                    Log.d(TAG,"onError"+e.message)
                }

            })
    }

    private fun getPostObservable(): Observable<Post> {
        return NetworkManager.getRequestApi().getPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMapObservable {
                Observable.fromIterable(it)
            }



    }

    private fun getCommentObservable(post: Post):Observable<Post>{
        return NetworkManager.getRequestApi().getComments(post.id).map(
            object : io.reactivex.functions.Function<List<Comment>, Post> {
                override fun apply(t: List<Comment>): Post {
                    post.comments = t
                    return post
                }

            }
        )
    }

    fun updatePost(post: Post){
        Observable.fromIterable(posts.value)
            .filter(object : Predicate<Post> {
                override fun test(t: Post): Boolean {
                    return post.id == t.id
                }

            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :Observer<Post>{
                override fun onComplete() {
                    Log.d(SearchActivity.TAG,"updatePost onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    Log.d(SearchActivity.TAG,"updatePost onSubscribe")
                    disposable.add(d)
                }

                override fun onNext(t: Post) {
                    Log.d(SearchActivity.TAG,"updatePost onNext")
                    val index = posts.value?.indexOf(t)
                    posts.value?.set(index!!,t)
                }

                override fun onError(e: Throwable) {
                    Log.d(SearchActivity.TAG,"updatePost onError")
                }

            })
    }




}