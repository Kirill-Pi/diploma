package com.example.diploma.view.rv_viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.diploma.data.Events
import com.example.diploma.data.Launch
import com.example.pigolevmyapplication.R

class EventsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title = itemView.findViewById<TextView>(R.id.title)
    private val poster = itemView.findViewById<ImageView>(R.id.poster)
    private val date = itemView.findViewById<TextView>(R.id.date)


    fun bind(events: Events) {
        title.text = events.name
        Glide.with(itemView)
            .load(events.image)
            .placeholder(R.drawable.launch_placeholder)
            .error(R.drawable.launch_placeholder)
            .centerCrop()
            .into(poster)
        date.text = events.date
    }
}