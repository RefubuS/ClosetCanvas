package com.filipzagulak.closetcanvas.presentation.view_all_items

import androidx.lifecycle.ViewModel
import com.filipzagulak.closetcanvas.data.remote.WardrobeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ViewAllItemsViewModel: ViewModel() {
    private val _state = MutableStateFlow(
        ViewAllItemsState(
            itemList = emptyList(),
            selectedItems = emptyList()
        )
    )
    val state = _state.asStateFlow()

    private val wardrobeRepository = WardrobeRepository.getInstance()

    fun fetchAllItemsFromFirebase(wardrobeId: String) {
        wardrobeRepository.getAllItems(wardrobeId) { itemList ->
            _state.value = _state.value.copy(itemList = itemList)
        }
    }

    fun toggleItemSelection(itemId: String) {
        _state.value = _state.value.copy(
            selectedItems = if(_state.value.selectedItems.contains(itemId)) {
                _state.value.selectedItems - itemId
            } else {
                _state.value.selectedItems + itemId
            }
        )
    }
}