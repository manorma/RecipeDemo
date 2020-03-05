package com.example.receipedemo.api

import com.example.receipedemo.data.Comment
import retrofit2.http.GET
import com.example.receipedemo.data.Post
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Path


interface PostApi{

    @GET("posts")
    fun getPosts(): Single<ArrayList<Post>>

    @GET("posts/{id}/comments")
    fun getComments(
        @Path("id") id: Int
    ): Observable<ArrayList<Comment>>
}