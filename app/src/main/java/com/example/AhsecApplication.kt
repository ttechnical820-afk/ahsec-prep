package com.example

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

private const val TAG = "AhsecApplication"

class AhsecApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this)
            }

            val databaseIdRes = resources.getIdentifier("firestore_database_id", "string", packageName)
            val databaseId = if (databaseIdRes != 0) getString(databaseIdRes) else null

            val firestore = if (!databaseId.isNullOrBlank()) {
                FirebaseFirestore.getInstance(databaseId)
            } else {
                FirebaseFirestore.getInstance()
            }

            // Enable offline caching and persistence for MCQ storage
            val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
            firestore.firestoreSettings = settings

            Log.d(TAG, "Firestore SDK initialized successfully for persistent storage (DB: $databaseId)")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize Firestore SDK in base application: ${e.message}", e)
        }
    }
}
