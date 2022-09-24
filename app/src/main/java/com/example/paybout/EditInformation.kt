package com.example.paybout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.paybout.databinding.ActivityEditInformationBinding
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_edit_information.*

class EditInformation : AppCompatActivity() {

    private lateinit var binding: ActivityEditInformationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        val username = binding.changeUsername
        val email = binding.changeEmail
        val newPassword = binding.changePassword
        val newConfirmPassword = binding.confirmPassword

        user?.let {
                database.reference.child("users").child(user.uid).child("username").get().addOnSuccessListener {
                username.setText(it.value.toString())
            }.addOnFailureListener{
                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
            }
            email.setText(user.email.toString().trim())
        }

        binding.saveChangesBtn.setOnClickListener {


            var tempEmail = binding.changeEmail.text

            if(email.text.toString().isNotEmpty()) {
                Log.d("test", "comin")
                user!!.updateEmail(email.text.toString().trim()).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Information Updated Successfully", Toast.LENGTH_LONG)
                            .show()
                    }
                }.addOnFailureListener{
                    Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
                }
            }

            if(newPassword.text.toString().trim() == newConfirmPassword.text.toString().trim()) {
                user!!.updatePassword(newPassword.text.toString()).addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        changeDatabasePassword(user.uid, newPassword.text.toString())
                        Toast.makeText(this, "Information Updated Successfully for pass ", Toast.LENGTH_LONG)
                            .show()
                    }
                }.addOnFailureListener{
                    Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Andha he kya thik se type ker", Toast.LENGTH_LONG).show()
            }

            changeDatabaseInfo(user!!.uid, username.text.toString(), email.text.toString())

        }

        binding.deleteAccountBtn.setOnClickListener {
            user!!.delete().addOnCompleteListener{ task ->
                if(task.isSuccessful) {
                    database.reference.child("users").child(user!!.uid).removeValue()
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }
            }.addOnFailureListener{
                Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
            }
        }

    }

    fun changeDatabaseInfo(userID: String, username: String, email: String) {
        database.reference.child("users").child(userID).child("username").setValue(username)
        database.reference.child("users").child(userID).child("email").setValue(email)
    }

    fun changeDatabasePassword(userID:String, password: String) {
        database.reference.child("users").child(userID).child("password").setValue(password)

    }

}