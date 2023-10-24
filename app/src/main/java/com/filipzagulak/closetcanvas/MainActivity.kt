package com.filipzagulak.closetcanvas

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.filipzagulak.closetcanvas.presentation.choose_wardrobe.ChooseWardrobeScreen
import com.filipzagulak.closetcanvas.presentation.choose_wardrobe.ChooseWardrobeViewModel
import com.filipzagulak.closetcanvas.presentation.create_wardrobe.CreateWardrobeScreen
import com.filipzagulak.closetcanvas.presentation.create_wardrobe.CreateWardrobeViewModel
import com.filipzagulak.closetcanvas.presentation.profile.ProfileScreen
import com.filipzagulak.closetcanvas.presentation.sign_in.GoogleAuthUiClient
import com.filipzagulak.closetcanvas.presentation.sign_in.SignInScreen
import com.filipzagulak.closetcanvas.presentation.sign_in.SignInViewModel
import com.filipzagulak.closetcanvas.ui.theme.ClosetCanvasTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClosetCanvasTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "sign_in") {
                        composable("sign_in") {
                            val viewModel = viewModel<SignInViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            LaunchedEffect(key1 = Unit) {
                                if (googleAuthUiClient.getSignedInUser() != null) {
                                    navController.navigate("wardrobes")
                                }
                            }

                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if (result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthUiClient.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            viewModel.onSignInResult(signInResult)
                                        }
                                    }
                                }
                            )

                            LaunchedEffect(key1 = state.isSignInSuccessful) {
                                if (state.isSignInSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Sign in successful",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate("wardrobes")
                                    viewModel.resetState()
                                }
                            }

                            SignInScreen(
                                state = state,
                                onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                }
                            )
                        }
                        composable("profile") {
                            ProfileScreen(
                                userData = googleAuthUiClient.getSignedInUser(),
                                onSignOut = {
                                    lifecycleScope.launch {
                                        googleAuthUiClient.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "Signed out",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        navController.navigate("sign_in")
                                    }
                                },
                                onBackButtonClicked = {
                                    navController.navigateUp()
                                }
                            )
                        }
                        composable("wardrobes") {
                            val viewModel = viewModel<ChooseWardrobeViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            LaunchedEffect(key1 = Unit) {
                                viewModel.fetchWardrobes(googleAuthUiClient.getSignedInUser()?.userId)
                            }

                            ChooseWardrobeScreen(
                                state = state,
                                userData = googleAuthUiClient.getSignedInUser(),
                                onProfileIconClicked = {
                                    navController.navigate("profile")
                                },
                                onBackButtonClicked = {
                                    navController.navigateUp()
                                },
                                onAddButtonClicked = {
                                    navController.navigate("add_wardrobe")
                                }
                            )
                        }
                        composable("add_wardrobe") {
                            val viewModel = viewModel<CreateWardrobeViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            CreateWardrobeScreen(
                                state = state
                            )
                        }
                    }
                }
            }
        }
    }
}