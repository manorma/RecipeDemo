package com.example.receipedemo.api

import com.example.receipedemo.data.model.RecipeResp
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApi {
    @GET("/api/search")
    fun getRecipe(@Query("q") search: String): Single<Response<RecipeResp>>
}