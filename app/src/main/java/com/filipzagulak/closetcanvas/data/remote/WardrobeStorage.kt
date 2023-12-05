package com.filipzagulak.closetcanvas.data.remote

import android.net.Uri
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class WardrobeStorage private constructor() {
    private val storage = Firebase.storage

    companion object {
        @Volatile
        private var instance: WardrobeStorage? = null
        fun getInstance(): WardrobeStorage {
            return instance ?: synchronized(this) {
                instance ?: WardrobeStorage().also { instance = it }
            }
        }
    }

    suspend fun savePhotoToStorage(fileUri: Uri?): String = suspendCoroutine { continuation ->
        val storageRef = storage.reference
        val imageRef = storageRef.child("${fileUri?.lastPathSegment}")
        val uploadTask = imageRef.putFile(fileUri!!)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result.toString()
                continuation.resume(downloadUri)
            } else {
                continuation.resumeWithException(task.exception ?: RuntimeException("Failed to upload image"))
            }
        }
    }

    fun deletePhotosFromStorage(photoUrlsList: List<String>) {
        for(downloadUrl in photoUrlsList) {
            val fileRef = storage.getReferenceFromUrl(downloadUrl)

            fileRef.delete()
                .addOnSuccessListener {
                    Log.d("WardrobeStorage", "File deleted successfully: $downloadUrl")
                }
        }
    }
}