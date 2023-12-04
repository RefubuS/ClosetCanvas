package com.filipzagulak.closetcanvas.presentation.view_collections

data class ViewCollectionsState(
    val collections: List<CollectionItem>
)

data class CollectionItem(
    val collectionId: String,
    val collectionName: String
)