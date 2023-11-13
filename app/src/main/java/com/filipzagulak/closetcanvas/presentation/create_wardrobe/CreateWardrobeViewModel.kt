package com.filipzagulak.closetcanvas.presentation.create_wardrobe

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filipzagulak.closetcanvas.data.remote.WardrobeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateWardrobeViewModel: ViewModel() {
    private val wardrobeRepository = WardrobeRepository.getInstance()

    val wardrobeName = mutableStateOf("")
    val selectedIconColor = mutableStateOf("gray")
    val showCancelConfirmation = mutableStateOf(false)
    val iconColors = listOf("gray", "red", "blue", "yellow")

    private val _state = MutableStateFlow(CreateWardrobeState(
        wardrobeName,
        selectedIconColor,
        showCancelConfirmation,
        iconColors)
    )

    val state = _state.asStateFlow()

    fun saveWardrobeToRepository(
        userId: String?,
        wardrobeName: String,
        wardrobeIconColor: String,
    ) {
        viewModelScope.launch {
            wardrobeRepository.addWardrobe(userId, wardrobeName, wardrobeIconColor)
        }
    }
}