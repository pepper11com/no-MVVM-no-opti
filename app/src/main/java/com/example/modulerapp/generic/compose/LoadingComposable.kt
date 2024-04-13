package com.example.modulerapp.generic.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.modulerapp.generic.ext.shimmerEffect
import com.example.modulerapp.generic.spacing.Spacing.x0
import com.example.modulerapp.generic.spacing.Spacing.x0_5
import com.example.modulerapp.generic.spacing.Spacing.x1_25
import com.example.modulerapp.generic.spacing.Spacing.x1_75
import com.example.modulerapp.generic.spacing.Spacing.x2_5

@Composable
fun LoadingComposable(
    modifier: Modifier = Modifier,
    showShimmer: Boolean = true,
    showImage: Boolean = true,
    showHeader: Boolean = true,
    length: Int = 10
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (showHeader) {
            TopAppBarComposable(color = Color.Gray)
        }
        LazyColumn {
            items(length) { LoadingRow(showShimmer, showImage) }
        }
    }
}

@Composable
private fun LoadingRow(showShimmer: Boolean, showImage: Boolean) {
    Row(
        modifier = Modifier.padding(x1_25)
            .fillMaxWidth()
    ) {
        if (showImage){
            Box(
                modifier = Modifier.size(148.dp, 148.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(10.dp))
                    .then(
                        if (showShimmer) Modifier.shimmerEffect() else Modifier
                    )
            )
        }
        Column(
            modifier = Modifier.padding(if (showImage) x2_5 else x0)
                .fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier.width(148.dp)
                    .height(x2_5)
                    .padding(x0_5)
                    .background(Color.Gray, shape = RoundedCornerShape(10.dp))
                    .then(
                        if (showShimmer) Modifier.shimmerEffect() else Modifier
                    )
            )

            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(x1_75)
                    .padding(x0_5)
                    .background(Color.Gray, shape = RoundedCornerShape(10.dp))
                    .then(
                        if (showShimmer) Modifier.shimmerEffect() else Modifier
                    )
            )
        }
    }
}