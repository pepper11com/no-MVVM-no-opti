package com.example.modulerapp.generic.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.example.modulerapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarComposable(
    modifier: Modifier = Modifier,
    onBackClicked: (() -> Unit)? = null,
    color: Color = colorResource(id = R.color.purple_200),
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(stringResource(R.string.photo_app_name), color = Color.White) },
        colors =  TopAppBarDefaults.topAppBarColors(
            containerColor = color,
        ),
        navigationIcon = {
            if (onBackClicked != null) {
                IconButton(onClick = onBackClicked) {
                    Icon(Icons.Filled.ArrowBack, tint = Color.White, contentDescription = null)
                }
            }
        }
    )
}