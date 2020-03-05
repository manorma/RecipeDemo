package com.example.receipedemo.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.receipedemo.R
import com.example.receipedemo.data.DataSource
import com.example.receipedemo.data.Task
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Callable

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG: String = "MainActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var dis = CompositeDisposable()

        //create observabl using fromIterable
        val observable1 = Observable.fromIterable(DataSource.createTakList())
            .subscribeOn(Schedulers.io())
            .filter {
                Log.d(TAG, "onFilter:" + Thread.currentThread().name)
                return@filter it.inComplete
            }
            .observeOn(AndroidSchedulers.mainThread())


        //create observable using create

        val observable2 = Observable.create(object : ObservableOnSubscribe<Task> {
            override fun subscribe(emitter: ObservableEmitter<Task>) {
                DataSource.createTakList().forEach {
                    if(!emitter.isDisposed){
                        emitter.onNext(it)
                    }

                }
                emitter.onComplete()
            }

        }).subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())

        val observable3  = Observable.fromCallable(object :Callable<List<Task>>{
            override fun call(): List<Task> {
                return DataSource.createTakList()
            }

        })

        observable1.subscribe(object : Observer<Task> {
            override fun onComplete() {
                Log.d(TAG, "onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, "onSubscribe")
                dis.add(d)
            }

            override fun onNext(t: Task) {
                Log.d(TAG, "onNext:" + Thread.currentThread().name)
                Log.d(TAG, "onNext:" + t.description)
                text.setText(t.description)
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, "onError")
            }

        })



    }
}
