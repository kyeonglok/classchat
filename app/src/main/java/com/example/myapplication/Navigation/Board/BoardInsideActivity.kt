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
import com.example.myapplication.Navigation.Board.ClassAdapter.ClassData
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_board_inside.*
import kotlinx.android.synthetic.main.item_board.view.*
import kotlinx.android.synthetic.main.toolbar_board_inside.*
import kotlinx.android.synthetic.main.toolbar_board_inside.view.*
import java.util.ArrayList

class BoardInsideActivity : AppCompatActivity() {
    var mainView: View? = null
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

        btn_plus.setOnClickListener { v->
            //when write button clicked
            var intent = Intent(v.context,BoardWriteActivity::class.java)
            intent.putExtra("className",className)
            intent.putExtra("classId",classId)
            v.context.startActivity(intent)
        }

        rv_board_inside.adapter = BoardInsideRecyclerViewAdapter()
        rv_board_inside.layoutManager = LinearLayoutManager(this)
    }

    inner class BoardInsideRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var contentUidList: MutableList<String> = mutableListOf<String>()
        var boardDTOs : MutableList<boardDTO> = mutableListOf<boardDTO>()

        init{
            var tmpBoardDTO : boardDTO = boardDTO("교양있는 현대인의 첫걸음","랄투부 구독",null,"greenday","익명",null,2400)
            boardDTOs.add(tmpBoardDTO)
            contentUidList.add("!#FJADKFJAHFKDHF!fd")
            boardDTOs.add(tmpBoardDTO)
            contentUidList.add("!#FJADKFJAHFKDHF!fd")
            boardDTOs.add(tmpBoardDTO)
            contentUidList.add("!#FJADKFJAHFKDHF!fd")
            boardDTOs.add(tmpBoardDTO)
            contentUidList.add("!#FJADKFJAHFKDHF!fd")
            boardDTOs.add(tmpBoardDTO)
            contentUidList.add("!#FJADKFJAHFKDHF!fd")
            boardDTOs.add(tmpBoardDTO)
            contentUidList.add("!#FJADKFJAHFKDHF!fd")
            boardDTOs.add(tmpBoardDTO)
            contentUidList.add("!#FJADKFJAHFKDHF!fd")
            boardDTOs.add(tmpBoardDTO)
            contentUidList.add("!#FJADKFJAHFKDHF!fd")
            boardDTOs.add(tmpBoardDTO)
            contentUidList.add("!#FJADKFJAHFKDHF!fd")
            boardDTOs.add(tmpBoardDTO)
            contentUidList.add("!#FJADKFJAHFKDHF!fd")
            boardDTOs.add(tmpBoardDTO)
            contentUidList.add("!#FJADKFJAHFKDHF!fd")
            boardDTOs.add(tmpBoardDTO)
            contentUidList.add("!#FJADKFJAHFKDHF!fd")
            boardDTOs.add(tmpBoardDTO)
            contentUidList.add("!#FJADKFJAHFKDHF!fd")
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_board, parent, false)
            return CustomViewHolder(view)
        }

        override fun getItemCount(): Int {
            return boardDTOs.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = (holder as CustomViewHolder).itemView

            viewHolder.tv_title.text = boardDTOs[position].title
            viewHolder.tv_content.text = boardDTOs[position].explain
            viewHolder.tv_like_count.text = "좋아요 "+boardDTOs[position].favoriteCount.toString()
            viewHolder.tv_nickname.text = boardDTOs[position].userId

            viewHolder.setOnClickListener {v->
                var intent = Intent(v.context,BoardDetailActivity::class.java)
                intent.putExtra("contentUid",contentUidList[position])
                v.context.startActivity(intent)
            }
        }

        inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

}