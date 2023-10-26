package com.filipzagulak.closetcanvas.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.filipzagulak.closetcanvas.R

@Composable
fun createIcon(color: String): Painter {
    val iconRes = when (color) {
        "red" -> R.drawable.red_wardrobe
        "blue" -> R.drawable.blue_wardrobe
        "gray" -> R.drawable.gray_wardrobe
        "yellow" -> R.drawable.yellow_wardrobe

        else -> R.drawable.gray_wardrobe
    }
    return painterResource(iconRes)
}