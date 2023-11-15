package com.filipzagulak.closetcanvas.presentation.view_wardrobe_layout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filipzagulak.closetcanvas.data.remote.WardrobeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ViewLayoutViewModel: ViewModel() {
    private val _state = MutableStateFlow(
        ViewLayoutState(
            wardrobeLayout = emptyList()
        )
    )
    val state = _state.asStateFlow()

    private val wardrobeRepository = WardrobeRepository.getInstance()

    suspend fun initializeStateWithRemoteData(wardrobeId: String?) {
        viewModelScope.launch {
            if (wardrobeId != null) {
                try {
                    val layoutData = wardrobeRepository.getWardrobeLayoutData(wardrobeId)
                    _state.value = ViewLayoutState(wardrobeLayout = layoutData)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}