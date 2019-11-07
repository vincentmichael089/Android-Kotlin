package com.example.landmarkindonesia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class ListLandmarkAdapter(private val listLandmark : ArrayList<LandmarkModel>) : RecyclerView.Adapter<ListLandmarkAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_landmark, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listLandmark.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val landmark = listLandmark[position]
        holder.tvName.text = landmark.name
        holder.tvDetail.text = landmark.detail
        Glide.with(holder.itemView.context).load(landmark.photo).apply(RequestOptions().override(55,55)).into(holder.ivPhoto)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName : TextView = itemView.findViewById(R.id.tv_landmark_name)
        var tvDetail : TextView = itemView.findViewById(R.id.tv_landmark_detail)
        var ivPhoto : ImageView = itemView.findViewById(R.id.iv_landmark_photo)
    }

}