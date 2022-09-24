package com.example.paybout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.paybout.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()

        setContentView(binding.root)

        binding.textView.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.username.text.toString()
//            val username = binding.username.text.toString()
            val pass = binding.passET.text.toString()
            database = FirebaseDatabase.getInstance()
//            val databaseRef = database.getReference("users")

            if (email.isNotEmpty() && pass.isNotEmpty()) {

//                val checkUser: Query = databaseRef.orderByChild("username").equalTo(username)
//
//                checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
//
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        if (snapshot.exists()) {
//                            val dbPassword = snapshot.child(username).child("password")
//                                .getValue(String.javaClass)
//
//                            if (dbPassword != null) {
//                                if(dbPassword.equals(pass)) {
//                                    val dbUsername = snapshot.child(username).child("username").getValue(String.javaClass)
//                                    val i = Intent(applicationContext, MainActivity::class.java)
//                                    i.putExtra("username", username)
//                                    startActivity(i)
//                                    finish()
//                                } else {
//                                    binding.passET.error = "Wrong Password!"
//                                }
//                            }
//                        }
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//
//                    }
//                })
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                    }
                }

            } else {
                Toast.makeText(applicationContext, "Fill all the details", Toast.LENGTH_SHORT).show()

            }
        }

    }

    override fun onStart() {
        super.onStart()

        if(auth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}