package com.example.receipedemo.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NetworkManager {

    companion object{
        private const val BASE_URL ="https://jsonplaceholder.typicode.com";
        private const val RECIPE_BASE_URL = "http://recipesapi.herokuapp.com/api/"




        fun getRequestApi():PostApi{
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level= HttpLoggingInterceptor.Level.BODY


            val okhttp = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okhttp)
                .build()

            return retrofit.create(PostApi::class.java)

        }

        fun getRecipeApi():RecipeApi{

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level= HttpLoggingInterceptor.Level.BODY


            val okhttp = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(RECIPE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okhttp)
                .build()

            return retrofit.create(RecipeApi::class.java)
        }

    }
}