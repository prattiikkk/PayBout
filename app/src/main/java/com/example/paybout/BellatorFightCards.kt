package com.example.paybout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.paybout.adapter.ItemAdapter
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class BellatorFightCards : AppCompatActivity() {

    lateinit var backBtn: ImageView

    val imageRef = Firebase.storage.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bellator_fight_cards)

        backBtn = findViewById(R.id.back_btn)

        backBtn.setOnClickListener{
            val intent = Intent(this, HomeFragment::class.java)
            startActivity(intent)
            finish()
        }

        listFightCards()
    }

    private fun listFightCards() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val bellatorFightCards: RecyclerView = findViewById(R.id.bellator_card_rc)
            val images = imageRef.child("card_images/bellator_card/").listAll().await()
            val imageUrls = mutableListOf<String>()
            for(image in images.items) {
                val url = image.downloadUrl.await()
                imageUrls.add(url.toString())
            }
            withContext(Dispatchers.Main) {
                val itemAdapter = ItemAdapter(this@BellatorFightCards, imageUrls, 2)
                val snapHelper: SnapHelper = LinearSnapHelper()

                bellatorFightCards.apply {
                    adapter = itemAdapter
                    bellatorFightCards.layoutManager = LinearLayoutManager(this@BellatorFightCards,
                        LinearLayoutManager.VERTICAL, false)
                    bellatorFightCards.adapter = adapter
                    snapHelper.attachToRecyclerView(bellatorFightCards)

                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@BellatorFightCards, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}