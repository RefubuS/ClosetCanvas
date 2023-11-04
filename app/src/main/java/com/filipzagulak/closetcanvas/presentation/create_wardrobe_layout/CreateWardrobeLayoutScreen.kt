package com.filipzagulak.closetcanvas.presentation.create_wardrobe_layout

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateWardrobeLayoutScreen(

) {
    val numRows = 8
    val numColumns = 32
    
    LazyRow(
        modifier = Modifier.fillMaxSize() then Modifier.horizontalScroll(rememberScrollState())
    ) {
        items(numRows) { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for(col in 0 until numColumns) {
                    GridItem()
                }
            }
        }
    }
}

@Composable
fun GridItem() {
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(MaterialTheme.colorScheme.background)
    )
}
