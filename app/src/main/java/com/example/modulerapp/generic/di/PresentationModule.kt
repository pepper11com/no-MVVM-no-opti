package com.example.modulerapp.generic.di

import com.example.presentation.photodetail.DetailViewModel
import com.example.presentation.photolist.PhotoListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel { PhotoListViewModel(get()) }
    viewModel { args -> DetailViewModel(get(), get(), args[0]) }
}