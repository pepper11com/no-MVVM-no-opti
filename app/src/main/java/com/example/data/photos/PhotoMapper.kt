package com.example.data.photos

import com.example.data.photos.network.response.PhotoResponse
import com.example.domain.photos.model.Photo

object PhotoMapper {

    internal fun List<PhotoResponse>.toDomain(): List<Photo> {
        return map { it.toDomain() }
    }

    fun PhotoResponse.toDomain(): Photo {
        return Photo(
            albumId = albumId,
            id = id,
            title = title,
            url = url,
            thumbnailUrl = thumbnailUrl,
        )
    }
}