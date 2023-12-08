package com.filipzagulak.closetcanvas.presentation.item_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.filipzagulak.closetcanvas.presentation.sign_in.UserData
import com.filipzagulak.closetcanvas.ui.common.TopClosetCanvasBar

@Composable
fun ItemDetailsScreen(
    state: ItemDetailsState,
    userData: UserData?,
    onProfileIconClicked: () -> Unit,
    onBackButtonClicked: () -> Unit,
    onEditButtonClicked: () -> Unit,
    onDeleteItem: () -> Unit
) {
    var deleteDialogVisible by remember { mutableStateOf(false) }
    val buttonBackgroundColor = MaterialTheme.colorScheme.primaryContainer

    Scaffold(
        topBar = { TopClosetCanvasBar(
            title = "Item Details",
            userData = userData,
            canNavigateBack = true,
            onProfileIconClicked = onProfileIconClicked,
            onBackButtonClicked = onBackButtonClicked
        ) },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                AsyncImage(
                    model = state.itemPictureUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(256.dp)
                        .fillMaxSize()
                )
                Text(
                    text = state.itemName,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .padding(top = 4.dp)
                )
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(3),
                    modifier = Modifier
                        .height(128.dp)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    //verticalItemSpacing = 4.dp
                ) {
                    state.itemTags.forEach { tag ->
                        item {
                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = tag,
                                        maxLines = 1,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            )
                        }
                    }
                }
                TextDisplay("Description", state.description)
                TextDisplay("Item Category", state.itemCategory)
                TextDisplay("Last Washed Date", state.lastWashed)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .background(buttonBackgroundColor, shape = RoundedCornerShape(8.dp))
                    ) {
                        IconButton(
                            onClick = {
                                onEditButtonClicked()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit item details"
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .background(buttonBackgroundColor, shape = RoundedCornerShape(8.dp))
                    ) {
                        IconButton(
                            onClick = {
                                deleteDialogVisible = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete item"
                            )
                        }
                    }
                }
                if(deleteDialogVisible) {
                    AlertDialog(
                        title = {
                            Text("Delete item?")
                        },
                        text = {
                            Text("Deleting item is irreversible")
                        },
                        onDismissRequest = {
                            deleteDialogVisible = false
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    onDeleteItem()
                                    deleteDialogVisible = false
                                }
                            ) {
                                Text("Confirm")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    deleteDialogVisible = false
                                }
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TextDisplay(type: String, details: String) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .fillMaxWidth()
    ) {
        Text(
            text = type,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier
                .padding(4.dp)
        )
        
        Text(
            text = details,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier
                .padding(4.dp)
        )
    }
}
