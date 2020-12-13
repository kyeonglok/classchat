package com.example.myapplication.Navigation.Board

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.myapplication.Model.boardDTO
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_board_write.*
import kotlinx.android.synthetic.main.activity_board_write.view.*
import kotlinx.android.synthetic.main.toolbar_board_write.*
import java.text.SimpleDateFormat
import java.util.*

class BoardWriteActivity : AppCompatActivity() {
    var className : String?= null
    var classId : String?= null

    var storage: FirebaseStorage? = null
    var firestore: FirebaseFirestore? = null
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_write)


        // Firebase storage
        storage = FirebaseStorage.getInstance()
        // Firebase Database
        firestore = FirebaseFirestore.getInstance()
        // Firebase Auth
        auth = FirebaseAuth.getInstance()

        className = intent.getStringExtra("className")
        classId = intent.getStringExtra("classId")

        tv_board_category.text = className + " 게시판"

        btn_back.setOnClickListener {
            finish()
        }
        btn_write.setOnClickListener {
            contentUpload()
        }
    }


    fun contentUpload(){
        progress_bar.visibility = View.VISIBLE

        // 글 생성
        val newBoard = boardDTO()

        //유저의 UID
        newBoard.uid = auth?.currentUser?.uid
        //게시물의 설명
        newBoard.explain = tv_explain.text.toString()
        //유저의 아이디
        newBoard.userId = auth?.currentUser?.email
        //게시물 업로드 시간
        newBoard.timestamp = System.currentTimeMillis() * -1
        //게시물 제목
        newBoard.title = tv_title.text.toString()

        newBoard.boardClassId = classId
        newBoard.boardClassName = className

        //게시물을 데이터를 생성 및 엑티비티 종료
        firestore?.collection("boards")?.document()?.set(newBoard)

        finish()
    }
}