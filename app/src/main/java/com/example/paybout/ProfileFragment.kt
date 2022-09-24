package com.example.paybout

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlin.math.log

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    lateinit var logoutBtn : TextView
    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var username: TextView
    lateinit var email: TextView
    lateinit var editInformation: TextView
    lateinit var helpSupport: TextView
    lateinit var feedback: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()
        logoutBtn = view.findViewById(R.id.btn_logout)
        database = FirebaseDatabase.getInstance()
        editInformation = view.findViewById(R.id.edit_information)
        username = view.findViewById(R.id.username)
        email = view.findViewById(R.id.email)
        helpSupport = view.findViewById(R.id.help_support)
        feedback = view.findViewById(R.id.feedback)

        val user = auth.currentUser

        editInformation.setOnClickListener {
            val intent = Intent(activity, EditInformation::class.java)
            startActivity(intent)
        }

        helpSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto: suryawanshipratik712@gmail.com")
            intent.putExtra(Intent.EXTRA_EMAIL, "example@mail.com")
            startActivity(intent)
        }

        feedback.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto: suryawanshipratik712@gmail.com")
            intent.putExtra(Intent.EXTRA_EMAIL, "example@mail.com")
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
            startActivity(intent)
        }

        logoutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(context, Login::class.java))
        }

        user?.let {
            database.reference.child("users").child(user.uid).child("username").get().addOnSuccessListener {
                username.text = it.value.toString()
            }.addOnFailureListener{
                Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show()
            }
            email.text = user.email.toString()
        }

        return view
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}