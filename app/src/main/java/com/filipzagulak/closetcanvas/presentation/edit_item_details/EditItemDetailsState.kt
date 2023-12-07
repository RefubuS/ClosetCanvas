package com.filipzagulak.closetcanvas.presentation.edit_item_details

import androidx.compose.runtime.MutableState

data class EditItemDetailsState(
    val itemName: MutableState<String>,
    val itemDescription: MutableState<String>,
    val itemCategory: MutableState<String>,
    val itemTags: MutableList<String>,
    val lastWashedDate: MutableState<String>
)
