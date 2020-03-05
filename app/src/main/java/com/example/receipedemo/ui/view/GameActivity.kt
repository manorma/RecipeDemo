package com.example.receipedemo.ui.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.receipedemo.R
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity(), View.OnClickListener {

    var isPlayer1 = true
    var player1_count = 0
    var player2_count = 0
    var turn = 0
    var isPause = true
    var isWinner = false
    var isDraw = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        supportActionBar?.title = "TicTacToe"
        reset()
        setLisener()


    }

    override fun onClick(v: View?) {
        Log.d("GameActivity","onClick" +isPlayer1+turn)
        val v :Button = v as Button

        if (isPlayer1) {
            v.text = "X"
        } else {
            v.text = "O"
        }
        turn++
        if (checkWinner()) {
            Log.d("GameActivity","checkWinner "+isPlayer1)
            if(isPlayer1){
                player1_count++;
                tvWinner.text = "Winner : Player1"
                Toast.makeText(this,"Player1 won the match",Toast.LENGTH_SHORT).show()
            }
            else{
                player2_count++;
                tvWinner.text = "Winner : Player2"
                Toast.makeText(this,"Player1 won the match",Toast.LENGTH_SHORT).show()
            }
            showWinnerView()

        }else if(turn == 9){
            drawMatch()
            return
        }
        else{
            isPlayer1 = !isPlayer1
        }

    }

    fun showWinnerView(){
        isWinner = true
        showWinner()
        isPause = false
        btn_start.text = "Start"
        tvPlayer1.text = "Player1 : "+player1_count
        tvPlayer2.text = "Player2 : "+player2_count
        setClickable(false)

    }

    fun hideWinnerView(){
        tvWinner.visibility = View.GONE
    }

    fun showWinner(){
        tvWinner.visibility = View.VISIBLE
    }

    fun drawMatch(){
        Log.d("GameActivity","drawMatch")
        isPause = false
        isDraw = true
        btn_start.text = "Start"
        tvWinner.text = "Match Draw"
        tvWinner.visibility = View.VISIBLE
        tvPlayer1.text = "Player1 : "+player1_count
        tvPlayer2.text = "Player2 : "+player2_count

    }

    fun checkWinner(): Boolean {
        Log.d("GameActivity","checkWinner")
        Log.d("GameActivity","value" +btn_01.text +btn_02.text+btn_03.text)

        if (btn_01.text.equals(btn_02.text) && btn_01.text.equals(btn_03.text) && btn_01.text != "") {
            return true
        } else if (btn_11.text.equals(btn_12.text) && btn_11.text.equals(btn_13.text) && btn_11.text != "") {
            return true
        } else if (btn_21.text.equals(btn_22.text) && btn_21.text.equals(btn_23.text) && btn_21.text != "") {
            return true
        } else if (btn_01.text.equals(btn_11.text) && btn_01.text.equals(btn_21.text) && btn_01.text != "") {
            return true
        } else if (btn_02.text != "" && btn_02.text.equals(btn_12.text) && btn_02.text.equals(btn_22.text)) {
            return true
        } else if (btn_03.text != "" && btn_03.text.equals(btn_13.text) && btn_03.text.equals(btn_23.text)) {
            return true
        } else if (btn_01.text != "" && btn_01.text.equals(btn_12.text) && btn_01.text.equals(btn_23.text)) {
            return true
        } else if (btn_03.text != "" && btn_03.text.equals(btn_12.text) && btn_03.text.equals(btn_21.text)) {
            return true
        }

        return false

    }

    private fun setLisener() {
        btn_01.setOnClickListener(this)
        btn_02.setOnClickListener(this)
        btn_03.setOnClickListener(this)
        btn_11.setOnClickListener(this)
        btn_12.setOnClickListener(this)
        btn_13.setOnClickListener(this)
        btn_21.setOnClickListener(this)
        btn_22.setOnClickListener(this)
        btn_23.setOnClickListener(this)
        btn_reset.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                reset()
            }

        })

        btn_start.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if(isPause){
                    setClickable(false)
                    btn_start.text = "Start"

                }else {
                    btn_start.text = "Pause"
                    setClickable(true)
                    if(isWinner ==  true || isDraw == true){
                        resetBtn()
                        turn = 0
                        isWinner = false
                        isDraw = false
                    }
                }
                hideWinnerView()
                isPause = !isPause
            }

        })


    }


    fun setClickable(clickable:Boolean){
        btn_01.isClickable = clickable
        btn_02.isClickable = clickable
        btn_03.isClickable = clickable
        btn_11.isClickable = clickable
        btn_12.isClickable = clickable
        btn_13.isClickable = clickable
        btn_21.isClickable = clickable
        btn_22.isClickable = clickable
        btn_23.isClickable = clickable
    }
    fun reset() {
        turn = 0
        resetBtn()
        hideWinnerView()
        setClickable(true)
        isPause = true
        btn_start.text = "Pause"
        player1_count = 0
        player2_count = 0
        tvPlayer1.text = "Player1 : "+player1_count
        tvPlayer2.text = "Player2 : "+player2_count

    }

    fun resetBtn(){
        btn_01.text = ""
        btn_02.text = ""
        btn_03.text = ""
        btn_11.text = ""
        btn_12.text = ""
        btn_13.text = ""
        btn_21.text = ""
        btn_22.text = ""
        btn_23.text = ""
    }
}