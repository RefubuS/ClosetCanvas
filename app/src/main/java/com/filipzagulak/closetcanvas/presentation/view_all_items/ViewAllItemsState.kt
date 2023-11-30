package com.filipzagulak.closetcanvas.presentation.view_all_items

import com.filipzagulak.closetcanvas.data.local.WardrobeItem

data class ViewAllItemsState(
    val itemList: List<WardrobeItem>,
    val selectedItems: List<String>
)
