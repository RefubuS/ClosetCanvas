package com.filipzagulak.closetcanvas.presentation.create_wardrobe_layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.filipzagulak.closetcanvas.R
import com.filipzagulak.closetcanvas.presentation.sign_in.UserData
import com.filipzagulak.closetcanvas.ui.common.TopClosetCanvasBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateWardrobeLayoutScreen(
    userData: UserData?,
    state: CreateWardrobeLayoutState,
    onSaveButtonClicked: (List<LayoutItem>) -> Unit,
    onBackButtonClicked: () -> Unit,
    onOptionClicked: (Int, Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopClosetCanvasBar(
                title = "Create Wardrobe Layout",
                userData = userData,
                canNavigateBack = true,
                onProfileIconClicked = { },
                onBackButtonClicked = onBackButtonClicked
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onSaveButtonClicked(state.layoutItemList) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_save_24),
                    contentDescription = "Save button"
                )
            }
        }
    ) { padding ->
        LazyHorizontalGrid(
            rows = GridCells.Fixed(8),
            content = {
                items(state.layoutItemList) { layoutItem ->
                    LayoutItemComposable(
                        layoutItem = layoutItem,
                        availableOptions = state.availableOptions,
                        onOptionClicked = onOptionClicked,
                        padding = padding
                    )
                }
            },
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        )
    }
}

@Composable
fun LayoutItemComposable(
    layoutItem: LayoutItem,
    availableOptions: List<Pair<String, Int>>,
    onOptionClicked: (Int, Int) -> Unit,
    padding: PaddingValues
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .clickable {
                expanded = true
            }
            .fillMaxWidth()
            .padding(padding),
        content = {
            Image(
                painter = painterResource(id = layoutItem.resourceId),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(MaterialTheme.shapes.large)
                    .padding(4.dp)
            )
        }
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
    ) {
        availableOptions.forEach { (text, resourceId) ->
            DropdownMenuItem(
                text = { Text(text) },
                onClick = {
                    expanded = false
                    onOptionClicked(layoutItem.itemId, resourceId)
                }
            )
        }
    }
}
