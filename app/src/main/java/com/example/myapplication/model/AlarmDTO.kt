package com.example.myapplication.model

data class AlarmDTO (
    var destinationUid: String? = null,
    var userId: String? = null,
    var uid: String? = null,
    var targetBoardId: String? = null,
    var kind: Int = 0, //0 : 좋아요, 1: 댓,
    var message: String? = null,
    var timestamp: Long? = null
)