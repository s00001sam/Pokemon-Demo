package com.sam.pokemondemo.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshDefaults
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.sam.pokemondemo.R
import com.sam.pokemondemo.ui.theme.body

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
    size: Dp,
    contentDescription: String?,
) {
    val sizePx = with(LocalDensity.current) {
        size.toPx().toInt()
    }

    SubcomposeAsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .size(sizePx)
            .data(url)
            .build(),
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        loading = {
            when (LocalInspectionMode.current) {
                true -> {
                    Image(
                        painter = painterResource(R.drawable.mock_image),
                        contentDescription = null,
                    )
                }

                false -> {
                    LoadingIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(0.2f),
                        size = 16.dp,
                    )
                }
            }
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

@Composable
fun MyTag(
    modifier: Modifier = Modifier,
    tagName: String,
) {
    val backgroundShape = RoundedCornerShape(50)

    Box(
        modifier = modifier
            .wrapContentSize()
            .background(
                color = colorResource(R.color.bg_tag),
                shape = backgroundShape,
            )
            .clip(backgroundShape)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = tagName,
            style = MaterialTheme.typography.body,
        )
    }
}

/**
 * Encountered a freezing issue with Material 3 PullToRefreshBox, switching to Material 2
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyPullToRefreshBox(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    refreshingOffset: Dp = PullRefreshDefaults.RefreshThreshold,
    content: @Composable BoxScope.() -> Unit,
) {
    val refreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh,
        refreshingOffset = refreshingOffset,
    )
    Box(
        modifier = modifier
            .pullRefresh(refreshState),
    ) {
        content()
        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = isRefreshing,
            state = refreshState,
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

/**
 * Ensure that the LazyColumn's position is maintained after navigation
 */
@Composable
fun <T : Any> List<T>.rememberLazyListState(): LazyListState {
    return when (size) {
        0 -> remember(this) { LazyListState(0, 0) }
        else -> androidx.compose.foundation.lazy.rememberLazyListState()
    }
}
