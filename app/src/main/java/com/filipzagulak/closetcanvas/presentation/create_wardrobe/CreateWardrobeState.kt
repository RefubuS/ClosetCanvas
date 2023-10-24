package com.filipzagulak.closetcanvas.presentation.create_wardrobe

import androidx.compose.runtime.MutableState

data class CreateWardrobeState(
    var wardrobeName: MutableState<String>,
    var selectedIconColor: MutableState<String>,
    var showCancelConfirmation: MutableState<Boolean>,
    val iconColors: List<String>
)