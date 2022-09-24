package com.example.paybout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.paybout.databinding.ActivitySignUpBinding
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var binding : ActivitySignUpBinding
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()


        binding.textView.setOnClickListener{
            val intent = Intent (this, Login::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSignUp.setOnClickListener{
            val username = binding.username.text.toString()
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty() && username.isNotEmpty()) {

                if(pass == confirmPass) {

                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener{
                        if(it.isSuccessful) {

                            val user = auth.currentUser

                            val databaseReference = database.getReference("users")

                            val userHelperClass = UserHelperClass(username, email, pass)
                            if (user != null) {
                                databaseReference.child(user.uid).setValue(userHelperClass)
                            }
                            val intent = Intent(this, Login::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fill all the details", Toast.LENGTH_SHORT).show()

            }
        }
    }
}