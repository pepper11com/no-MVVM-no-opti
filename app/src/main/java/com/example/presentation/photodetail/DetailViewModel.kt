package com.example.presentation.photodetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.domain.comments.GetComments
import com.example.domain.photos.GetPhoto
import com.example.presentation.photodetail.model.DetailState
import com.example.presentation.photodetail.model.DetailViewModelArgs
import com.example.presentation.util.EventFlow
import com.example.presentation.util.MutableEventFlow
import com.example.presentation.util.TypedUIState
import com.example.presentation.util.launchCatchingOnIO
import com.example.presentation.util.setError
import com.example.presentation.util.setLoading
import com.example.presentation.util.setNormal
import com.example.presentation.util.updateIfNormal
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
class DetailViewModel(
    private val getPhoto: GetPhoto,
    private val getComments: GetComments,
    @InjectedParam private val args: DetailViewModelArgs,
) : ViewModel() {

    private val _combinedState = MutableStateFlow<TypedUIState<DetailState, Unit>>(TypedUIState.NormalN)
    val combinedState: StateFlow<TypedUIState<DetailState, Unit>> by lazy {
        fetchPhoto()
        _combinedState.asStateFlow()
    }

    private val _navigation = MutableEventFlow<DetailEvent>()
    val navigation: EventFlow<DetailEvent> = _navigation.asEventFlow()

    fun onRetryClicked() { fetchPhoto() }

    fun onBackClicked() { _navigation.setEvent(DetailEvent.CloseDetails) }

    private fun fetchPhoto() {
        viewModelScope.launchCatchingOnIO(onError = { _combinedState.setError() }) {
            _combinedState.setLoading()
            val photo = getPhoto(args.photoId)
            _combinedState.setNormal(DetailState(photo, TypedUIState.Loading))

            fetchComments()
        }
    }

    private fun fetchComments() {
        viewModelScope.launchCatchingOnIO(onError = { _combinedState.setError() }) {
            _combinedState.updateIfNormal { currentState ->
                currentState.copy(commentsState = TypedUIState.Loading)
            }

            val comments = getComments()

            _combinedState.updateIfNormal { currentState ->
                currentState.copy(commentsState = TypedUIState.Normal(comments))
            }
        }
    }
}