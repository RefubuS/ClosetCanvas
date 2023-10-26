package com.filipzagulak.closetcanvas.presentation.choose_wardrobe

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.filipzagulak.closetcanvas.presentation.sign_in.UserData
import com.filipzagulak.closetcanvas.ui.common.TopClosetCanvasBar
import com.filipzagulak.closetcanvas.ui.common.createIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseWardrobeScreen(
    state: ChooseWardrobeState,
    userData: UserData?,
    onBackButtonClicked: () -> Unit,
    onProfileIconClicked: () -> Unit,
    onAddButtonClicked: () -> Unit
) {
    Scaffold(
        topBar = { TopClosetCanvasBar(
            title = "Wardrobes Screen",
            userData = userData,
            canNavigateBack = false,
            onProfileIconClicked = onProfileIconClicked,
            onBackButtonClicked = onBackButtonClicked
        ) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome, ${userData?.username}",
                    style = TextStyle(fontSize = 24.sp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.background),
                    horizontalArrangement = Arrangement.Center,
                    content = {
                        items(state.listOfWardrobes) { wardrobeData ->
                            WardrobeItem(wardrobeData)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                FloatingActionButton(
                    onClick = onAddButtonClicked,
                    content = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add wardrobe"
                        )
                    }
                )
            }
        }
    )
}

@Composable
fun WardrobeItem(wardrobeData: WardrobeData) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable { },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = createIcon(color = wardrobeData.wardrobeIconColor),
            contentDescription = wardrobeData.wardrobeName,
            modifier = Modifier
                .size(64.dp)
                .padding(8.dp),
            tint = Color.Unspecified
        )
        Text(
            text = wardrobeData.wardrobeName ?: "Unnamed Wardrobe",
            style = TextStyle(fontSize = 16.sp),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

