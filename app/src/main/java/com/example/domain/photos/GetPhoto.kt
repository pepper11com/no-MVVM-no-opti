package com.example.domain.photos

import com.example.domain.photos.data.PhotoRepository
import com.example.domain.photos.model.Photo

class GetPhoto(private val photoRepository: PhotoRepository) {

    suspend operator fun invoke(id: Int): Photo {
        return photoRepository.fetchPhoto(id)
    }
}