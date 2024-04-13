package com.example.modulerapp.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.domain.comments.model.Comment
import com.example.domain.photos.model.Photo
import com.example.modulerapp.generic.compose.ErrorComposable
import com.example.modulerapp.generic.compose.LoadingComposable
import com.example.modulerapp.generic.compose.TopAppBarComposable
import com.example.modulerapp.generic.spacing.Spacing.x0_5
import com.example.modulerapp.generic.spacing.Spacing.x1_25
import com.example.modulerapp.generic.spacing.Spacing.x2_5
import com.example.presentation.photodetail.DetailEvent
import com.example.presentation.photodetail.DetailViewModel
import com.example.presentation.photodetail.model.DetailViewModelArgs
import com.example.presentation.util.TypedUIState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Destination<RootGraph>
@Composable
fun DetailScreen(
    destinationsNavigator: DestinationsNavigator,
    photoId: Int,
    viewModel: DetailViewModel = koinViewModel(
        parameters = {
            parametersOf(DetailViewModelArgs(photoId))
        }
    ),
) {
    val state by viewModel.combinedState.collectAsState()

    val navigator = remember(destinationsNavigator) { DetailNavigator(destinationsNavigator) }

    LaunchedEffect(Unit) {
        viewModel.navigation.retrieveEach { event ->
            when (event) {
                is DetailEvent.CloseDetails -> navigator.closeDetails()
                else -> Unit
            }
        }
    }

    when (val currentState = state) {
        // this causes a short flicker when the screen is first opened
        //is TypedUIState.Loading -> LoadingComposable(length = 1)

        is TypedUIState.Normal -> {
            val detailState = currentState.data
            Content(
                photo = detailState.photo,
                comments = detailState.commentsState,
                onBackClicked = viewModel::onBackClicked,
                onRetry = viewModel::onRetryClicked,
                modifier = Modifier.fillMaxSize()
            )
        }
        is TypedUIState.Error -> {
            ErrorComposable(
                onRetry = viewModel::onRetryClicked,
                length = 1,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        else -> Unit
    }
}

@Composable
private fun Content(
    photo: Photo,
    comments: TypedUIState<List<Comment>, Unit>,
    onBackClicked: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier
) {
    TopAppBarComposable(onBackClicked = onBackClicked)

    Row(
        modifier = Modifier.padding(x1_25)
            .fillMaxWidth()
    ) {
        AsyncImage(
            modifier = Modifier.size(148.dp, 148.dp)
                .clip(RoundedCornerShape(10.dp)),
            model = photo.url,
            contentDescription = photo.title,
        )

        Column(
            modifier = Modifier.padding(start = x2_5)
                .fillMaxWidth()
        ) {
            Text(
                text = photo.title,
                modifier = Modifier.padding(x0_5)
            )

            Text(
                text = photo.url,
                modifier = Modifier.padding(x0_5)
            )
        }
    }

    AnimatedContent(
        targetState = comments,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "comments"
    ) { targetState ->
        when (targetState) {
            is TypedUIState.Loading -> LoadingComposable(showHeader = false, showImage = false)
            is TypedUIState.Normal -> {
                LazyColumn(
                    modifier = Modifier.padding(x1_25)
                        .fillMaxWidth()
                ) {
                    items(targetState.data) { comment ->
                        Text(
                            text = comment.body,
                            modifier = Modifier.padding(x0_5)
                        )
                    }
                }
            }
            is TypedUIState.Error -> {
                ErrorComposable(
                    onRetry = onRetry,
                    length = 1,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            TypedUIState.NormalN -> Unit
        }
    }
}