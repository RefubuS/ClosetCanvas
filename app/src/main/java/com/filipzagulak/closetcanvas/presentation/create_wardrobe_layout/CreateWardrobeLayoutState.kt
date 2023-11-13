package com.filipzagulak.closetcanvas.presentation.create_wardrobe_layout

data class CreateWardrobeLayoutState(
    val layoutItemList: List<LayoutItem>,
    val availableOptions: List<Pair<String, Int>>
)

data class LayoutItem(
    val itemId: Int,
    val resourceId: Int
)