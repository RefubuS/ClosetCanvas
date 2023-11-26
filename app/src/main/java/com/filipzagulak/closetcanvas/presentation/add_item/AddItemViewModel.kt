package com.filipzagulak.closetcanvas.presentation.add_item

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filipzagulak.closetcanvas.data.remote.WardrobeRepository
import com.filipzagulak.closetcanvas.data.remote.WardrobeStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AddItemViewModel: ViewModel() {
    private val availableTags = listOf(
        "Casual",
        "Formal",
        "Sportswear",
        "Business Casual",
        "Athleisure",
        "Outdoor",
        "Workout",
        "Party",
        "Date Night",
        "Travel",
        "Winter",
        "Summer",
        "Spring",
        "Autumn",
        "Vintage",
        "Boho",
        "Classic",
        "Streetwear",
        "Minimalist",
        "Bold Colors",
        "Neutral Colors",
        "Prints",
        "Stripes",
        "Plaids",
        "Denim",
        "Leather",
        "Cotton",
        "Silk",
        "Wool",
        "Sustainable",
        "Ethical",
        "DIY",
        "Handmade",
        "Custom",
        "Thrifted",
        "Sale",
        "Favorite",
        "To Donate",
        "To Sell",
        "To Repair",
        "To Clean"
    )

    private val categories = listOf(
        "T-shirt",
        "Blouse",
        "Shirt",
        "Sweater",
        "Hoodie",
        "Jacket",
        "Coat",
        "Jeans",
        "Trousers",
        "Shorts",
        "Skirt",
        "Dress",
        "Suit",
        "Tie",
        "Scarf",
        "Hat",
        "Cap",
        "Socks",
        "Shoes",
        "Boots",
        "Sandals",
        "Sneakers",
        "Handbag",
        "Backpack",
        "Wallet",
        "Watch",
        "Bracelet",
        "Necklace",
        "Earrings",
        "Ring",
        "Gloves",
        "Belt",
        "Underwear",
        "Pajamas",
        "Swimsuit",
        "Sportswear",
        "Workout Gear",
        "Uniform",
        "Costume"
    )

    private val _state = MutableStateFlow(
        AddItemState(
            availableTags = availableTags,
            categories = categories
        )
    )
    val state = _state.asStateFlow()

    private val wardrobeStorage = WardrobeStorage.getInstance()
    private val wardrobeRepository = WardrobeRepository.getInstance()

    fun toggleTag(tag: String) {
        _state.value = _state.value.copy(
            selectedTags = if(_state.value.selectedTags.contains(tag)) {
                _state.value.selectedTags - tag
            } else {
                _state.value.selectedTags + tag
            }
        )
    }

    suspend fun saveItem(
        imageUri: Uri?,
        wardrobeId: String,
        wardrobeSpaceId: Int,
        itemTags: List<String>,
        itemName: String,
        itemDescription: String,
        itemCategory: String,
        lastWashed: String
    ) {
        val downloadUrl = suspendCoroutine<String> { continuation ->
            viewModelScope.launch {
                val url = wardrobeStorage.savePhotoToStorage(imageUri)
                continuation.resume(url)
            }
        }

        val newItem = ItemDTO(
            wardrobeSpaceId = wardrobeSpaceId,
            itemPictureUrl = downloadUrl,
            itemTags = itemTags,
            itemName = itemName,
            itemDescription = itemDescription,
            itemCategory = itemCategory,
            lastWashed = lastWashed
        )

        viewModelScope.launch {
            wardrobeRepository.saveItemToWardrobe(wardrobeId, newItem)
        }
    }
}