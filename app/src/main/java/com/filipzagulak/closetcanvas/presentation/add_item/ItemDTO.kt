package com.filipzagulak.closetcanvas.presentation.add_item

data class ItemDTO(
    val wardrobeSpaceId: Int,
    val itemPictureUrl: String,
    val itemTags: List<String>,
    val itemName: String,
    val itemDescription: String,
    val itemCategory: String,
    val lastWashed: String
)
