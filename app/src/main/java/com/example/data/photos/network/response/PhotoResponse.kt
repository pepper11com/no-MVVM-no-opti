package com.example.data.photos.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoResponse(

    @SerialName("albumId")
    val albumId: Int,

    @SerialName("id")
    val id: Int,

    @SerialName("title")
    val title: String,

    @SerialName("url")
    val url: String,

    @SerialName("thumbnailUrl")
    val thumbnailUrl: String,
)