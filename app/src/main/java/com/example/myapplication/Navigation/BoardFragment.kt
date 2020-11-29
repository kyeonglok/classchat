package com.example.myapplication.Navigation

import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R


class BoardFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_board, container, false)
        view.boardFragment_recyclerview.adapter = ClassListRecyclerViewAdapter()

        return view
    }

    inner class ClassListRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        val ClassList = ArrayList<String>()

        init{
            //need to be get from userData
            ClassList.add("종합설계프로젝트")
            ClassList.add("과학영어")
            ClassList.add("네트워크개론")
            ClassList.add("언어논리입문")
            ClassList.add("핵심취업전략")
            notifyDataSetChanged()
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_class,parent,false)
            return CustumViewHolder(view)
        }



        override fun getItemCount(): Int {
            return ClassList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            TODO("Not yet implemented")
        }
        inner class CustumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}