package com.example.domain.comments

import com.example.domain.comments.data.CommentRepository
import com.example.domain.comments.model.Comment

class GetComments(private val commentRepository: CommentRepository) {

    suspend operator fun invoke(): List<Comment> {
        return commentRepository.fetchComments()
            .take(Size)
    }

    companion object {

        private const val Size = 20
    }
}