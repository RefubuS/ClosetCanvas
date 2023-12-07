package com.filipzagulak.closetcanvas.presentation.edit_item_details

data class ItemDetails(
    val itemName: String,
    val itemDescription: String,
    val itemCategory: String,
    val itemTags: List<String>,
    val lastWashedDate: String
)
