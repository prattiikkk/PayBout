package com.example.paybout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.paybout.adapter.ItemAdapter
import com.example.paybout.data.Datasource
import com.example.paybout.model.Card
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var storage: FirebaseStorage
    val imageRef = Firebase.storage.reference
    lateinit var ufcViewAllBtn: TextView
    lateinit var oneViewAllBtn: TextView
    lateinit var bellatorViewAllBtn: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.trending_card_rc_view)
        recyclerView.setHasFixedSize(true)

        ufcViewAllBtn = view.findViewById(R.id.ufc_view_all)
        oneViewAllBtn = view.findViewById(R.id.one_view_all)
        bellatorViewAllBtn = view.findViewById(R.id.bellator_view_all)

        ufcViewAllBtn.setOnClickListener {
            val intent = Intent(activity, UfcFightCards::class.java)
            startActivity(intent)
        }

        oneViewAllBtn.setOnClickListener {
            val intent = Intent(activity, OneFightCards::class.java)
            startActivity(intent)
        }

        bellatorViewAllBtn.setOnClickListener {
            val intent = Intent(activity, BellatorFightCards::class.java)
            startActivity(intent)
        }

        storage = Firebase.storage

        val trendingCardRC = view.findViewById<RecyclerView>(R.id.trending_card_rc_view)
        val ufcMinCardRC = view.findViewById<RecyclerView>(R.id.ufc_min_card_rc)
        val oneMinCardRC = view.findViewById<RecyclerView>(R.id.one_min_card_rc)
        val bellatorMinCardRC = view.findViewById<RecyclerView>(R.id.bellator_min_card_rc)

        //RecyclerView For Trending fight section
        listTrendingFightCards(trendingCardRC)

        // RecyclerView For UFC min Fight section
        listUfcMinCards(ufcMinCardRC)

        // RecyclerView For One min Fight section
        listOneMinCards(oneMinCardRC)

        // RecyclerView For Bellator min Fight section
        listBellatorMinCards(bellatorMinCardRC)

        // Inflate the layout for this fragment
        return view
    }


//    private fun testTrendingFightCards(trendingCardRC: RecyclerView) = {
//        val myDataset = Datasource("/trending_card")
//    }

    private fun listTrendingFightCards(trendingCardRC: RecyclerView) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val images = imageRef.child("card_images/trending_card/").listAll().await()
                var imageUrls = mutableListOf<String>()
                for (image in images.items) {
                    val url = image.downloadUrl.await()
                    imageUrls.add(url.toString())
                }
                withContext(Dispatchers.Main) {

                    val itemAdapter = activity?.let { ItemAdapter(it, imageUrls, 1) }
                    trendingCardRC.apply {
                        adapter = itemAdapter
                        val snapHelper: SnapHelper = LinearSnapHelper()
                        trendingCardRC.layoutManager =
                            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                            trendingCardRC.adapter = itemAdapter
                            snapHelper.attachToRecyclerView(trendingCardRC)

                    }

                    trendingCardRC.setHasFixedSize(false)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }

    private fun listUfcMinCards(ufcMinCardRC: RecyclerView) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val images = imageRef.child("card_images/ufc_card/").listAll().await()
                val imageUrls = mutableListOf<String>()
                var i = 0
                while (i < 3) {
                    val url = images.items[i].downloadUrl.await()
                    imageUrls.add(url.toString())
                    i++
                }
                withContext(Dispatchers.Main) {
                    val itemAdapter = activity?.let { ItemAdapter(it, imageUrls, 0) }
                    val snapHelper: SnapHelper = LinearSnapHelper()
                    ufcMinCardRC.apply {
                        adapter = itemAdapter
                        ufcMinCardRC.layoutManager =
                            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                        ufcMinCardRC.adapter = adapter
                        snapHelper.attachToRecyclerView(ufcMinCardRC)

                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }

    private fun listOneMinCards(oneMinCardRC: RecyclerView) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val images = imageRef.child("card_images/one_card/").listAll().await()
                val imageUrls = mutableListOf<String>()
                var i = 0
                while (i < 3) {
                    val url = images.items[i].downloadUrl.await()
                    imageUrls.add(url.toString())
                    i++
                }
                withContext(Dispatchers.Main) {
                    val itemAdapter = activity?.let { ItemAdapter(it, imageUrls, 0) }
                    val snapHelper: SnapHelper = LinearSnapHelper()

                    oneMinCardRC.apply {
                        adapter = itemAdapter
                        oneMinCardRC.layoutManager =
                            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                        oneMinCardRC.adapter = adapter
                        snapHelper.attachToRecyclerView(oneMinCardRC)
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }

    private fun listBellatorMinCards(bellatorMinCardRC: RecyclerView) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val images = imageRef.child("card_images/bellator_card/").listAll().await()
                val imageUrls = mutableListOf<String>()
                var i = 0
                while (i < 3) {
                    val url = images.items[i].downloadUrl.await()
                    imageUrls.add(url.toString())
                    i++
                }
                withContext(Dispatchers.Main) {
                    val itemAdapter = activity?.let { ItemAdapter(it, imageUrls, 0) }
                    val snapHelper: SnapHelper = LinearSnapHelper()

                    bellatorMinCardRC.apply {
                        adapter = itemAdapter
                        bellatorMinCardRC.layoutManager =
                            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                        bellatorMinCardRC.adapter = adapter
                        snapHelper.attachToRecyclerView(bellatorMinCardRC)

                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}