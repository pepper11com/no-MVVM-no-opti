package com.example.presentation.photodetail.model

import com.example.domain.comments.model.Comment
import com.example.domain.photos.model.Photo
import com.example.presentation.util.TypedUIState

data class DetailState(
    val photo: Photo,
    val commentsState: TypedUIState<List<Comment>, Unit>,
)