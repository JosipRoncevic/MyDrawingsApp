package org.unizd.rma.roncevic.ui.screen.drawing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PhotoDialog(
    launchPhotoPicker: () -> Unit,
    onDismiss: () -> Unit,
    cameraPermissionState: PermissionState,
    launchCamera: () -> Unit,
    showRationalDialog: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(5.dp))
                .padding(16.dp)
                .height(60.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    launchPhotoPicker()
                    onDismiss()
                },

            ) {
                Text("Gallery".uppercase())
            }
            Button(
                onClick = {
                    when (val status = cameraPermissionState.status) {
                        PermissionStatus.Granted -> launchCamera()
                        is PermissionStatus.Denied -> {
                            if (status.shouldShowRationale) showRationalDialog()
                            else cameraPermissionState.launchPermissionRequest()
                        }
                    }
                    onDismiss()
                },
            ) {
                Text("Camera".uppercase())
            }
        }
    }
}