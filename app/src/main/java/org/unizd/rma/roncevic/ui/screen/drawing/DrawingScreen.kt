@file:OptIn(ExperimentalMaterial3Api::class)

package org.unizd.rma.roncevic.ui.screen.drawing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun DrawingScreen(
    state: DrawingState,
    onEvent:(DrawingEvent)-> Unit
){
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { "Drawing details" },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onEvent(DrawingEvent.NavigateBack)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = "navigate back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onEvent(DrawingEvent.DeleteDrawing)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "delete")
                    }
                }
            )
        }
    ) { padding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(
                    horizontal = 20.dp,
                    vertical = 15.dp
                ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            OutlinedTextField(
                value = state.title,
                onValueChange = {
                    onEvent(DrawingEvent.TitleChange(it))
                },
                placeholder = {
                    Text(text = "Title")
                }
            )
            OutlinedTextField(
                value = state.author,
                onValueChange = {
                    onEvent(DrawingEvent.AuthorChange(it))
                },
                placeholder = {
                    Text(text = "Author")
                }
            )

            Box(
                modifier=Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Button(
                    onClick = {
                        onEvent(DrawingEvent.Save)
                    },
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Text(text = "Save")
                }
            }
        }
    }
}