package com.example.data.comments.network

import io.ktor.client.request.get
import nl.move.data.comments.network.response.CommentResponse
import com.example.data.network.HttpClientProvider

class CommentService(private val httpClientProvider: HttpClientProvider) {

        suspend fun fetchComments(): List<CommentResponse> = httpClientProvider.client.get("comments")
}