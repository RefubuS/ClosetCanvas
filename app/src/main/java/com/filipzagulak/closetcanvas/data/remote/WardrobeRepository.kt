package com.filipzagulak.closetcanvas.data.remote

import com.filipzagulak.closetcanvas.presentation.choose_wardrobe.WardrobeData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class WardrobeRepository {
    private val db = Firebase.firestore

    suspend fun getWardrobes(userId: String?): List<WardrobeData> {
        val wardrobeList = mutableListOf<WardrobeData>()

        val querySnapshot = db.collection("wardrobes")
            .whereEqualTo("userId", userId)
            .get()
            .await()

        for(document in querySnapshot.documents) {
            val wardrobeId = document.getString("wardrobeId") ?: ""
            val wardrobeName = document.getString("wardrobeName") ?: ""
            wardrobeList.add(WardrobeData(wardrobeId, wardrobeName))
        }

        return wardrobeList
    }
}