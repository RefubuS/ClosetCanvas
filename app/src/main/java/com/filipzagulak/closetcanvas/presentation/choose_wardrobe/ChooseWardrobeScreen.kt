package com.filipzagulak.closetcanvas.presentation.choose_wardrobe

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.filipzagulak.closetcanvas.presentation.sign_in.UserData
import com.filipzagulak.closetcanvas.ui.common.TopClosetCanvasBar
import com.filipzagulak.closetcanvas.ui.common.createIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseWardrobeScreen(
    state: ChooseWardrobeState,
    userData: UserData?,
    onBackButtonClicked: () -> Unit,
    onProfileIconClicked: () -> Unit,
    onAddButtonClicked: () -> Unit,
    onWardrobeSelected: (String?) -> Unit,
    onDeleteWardrobe: (String) -> Unit
) {
    var deleteDialogVisible by remember { mutableStateOf(false) }
    var wardrobeToDelete by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopClosetCanvasBar(
            title = "Main Screen",
            userData = userData,
            canNavigateBack = false,
            onProfileIconClicked = onProfileIconClicked,
            onBackButtonClicked = onBackButtonClicked
        ) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome, ${userData?.username}",
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier
                        .padding(16.dp)
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .background(color = MaterialTheme.colorScheme.background),
                    horizontalArrangement = Arrangement.Center,
                    content = {
                        items(state.listOfWardrobes) { wardrobeData ->
                            WardrobeItem(
                                wardrobeData,
                                onWardrobeSelected = { wardrobeId ->
                                    onWardrobeSelected(wardrobeId)
                                },
                                changeDeleteDialogVisibility = { wardrobeId ->
                                    wardrobeToDelete = wardrobeId
                                    deleteDialogVisible = true
                                }
                            )
                        }
                    }
                )
                FloatingActionButton(
                    modifier = Modifier
                        .padding(16.dp),
                    onClick = onAddButtonClicked,
                    content = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add wardrobe"
                        )
                    }
                )
            }
            if(deleteDialogVisible) {
                AlertDialog(
                    title = {
                        Text("Delete wardrobe?")
                    },
                    text = {
                        Text("Deleting wardrobe is irreversible and all data will be lost")
                    },
                    onDismissRequest = {
                        deleteDialogVisible = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                onDeleteWardrobe(wardrobeToDelete)
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
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WardrobeItem(
    wardrobeData: WardrobeData,
    onWardrobeSelected: (String?) -> Unit,
    changeDeleteDialogVisibility: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(120.dp)
            .combinedClickable(
                onClick = {
                    onWardrobeSelected(wardrobeData.wardrobeId)
                },
                onLongClick = {
                    changeDeleteDialogVisibility(wardrobeData.wardrobeId!!)
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = createIcon(color = wardrobeData.wardrobeIconColor),
            contentDescription = wardrobeData.wardrobeName,
            modifier = Modifier
                .size(64.dp)
                .padding(8.dp),
            tint = Color.Unspecified
        )
        Text(
            text = wardrobeData.wardrobeName ?: "Unnamed Wardrobe",
            style = TextStyle(fontSize = 16.sp),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}