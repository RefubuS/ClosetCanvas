package com.filipzagulak.closetcanvas.presentation.create_wardrobe

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.filipzagulak.closetcanvas.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateWardrobeScreen(
    state: CreateWardrobeState,
    //onWardrobeCreated: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Select icon color",
            style = MaterialTheme.typography.bodyMedium
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            state.iconColors.forEach { color ->
                Icon(
                    painter = createIcon(color = color),
                    contentDescription = "$color icon",
                    modifier = Modifier
                        .size(64.dp)
                        .clickable {
                            color.also { state.selectedIconColor.value = it }
                        }
                        .padding(4.dp)
                        .background(
                            color = if (color == state.selectedIconColor.value) MaterialTheme.colorScheme.outline else Color.Transparent,
                            shape = MaterialTheme.shapes.small
                        ),
                    tint = Color.Unspecified
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = state.wardrobeName.value,
            onValueChange = { newState: String ->
                state.wardrobeName.value = newState
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { /* TODO */ }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                //onWardrobeCreated(state.wardrobeName.value, state.selectedIconColor.value)
            }
        ) {
            Text(text = "Save")
        }
    }
}

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
