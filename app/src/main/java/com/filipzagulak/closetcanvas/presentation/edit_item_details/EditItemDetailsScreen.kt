package com.filipzagulak.closetcanvas.presentation.edit_item_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.filipzagulak.closetcanvas.presentation.add_item.AddItemViewModel
import com.filipzagulak.closetcanvas.presentation.add_item.convertMillisToDate
import com.filipzagulak.closetcanvas.presentation.sign_in.UserData
import com.filipzagulak.closetcanvas.ui.common.TopClosetCanvasBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemDetailsScreen(
    state: EditItemDetailsState,
    userData: UserData?,
    onBackButtonClicked: () -> Unit,
    onProfileIconClicked: () -> Unit,
    onSaveChanges: () -> Unit,
    onTagClicked: (String) -> Unit
) {
    val viewModelHelper = AddItemViewModel()
    val availableCategories = viewModelHelper.getCategories()
    val availableTags = viewModelHelper.getAvailableTags()

    val datePickerState = rememberDatePickerState()
    var isDatePickerVisible by remember { mutableStateOf(false) }
    var isDropdownMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopClosetCanvasBar(
                title = "Edit details",
                userData = userData,
                canNavigateBack = true,
                onProfileIconClicked = onProfileIconClicked,
                onBackButtonClicked = onBackButtonClicked
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onSaveChanges()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Confirm changes"
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                OutlinedTextField(
                    value = state.itemName.value,
                    onValueChange = {
                        state.itemName.value = it
                    },
                    label = { Text("Item Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                OutlinedTextField(
                    value = state.itemDescription.value,
                    onValueChange = {
                        state.itemDescription.value = it
                    },
                    label = { Text("Item Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                OutlinedTextField(
                    readOnly = true,
                    value = state.lastWashedDate.value,
                    onValueChange = {
                        state.lastWashedDate.value = it
                    },
                    label = { Text("Last Washed Date") },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                isDatePickerVisible = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Date picker"
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                if (isDatePickerVisible) {
                    DatePickerDialog(
                        onDismissRequest = {
                            isDatePickerVisible = false
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    state.lastWashedDate.value =
                                        convertMillisToDate(datePickerState.selectedDateMillis)
                                    isDatePickerVisible = false
                                }
                            ) {
                                Text("OK")
                            }
                        }
                    ) {
                        DatePicker(
                            state = datePickerState
                        )
                    }
                }
            }
            item {
                ExposedDropdownMenuBox(
                    expanded = isDropdownMenuExpanded,
                    onExpandedChange = {
                        isDropdownMenuExpanded = !isDropdownMenuExpanded
                    }
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .menuAnchor()
                            .padding(8.dp)
                            .fillMaxWidth(),
                        readOnly = true,
                        value = state.itemCategory.value,
                        onValueChange = { },
                        label = { Text("Category") },
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
                        }
                    ) {
                        availableCategories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    state.itemCategory.value = category
                                    isDropdownMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            item {
                LazyHorizontalStaggeredGrid(
                    rows = StaggeredGridCells.Fixed(5),
                    modifier = Modifier
                        .height(180.dp)
                        .padding(8.dp),
                    horizontalItemSpacing = 4.dp,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(availableTags) { tag ->
                        FilterChip(
                            selected = state.itemTags.contains(tag),
                            onClick = { onTagClicked(tag) },
                            label = {
                                Text(tag)
                            },
                            leadingIcon = {
                                if(state.itemTags.contains(tag)) {
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
            }
        }
    }
}