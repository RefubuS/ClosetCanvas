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
import com.filipzagulak.closetcanvas.presentation.add_item.AddItemScreen
import com.filipzagulak.closetcanvas.presentation.add_item.AddItemViewModel
import com.filipzagulak.closetcanvas.presentation.choose_wardrobe.ChooseWardrobeScreen
import com.filipzagulak.closetcanvas.presentation.choose_wardrobe.ChooseWardrobeViewModel
import com.filipzagulak.closetcanvas.presentation.create_wardrobe.CreateWardrobeScreen
import com.filipzagulak.closetcanvas.presentation.create_wardrobe.CreateWardrobeViewModel
import com.filipzagulak.closetcanvas.presentation.create_wardrobe_layout.CreateWardrobeLayoutScreen
import com.filipzagulak.closetcanvas.presentation.create_wardrobe_layout.CreateWardrobeLayoutViewModel
import com.filipzagulak.closetcanvas.presentation.item_details.ItemDetailsScreen
import com.filipzagulak.closetcanvas.presentation.item_details.ItemDetailsViewModel
import com.filipzagulak.closetcanvas.presentation.manage_wardrobe.ManageWardrobeScreen
import com.filipzagulak.closetcanvas.presentation.manage_wardrobe.ManageWardrobeViewModel
import com.filipzagulak.closetcanvas.presentation.profile.ProfileScreen
import com.filipzagulak.closetcanvas.presentation.sign_in.GoogleAuthUiClient
import com.filipzagulak.closetcanvas.presentation.sign_in.SignInScreen
import com.filipzagulak.closetcanvas.presentation.sign_in.SignInViewModel
import com.filipzagulak.closetcanvas.presentation.view_all_items.ViewAllItemsScreen
import com.filipzagulak.closetcanvas.presentation.view_all_items.ViewAllItemsViewModel
import com.filipzagulak.closetcanvas.presentation.view_items.ViewItemsScreen
import com.filipzagulak.closetcanvas.presentation.view_items.ViewItemsViewModel
import com.filipzagulak.closetcanvas.presentation.view_wardrobe_layout.ViewLayoutScreen
import com.filipzagulak.closetcanvas.presentation.view_wardrobe_layout.ViewLayoutViewModel
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
                                },
                                onWardrobeSelected = { wardrobeId ->
                                    navController.navigate("manage_wardrobe/${wardrobeId}")
                                }
                            )
                        }
                        composable("add_wardrobe") {
                            val viewModel = viewModel<CreateWardrobeViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            CreateWardrobeScreen(
                                state = state,
                                userData = googleAuthUiClient.getSignedInUser(),
                                onWardrobeCreated = { userId, wardrobeName, wardrobeIconColor ->
                                    viewModel.saveWardrobeToRepository(userId, wardrobeName, wardrobeIconColor)
                                    Toast.makeText(
                                        applicationContext,
                                        "Wardrobe created successfully",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate("wardrobes")
                                },
                                onBackButtonClicked = {
                                    navController.navigateUp()
                                }
                            )
                        }
                        composable("manage_wardrobe/{wardrobeId}") {
                            val viewModel = viewModel<ManageWardrobeViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            LaunchedEffect(key1 = Unit) {
                                viewModel.updateWardrobeId(it.arguments?.getString("wardrobeId"))
                            }

                            ManageWardrobeScreen(
                                state = state,
                                userData = googleAuthUiClient.getSignedInUser(),
                                onBackButtonClicked = {
                                    navController.navigateUp()
                                },
                                onProfileIconClicked = {
                                    navController.navigate("profile")
                                },
                                navigateToScreen = { screenName ->
                                    navController.navigate(screenName + "/${state.wardrobeId}")
                                }
                            )
                        }
                        composable("create_wardrobe_layout/{wardrobeId}") { navBackStackEntry ->
                            val viewModel = viewModel<CreateWardrobeLayoutViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            val wardrobeId = navBackStackEntry.arguments?.getString("wardrobeId") ?: ""

                            CreateWardrobeLayoutScreen(
                                userData = googleAuthUiClient.getSignedInUser(),
                                state = state,
                                onSaveButtonClicked = { layoutItemList ->
                                    viewModel.saveLayoutToRepository(wardrobeId = wardrobeId, layoutItemList = layoutItemList)
                                    navController.navigateUp()
                                },
                                onBackButtonClicked = {
                                    navController.navigateUp()
                                },
                                onOptionClicked = { itemId, imageIdToSwap ->
                                    viewModel.updateItemList(itemId = itemId, newImageResourceId = imageIdToSwap)
                                }
                            )
                        }
                        composable("view_wardrobe_layout/{wardrobeId}") { navBackStackEntry ->
                            val viewModel = viewModel<ViewLayoutViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            val wardrobeId = navBackStackEntry.arguments?.getString("wardrobeId") ?: ""

                            LaunchedEffect(key1 = Unit) {
                                viewModel.initializeStateWithRemoteData(wardrobeId)
                            }

                            ViewLayoutScreen(
                                state = state,
                                userData = googleAuthUiClient.getSignedInUser(),
                                onProfileIconClicked = {
                                    navController.navigate("profile")
                                },
                                onBackButtonClicked = {
                                    navController.navigateUp()
                                },
                                onLayoutItemClicked = { spaceId ->
                                    navController.navigate("view_items/${wardrobeId}/${spaceId}")
                                }
                            )
                        }
                        composable("view_items/{wardrobeId}/{spaceId}") { navBackStackEntry ->
                            val viewModel = viewModel<ViewItemsViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            val wardrobeId = navBackStackEntry.arguments?.getString("wardrobeId") ?: ""
                            val spaceId = navBackStackEntry.arguments?.getString("spaceId") ?: ""

                            LaunchedEffect(key1 = Unit) {
                                viewModel.fetchItemsFromFirebase(
                                    spaceId = spaceId.toInt(),
                                    wardrobeId = wardrobeId
                                )
                            }

                            ViewItemsScreen(
                                state = state,
                                userData = googleAuthUiClient.getSignedInUser(),
                                onBackButtonClicked = {
                                    navController.navigateUp()
                                },
                                onProfileIconClicked = {
                                    navController.navigate("profile")
                                },
                                onAddButtonClicked = {
                                    navController.navigate("add_item/${wardrobeId}/${spaceId}")
                                }
                            )
                        }
                        composable("add_item/{wardrobeId}/{spaceId}") { navBackStackEntry ->
                            val viewModel = viewModel<AddItemViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            val wardrobeId = navBackStackEntry.arguments?.getString("wardrobeId") ?: ""
                            val spaceId = navBackStackEntry.arguments?.getString("spaceId") ?: ""

                            AddItemScreen(
                                addItemState = state,
                                userData = googleAuthUiClient.getSignedInUser(),
                                onProfileIconClicked = {
                                    navController.navigate("profile")
                                },
                                onBackButtonClicked = {
                                    navController.navigateUp()
                                },
                                onTagClicked = { tag ->
                                    viewModel.toggleTag(tag)
                                },
                                onSaveButtonClicked = { fileUri, tags, name, description, category, dateWashed ->
                                    lifecycleScope.launch {
                                        viewModel.saveItem(
                                            imageUri = fileUri,
                                            wardrobeId = wardrobeId,
                                            wardrobeSpaceId = spaceId.toInt(),
                                            itemTags = tags,
                                            itemName = name,
                                            itemDescription = description,
                                            itemCategory = category,
                                            lastWashed = dateWashed
                                        )
                                        navController.navigateUp()
                                    }
                                }
                            )
                        }
                        composable("view_all_items/{wardrobeId}") { navBackStackEntry ->
                            val viewModel = viewModel<ViewAllItemsViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            val wardrobeId = navBackStackEntry.arguments?.getString("wardrobeId") ?: ""

                            LaunchedEffect(key1 = Unit) {
                                viewModel.fetchAllItemsFromFirebase(
                                    wardrobeId = wardrobeId
                                )
                            }

                            ViewAllItemsScreen(
                                state = state,
                                userData = googleAuthUiClient.getSignedInUser(),
                                onProfileIconClicked = {
                                    navController.navigate("profile")
                                },
                                onBackButtonClicked = {
                                    navController.navigateUp()
                                },
                                viewItemDetails = { itemId ->
                                    navController.navigate("view_item_details/${wardrobeId}/${itemId}")
                                },
                                onItemLongClick = { itemId ->
                                    viewModel.toggleItemSelection(itemId)
                                },
                                filterItems = { nameFilter, selectedCategory, selectedTags ->
                                    viewModel.filterItems(wardrobeId, nameFilter, selectedCategory, selectedTags)
                                },
                                onSaveCollectionClicked = { collectionName ->
                                    viewModel.saveCollectionToWardrobe(wardrobeId, collectionName)
                                }
                            )
                        }
                        composable("view_item_details/{wardrobeId}/{itemId}") { navBackStackEntry ->
                            val viewModel = viewModel<ItemDetailsViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            val wardrobeId = navBackStackEntry.arguments?.getString("wardrobeId") ?: ""
                            val itemId = navBackStackEntry.arguments?.getString("itemId") ?: ""

                            LaunchedEffect(key1 = Unit) {
                                viewModel.initalizeItemDetails(
                                    wardrobeId = wardrobeId,
                                    itemId = itemId
                                )
                            }

                            ItemDetailsScreen(
                                state = state,
                                userData = googleAuthUiClient.getSignedInUser(),
                                onProfileIconClicked = {
                                    navController.navigate("profile")
                                },
                                onBackButtonClicked = {
                                    navController.navigateUp()
                                }
                            )
                        }
                        composable("view_collections") {

                        }
                    }
                }
            }
        }
    }
}