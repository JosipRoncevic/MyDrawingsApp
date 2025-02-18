@file:OptIn(ExperimentalMaterial3Api::class)

package org.unizd.rma.roncevic.ui.screen.drawing

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import org.unizd.rma.roncevic.ComposeFileProvider
import org.unizd.rma.roncevic.ui.util.LoadImageFromInternalStorage
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DrawingScreen(
    state: DrawingState,
    onEvent: (DrawingEvent) -> Unit
) {

    val themes = listOf("Romance", "Fantasy", "Mystery", "Horror", "Sci-Fi")
    var expanded by remember { mutableStateOf(false) }
    var imagePath by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val (tempUri, tempFile) = ComposeFileProvider.getImageUri(context)
    var uri by remember { mutableStateOf(tempUri) }
    var file by remember { mutableStateOf<File?>(tempFile) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = TakePicture(),
        onResult = { isSuccess ->
            if (isSuccess) {
                onEvent(DrawingEvent.ImageChange(uri.toString()))
                imagePath = savePhotoToInternalStorage(context, uri)
            }
        }
    )

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = PickVisualMedia(),
        onResult = { it?.let { imagePath = savePhotoToInternalStorage(context, it) } }
    )

    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA,
        onPermissionResult = { granted ->
            if (granted) {
                val (tempUri, tempFile) = ComposeFileProvider.getImageUri(context)
                file = tempFile
                uri = tempUri
                cameraLauncher.launch(uri)
            }
        }
    )

    var shouldShowPhotoDialog by remember { mutableStateOf(false) }
    var shouldShowRational by remember { mutableStateOf(false) }

    if (shouldShowPhotoDialog) {
        PhotoDialog(
            launchPhotoPicker = {
                singlePhotoPickerLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
            },
            onDismiss = { shouldShowPhotoDialog = false },
            cameraPermissionState = cameraPermissionState,
            launchCamera = {
                val (tempUri, tempFile) = ComposeFileProvider.getImageUri(context)
                file = tempFile
                uri = tempUri
                cameraLauncher.launch(uri)
            },
            showRationalDialog = { shouldShowRational = true },
        )
    }

    if (shouldShowRational) {
        Dialog(onDismissRequest = { shouldShowRational = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = androidx.compose.ui.graphics.Color.White,
                        shape = RoundedCornerShape(5.dp),
                    )
                    .padding(16.dp)
            ) {
                Text("To take a picture, you need to grant camera permission.")
                Button(
                    modifier = Modifier.align(Alignment.End),
                    onClick = {
                        cameraPermissionState.launchPermissionRequest()
                        shouldShowRational = false
                    }
                ) {
                    Text("OK")
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Drawing details") },
                navigationIcon = {
                    IconButton(onClick = { onEvent(DrawingEvent.NavigateBack) }) {
                        Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "Navigate back")
                    }
                },
                actions = {
                    IconButton(onClick = { onEvent(DrawingEvent.DeleteDrawing) }) {
                        Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 15.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            var titleError by remember { mutableStateOf<String?>(null) }
            OutlinedTextField(
                value = state.title,
                onValueChange = { newTitle ->
                    titleError = null
                    onEvent(DrawingEvent.TitleChange(newTitle))
                },
                placeholder = { Text("Enter Title") },
                isError = titleError != null,
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    if (titleError != null) {
                        Text(text = titleError!!, color = androidx.compose.ui.graphics.Color.Red)
                    }
                }
            )
            OutlinedTextField(
                value = state.author,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { onEvent(DrawingEvent.AuthorChange(it)) },
                placeholder = { Text(text = "Author") }
            )

            Box {
                OutlinedTextField(
                    value = state.theme,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text(text = "Select Theme") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true },
                    trailingIcon = {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown", Modifier.clickable { expanded = true })
                    }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    themes.forEach { theme ->
                        DropdownMenuItem(
                            text = { Text(theme) },
                            onClick = {
                                onEvent(DrawingEvent.ThemeChange(theme))
                                expanded = false
                            }
                        )
                    }
                }
            }

            var dateError by remember { mutableStateOf<String?>(null) }

            OutlinedTextField(
                value = state.date,
                onValueChange = { newDate ->
                    dateError = null
                    onEvent(DrawingEvent.DateChange(newDate))
                },
                placeholder = { Text("Enter Date (DD/MM/YYYY)") },
                isError = dateError != null,
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    dateError?.let { errorMessage ->
                        Text(text = errorMessage, color = androidx.compose.ui.graphics.Color.Red)
                    }
                }
            )

            Button(
                onClick = { shouldShowPhotoDialog = true },
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Take Picture")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Image")
            }

            state.imageUri?.let {
                onEvent(DrawingEvent.ImageChange(it))
                LoadImageFromInternalStorage(
                    it, Modifier
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        var isValid = true
                        if (state.title.isBlank()) {
                            titleError = "Title is required"
                            isValid = false
                        }

                        if (state.date.isNotEmpty() && !state.date.matches(Regex("""\d{2}/\d{2}/\d{4}"""))) {
                            dateError = "Invalid date format. Use DD/MM/YYYY"
                            isValid = false
                        }
                        if (isValid) {
                            onEvent(DrawingEvent.Save)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Text(text = "Save")
                }
            }
        }
    }

}
private fun savePhotoToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
        val fileName = "photo_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        file.absolutePath
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}




