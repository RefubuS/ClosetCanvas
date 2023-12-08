package com.filipzagulak.closetcanvas.presentation.add_item

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.filipzagulak.closetcanvas.ComposeFileProvider
import com.filipzagulak.closetcanvas.presentation.sign_in.UserData
import com.filipzagulak.closetcanvas.ui.common.TopClosetCanvasBar
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(
    addItemState: AddItemState,
    userData: UserData?,
    onProfileIconClicked: () -> Unit,
    onBackButtonClicked: () -> Unit,
    onTagClicked: (String) -> Unit,
    onSaveButtonClicked: (Uri?,
                          List<String>,
                          String,
                          String,
                          String,
                          String) -> Unit
) {
    var outlineColor = MaterialTheme.colorScheme.outline
    var iconColor = MaterialTheme.colorScheme.primary
    var isNameValid by remember { mutableStateOf(false) }
    var isPhotoUrlValid by remember { mutableStateOf(false) }
    var isSelectedDateValid by remember { mutableStateOf(false) }
    var isDescriptionValid by remember { mutableStateOf(false) }
    var isSelectedTagsEmpty by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val datePickerState = rememberDatePickerState()

    var selectedCategory by remember { mutableStateOf(addItemState.categories[0]) }
    var isDatePickerVisible by remember { mutableStateOf(false) }
    var isDropdownMenuExpanded by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(TextFieldValue()) }
    var description by remember { mutableStateOf(TextFieldValue()) }
    var selectedDate by remember { mutableStateOf("") }

    var hasImage by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            hasImage = success
        }
    )

    Scaffold(
        topBar = { TopClosetCanvasBar(
            title = "Add New Item",
            userData = userData,
            canNavigateBack = true,
            onProfileIconClicked = onProfileIconClicked,
            onBackButtonClicked = onBackButtonClicked
        ) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = {
                item {
                    if(hasImage && imageUri != null) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = null,
                            modifier = Modifier
                                .size(240.dp)
                                .padding(8.dp)
                                .fillMaxSize()
                        )
                    } else {
                        if(isPhotoUrlValid) {
                            outlineColor = MaterialTheme.colorScheme.error
                            iconColor = MaterialTheme.colorScheme.error
                        }
                        Box(
                            modifier = Modifier
                                .size(238.dp)
                                .padding(8.dp)
                                .border(1.dp, outlineColor, shape = RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = {
                                    val uri = ComposeFileProvider.getImageUri(context)
                                    imageUri = uri
                                    cameraLauncher.launch(uri)
                                    isPhotoUrlValid = false
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = iconColor
                                )
                            }
                        }
                        if(isPhotoUrlValid) {
                            Text(
                                text = "Please take a photo before saving",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
                item {
                    LazyHorizontalStaggeredGrid(
                        rows = StaggeredGridCells.Fixed(5),
                        modifier = Modifier
                            .height(180.dp)
                            .padding(start = 8.dp, top = 8.dp, bottom = 4.dp, end = 8.dp),
                        horizontalItemSpacing = 4.dp,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(addItemState.availableTags) { tag ->
                            FilterChip(
                                selected = addItemState.selectedTags.contains(tag),
                                onClick = {
                                    onTagClicked(tag)
                                    isSelectedTagsEmpty = false
                                },
                                label = {
                                    Text(tag)
                                },
                                leadingIcon = {
                                    if(addItemState.selectedTags.contains(tag)) {
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
                    if(isSelectedTagsEmpty) {
                        Text(
                            text = "Please select at least one tag",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            isNameValid = false
                        },
                        label = { Text("Item Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
                        isError = isNameValid
                    )
                    if(isNameValid) {
                        Text(
                            text = "Please enter valid name",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            description = it
                            isDescriptionValid = false
                        },
                        label = { Text("Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
                        isError = isDescriptionValid
                    )
                    if(isDescriptionValid) {
                        Text(
                            text = "Please enter valid description",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    OutlinedTextField(
                        readOnly = true,
                        value = selectedDate,
                        onValueChange = {
                            selectedDate = it
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
                            .padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
                        isError = isSelectedDateValid
                    )
                    if(isSelectedDateValid) {
                        Text(
                            text = "Please select valid date in Date Picker",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    if(isDatePickerVisible) {
                        DatePickerDialog(
                            onDismissRequest = {
                                isDatePickerVisible = false
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        if(datePickerState.selectedDateMillis != null) {
                                            selectedDate = convertMillisToDate(datePickerState.selectedDateMillis)
                                        }
                                        isSelectedDateValid = false
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
                                .fillMaxWidth()
                                .padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
                            readOnly = true,
                            value = selectedCategory,
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
                            },
                            modifier = Modifier
                                .height(256.dp)
                        ) {
                            addItemState.categories.forEach { category ->
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
                }
                item {
                    Button(
                        onClick = {
                            when {
                                imageUri == null -> isPhotoUrlValid = true
                                addItemState.selectedTags.isEmpty() -> isSelectedTagsEmpty = true
                                name.text.isEmpty() -> isNameValid = true
                                description.text.isEmpty() -> isDescriptionValid = true
                                selectedDate.isEmpty() -> isSelectedDateValid = true
                                else -> onSaveButtonClicked(
                                    imageUri,
                                    addItemState.selectedTags,
                                    name.text,
                                    description.text,
                                    selectedCategory,
                                    selectedDate
                                )
                            }
                        },
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Text("Save")
                    }
                }
            }
        )
    }
}

fun convertMillisToDate(selectedDateMillis: Long?): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    return formatter.format(selectedDateMillis)
}
