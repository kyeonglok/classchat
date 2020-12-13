package com.example.myapplication.utils

import android.util.Log

object dateUtils {
    enum class TimeValue(val value: Int,val maximum : Int, val msg : String) {
        SEC(60,60,"분 전"),
        MIN(60,24,"시간 전"),
        HOUR(24,30,"일 전"),
        DAY(30,12,"달 전"),
        MONTH(12,Int.MAX_VALUE,"년 전")
    }


    fun parseTime(writeTime:Long?): String?{
        if(writeTime == null)
            return ""
        var writeTime_positive : Long? = null
        if(writeTime <0)
            writeTime_positive = writeTime * -1
        else
            writeTime_positive = writeTime
        val curTime = System.currentTimeMillis()
        var diffTime = (curTime - writeTime_positive) / 1000
        var msg : String? = ""
        Log.i("timeutil",curTime.toString()+" "+writeTime_positive)
        if(diffTime < TimeValue.SEC.value )
            msg= "방금 전"
        else {
            for (i in TimeValue.values()) {
                diffTime /= i.value
                if (diffTime < i.maximum) {
                    msg+=diffTime.toString()
                    msg+=i.msg
                    break
                }
            }
        }
        return msg
    }
}