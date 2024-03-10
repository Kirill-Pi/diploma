package com.example.diploma.view.rv_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.diploma.data.SpacecraftConfig
import com.example.diploma.view.rv_viewholders.SpaceShipsViewHolder
import com.example.pigolevmyapplication.R

class SpaceShipsListRecyclerAdapter(private val clickListener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items = mutableListOf<SpacecraftConfig>()
    override fun getItemCount() = items.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SpaceShipsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.spacecraft_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SpaceShipsViewHolder -> {
                holder.bind(items[position])
                val itemContainer = holder.itemView.findViewById<ViewGroup>(R.id.item_container)
                itemContainer.setOnClickListener {
                    clickListener.click(items[position])
                }
            }
        }
    }



    //Метод для добавления объектов в наш список
    fun addItems(list: MutableList<SpacecraftConfig>) {
        //Сначала очишаем(если не реализовать DiffUtils)
        items.clear()
        //Добавляем
        items.addAll(list)
        //Уведомляем RV, что пришел новый список и ему нужно заново все "привязывать"
        notifyDataSetChanged()
    }

    fun addPage(list: MutableList<SpacecraftConfig>) {
        //Добавляем
        items.addAll(list)
        //Уведомляем RV, что пришел новый список и ему нужно заново все "привязывать"
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun click(spaceCraft: SpacecraftConfig)
    }
}