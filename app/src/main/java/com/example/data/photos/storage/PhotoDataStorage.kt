package com.example.data.photos.storage

import android.util.Log
import com.example.domain.photos.model.Photo

class PhotoDataStorage {

    /**
     * I want to keep this here just so I can see the difference between the two for later :)
     */
   /* var photoCache: List<Photo>? = null
        set(value) {
            field = value
            lastUpdated = Date().time
        }
        get() {
            return if (isCacheExpired()) null else { field }
        }*/

    private var photoCache: List<Photo>? = null
    private var lastUpdated: Long = 0

    fun setPhotoCache(items: List<Photo>) {
        photoCache = items
        lastUpdated = System.currentTimeMillis()
    }

    fun getPhotosCache(): List<Photo>? = photoCache

    fun isCacheExpired(): Boolean {
        Log.d("PhotoDataStorage", "isCacheExpired: ${System.currentTimeMillis() - lastUpdated}")
        return (System.currentTimeMillis() - lastUpdated) > FifteenMinutes
    }

    companion object {

        const val FifteenMinutes: Long = 15 * 60 * 1000
    }
}