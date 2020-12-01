package com.example.myapplication.Navigation.Board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_board_write.*
import kotlinx.android.synthetic.main.activity_board_write.view.*
import kotlinx.android.synthetic.main.toolbar_board_write.*

class BoardWriteActivity : AppCompatActivity() {
    var className : String?= null
    var classId : String?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_write)

        className = intent.getStringExtra("className")
        classId = intent.getStringExtra("classId")

        tv_board_category.text = className + " 게시판"

        btn_back.setOnClickListener {
            finish()
        }
    }
}