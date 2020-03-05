package com.example.receipedemo.ui.view


import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.receipedemo.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnPost.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(this@HomeActivity,SearchActivity::class.java)
                startActivity(intent)

            }

        })

        btnObserveable.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(this@HomeActivity,MainActivity::class.java)
                startActivity(intent)
            }

        })

        btnRecipe.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(this@HomeActivity,RecipeActivity::class.java)
                startActivity(intent)
            }

        })

        btnStart.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(this@HomeActivity,GameActivity::class.java)
                startActivity(intent)
            }

        })


    }
}