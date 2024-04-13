package com.example.domain.comments.data

import com.example.domain.comments.model.Comment

interface CommentRepository {

    suspend fun fetchComments(): List<Comment>
}