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

class UfcFightCards : AppCompatActivity() {

    lateinit var backBtn: ImageView

    val imageRef = Firebase.storage.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ufc_fight_cards)
        backBtn = findViewById(R.id.back_btn)

        backBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("check", 1)
            startActivity(intent)
            finish()
        }

        listFightCards()
    }

    private fun listFightCards() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val ufcFightCards = findViewById<RecyclerView>(R.id.ufc_card_rc)
            val images = imageRef.child("card_images/").child("ufc_card/").listAll().await()
            val imageUrls = mutableListOf<String>()
            for(image in images.items) {
                val url = image.downloadUrl.await()
                imageUrls.add(url.toString())
            }
            withContext(Dispatchers.Main) {
                val itemAdapter = ItemAdapter(this@UfcFightCards, imageUrls, 2)
                val snapHelper: SnapHelper = LinearSnapHelper()

                ufcFightCards.apply {
                    adapter = itemAdapter
                    ufcFightCards.layoutManager = LinearLayoutManager(this@UfcFightCards,
                        LinearLayoutManager.VERTICAL, false)
                    ufcFightCards.adapter = adapter
                    snapHelper.attachToRecyclerView(ufcFightCards)

                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@UfcFightCards, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }


}