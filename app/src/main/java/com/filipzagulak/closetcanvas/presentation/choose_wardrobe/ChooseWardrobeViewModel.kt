package com.filipzagulak.closetcanvas.presentation.choose_wardrobe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filipzagulak.closetcanvas.data.remote.WardrobeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChooseWardrobeViewModel: ViewModel() {
    private val wardrobeRepository = WardrobeRepository.getInstance()
    private val _state = MutableStateFlow(ChooseWardrobeState(emptyList()))
    val state = _state.asStateFlow()

    fun fetchWardrobes(userId: String?) {
        viewModelScope.launch {
            val wardrobeList = wardrobeRepository.getWardrobes(userId)
            ChooseWardrobeState(wardrobeList).also { _state.value = it }
        }
    }

    /* fun resetState() {
        _state.update { ChooseWardrobeState(emptyList()) }
    } */
}