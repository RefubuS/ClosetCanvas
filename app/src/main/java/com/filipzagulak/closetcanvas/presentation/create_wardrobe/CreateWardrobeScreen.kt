package com.filipzagulak.closetcanvas.presentation.create_wardrobe

import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.filipzagulak.closetcanvas.presentation.sign_in.UserData
import com.filipzagulak.closetcanvas.ui.common.TopClosetCanvasBar
import com.filipzagulak.closetcanvas.ui.common.createIcon


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CreateWardrobeScreen(
    state: CreateWardrobeState,
    userData: UserData?,
    onWardrobeCreated: (String?, String, String) -> Unit,
    onBackButtonClicked: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopClosetCanvasBar(
                title = "Wardrobe creation",
                userData = userData,
                canNavigateBack = true,
                onProfileIconClicked = { },
                onBackButtonClicked = onBackButtonClicked
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                text = "Select icon color",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
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
                            .border(
                                width = 2.dp,
                                color = if (color == state.selectedIconColor.value) MaterialTheme.colorScheme.outlineVariant else Color.Transparent,
                                shape = RoundedCornerShape(2.dp)
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
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        onWardrobeCreated(userData?.userId, state.wardrobeName.value, state.selectedIconColor.value)
                    }
                ) {
                    Text(text = "Save")
                }
                Button(
                    onClick = {
                        onBackButtonClicked()
                    }
                ) {
                    Text(text = "Cancel")
                }
            }
        }
    }
}
