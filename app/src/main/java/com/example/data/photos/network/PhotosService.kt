package com.example.data.photos.network

import io.ktor.client.request.get
import com.example.data.network.HttpClientProvider
import com.example.data.photos.network.response.PhotoResponse

class PhotosService(private val httpClientProvider: HttpClientProvider) {

    suspend fun fetchPhotos(): List<PhotoResponse> = httpClientProvider.client.get("photos")
}