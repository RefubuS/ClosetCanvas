package com.filipzagulak.closetcanvas.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.filipzagulak.closetcanvas.presentation.sign_in.UserData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopClosetCanvasBar(
    title: String,
    userData: UserData?,
    onSignOut: () -> Unit,

) {
    TopAppBar(
        title = {
            Text(title)
        },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go back"
                )
            }
        },
        actions = {
            IconButton(onClick = { onSignOut } ) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "Log out of application"
                )
            };
            IconButton(onClick = { /*TODO*/ }) {
                AsyncImage(
                    model = userData?.profilePictureUrl,
                    contentDescription = "profilePicture",
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        },
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth(),
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    )
}
