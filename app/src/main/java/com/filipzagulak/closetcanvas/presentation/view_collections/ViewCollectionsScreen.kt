package com.filipzagulak.closetcanvas.presentation.view_collections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.filipzagulak.closetcanvas.presentation.sign_in.UserData
import com.filipzagulak.closetcanvas.ui.common.TopClosetCanvasBar

@Composable
fun ViewCollectionsScreen(
    userData: UserData?,
    state: ViewCollectionsState,
    onBackButtonClicked: () -> Unit,
    onProfileIconClicked: () -> Unit,
    onCollectionCardClicked: (String) -> Unit,
    onDeleteCollection: (String) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var collectionToDelete by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = { TopClosetCanvasBar(
            title = "Available collections",
            userData = userData,
            canNavigateBack = true,
            onProfileIconClicked = onProfileIconClicked,
            onBackButtonClicked = onBackButtonClicked
        ) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(state.collections) { collection ->
                CollectionCard(
                    collection = collection,
                    changeDialogVisibility = { collectionId ->
                        collectionToDelete = collectionId
                        showDeleteDialog = true
                    },
                    onCollectionCardClicked = onCollectionCardClicked
                )
            }
        }
        if(showDeleteDialog) {
            AlertDialog(
                title = {
                    Text("Delete collection?")
                },
                text = {
                    Text("Deleting collection is irreversible")
                },
                onDismissRequest = { showDeleteDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onDeleteCollection(collectionToDelete)
                            showDeleteDialog = false
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun CollectionCard(
    collection: CollectionItem,
    changeDialogVisibility: (String) -> Unit,
    onCollectionCardClicked: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        onClick = {
            onCollectionCardClicked(collection.collectionId)
        }
    ) {
        LazyRow(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            item {
                Text(
                    text = collection.collectionName,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            item {
                IconButton(
                    onClick = {
                        changeDialogVisibility(collection.collectionId)
                    }
                ) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                }
            }
        }
    }
}
