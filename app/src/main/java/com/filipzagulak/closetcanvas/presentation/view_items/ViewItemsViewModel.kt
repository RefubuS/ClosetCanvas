package com.filipzagulak.closetcanvas.presentation.view_items

import androidx.lifecycle.ViewModel
import com.filipzagulak.closetcanvas.data.remote.WardrobeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ViewItemsViewModel: ViewModel() {
    private val _state = MutableStateFlow(
        ViewItemsState(
            itemList = emptyList()
        )
    )
    val state = _state.asStateFlow()

    private val wardrobeRepository = WardrobeRepository.getInstance()

    fun fetchItemsFromFirebase(spaceId: Int, wardrobeId: String) {
        wardrobeRepository.getItemsFromSpaceInWardrobe(spaceId, wardrobeId) { itemList ->
            _state.value = _state.value.copy(itemList = itemList)
        }
    }
}