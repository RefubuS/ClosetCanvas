package com.filipzagulak.closetcanvas.presentation.manage_wardrobe

import androidx.lifecycle.ViewModel
import com.filipzagulak.closetcanvas.R
import com.filipzagulak.closetcanvas.data.remote.WardrobeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ManageWardrobeViewModel: ViewModel() {
    val wardrobeRepository = WardrobeRepository.getInstance()

    val tileItems = listOf(
        TileItem("Add wardrobe layout", R.drawable.baseline_add_24, "create_wardrobe_layout"),
        TileItem("View wardrobe layout", R.drawable.baseline_wardrobe_layout_24, "view_wardrobe_layout"),
        TileItem("View all items", R.drawable.baseline_dynamic_feed_24, "view_all_items"),
        TileItem("View collections", R.drawable.baseline_collections_bookmark_24, "view_collections"),
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