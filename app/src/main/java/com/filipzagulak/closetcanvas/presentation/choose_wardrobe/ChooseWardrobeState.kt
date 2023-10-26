package com.filipzagulak.closetcanvas.presentation.choose_wardrobe

data class ChooseWardrobeState(
    val listOfWardrobes: List<WardrobeData>
)

data class WardrobeData(
    val wardrobeId: String?,
    val wardrobeName: String?,
    val wardrobeIconColor: String
)
