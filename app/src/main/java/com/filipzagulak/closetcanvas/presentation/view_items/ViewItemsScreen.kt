package com.filipzagulak.closetcanvas.presentation.view_items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.filipzagulak.closetcanvas.R
import com.filipzagulak.closetcanvas.presentation.sign_in.UserData
import com.filipzagulak.closetcanvas.ui.common.TopClosetCanvasBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewItemsScreen(
    state: ViewItemsState,
    userData: UserData?,
    onBackButtonClicked: () -> Unit,
    onProfileIconClicked: () -> Unit,
    onAddButtonClicked: () -> Unit,
    viewItemDetails: (String) -> Unit
) {
    Scaffold(
        topBar = { TopClosetCanvasBar(
            title = "Available Items",
            userData = userData,
            canNavigateBack = true,
            onProfileIconClicked = onProfileIconClicked,
            onBackButtonClicked = onBackButtonClicked
        ) },
        floatingActionButton = { FloatingActionButton(
            onClick = onAddButtonClicked,
            content = {
                Icon(painter = painterResource(id = R.drawable.baseline_add_24), contentDescription = null)
            }
        ) }
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
        )
    }
}
