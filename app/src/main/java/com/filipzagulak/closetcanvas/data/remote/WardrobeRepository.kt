package com.filipzagulak.closetcanvas.data.remote

import com.filipzagulak.closetcanvas.presentation.choose_wardrobe.WardrobeData
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
}