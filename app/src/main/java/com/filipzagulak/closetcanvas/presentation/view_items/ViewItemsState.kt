package com.filipzagulak.closetcanvas.presentation.view_items

data class ViewItemsState(
    val itemList: List<WardrobeItem>
)

data class WardrobeItem(
    val itemId: String,
    val wardrobeId: String,
    val itemPictureUrl: String
)
