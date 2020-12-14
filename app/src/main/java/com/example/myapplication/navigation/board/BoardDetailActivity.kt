package com.example.myapplication.navigation.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.model.boardDTO
import com.example.myapplication.R
import com.example.myapplication.model.AlarmDTO
import com.example.myapplication.utils.FcmPush
import com.example.myapplication.utils.dateUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_board_detail.*
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.android.synthetic.main.toolbar_board_detail.*
import java.text.SimpleDateFormat
import java.util.*

class BoardDetailActivity : AppCompatActivity() {
    var boardId : String? = null
    var className : String? = null
    var commentSnapshot : ListenerRegistration? = null
    var firestore : FirebaseFirestore? = null
    var destinationUid : String? = null
    var user : FirebaseUser?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)
        firestore = FirebaseFirestore.getInstance()
        user = FirebaseAuth.getInstance().currentUser
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
            commentAlarm(destinationUid!!, comment_edit_message.text.toString())
            comment_edit_message.setText("")

        }
        
    }
    fun commentAlarm(destinationUid: String, message: String) {

        val alarmDTO = AlarmDTO()
        alarmDTO.destinationUid = destinationUid
        alarmDTO.userId = user?.email
        alarmDTO.uid = user?.uid
        alarmDTO.kind = 1
        alarmDTO.message = message
        alarmDTO.timestamp = System.currentTimeMillis()
        alarmDTO.targetBoardId = className
        FirebaseFirestore.getInstance().collection("alarms").document().set(alarmDTO)

        var message = "누군가 당신의 글에 댓글을 남겼습니다 : $message"
        FcmPush.instance?.sendMessage(destinationUid, "알림 메세지 입니다.", message)
    }
    fun favoriteAlarm(destinationUid: String) {

        val alarmDTO = AlarmDTO()
        alarmDTO.destinationUid = destinationUid
        alarmDTO.userId = user?.email
        alarmDTO.uid = user?.uid
        alarmDTO.kind = 0
        alarmDTO.timestamp = System.currentTimeMillis()
        alarmDTO.targetBoardId = className

        FirebaseFirestore.getInstance().collection("alarms").document().set(alarmDTO)
        var message = "누군가 당신의 글에 좋아요를 눌렀습니다."
        FcmPush.instance?.sendMessage(destinationUid, "알림 메세지 입니다.", message)
    }
    override fun onStop() {
        super.onStop()
        commentSnapshot?.remove()
    }
    private fun bindContent(){
        firestore?.
                collection("boards")?.document(boardId!!)
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if(querySnapshot == null)return@addSnapshotListener
                    val content = querySnapshot.toObject(boardDTO::class.java)
                    destinationUid = content?.uid
                    detailviewitem_profile_textview.text = "익명"
                    tv_board_detail_title.text = content!!.title
                    detailviewitem_explain_textview.text = content!!.explain
                    if (content.favorites.containsKey(FirebaseAuth.getInstance().currentUser!!.uid)) {
                        btn_heart.setBackgroundResource(R.drawable.class_board_ic_heart)

                    } else {
                        btn_heart.setBackgroundResource(R.drawable.class_board_detail_ic_heart_unactivated)
                    }
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
                contentDTO?.favoriteCount = contentDTO?.favoriteCount!! - 1
                contentDTO?.favorites.remove(uid)
            } else {
                // Star the post and add self to stars
                contentDTO?.favoriteCount = contentDTO?.favoriteCount!! + 1
                contentDTO?.favorites[uid] = true
                favoriteAlarm(contentDTO.uid!!)
            }
            transaction.set(tsDoc, contentDTO)
            bindContent()
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

            viewHolder.tv_time.text = dateUtils.parseTime(commentDTOs[position].timestamp)
            viewHolder.tv_content.text = commentDTOs[position].comment
            viewHolder.tv_nickname.text = "익명"


        }

        inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

}