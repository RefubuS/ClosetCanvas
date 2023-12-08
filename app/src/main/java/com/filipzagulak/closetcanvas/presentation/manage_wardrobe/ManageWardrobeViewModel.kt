package com.filipzagulak.closetcanvas.presentation.manage_wardrobe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filipzagulak.closetcanvas.R
import com.filipzagulak.closetcanvas.data.remote.WardrobeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ManageWardrobeViewModel: ViewModel() {
    val wardrobeRepository = WardrobeRepository.getInstance()

    val tileItems = listOf(
        TileItem("Add wardrobe layout", R.drawable.baseline_add_24, "create_wardrobe_layout"),
        TileItem("Wardrobe layout", R.drawable.baseline_wardrobe_layout_24, "view_wardrobe_layout"),
        TileItem("All items", R.drawable.baseline_dynamic_feed_24, "view_all_items"),
        TileItem("Collections", R.drawable.baseline_collections_bookmark_24, "view_collections"),
    )

    private val _state = MutableStateFlow(
        ManageWardrobeState(
            wardrobeId = "",
            tileItems = emptyList()
        )
    )
    val state = _state.asStateFlow()

    fun updateWardrobeDetails(wardrobeId: String) {
        _state.value = _state.value.copy(wardrobeId = wardrobeId)

        viewModelScope.launch {
            wardrobeRepository.checkIfLayoutExists(wardrobeId) { layoutItemList ->
                val layoutItemListExists = layoutItemList
                updateTileItems(layoutItemListExists)
            }
        }
    }

    private fun updateTileItems(layoutItemListExists: Boolean) {
        val updatedTileItems = if (layoutItemListExists) {
            tileItems.filter { it.text != "Add wardrobe layout" }
        } else {
            listOf(tileItems.first { it.text == "Add wardrobe layout" })
        }
        _state.value = _state.value.copy(tileItems = updatedTileItems)
    }
}