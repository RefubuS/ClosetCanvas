package com.filipzagulak.closetcanvas.presentation.manage_wardrobe

import androidx.lifecycle.ViewModel
import com.filipzagulak.closetcanvas.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ManageWardrobeViewModel: ViewModel() {
    val tileItems = listOf(
        TileItem("Add wardrobe layout", R.drawable.baseline_add_24, "create_wardrobe_layout"),
        TileItem("View wardrobe layout", R.drawable.baseline_wardrobe_layout_24, "view_wardrobe_layout")
    )

    private val _state = MutableStateFlow(
        ManageWardrobeState(
            wardrobeId = "",
            tileItems = tileItems
        )
    )
    val state = _state.asStateFlow()

    fun updateWardrobeId(wardrobeId: String?) {
        _state.value = _state.value.copy(wardrobeId = wardrobeId)
    }
}