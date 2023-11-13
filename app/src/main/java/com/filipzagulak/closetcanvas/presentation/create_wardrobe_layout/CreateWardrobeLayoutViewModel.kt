package com.filipzagulak.closetcanvas.presentation.create_wardrobe_layout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filipzagulak.closetcanvas.R
import com.filipzagulak.closetcanvas.data.remote.WardrobeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateWardrobeLayoutViewModel: ViewModel() {
    private val _state = MutableStateFlow(
        CreateWardrobeLayoutState(
            layoutItemList = listOf(),
            availableOptions = listOf(
                "Drawers" to R.drawable.drawers,
                "Shoe rack" to R.drawable.shoe_rack,
                "Basket" to R.drawable.basket,
                "Clothing rod" to R.drawable.clothing_rod,
                "Shelving" to R.drawable.shelving
            )
        )
    )
    val state = _state.asStateFlow()

    private val wardrobeRepository = WardrobeRepository.getInstance()

    init {
        initializeLayoutItems()
    }

    private fun initializeLayoutItems() {
        val layoutItemList = (1..256).map { itemId ->
            LayoutItem(itemId, resourceId = R.drawable.baseline_add_24)
        }

        _state.value = _state.value.copy(layoutItemList = layoutItemList)
    }

    fun updateItemList(
        itemId: Int,
        newImageResourceId: Int
    ) {
        val updatedLayoutItemList = _state.value.layoutItemList.map { layoutItem ->
            if(layoutItem.itemId == itemId) {
                layoutItem.copy(itemId = layoutItem.itemId, resourceId = newImageResourceId)
            } else {
                layoutItem
            }
        }

        _state.value = _state.value.copy(layoutItemList = updatedLayoutItemList)
    }

    fun saveLayoutToRepository(
        wardrobeId: String?,
        layoutItemList: List<LayoutItem>
    ) {
        val layoutData = layoutItemList.map { layoutItem ->  
            mapOf(
                "spaceId" to layoutItem.itemId,
                "resourceId" to layoutItem.resourceId
            )
        }

        viewModelScope.launch {
            wardrobeRepository.saveWardrobeLayout(wardrobeId, layoutData)
        }
    }
}