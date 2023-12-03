package com.filipzagulak.closetcanvas.data.remote

import android.util.Log
import com.filipzagulak.closetcanvas.data.local.WardrobeItem
import com.filipzagulak.closetcanvas.presentation.add_item.ItemDTO
import com.filipzagulak.closetcanvas.presentation.choose_wardrobe.WardrobeData
import com.filipzagulak.closetcanvas.presentation.create_wardrobe_layout.LayoutItem
import com.filipzagulak.closetcanvas.presentation.item_details.ItemDetailsState
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class WardrobeRepository private constructor() {
    private val db = Firebase.firestore

    companion object {
        @Volatile
        private var instance: WardrobeRepository? = null

        fun getInstance(): WardrobeRepository {
            return instance ?: synchronized(this) {
                instance ?: WardrobeRepository().also { instance = it }
            }
        }
    }

    suspend fun getWardrobes(userId: String?): List<WardrobeData> {
        val wardrobeList = mutableListOf<WardrobeData>()

        val querySnapshot = db.collection("wardrobes")
            .whereEqualTo("userId", userId)
            .get()
            .await()

        for (document in querySnapshot.documents) {
            val wardrobeId = document.getString("wardrobeId") ?: ""
            val wardrobeName = document.getString("wardrobeName") ?: ""
            val wardrobeIconColor = document.getString("wardrobeIconColor") ?: ""
            wardrobeList.add(WardrobeData(wardrobeId, wardrobeName, wardrobeIconColor))
        }

        return wardrobeList
    }

    suspend fun addWardrobe(
        userId: String?,
        wardrobeName: String,
        wardrobeIconColor: String
    ) {
        try {
            val wardrobeReference = db.collection("wardrobes").document()
            val newWardrobeData = hashMapOf(
                "userId" to userId,
                "wardrobeName" to wardrobeName,
                "wardrobeId" to wardrobeReference.id,
                "wardrobeIconColor" to wardrobeIconColor
            )

            wardrobeReference.set(newWardrobeData).await()

            val itemsCollection = wardrobeReference.collection("items")
            itemsCollection.add(hashMapOf<String, Any>()).await()

            val collectionsCollection = wardrobeReference.collection("collections")
            collectionsCollection.add(hashMapOf<String, Any>()).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun saveWardrobeLayout(
        wardrobeId: String?,
        layoutData: List<Map<String, Int>>
    ) {
        if (wardrobeId != null) {
            db.collection("wardrobes")
                .document(wardrobeId)
                .update("layoutItemList", layoutData)
                .await()
        }
    }

    suspend fun getWardrobeLayoutData(wardrobeId: String): List<LayoutItem> {
        val documentSnapshot = db.collection("wardrobes")
            .document(wardrobeId)
            .get()
            .await()

        val layoutData =
            documentSnapshot["layoutItemList"] as? List<Map<String, Long>> ?: emptyList()

        return layoutData.map {
            LayoutItem(
                itemId = it["spaceId"]?.toInt() ?: 0,
                resourceId = it["resourceId"]?.toInt() ?: 0
            )
        }
    }

    fun getItemsFromSpaceInWardrobe(
        spaceId: Int,
        wardrobeId: String,
        completion: (List<WardrobeItem>) -> Unit
    ) {
        db.collection("wardrobes")
            .document(wardrobeId)
            .collection("items")
            .whereEqualTo("wardrobeSpaceId", spaceId)
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<WardrobeItem>()

                for (document in result) {
                    val itemId = document.getString("itemId") ?: ""
                    val itemPictureUrl = document.getString("itemPictureUrl") ?: ""

                    val wardrobeItem = WardrobeItem(
                        itemId = itemId,
                        wardrobeId = wardrobeId,
                        itemPictureUrl = itemPictureUrl
                    )
                    itemList.add(wardrobeItem)
                }
                completion(itemList)
            }
            .addOnFailureListener { exception ->
                Log.e("WardrobeRepository", "Error fetching items", exception)
                completion(emptyList())
            }
    }

    suspend fun saveItemToWardrobe(
        wardrobeId: String,
        itemData: ItemDTO
    ) {
        val wardrobeDocumentReference = db.collection("wardrobes").document(wardrobeId)
        val newItemDocumentReference = wardrobeDocumentReference.collection("items").document()

        val itemDataHashMap = hashMapOf(
            "itemId" to newItemDocumentReference.id,
            "wardrobeSpaceId" to itemData.wardrobeSpaceId,
            "itemPictureUrl" to itemData.itemPictureUrl,
            "itemTags" to itemData.itemTags,
            "itemName" to itemData.itemName,
            "itemDescription" to itemData.itemDescription,
            "itemCategory" to itemData.itemCategory,
            "lastWashed" to itemData.lastWashed
        )

        newItemDocumentReference.set(itemDataHashMap).await()
    }

    fun getAllItems(wardrobeId: String, completion: (List<WardrobeItem>) -> Unit) {
        db.collection("wardrobes")
            .document(wardrobeId)
            .collection("items")
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<WardrobeItem>()

                for (document in result) {
                    val itemId = document.getString("itemId") ?: ""
                    val itemPictureUrl = document.getString("itemPictureUrl") ?: ""

                    val wardrobeItem = WardrobeItem(
                        itemId = itemId,
                        wardrobeId = wardrobeId,
                        itemPictureUrl = itemPictureUrl
                    )
                    itemList.add(wardrobeItem)
                }
                completion(itemList)
            }
            .addOnFailureListener { exception ->
                Log.e("WardrobeRepository", "Error fetching items", exception)
                completion(emptyList())
            }
    }

    suspend fun getItemDetails(wardrobeId: String, itemId: String): ItemDetailsState {
        val documentSnapshot = db.collection("wardrobes")
            .document(wardrobeId)
            .collection("items")
            .document(itemId)
            .get()
            .await()

        return ItemDetailsState(
            itemPictureUrl = documentSnapshot.getString("itemPictureUrl") ?: "",
            itemName = documentSnapshot.getString("itemName") ?: "",
            itemTags = documentSnapshot.get("itemTags") as? List<String> ?: emptyList(),
            description = documentSnapshot.getString("itemDescription") ?: "",
            itemCategory = documentSnapshot.getString("itemCategory") ?: "",
            lastWashed = documentSnapshot.getString("lastWashed") ?: ""
        )
    }

    fun getFilteredItems(
        wardrobeId: String,
        nameFilter: String,
        selectedCategory: String,
        selectedTags: Set<String>,
        completion: (List<WardrobeItem>) -> Unit
    ) {
        val itemsCollection = db.collection("wardrobes")
            .document(wardrobeId)
            .collection("items")

        var query: Query = itemsCollection

        if(nameFilter.isNotEmpty()) {
            query = query.orderBy("itemName")
                .startAt(nameFilter)
                .endAt("$nameFilter~")
        }
        if(selectedCategory.isNotEmpty()) {
            query = query.whereEqualTo("itemCategory", selectedCategory)
        }
        if(selectedTags.isNotEmpty()) {
            query = query.whereArrayContainsAny("itemTags", selectedTags.toList())
        }

        query.get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<WardrobeItem>()

                for (document in result) {
                    val itemId = document.getString("itemId") ?: ""
                    val itemPictureUrl = document.getString("itemPictureUrl") ?: ""

                    val wardrobeItem = WardrobeItem(
                        itemId = itemId,
                        wardrobeId = wardrobeId,
                        itemPictureUrl = itemPictureUrl
                    )
                    itemList.add(wardrobeItem)
                }
                completion(itemList)
            }
            .addOnFailureListener { exception ->
                completion(emptyList())
                exception.printStackTrace()
            }
    }

    suspend fun saveCollection(wardrobeId: String, collectionName: String, selectedItems: List<String>) {
        val wardrobeDocumentReference = db.collection("wardrobes").document(wardrobeId)
        val newCollectionDocumentReference = wardrobeDocumentReference.collection("collections").document()

        val collectionData = hashMapOf(
            "collectionId" to newCollectionDocumentReference.id,
            "collectionName" to collectionName,
            "itemsInCollection" to selectedItems
        )

        newCollectionDocumentReference.set(collectionData).await()
    }
}