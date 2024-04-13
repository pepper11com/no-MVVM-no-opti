package com.example.presentation.photolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.presentation.util.EventFlow
import com.example.presentation.util.MutableEventFlow
import com.example.presentation.util.TypedUIState
import com.example.presentation.util.launchCatchingOnIO
import com.example.presentation.util.setError
import com.example.presentation.util.setLoading
import com.example.presentation.util.setNormal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.domain.photos.GetPhotos
import com.example.domain.photos.model.Photo
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PhotoListViewModel(private val getPhotos: GetPhotos) : ViewModel() {

    private val _navigation = MutableEventFlow<PhotoListEvent>()
    val navigation: EventFlow<PhotoListEvent> = _navigation.asEventFlow()

    private val _uiState = MutableStateFlow<TypedUIState<List<Photo>, Unit>>(TypedUIState.Loading)
    val uiState: StateFlow<TypedUIState<List<Photo>, Unit>> by lazy {
        fetchPhotos()
        _uiState.asStateFlow()
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    fun onRefresh() = fetchPhotos(true)

    fun onRetryClicked() = fetchPhotos()

    fun onPhotoClicked(photo: Photo) { _navigation.setEvent(PhotoListEvent.PhotoClicked(photo.id)) }

    private fun fetchPhotos(isRefresh: Boolean = false) {
        viewModelScope.launchCatchingOnIO({ onFetchPhotosError(isRefresh) }) {
            if (isRefresh) _isRefreshing.tryEmit(true) else _uiState.setLoading()

            val photosList = getPhotos()
            _uiState.setNormal(photosList)

            if (isRefresh) _isRefreshing.tryEmit(false)
        }
    }

    private fun onFetchPhotosError(isRefresh: Boolean) {
        if (isRefresh) _isRefreshing.tryEmit(false) else _uiState.setError()
    }
}