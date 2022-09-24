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

class OneFightCards : AppCompatActivity() {

    lateinit var backBtn: ImageView

    val imageRef = Firebase.storage.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_fight_cards)

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
            val oneFightCards: RecyclerView = findViewById(R.id.one_card_rc)
            val images = imageRef.child("card_images/one_card/").listAll().await()
            val imageUrls = mutableListOf<String>()
            for(image in images.items) {
                val url = image.downloadUrl.await()
                imageUrls.add(url.toString())
            }
            withContext(Dispatchers.Main) {
                val itemAdapter = ItemAdapter(this@OneFightCards,imageUrls, 2)
                val snapHelper: SnapHelper = LinearSnapHelper()

                oneFightCards.apply {
                    adapter = itemAdapter
                    oneFightCards.layoutManager = LinearLayoutManager(this@OneFightCards,
                        LinearLayoutManager.VERTICAL, false)
                    oneFightCards.adapter = adapter
                    snapHelper.attachToRecyclerView(oneFightCards)

                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@OneFightCards, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}