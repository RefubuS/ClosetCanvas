package com.filipzagulak.closetcanvas.presentation.view_wardrobe_layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.filipzagulak.closetcanvas.R
import com.filipzagulak.closetcanvas.presentation.create_wardrobe_layout.LayoutItem
import com.filipzagulak.closetcanvas.presentation.sign_in.UserData
import com.filipzagulak.closetcanvas.ui.common.TopClosetCanvasBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewLayoutScreen(
    state: ViewLayoutState,
    userData: UserData?,
    onBackButtonClicked: () -> Unit,
    onProfileIconClicked: () -> Unit,
    onLayoutItemClicked: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopClosetCanvasBar(
                title = "Select Space",
                userData = userData,
                canNavigateBack = true,
                onProfileIconClicked = onProfileIconClicked,
                onBackButtonClicked = onBackButtonClicked
            )
        }
    ) { padding ->
        LazyHorizontalGrid(
            rows = GridCells.Fixed(8),
            content = {
                items(state.wardrobeLayout) { layoutItem ->
                    LayoutItemComposable(
                        layoutItem = layoutItem,
                        onLayoutItemClicked = onLayoutItemClicked
                    )
                }
            },
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .padding(padding)
        )
    }
}

@Composable
fun LayoutItemComposable(
    layoutItem: LayoutItem,
    onLayoutItemClicked: (Int) -> Unit
) {
    val resourceId = when (layoutItem.resourceId) {
        R.drawable.baseline_add_24 -> R.drawable.baseline_crop_free_24
        else -> layoutItem.resourceId
    }

    Card(
        modifier = Modifier
            .clickable {
                if(resourceId != R.drawable.baseline_crop_free_24) {
                    onLayoutItemClicked(layoutItem.itemId)
                }
            }
            .fillMaxSize()
            .padding(4.dp),
        content = {
            Image(
                painter = painterResource(id = resourceId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .size(96.dp)
                    .padding(4.dp)

            )
        }
    )
}