package com.example.domain.photos.data

import com.example.domain.photos.model.Photo

interface PhotoRepository {

    suspend fun fetchPhotos(): List<Photo>

    suspend fun fetchPhoto(id: Int): Photo
}