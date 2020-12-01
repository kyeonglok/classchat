package com.example.myapplication.Navigation.Board.ClassAdapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Navigation.Board.BoardInsideActivity
import com.example.myapplication.R
import kotlinx.android.synthetic.main.item_class.view.*

class ClassListRecyclerViewAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var classList : MutableList<ClassData> = mutableListOf<ClassData>()

    init{
        //need to be get from userData
        classList.add(ClassData("GED-1234","종합설계프로젝트"))
        classList.add(ClassData("EDD-202","과학영어"))
        classList.add(ClassData("SAM-572","네트워크개론"))
        classList.add(ClassData("SAS-611","언어논리입문"))
        classList.add(ClassData("SWE-1552","핵심취업전략"))
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_class,parent,false)
        return CustumViewHolder(view)
    }

    override fun getItemCount(): Int {
        return classList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tv_class_name.text = classList[position].className
        holder.itemView.setOnClickListener {v->
            var intent = Intent(v.context,BoardInsideActivity::class.java)
            intent.putExtra("className",classList[position].className)
            intent.putExtra("classId",classList[position].classId)
            v.context.startActivity(intent)
        }

    }
    inner class CustumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}