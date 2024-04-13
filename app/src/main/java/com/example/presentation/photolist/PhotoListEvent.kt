package com.example.presentation.photolist

sealed class PhotoListEvent {
    data class PhotoClicked(val id: Int) : PhotoListEvent()
}