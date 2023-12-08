package com.filipzagulak.closetcanvas.presentation.manage_wardrobe

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.filipzagulak.closetcanvas.presentation.sign_in.UserData
import com.filipzagulak.closetcanvas.ui.common.TopClosetCanvasBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageWardrobeScreen(
    state: ManageWardrobeState,
    userData: UserData?,
    onBackButtonClicked: () -> Unit,
    onProfileIconClicked: () -> Unit,
    navigateToScreen: (String) -> Unit
) {
    Scaffold(
        topBar = { TopClosetCanvasBar(
            title = "Manage Wardrobe",
            userData = userData,
            canNavigateBack = true,
            onProfileIconClicked = onProfileIconClicked,
            onBackButtonClicked = onBackButtonClicked
        ) },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = padding
            ) {
                items(state.tileItems) { item ->
                    ClickableCard(item, navigateToScreen = navigateToScreen)
                    Spacer(
                        modifier = Modifier
                            .height(8.dp)
                    )
                }
            }
        }
    )
}

@Composable
fun ClickableCard(item: TileItem, navigateToScreen: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigateToScreen(item.routeName)
            }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = item.iconRes),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .size(48.dp)
            )
            Text(
                text = item.text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
        }
    }
}
