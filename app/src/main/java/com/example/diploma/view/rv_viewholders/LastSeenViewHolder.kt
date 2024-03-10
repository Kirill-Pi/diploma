package com.example.diploma.view.rv_viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.diploma.data.RecentlySeen
import com.example.pigolevmyapplication.R

class LastSeenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title = itemView.findViewById<TextView>(R.id.title)
    private val poster = itemView.findViewById<ImageView>(R.id.poster)


    fun bind(spacecraft: RecentlySeen) {
        title.text = spacecraft.name
        Glide.with(itemView)
            .load(spacecraft.imageUrl)
            .placeholder(R.drawable.launch_placeholder)
            .error(R.drawable.launch_placeholder)
            .centerCrop()
            .into(poster)
    }
}