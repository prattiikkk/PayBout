package com.example.paybout.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paybout.R
import com.example.paybout.adapter.ItemAdapter
import com.example.paybout.model.Card
import com.google.firebase.firestore.core.View
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File

class Datasource (val location: String) {

//    var imageRef = Firebase.storage.reference
//    var cards = mutableListOf<Card>()
//
//    suspend fun loadCards(): MutableList<Card> {
//
//        val images = imageRef.child("card_images/trending_card/").listAll().await()
//        for (image in images.items) {
//            val url = image.downloadUrl.await()
//            cards.add(Card(url.toString()))
//        }
//
//        return cards
//    }


}