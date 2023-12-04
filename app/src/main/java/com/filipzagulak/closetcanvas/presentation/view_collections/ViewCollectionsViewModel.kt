package com.filipzagulak.closetcanvas.presentation.view_collections

import androidx.lifecycle.ViewModel
import com.filipzagulak.closetcanvas.data.remote.WardrobeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ViewCollectionsViewModel: ViewModel() {
    private val _state = MutableStateFlow(
        ViewCollectionsState(
            collections = emptyList()
        )
    )
    val state = _state.asStateFlow()

    private val wardrobeRepository = WardrobeRepository.getInstance()

    fun fetchAllCollectionFromFirebase(wardrobeId: String) {
        wardrobeRepository.getAllCollections(wardrobeId) { collectionList ->
            _state.value = _state.value.copy(collections = collectionList)
        }
    }

    suspend fun deleteCollection(wardrobeId: String, collectionId: String) {
        wardrobeRepository.deleteCollection(wardrobeId, collectionId)
    }
}