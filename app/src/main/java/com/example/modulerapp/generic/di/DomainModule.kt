package com.example.modulerapp.generic.di

import com.example.domain.comments.GetComments
import com.example.domain.photos.GetPhoto
import com.example.domain.photos.GetPhotos
import org.koin.dsl.module

val domainModule = module {

    factory { GetPhoto(get()) }
    factory { GetPhotos(get()) }
    factory { GetComments(get()) }
}