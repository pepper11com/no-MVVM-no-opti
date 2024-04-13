package com.example.domain.photos

import com.example.domain.photos.data.PhotoRepository
import com.example.domain.photos.model.Photo

class GetPhotos(private val photoRepository: PhotoRepository) {

    suspend operator fun invoke(): List<Photo> {
        return photoRepository.fetchPhotos()
            .sortedBy { it.title }
    }
}