package com.example.receipedemo.api

import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class NetworkManager {

    companion object{
        private const val BASE_URL ="https://jsonplaceholder.typicode.com";
        private const val RECIPE_BASE_URL = "http://recipesapi.herokuapp.com/api/"

        fun getRequestApi():PostApi{
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level= HttpLoggingInterceptor.Level.BODY
            val okhttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okhttpClient)
                .build()
            return retrofit.create(PostApi::class.java)

        }

        fun getRecipeApi():RecipeApi{
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level= HttpLoggingInterceptor.Level.BODY
            val okhttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(RECIPE_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(okhttpClient)
                .build()
            return retrofit.create(RecipeApi::class.java)
        }
    }
}