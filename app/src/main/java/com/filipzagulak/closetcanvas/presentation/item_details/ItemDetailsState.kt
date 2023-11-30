package com.filipzagulak.closetcanvas.presentation.item_details

data class ItemDetailsState(
    val itemPictureUrl: String,
    val itemName: String,
    val itemTags: List<String>,
    val description: String,
    val itemCategory: String,
    val lastWashed: String
)
