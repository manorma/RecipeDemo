package com.example.receipedemo.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.receipedemo.R
import com.example.receipedemo.data.model.RecipeResp
import kotlinx.android.synthetic.main.recipe_item.view.*

class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
    private var recipeList = ArrayList<RecipeResp.RecipeItem>()
    private lateinit var recipeListener: OnRecipelickListener
    lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        mContext = parent.context
        return RecipeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recipe_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        Log.d("RecipeAdapter", "onBind$position")
        holder.bind(recipeList[position])
    }

    fun setListener(listener: OnRecipelickListener) {
        this.recipeListener = listener
    }

    fun setRecipe(result: ArrayList<RecipeResp.RecipeItem>) {
        Log.d("RecipeAdapter", "setRecipe")
        recipeList.clear()
        recipeList.addAll(result)
        notifyDataSetChanged()
    }

    interface OnRecipelickListener {
        fun onRecipeClick()
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView.rootView) {
        fun bind(recipe: RecipeResp.RecipeItem) {
            Log.d("RecipeAdapter", "bind$recipe")
            itemView.title.text = recipe.title
            itemView.publisher.text = recipe.publisher
            itemView.rank.text = recipe.socialRank.toString()
            Glide.with(mContext)
                .load(recipe.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(itemView.imageView)
            itemView.imageView
            itemView.setOnClickListener { recipeListener.onRecipeClick() }
        }
    }
}