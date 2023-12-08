package com.filipzagulak.closetcanvas.presentation.sign_in

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.filipzagulak.closetcanvas.R

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.round_logo_no_text),
            contentDescription = stringResource(id = R.string.logo),
            modifier = Modifier
                .padding(bottom = 16.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.text_no_background),
            contentDescription = stringResource(id = R.string.app_name),
            modifier = Modifier
                .padding(bottom = 16.dp)
        )
        Button(onClick = onSignInClick,
            modifier = Modifier
        ) {
            Text(
                text = "Sign In",
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "App created by Filip Zagulak",
            style = MaterialTheme.typography.labelLarge
        )
    }
}