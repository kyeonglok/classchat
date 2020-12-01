package com.example.myapplication.Navigation.Board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.boardDTO
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_board_detail.*
import kotlinx.android.synthetic.main.activity_board_detail.view.*
import kotlinx.android.synthetic.main.activity_board_write.*
import kotlinx.android.synthetic.main.item_board.view.*
import kotlinx.android.synthetic.main.item_class.*
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.android.synthetic.main.toolbar_board_detail.*
import kotlinx.android.synthetic.main.item_board.view.tv_content as tv_content1
import kotlinx.android.synthetic.main.item_board.view.tv_nickname as tv_nickname1

class BoardDetailActivity : AppCompatActivity() {
    var contentUid : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)
        contentUid = intent.getStringExtra("contentUid")
        //tv_detail_classname.text = contentUid + " 게시판"
        rv_comment.adapter = CommentRecyclerViewAdapter()
        rv_comment.layoutManager = LinearLayoutManager(this)
        //rv_comment.isNestedScrollingEnabled = false
        btn_back.setOnClickListener {
            finish()
        }
    }

    inner class CommentRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var commentDTOs : MutableList<boardDTO.Comment> = mutableListOf<boardDTO.Comment>()

        init{
            val tmp : boardDTO.Comment = boardDTO.Comment("123","익명","자~~드가자 자~~드가자",null)
            commentDTOs.add(tmp)
            commentDTOs.add(tmp)
            commentDTOs.add(tmp)
            commentDTOs.add(tmp)
            commentDTOs.add(tmp)
            commentDTOs.add(tmp)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
            return CustomViewHolder(view)
        }

        override fun getItemCount(): Int {
            return commentDTOs.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = (holder as CustomViewHolder).itemView

            viewHolder.tv_content.text = commentDTOs[position].comment
            viewHolder.tv_nickname.text = commentDTOs[position].userId

        }

        inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

}