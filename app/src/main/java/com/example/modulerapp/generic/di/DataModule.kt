package com.example.modulerapp.generic.di

import com.example.data.comments.RemoteCommentRepository
import com.example.data.comments.network.CommentService
import com.example.data.network.HttpClientProvider
import com.example.data.photos.RemotePhotosRepository
import com.example.data.photos.network.PhotosService
import com.example.data.photos.storage.PhotoDataStorage
import com.example.domain.comments.data.CommentRepository
import com.example.domain.photos.data.PhotoRepository
import org.koin.dsl.module

val dataModule = module {

    single { HttpClientProvider() }
    single { PhotoDataStorage() }
    factory { PhotosService(get()) }
    factory { CommentService(get()) }
    factory<CommentRepository> { RemoteCommentRepository(get()) }
    factory<PhotoRepository> { RemotePhotosRepository(get(), get()) }
}