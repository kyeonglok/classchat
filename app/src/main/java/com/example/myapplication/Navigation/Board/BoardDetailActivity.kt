package com.example.myapplication.Navigation.Board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.boardDTO
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_board_detail.*
import kotlinx.android.synthetic.main.activity_board_detail.view.*
import kotlinx.android.synthetic.main.activity_board_write.*
import kotlinx.android.synthetic.main.item_board.view.*
import kotlinx.android.synthetic.main.item_class.*
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.android.synthetic.main.toolbar_board_detail.*
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.synthetic.main.item_board.view.tv_content as tv_content1
import kotlinx.android.synthetic.main.item_board.view.tv_nickname as tv_nickname1
import kotlinx.android.synthetic.main.item_board.view.tv_time as tv_time1

class BoardDetailActivity : AppCompatActivity() {
    var boardId : String? = null
    var className : String? = null
    var commentSnapshot : ListenerRegistration? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)
        boardId = intent.getStringExtra("boardId")
        className = intent.getStringExtra("className")
        tv_detail_classname.text = className + " 게시판"
        rv_comment.adapter = CommentRecyclerViewAdapter()
        rv_comment.layoutManager = LinearLayoutManager(this)
        
        bindContent()
        btn_back.setOnClickListener {
            finish()
        }


        comment_btn_send.setOnClickListener {
            val comment = boardDTO.Comment()

            comment.userId = FirebaseAuth.getInstance().currentUser!!.email
            comment.comment = comment_edit_message.text.toString()
            comment.uid = FirebaseAuth.getInstance().currentUser!!.uid
            comment.timestamp = System.currentTimeMillis()

            FirebaseFirestore.getInstance()
                    .collection("boards")
                    .document(boardId!!)
                    .collection("comments")
                    .document()
                    .set(comment)

            //commentAlarm(destinationUid!!, comment_edit_message.text.toString())
            comment_edit_message.setText("")

        }
        
    }

    override fun onStop() {
        super.onStop()
        commentSnapshot?.remove()
    }
    fun bindContent(){
        Log.d("boardId",boardId.toString())
        FirebaseFirestore.getInstance()
                .collection("boards")
                .document(boardId!!)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if(querySnapshot == null)return@addSnapshotListener
                    val content = querySnapshot.toObject(boardDTO::class.java)
                    detailviewitem_profile_textview.text = "익명"
                    tv_board_detail_title.text = content!!.title
                    detailviewitem_explain_textview.text = content!!.explain
                    tv_like_count.text = "좋아요 "+content!!.favoriteCount.toString()
                }
    }
    private fun getDateTime(s: String): String? {
        try {
            val sdf = SimpleDateFormat("MM/dd hh:mm")
            val netDate = Date(s.toLong())
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }
    inner class CommentRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var commentDTOs : MutableList<boardDTO.Comment> = mutableListOf<boardDTO.Comment>()

        init{
            commentSnapshot = FirebaseFirestore
                    .getInstance()
                    .collection("boards")
                    .document(boardId!!)
                    .collection("comments")
                    .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        commentDTOs.clear()
                        if (querySnapshot == null) return@addSnapshotListener
                        for (snapshot in querySnapshot?.documents!!) {
                            commentDTOs.add(snapshot.toObject(boardDTO.Comment::class.java)!!)
                        }
                        commentDTOs.sortBy{it.timestamp}
                        tv_comment_count.text = "댓글 "+ commentDTOs.size.toString()
                        notifyDataSetChanged()

                    }
"""
            val tmp : boardDTO.Comment = boardDTO.Comment("123","익명","자~~드가자 자~~드가자",null)
            commentDTOs.add(tmp)
            commentDTOs.add(tmp)
            commentDTOs.add(tmp)
            commentDTOs.add(tmp)
            commentDTOs.add(tmp)
            commentDTOs.add(tmp)
"""
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

            viewHolder.tv_time.text = getDateTime(commentDTOs[position].timestamp.toString())
            viewHolder.tv_content.text = commentDTOs[position].comment
            viewHolder.tv_nickname.text = "익명"

        }

        inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

}