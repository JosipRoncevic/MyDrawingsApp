package org.unizd.rma.roncevic

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

class ComposeFileProvider : FileProvider(R.xml.filepaths) {
    companion object {
        fun getImageUri(context: Context): Pair<Uri, File> {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File.createTempFile("selected_image_", ".jpg", directory)
            val authority = context.packageName + ".provider"
            return getUriForFile(context, authority, file) to file
        }
    }
}