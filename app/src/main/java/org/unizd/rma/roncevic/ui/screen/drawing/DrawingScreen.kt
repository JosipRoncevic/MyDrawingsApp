@file:OptIn(ExperimentalMaterial3Api::class)

package org.unizd.rma.roncevic.ui.screen.drawing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DrawingScreen(
    state: DrawingState,
    onEvent: (DrawingEvent) -> Unit
) {

    val themes = listOf("Romance", "Fantasy", "Mystery", "Horror", "Sci-Fi")
    var expanded by remember { mutableStateOf(false) }

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
                            onEvent(DrawingEvent.Save) // Only save if all checks pass
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




