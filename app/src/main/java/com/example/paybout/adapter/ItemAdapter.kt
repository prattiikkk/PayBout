package com.example.paybout.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.paybout.ConfirmBet
import com.example.paybout.R
import com.example.paybout.data.Datasource
import com.example.paybout.model.Card
import kotlinx.android.synthetic.main.item_image.view.*

class ItemAdapter(
    val context: Context,
    val urls: List<String>,
    val type: Int
): RecyclerView.Adapter<ItemAdapter.ImageViewHolder>() {


    inner class ImageViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.trending_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        var adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.min_item_image, parent, false)

        if(type == 1) {
            adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
            return ImageViewHolder(adapterLayout)
        } else if(type == 2) {
            adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.item_image_margin, parent, false)
        }
        return ImageViewHolder(adapterLayout)
    }

    override fun getItemCount() : Int {
        return urls.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        var item = urls[position]
        Glide.with(holder.imageView).load(item).into(holder.imageView.trending_image)

        holder.imageView.setOnClickListener {
            val intent = Intent(context, ConfirmBet::class.java)
            intent.putExtra("url", item)
            intent.putExtra("pos", position)
            context.startActivity(intent)
        }

    }
}