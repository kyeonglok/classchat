package com.example.myapplication.Navigation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.LoginActivity
import com.example.myapplication.Model.userDTO
import com.example.myapplication.MyGlobals
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_mypage.*
import kotlinx.android.synthetic.main.fragment_mypage.view.*

class MyPageFragment : Fragment() {
    var auth : FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mypage, container, false)
        auth = FirebaseAuth.getInstance()


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_nickname.setText(MyGlobals.getInstance().getMyNickname())
        btn_get_classes.setOnClickListener {
            val intent = Intent(context,GetClassActivity::class.java)
            context?.startActivity(intent)
        }
        btn_logout.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
            auth?.signOut()
        }
    }

}