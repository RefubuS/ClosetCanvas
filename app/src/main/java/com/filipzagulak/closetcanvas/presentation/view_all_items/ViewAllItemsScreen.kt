package com.filipzagulak.closetcanvas.presentation.view_all_items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.filipzagulak.closetcanvas.presentation.sign_in.UserData
import com.filipzagulak.closetcanvas.ui.common.TopClosetCanvasBar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewAllItemsScreen(
    state: ViewAllItemsState,
    userData: UserData?,
    onProfileIconClicked: () -> Unit,
    onBackButtonClicked: () -> Unit,
    onNextClicked: () -> Unit,
    viewItemDetails: (String) -> Unit,
    onItemLongClick: (String) -> Unit
) {
    var numberSelected by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = { TopClosetCanvasBar(
            title = "Available Items",
            userData = userData,
            canNavigateBack = true,
            onProfileIconClicked = onProfileIconClicked,
            onBackButtonClicked = onBackButtonClicked
        ) },
        bottomBar = { BottomAppBar(
            modifier = Modifier
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Selected items: $numberSelected",
                    modifier = Modifier
                        .weight(1f)
                )
                if(numberSelected != 0) {
                    IconButton(
                        onClick = onNextClicked
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Create new collection"
                        )
                    }
                }
            }
        } }
    ) { padding ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(3),
            modifier = Modifier
                .padding(padding),
            content = {
                items(state.itemList) { wardrobeItem ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .combinedClickable(
                                onClick = {
                                    viewItemDetails(wardrobeItem.itemId)
                                },
                                onLongClick = {
                                    if (state.selectedItems.contains(wardrobeItem.itemId)) {
                                        onItemLongClick(wardrobeItem.itemId)
                                        numberSelected -= 1
                                    } else {
                                        onItemLongClick(wardrobeItem.itemId)
                                        numberSelected += 1
                                    }
                                }
                            )
                            .then(
                                if(state.selectedItems.contains(wardrobeItem.itemId)) {
                                    Modifier.border(
                                        width = 2.dp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                } else Modifier
                            )
                    ) {
                        AsyncImage(
                            model = wardrobeItem.itemPictureUrl,
                            contentDescription = null
                        )
                    }
                }
            }
        )
    }
}