package com.sam.pokemondemo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.sam.pokemondemo.R

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(0.dp),
    size: Dp = 30.dp,
    backgroundColor: Color = Color.Transparent,
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .padding(padding),
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(size)
                .align(Alignment.Center),
        )
    }
}

@Composable
fun MyImage(
    modifier: Modifier = Modifier,
    url: String,
    contentDescription: String?,
) {
    SubcomposeAsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .build(),
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        loading = {
            LoadingIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.2f),
                size = 16.dp,
            )
        },
        error = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = colorResource(R.color.bg_image_error),
                        shape = CircleShape,
                    )
                    .clip(CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = stringResource(R.string.image_error))
            }
        }
    )
}

@Composable
fun MyErrorSnackbar(
    data: SnackbarData,
) {
    Snackbar(
        snackbarData = data,
        containerColor = Color.Red,
        contentColor = colorResource(id = R.color.snackbar_text),
    )
}
