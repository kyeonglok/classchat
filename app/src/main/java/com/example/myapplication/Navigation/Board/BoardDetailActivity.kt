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
    var firestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)
        firestore = FirebaseFirestore.getInstance()
        boardId = intent.getStringExtra("boardId")
        className = intent.getStringExtra("className")
        tv_detail_classname.text = className + " 게시판"
        rv_comment.adapter = CommentRecyclerViewAdapter()
        rv_comment.layoutManager = LinearLayoutManager(this)
        btn_heart.setOnClickListener { favoriteEvent() }
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
    private fun bindContent(){
        Log.d("boardId",boardId.toString())
        firestore?.
                collection("boards")?.document(boardId!!)
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if(querySnapshot == null)return@addSnapshotListener
                    val content = querySnapshot.toObject(boardDTO::class.java)
                    detailviewitem_profile_textview.text = "익명"
                    tv_board_detail_title.text = content!!.title
                    detailviewitem_explain_textview.text = content!!.explain
                    if (content.favorites.containsKey(FirebaseAuth.getInstance().currentUser!!.uid)) {
                        btn_heart.setBackgroundResource(R.drawable.class_board_ic_heart)

                    } else {
                        btn_heart.setBackgroundResource(R.drawable.class_board_detail_ic_heart_unactivated)
                    }
                    Log.i("bind","done bind")
                    tv_like_count.text = "좋아요 "+content!!.favoriteCount.toString()
                }
    }
    private fun favoriteEvent(){
        var tsDoc = firestore?.collection("boards")?.document(boardId!!)
        firestore?.runTransaction { transaction ->

            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val contentDTO = transaction.get(tsDoc!!).toObject(boardDTO::class.java)
            Log.i("favoriteEvenet",contentDTO.toString())
            if (contentDTO!!.favorites.containsKey(uid)) {
                // Unstar the post and remove self from stars
                Log.i("unstar","unstar")
                contentDTO?.favoriteCount = contentDTO?.favoriteCount!! - 1
                contentDTO?.favorites.remove(uid)
            } else {
                // Star the post and add self to stars
                contentDTO?.favoriteCount = contentDTO?.favoriteCount!! + 1
                contentDTO?.favorites[uid] = true
                Log.i("star","star")
                //favoriteAlarm(contentDTOs[position].uid!!)
            }
            transaction.set(tsDoc, contentDTO)
            bindContent()
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
            commentSnapshot = firestore?.collection("boards")
                    ?.document(boardId!!)
                    ?.collection("comments")
                    ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        commentDTOs.clear()
                        if (querySnapshot == null) return@addSnapshotListener
                        for (snapshot in querySnapshot?.documents!!) {
                            commentDTOs.add(snapshot.toObject(boardDTO.Comment::class.java)!!)
                        }
                        commentDTOs.sortBy{it.timestamp}
                        tv_comment_count.text = "댓글 "+ commentDTOs.size.toString()
                        val updates = mutableMapOf<String,Any>()
                        updates["commentCount"] = commentDTOs.size
                        firestore?.collection("boards")?.document(boardId!!)?.update(updates)
                        notifyDataSetChanged()

                    }
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