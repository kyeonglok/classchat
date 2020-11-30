package com.example.myapplication.Navigation.Board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.R
import kotlinx.android.synthetic.main.toolbar_board_inside.*
import kotlinx.android.synthetic.main.toolbar_board_inside.view.*

class BoardInsideActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_inside)
        val className = intent.getStringExtra("className")
        val classId = intent.getStringExtra("classId")
        tv_classname.text = className + " 게시판"

        btn_back.setOnClickListener {
            finish()
        }

        btn_search.setOnClickListener {
           //when search button clicked
        }

        btn_plus.setOnClickListener {
            //when write button clicked
        }
    }
}