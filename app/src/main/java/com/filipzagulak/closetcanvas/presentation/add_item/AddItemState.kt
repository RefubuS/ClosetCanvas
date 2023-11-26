package com.filipzagulak.closetcanvas.presentation.add_item

data class AddItemState(
    val availableTags: List<String>,
    val categories: List<String>,
    var selectedTags: List<String> = emptyList(),
    var selectedDate: String = ""
)
