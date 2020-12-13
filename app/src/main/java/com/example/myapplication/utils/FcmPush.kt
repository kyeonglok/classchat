package com.example.myapplication.utils

import android.util.Log
import com.example.myapplication.model.PushDTO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class FcmPush() {
    val JSON = MediaType.parse("application/json; charset=utf-8")
    val url = "https://fcm.googleapis.com/fcm/send"
    val serverKey = "AAAAeOKNtGo:APA91bGhw_peAjE0fShAQTqA3Eca623cgGFBUS8PIz9YAf_ymMYO8LlqA2yk-c1nNMDNJDtlpXkSZZHc7X_ahreNM1IrOjF8379raEEjfSQ83wSwBYSz_MT9uNx_klStV0waFKN2OJHt"
    var okHttpClient: OkHttpClient? = null
    var gson: Gson? = null
    init {
        gson = Gson()
        okHttpClient = OkHttpClient()
    }

    companion object {
        var instance = FcmPush()
    }

    fun sendMessage(destinationUid: String, title: String, message: String) {
        FirebaseFirestore.getInstance().collection("pushtokens").document(destinationUid).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var token = task.result?.get("pushToken")?.toString()
                var pushDTO = PushDTO()
                pushDTO.to = token
                pushDTO.notification?.title = title
                pushDTO.notification?.body = message
                var body = RequestBody.create(JSON, gson?.toJson(pushDTO))
                var request = Request
                        .Builder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "key=" + serverKey)
                        .url(url)
                        .post(body)
                        .build()
                okHttpClient?.newCall(request)?.enqueue(object : Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
                        Log.d("pushTest",e.toString())
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        Log.d("pushtest! ",response.toString())
                        println(response?.body()?.string())
                    }
                })
            }
        }
    }
}