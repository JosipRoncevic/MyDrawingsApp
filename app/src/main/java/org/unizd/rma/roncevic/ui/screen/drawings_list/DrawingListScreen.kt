@file:OptIn(ExperimentalMaterial3Api::class)

package org.unizd.rma.roncevic.ui.screen.drawings_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.unizd.rma.roncevic.domain.model.Drawing

@Composable
fun DrawingListScreen(
   drawingList: List<Drawing>,
   onDrawingClick: (Drawing) -> Unit,
   onAddDrawingClick: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddDrawingClick
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add drawing"
                )
            }

        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 15.dp + paddingValues.calculateTopPadding(),
                bottom = 15.dp + paddingValues.calculateBottomPadding()

            )
        ) {
            item {
                Text(
                    text = "Drawings",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            items(drawingList) {drawing ->
                ListItem(
                    headlineContent = {
                        Text(
                            text = drawing.title,
                            style = MaterialTheme.typography.titleMedium)
                    },
                    supportingContent = {
                        Text(
                            text = drawing.author,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis)
                    },
                    modifier = Modifier.clickable(onClick = {
                        onDrawingClick(drawing)
                    })
                )
            }
        }
    }
}