package com.filipzagulak.closetcanvas.presentation.item_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filipzagulak.closetcanvas.data.remote.WardrobeRepository
import com.filipzagulak.closetcanvas.data.remote.WardrobeStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ItemDetailsViewModel: ViewModel() {
    private val _state = MutableStateFlow(
        ItemDetailsState(
            itemPictureUrl = "",
            itemName = "",
            itemTags = emptyList(),
            description = "",
            itemCategory = "",
            lastWashed = ""
        )
    )
    val state = _state.asStateFlow()

    private val wardrobeRepository = WardrobeRepository.getInstance()
    private val wardrobeStorage = WardrobeStorage.getInstance()

    suspend fun initalizeItemDetails(wardrobeId: String, itemId: String) {
        viewModelScope.launch {
            val itemDetails = wardrobeRepository.getItemDetails(wardrobeId, itemId)

            _state.value = _state.value.copy(
                itemPictureUrl = itemDetails.itemPictureUrl,
                itemName = itemDetails.itemName,
                itemTags = itemDetails.itemTags,
                description = itemDetails.description,
                itemCategory = itemDetails.itemCategory,
                lastWashed = itemDetails.lastWashed
            )
        }
    }

    fun deleteItem(wardrobeId: String, itemId: String) {
        viewModelScope.launch {
            wardrobeRepository.getPhotoUrl(wardrobeId, itemId) { photoUrl ->
                wardrobeStorage.deletePhotosFromStorage(listOf(photoUrl))
            }
            wardrobeRepository.deleteItem(wardrobeId, itemId)
        }
    }
}