package com.example.paybout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.paybout.databinding.ActivityConfirmBetBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ConfirmBet : AppCompatActivity() {

    lateinit var database: FirebaseDatabase
    lateinit var binding: ActivityConfirmBetBinding
    var checkCard = mutableListOf<String>()


    val user = Firebase.auth.currentUser!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmBetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadCardInfo()

        val betCard = findViewById<ImageView>(R.id.bet_card)

        val imgUrl: String = intent.getStringExtra("url").toString()
        Glide.with(this).load(imgUrl).into(betCard)

        val num: Int = intent.getIntExtra("pos", 89)
        var childNode: String = ""

        if(imgUrl.contains("trending_card")) {
            childNode = "trendingCardDetails"
            cardInfo(num, childNode)
        } else if(imgUrl.contains("ufc_card")) {
            childNode = "ufcCardDetails"
            cardInfo(num, childNode)
        } else if(imgUrl.contains("one_card")) {
            childNode = "oneCardDetails"
            cardInfo(num, childNode)
        } else {
            childNode = "bellatorCardDetails"
            cardInfo(num, childNode)
        }

        binding.confirmBet.setOnClickListener {

            if(checkCard.contains(imgUrl)) {
                Toast.makeText(this, "You already placed your bet on this fight!", Toast.LENGTH_LONG).show()
            } else {
                var betAmount = binding.betAmount.text.toString()
                if(betAmount.isNotEmpty()) {
                    Toast.makeText(this, "Your bet has been placed", Toast.LENGTH_LONG).show()
                    database.reference.child("users").child(user.uid).child("bets").push()
                        .setValue(imgUrl)
                } else {
                    Toast.makeText(this, "Please enter your betting amount", Toast.LENGTH_LONG).show()

                }
            }

        }

    }

    fun loadCardInfo() {
        database = FirebaseDatabase.getInstance()
        val myTopPostsQuery = database.reference.child("users").child(user.uid).child("bets")

        myTopPostsQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    checkCard.add(postSnapshot.value.toString())
                }
                Log.d("check", checkCard.toString())

            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("topQuery", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    fun cardInfo(num: Int, childNode: String) {

        val cardReference = database.reference.child("fightersDetails").child(childNode).child(num.toString())

        cardReference.child("blueAge").get().addOnSuccessListener {
            binding.blueAge.setText(it.value.toString())
        }

        cardReference.child("redAge").get().addOnSuccessListener {
            binding.redAge.setText(it.value.toString())
        }

        cardReference.child("blueHeight").get().addOnSuccessListener {
            binding.blueHeight.setText(it.value.toString())
        }

        cardReference.child("redHeight").get().addOnSuccessListener {
            binding.redHeight.setText(it.value.toString())
        }

        cardReference.child("blueRecord").get().addOnSuccessListener {
            binding.blueRecord.setText(it.value.toString())
        }

        cardReference.child("redRecord").get().addOnSuccessListener {
            binding.redRecord.setText(it.value.toString())
        }

        cardReference.child("blueStyle").get().addOnSuccessListener {
            binding.blueStyle.setText(it.value.toString())
        }

        cardReference.child("redStyle").get().addOnSuccessListener {
            binding.redStyle.setText(it.value.toString())
        }

        cardReference.child("blueWeight").get().addOnSuccessListener {
            binding.blueWeight.setText(it.value.toString())
        }

        cardReference.child("redWeight").get().addOnSuccessListener {
            binding.redWeight.setText(it.value.toString())
        }

    }

}


