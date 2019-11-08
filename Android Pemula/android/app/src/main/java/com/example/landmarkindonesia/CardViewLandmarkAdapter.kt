package com.example.landmarkindonesia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class CardViewLandmarkAdapter(private val listLandmark: ArrayList<LandmarkModel>): RecyclerView.Adapter<CardViewLandmarkAdapter.CardViewViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_cardview_landmarks,parent,false)
        return CardViewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listLandmark.size
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        val landmark = listLandmark[position]
        holder.tvName.text = landmark.name
        holder.tvDetail.text = landmark.detail

        Glide.with(holder.itemView.context).load(landmark.photo).apply(RequestOptions().override(350,550)).into(holder.imgPhoto)

        holder.itemView.setOnClickListener{
            Toast.makeText(holder.itemView.context, landmark.name, Toast.LENGTH_LONG).show()
        }

    }

    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.iv_item_photo)
        var tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        var tvDetail: TextView = itemView.findViewById(R.id.tv_item_detail)
    }

}