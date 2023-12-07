package com.filipzagulak.closetcanvas.presentation.edit_item_details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filipzagulak.closetcanvas.data.remote.WardrobeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditItemDetailsViewModel: ViewModel() {
    private val _state = MutableStateFlow(
        EditItemDetailsState(
            itemName = mutableStateOf(""),
            itemTags = mutableListOf(),
            itemDescription = mutableStateOf(""),
            itemCategory = mutableStateOf(""),
            lastWashedDate = mutableStateOf("")
        )
    )
    val state = _state.asStateFlow()

    private val wardrobeRepository = WardrobeRepository.getInstance()

    suspend fun initializeDetails(wardrobeId: String, itemId: String) {
        val itemDetails = wardrobeRepository.getItemDetails(wardrobeId, itemId)

        _state.value = _state.value.copy(
            itemName = mutableStateOf(itemDetails.itemName),
            itemTags = mutableListOf<String>().apply { addAll(itemDetails.itemTags) },
            itemDescription = mutableStateOf(itemDetails.description),
            itemCategory = mutableStateOf(itemDetails.itemCategory),
            lastWashedDate = mutableStateOf(itemDetails.lastWashed)
        )
    }

    fun toggleTag(tag: String) {
        _state.value = _state.value.copy(
            itemTags = if(_state.value.itemTags.contains(tag)) {
                (_state.value.itemTags - tag).toMutableList()
            } else {
                (_state.value.itemTags + tag).toMutableList()
            }
        )
    }

    fun saveChanges(wardrobeId: String, itemId: String) {
        val itemDetails = ItemDetails(
            itemName = _state.value.itemName.value,
            itemDescription = _state.value.itemDescription.value,
            itemCategory = _state.value.itemCategory.value,
            itemTags = _state.value.itemTags,
            lastWashedDate = _state.value.lastWashedDate.value
        )

        viewModelScope.launch {
            wardrobeRepository.saveChangesToItem(wardrobeId, itemId, itemDetails)
        }
    }
}