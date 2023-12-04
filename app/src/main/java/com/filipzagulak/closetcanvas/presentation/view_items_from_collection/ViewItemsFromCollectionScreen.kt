package com.filipzagulak.closetcanvas.presentation.view_items_from_collection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.filipzagulak.closetcanvas.presentation.sign_in.UserData
import com.filipzagulak.closetcanvas.ui.common.TopClosetCanvasBar

@Composable
fun ViewItemsFromCollectionScreen(
    state: ViewItemsFromCollectionState,
    userData: UserData?,
    onProfileIconClicked: () -> Unit,
    onBackButtonClicked: () -> Unit,
    viewItemDetails: (String) -> Unit
) {
    Scaffold(
        topBar = { TopClosetCanvasBar(
            title = "Items In Collection",
            userData = userData,
            canNavigateBack = true,
            onProfileIconClicked = onProfileIconClicked,
            onBackButtonClicked = onBackButtonClicked
        ) }
    ) { padding ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(3),
            modifier = Modifier
                .padding(padding)
        ) {
            items(state.itemList) { wardrobeItem ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                            viewItemDetails(wardrobeItem.itemId)
                        }
                ) {
                    AsyncImage(
                        model = wardrobeItem.itemPictureUrl,
                        contentDescription = null
                    )
                }
            }
        }
    }
}