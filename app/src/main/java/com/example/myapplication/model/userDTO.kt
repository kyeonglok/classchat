package com.example.myapplication.model

import java.util.HashMap

data class userDTO(
        var nickname: String? = null,
        var email : String? = null,
        var imageUrl : String? = null,
        var classes: MutableMap<String, String> = HashMap()
)