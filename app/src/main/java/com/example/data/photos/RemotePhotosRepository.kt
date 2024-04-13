package com.example.data.photos

import com.example.data.photos.PhotoMapper.toDomain
import com.example.data.photos.network.PhotosService
import com.example.data.photos.storage.PhotoDataStorage
import com.example.domain.photos.data.PhotoRepository
import com.example.domain.photos.model.Photo

class RemotePhotosRepository(
    private val photosService: PhotosService,
    private val photosDataStorage: PhotoDataStorage,
) : PhotoRepository {

    override suspend fun fetchPhotos(): List<Photo> {
        val cachedPhotos = photosDataStorage.getPhotosCache()
        return if (cachedPhotos != null && !photosDataStorage.isCacheExpired()) {
            cachedPhotos
        } else {
            fetchCachePhotosFromRemote()
        }
    }

    override suspend fun fetchPhoto(id: Int): Photo {
        val photos = photosDataStorage.getPhotosCache() ?: fetchCachePhotosFromRemote()
        return photos.find { it.id == id } ?: throw Exception()
    }

    private suspend fun fetchCachePhotosFromRemote(): List<Photo> {
        return photosService.fetchPhotos()
            .toDomain()
            .also(photosDataStorage::setPhotoCache)
    }
}