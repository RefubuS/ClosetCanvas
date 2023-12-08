package com.filipzagulak.closetcanvas.presentation.view_all_items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.filipzagulak.closetcanvas.R
import com.filipzagulak.closetcanvas.presentation.add_item.AddItemViewModel
import com.filipzagulak.closetcanvas.presentation.sign_in.UserData
import com.filipzagulak.closetcanvas.ui.common.TopClosetCanvasBar

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ViewAllItemsScreen(
    state: ViewAllItemsState,
    userData: UserData?,
    onProfileIconClicked: () -> Unit,
    onBackButtonClicked: () -> Unit,
    viewItemDetails: (String) -> Unit,
    onItemLongClick: (String) -> Unit,
    filterItems: (String, String, Set<String>) -> Unit,
    onSaveCollectionClicked: (String) -> Unit,
    clearSelectedItems: () -> Unit
) {
    var numberSelected by remember { mutableIntStateOf(0) }
    var filteringDialogOpened by remember { mutableStateOf(false) }
    var saveCollectionDialogOpened by remember { mutableStateOf(false) }
    var collectionName by remember { mutableStateOf("") }

    val viewModelHelper = AddItemViewModel()
    val availableCategories = viewModelHelper.getCategories()
    val availableTags = viewModelHelper.getAvailableTags()

    var nameFilter by remember { mutableStateOf("") }
    var isDropdownMenuExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(setOf<String>()) }

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
                        onClick = {
                            saveCollectionDialogOpened = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Create new collection"
                        )
                    }
                }
            }
        } },
        floatingActionButton = { FloatingActionButton(
            onClick = {
                filteringDialogOpened = true
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_filter_alt_24),
                contentDescription = null
            )
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
                                    clearSelectedItems()
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
                                if (state.selectedItems.contains(wardrobeItem.itemId)) {
                                    Modifier.border(
                                        width = 4.dp,
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
        if(filteringDialogOpened) {
            Dialog(
                onDismissRequest = {
                    filteringDialogOpened = false
                },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false
                )
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(16.dp)
                        .height(450.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Filtering Options",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .padding(4.dp),
                            value = nameFilter,
                            onValueChange = {
                                nameFilter = it
                            },
                            label = { Text("Filter by name") }
                        )
                        ExposedDropdownMenuBox(
                            expanded = isDropdownMenuExpanded,
                            onExpandedChange = {
                                isDropdownMenuExpanded = !isDropdownMenuExpanded
                            }
                        ) {
                            OutlinedTextField(
                                value = selectedCategory,
                                onValueChange = { },
                                label = { Text("Filter by category") },
                                readOnly = true,
                                modifier = Modifier
                                    .menuAnchor()
                                    .padding(4.dp),
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = isDropdownMenuExpanded
                                    )
                                }
                            )
                            ExposedDropdownMenu(
                                expanded = isDropdownMenuExpanded,
                                onDismissRequest = {
                                    isDropdownMenuExpanded = false
                                },
                                modifier = Modifier
                                    .padding(4.dp)
                                    .height(256.dp)
                            ) {
                                availableCategories.forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(category) },
                                        onClick = {
                                            selectedCategory = category
                                            isDropdownMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                        LazyHorizontalStaggeredGrid(
                            rows = StaggeredGridCells.Fixed(5),
                            modifier = Modifier
                                .height(180.dp)
                                .padding(8.dp),
                            horizontalItemSpacing = 4.dp,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(availableTags) { tag ->
                                val isSelected = selectedTags.contains(tag)

                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        selectedTags = if(isSelected) {
                                            selectedTags - tag
                                        } else {
                                            selectedTags + tag
                                        }
                                    },
                                    label = {
                                        Text(tag)
                                    },
                                    leadingIcon = {
                                        if(isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = tag,
                                                modifier = Modifier
                                                    .size(FilterChipDefaults.IconSize)
                                            )
                                        }
                                    }
                                )
                            }
                        }
                        Button(
                            onClick = {
                                filterItems(
                                    nameFilter,
                                    selectedCategory,
                                    selectedTags
                                )
                                filteringDialogOpened = false
                            }
                        ) {
                            Text("Filter")
                        }
                    }
                }
            }
        }
        if(saveCollectionDialogOpened) {
            Dialog(
                onDismissRequest = {
                    saveCollectionDialogOpened = false
                }
            ) {
                Card(
                    modifier = Modifier
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(4.dp),
                            text = "Save collection?",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .padding(4.dp),
                            value = collectionName,
                            label = {
                                Text("Collection name")
                            },
                            onValueChange = {
                                collectionName = it
                            }
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = {
                                    onSaveCollectionClicked(collectionName)
                                    saveCollectionDialogOpened = false
                                }
                            ) {
                                Text("Save")
                            }
                            Button(
                                onClick = {
                                    saveCollectionDialogOpened = false
                                }
                            ) {
                                Text("Cancel")
                            }
                        }
                    }
                }
            }
        }
    }
}