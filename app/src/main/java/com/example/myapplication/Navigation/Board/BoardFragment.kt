package com.example.myapplication.Navigation.Board

import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Navigation.Board.ClassAdapter.ClassData
import com.example.myapplication.Navigation.Board.ClassAdapter.ClassListRecyclerViewAdapter
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_board.view.*
import kotlinx.android.synthetic.main.item_class.view.*


class BoardFragment : Fragment() {
    lateinit var classListAdapter : ClassListRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_board, container, false)
        classListAdapter = ClassListRecyclerViewAdapter()
        view.boardFragment_recyclerview.adapter = classListAdapter
        view.boardFragment_recyclerview.layoutManager = LinearLayoutManager(activity)
        return view
    }

}