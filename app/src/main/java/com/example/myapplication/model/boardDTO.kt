package com.example.myapplication.model

import java.util.HashMap

data class boardDTO(
        var title: String? = null,
        var explain: String? = null,
        var imageUrl: String? = null,
        var uid: String? = null,
        var userId: String? = null,
        var userNickname : String? = null,
        var boardClassId: String? = null,
        var boardClassName: String? = null,
        var timestamp: Long? = null,
        var commentCount: Int = 0,
        var favoriteCount: Int = 0,
        var isAnony: Boolean = true,
        var favorites: MutableMap<String, Boolean> = HashMap()) {

    data class Comment(var uid: String? = null,
                       var userId: String? = null,
                       var comment: String? = null,
                       var timestamp: Long? = null,
                       var isAnony: Boolean = true)
}


