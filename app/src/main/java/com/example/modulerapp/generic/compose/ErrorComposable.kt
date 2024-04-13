package com.example.modulerapp.generic.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.modulerapp.R
import com.example.modulerapp.generic.spacing.Spacing.x0_5
import com.example.modulerapp.generic.spacing.Spacing.x1
import com.example.modulerapp.generic.spacing.Spacing.x2

@Composable
fun ErrorComposable(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    length: Int = 10,
    showHeader: Boolean = true,
    showImage: Boolean = true,
) = Column(
    modifier = modifier,
) {
    if (showHeader){ TopAppBarComposable(color = Color.Gray) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            tonalElevation = x0_5,
            color = Color.White,
            modifier = Modifier.padding(x2)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(x2)
            ) {
                Text(
                    stringResource(R.string.retry_message),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(x1))
                Button(onClick = onRetry) {
                    Text(stringResource(R.string.retry))
                }
            }
        }
    }
}