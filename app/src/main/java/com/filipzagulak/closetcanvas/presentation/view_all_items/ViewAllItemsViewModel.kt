package com.filipzagulak.closetcanvas.presentation.view_all_items

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filipzagulak.closetcanvas.data.remote.WardrobeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

    fun filterItems(
        wardrobeId: String,
        nameFilter: String,
        selectedCategory: String,
        selectedTags: Set<String>
    ) {
        wardrobeRepository.getFilteredItems(
            wardrobeId,
            nameFilter,
            selectedCategory,
            selectedTags
        ) { filteredItemList ->
            _state.value = _state.value.copy(itemList = filteredItemList)
        }
    }

    fun saveCollectionToWardrobe(wardrobeId: String, collectionName: String) {
        viewModelScope.launch {
            wardrobeRepository.saveCollection(wardrobeId, collectionName, _state.value.selectedItems)
        }
    }

    fun clearSelectedItems() {
        _state.value = _state.value.copy(selectedItems = emptyList())
    }
}