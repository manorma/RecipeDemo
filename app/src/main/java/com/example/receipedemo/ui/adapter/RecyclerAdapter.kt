package com.example.receipedemo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.receipedemo.R
import com.example.receipedemo.data.Post
import kotlinx.android.synthetic.main.item_post.view.*

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    companion object{
        const val TAG ="RecyclerAdapter"
    }
    private var posts :ArrayList<Post> = ArrayList<Post>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.item_post, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    public fun setPost(posts:List<Post>) {
        this.posts.addAll(posts)
        notifyDataSetChanged()
    }

    public fun getPost():List<Post>{
        return posts
    }

    public fun updatePost(post: Post){
        var index =posts.indexOf(post)
        posts.set(index,post)
        notifyItemChanged(posts.indexOf(post))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(posts.get(position))
    }


    class MyViewHolder(itemView :View): RecyclerView.ViewHolder(itemView.rootView){

        fun bind(post: Post){
            itemView.title.setText(post.title)
            if(post.comments == null){
                showProgressBar(true)
            }
            post.comments?.let {
                showProgressBar(false)
                itemView.num_comments.setText(post.comments!!.size.toString())
            }

        }

        fun showProgressBar(show:Boolean){
            if(show) {
                itemView.progress_bar.visibility = View.VISIBLE
            }
            else{
                itemView.progress_bar.visibility = View.GONE
            }
        }

    }
}