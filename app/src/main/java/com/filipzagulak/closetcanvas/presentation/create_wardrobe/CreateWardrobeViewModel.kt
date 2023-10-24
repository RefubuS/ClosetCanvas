package com.filipzagulak.closetcanvas.presentation.create_wardrobe

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateWardrobeViewModel: ViewModel() {
    val wardrobeName = mutableStateOf("")
    val selectedIconColor = mutableStateOf("gray")
    val showCancelConfirmation = mutableStateOf(false)
    val iconColors = listOf("red", "blue", "gray", "yellow")

    private val _state = MutableStateFlow(CreateWardrobeState(
        wardrobeName,
        selectedIconColor,
        showCancelConfirmation,
        iconColors)
    )

    val state = _state.asStateFlow()


}