package com.example.receipedemo.data

data class Post(
    var userId: Int,
    var id: Int,
    var title :String,
    var body:String,
    var comments:List<Comment>?
){


}

data class Comment(
    val postId :Int,
    val id:Int,
    val name:String,
    val email:String,
    val body:String
    ){
}