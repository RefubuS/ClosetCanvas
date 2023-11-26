package com.filipzagulak.closetcanvas.data.remote

import android.util.Log
import com.filipzagulak.closetcanvas.presentation.add_item.ItemDTO
import com.filipzagulak.closetcanvas.presentation.choose_wardrobe.WardrobeData
import com.filipzagulak.closetcanvas.presentation.create_wardrobe_layout.LayoutItem
import com.filipzagulak.closetcanvas.presentation.view_items.WardrobeItem
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

        for(document in querySnapshot.documents) {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun saveWardrobeLayout(
        wardrobeId: String?,
        layoutData: List<Map<String, Int>>
    ) {
        if(wardrobeId != null) {
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

        val layoutData = documentSnapshot["layoutItemList"] as? List<Map<String, Long>> ?: emptyList()

        return layoutData.map {
            LayoutItem(
                itemId = it["spaceId"]?.toInt() ?: 0,
                resourceId = it["resourceId"]?.toInt() ?: 0
            )
        }
    }

    fun getItemsFromSpaceInWardrobe(spaceId: Int, wardrobeId: String, completion: (List<WardrobeItem>) -> Unit) {
        db.collection("wardrobes")
            .document(wardrobeId)
            .collection("items")
            .whereEqualTo("wardrobeSpaceId", spaceId)
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<WardrobeItem>()

                for(document in result) {
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
                Log.e("WardrobeRespository", "Error fetching items", exception)
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
}