package com.filipzagulak.closetcanvas.presentation.view_items_from_collection

import androidx.lifecycle.ViewModel
import com.filipzagulak.closetcanvas.data.remote.WardrobeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ViewItemsFromCollectionViewModel: ViewModel() {
    private val _state = MutableStateFlow(
        ViewItemsFromCollectionState(
            itemList = emptyList()
        )
    )
    val state = _state.asStateFlow()

    private val wardrobeRepository = WardrobeRepository.getInstance()

    fun fetchItemsFromCollection(wardrobeId: String, collectionId: String) {
        wardrobeRepository.getItemsFromCollection(wardrobeId, collectionId) { itemList ->
            _state.value = _state.value.copy(itemList = itemList)
        }
    }
}