package com.example.data.comments

import com.example.data.comments.CommentMapper.toDomain
import com.example.data.comments.network.CommentService
import com.example.domain.comments.data.CommentRepository
import com.example.domain.comments.model.Comment

class RemoteCommentRepository(
    private val commentService: CommentService,
) : CommentRepository {

    override suspend fun fetchComments(): List<Comment> {
        return commentService.fetchComments()
            .toDomain()
    }
}