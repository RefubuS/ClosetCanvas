package com.filipzagulak.closetcanvas.presentation.create_wardrobe

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var isWardrobeNameValid by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopClosetCanvasBar(
                title = "Create New Wardrobe",
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
                text = "Select Icon Color",
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
                                width = 3.dp,
                                color = if (color == state.selectedIconColor.value) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = RoundedCornerShape(2.dp)
                            ),
                        tint = Color.Unspecified
                    )
                }
            }

            OutlinedTextField(
                value = state.wardrobeName.value,
                onValueChange = { newState: String ->
                    state.wardrobeName.value = newState
                    isWardrobeNameValid = false
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Wardrobe Name") },
                isError = isWardrobeNameValid
            )
            if (isWardrobeNameValid) {
                Text(
                    text = "Please enter valid name",
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        if (state.wardrobeName.value.isEmpty()) {
                            isWardrobeNameValid = true
                        } else {
                            onWardrobeCreated(
                                userData?.userId,
                                state.wardrobeName.value,
                                state.selectedIconColor.value
                            )
                        }
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
