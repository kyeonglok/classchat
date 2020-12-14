package com.example.myapplication.navigation

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.LoginActivity
import com.example.myapplication.MyGlobals
import com.example.myapplication.R
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import kotlinx.android.synthetic.main.fragment_mypage.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MyPageFragment : Fragment() {
    var auth : FirebaseAuth? = null
    var filepath : Uri? = null
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
        setProfileImg()
        my_profile_picture.setOnClickListener {
            val oItems = arrayOf("프로필 사진 업로드", "프로필 사진 삭제")
            val oDialog = AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
            var listener = DialogInterface.OnClickListener { _, which ->
                when (which) {
                    0 -> set_and_upload_img()
                    1 -> delete_img()
                }
            }
            oDialog.setItems(oItems, listener).show()
        }
        btn_get_classes.setOnClickListener {
            val intent = Intent(context,GetClassActivity::class.java)
            context?.startActivity(intent)
        }
        btn_changenickname.setOnClickListener {
            val intent = Intent(context,ChangeNicknameActivity::class.java)
            context?.startActivity(intent)
        }
        btn_changepwd.setOnClickListener {
            val intent = Intent(context,ChangePwdActivity::class.java)
            context?.startActivity(intent)
        }
        btn_logout.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
            auth?.signOut()
        }
    }

    override fun onResume() {
        super.onResume()
        tv_nickname.setText(MyGlobals.getInstance().getMyNickname())
        setProfileImg()
    }
    fun set_and_upload_img() {
        Log.i("classchat", "Upload Image")
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요"), 0)
    }
    fun delete_img() {
        Log.i("classchat", "Delete Image")
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            var imgFileName = uid + "_profile.png"
            var storageRef = FirebaseStorage.getInstance().getReference(imgFileName)
            storageRef.delete()
                    .addOnCompleteListener() {
                        FirebaseFirestore.getInstance().collection("users").document(uid)
                                .update("imageUrl", null)
                                .addOnCompleteListener {
                                    MyGlobals.getInstance().setMyImageUrl(null)
                                    setProfileImg()
                                    Toast.makeText(context, "프로필 이미지를 삭제하였습니다.", Toast.LENGTH_SHORT).show()
                                }
                    }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == RESULT_OK) {
            filepath = data?.data
            upload_img()
        }
    }

    fun upload_img() {
        if(filepath != null) {
            val progressDialog = ProgressDialog(context)
            progressDialog.setTitle("업로드")
            progressDialog.show()
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            var imgFileName = uid + "_profile.png"
            var storageRef = FirebaseStorage.getInstance().getReference(imgFileName)
            storageRef.delete()
                    .addOnCompleteListener {
                        storageRef.putFile(filepath!!)
                                .addOnFailureListener {
                                    progressDialog.dismiss()
                                    Toast.makeText(context, "업로드에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                                }.addOnSuccessListener { taskSnapshot ->
                                    storageRef.getDownloadUrl()
                                            .addOnSuccessListener { uri ->
                                                var uri = uri.toString()
                                                val uid = FirebaseAuth.getInstance().currentUser?.uid
                                                if (uid != null) {
                                                    FirebaseFirestore.getInstance().collection("users")
                                                            .document(uid)
                                                            .update("imageUrl", uri)
                                                            .addOnSuccessListener {
                                                                MyGlobals.getInstance().setMyImageUrl(uri)
                                                                Log.i("classchat", "Profile Image URL: " + MyGlobals.getInstance().getMyImageUrl())
                                                                setProfileImg()
                                                            }
                                                }
                                                progressDialog.dismiss()
                                                Toast.makeText(context, "업로드에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                                            }
                                }.addOnProgressListener { (bytesTransferred, totalByteCount) ->
                                    val progress = ((100.0 * bytesTransferred) / totalByteCount).toInt()
                                    progressDialog.setMessage(progress.toString() + "%")
                                }
                    }
        }
    }
    fun setProfileImg() {
        context?.let {
            Glide.with(it)
                .load(MyGlobals.getInstance().myImageUrl)
                .placeholder(R.drawable.mypage_img_profile)
                .error(R.drawable.mypage_img_profile)
                .apply(RequestOptions().circleCrop())
                .into(my_profile_picture)
        }

    }
}