package com.example.myapplication.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MyGlobals
import com.example.myapplication.R
import com.example.myapplication.model.AlarmDTO
import com.example.myapplication.utils.dateUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.fragment_alarm.*
import kotlinx.android.synthetic.main.fragment_alarm.view.*
import kotlinx.android.synthetic.main.fragment_board.*
import kotlinx.android.synthetic.main.item_alarm.view.*
import java.util.ArrayList

class AlarmFragment : Fragment() {
    var alarmSnapshot: ListenerRegistration? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_alarm, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.alarmfragment_recyclerview.adapter = AlarmRecyclerViewAdapter()
        view.alarmfragment_recyclerview.layoutManager = LinearLayoutManager(activity)
    }

    override fun onStop() {
        super.onStop()
        alarmSnapshot?.remove()
    }
    inner class AlarmRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        val alarmDTOList = ArrayList<AlarmDTO>()

        init {

            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            Log.i("curuid",uid)
            FirebaseFirestore.getInstance()
                    .collection("alarms")
                    .whereEqualTo("destinationUid", uid)
                    .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        alarmDTOList.clear()
                        if(querySnapshot == null)return@addSnapshotListener
                        for (snapshot in querySnapshot?.documents!!) {
                            Log.i("alarm",snapshot.toString())
                            alarmDTOList.add(snapshot.toObject(AlarmDTO::class.java)!!)
                        }
                        alarmDTOList.sortByDescending { it.timestamp }
                        if(alarmDTOList.size == 0){
                            if(alarmfragment_recyclerview != null)
                                alarmfragment_recyclerview.visibility = View.GONE
                            tv_no_alarm.visibility = View.VISIBLE
                        }
                        else{
                            if(alarmfragment_recyclerview != null)
                                alarmfragment_recyclerview.visibility = View.VISIBLE
                            tv_no_alarm.visibility = View.GONE
                        }
                        notifyDataSetChanged()
                    }
            Log.d("alarm",alarmDTOList.toString())
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alarm, parent, false)
            return CustomViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val boardName = holder.itemView.tv_alarm_board_name
            val alarmTextView = holder.itemView.tv_alarm_content
            val alarmImageView = holder.itemView.img_alarm
            val alarmTimeTv = holder.itemView.tv_alarm_time
            /*
            FirebaseFirestore.getInstance().collection("profileImages")
                    .document(alarmDTOList[position].uid!!).get().addOnCompleteListener {
                        task ->
                        if(task.isSuccessful){
                            val url = task.result["image"]
                            Glide.with(activity)
                                    .load(url)
                                    .apply(RequestOptions().circleCrop())
                                    .into(profileImage)
                        }
                    }
*/
            boardName.text = alarmDTOList[position].targetBoardId + " 게시판"
            alarmTimeTv.text = dateUtils.getDateTime(alarmDTOList[position].timestamp.toString())
            when (alarmDTOList[position].kind) {
                0 -> {
                    val str_0 = "누군가가 당신의 글을 좋아합니다."
                    alarmImageView.setImageResource(R.drawable.class_board_detail_ic_heart_unactivated_hdpi)
                    alarmTextView.text = str_0
                }

                1 -> {
                    val str_1 = "새로운 댓글이 달렸어요: " + alarmDTOList[position].message
                    alarmImageView.setImageResource(R.drawable.class_board_ic_reply_hdpi)
                    alarmTextView.text = str_1
                }
            }
        }

        override fun getItemCount(): Int {

            return alarmDTOList.size
        }
        inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    }
}