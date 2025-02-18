package org.unizd.rma.roncevic.ui.util

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun LoadImageFromInternalStorage(filePath: String, modifier: Modifier) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(filePath)
            .build()
    )
    Image(
        painter = painter,
        contentDescription = "Drawing Image",
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}
