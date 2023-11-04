package com.filipzagulak.closetcanvas.presentation.manage_wardrobe

data class ManageWardrobeState(
    val wardrobeId: String?,
    val tileItems: List<TileItem>
)

data class TileItem(
    val text: String,
    val iconRes: Int
)