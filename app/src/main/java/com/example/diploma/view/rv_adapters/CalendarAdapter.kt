package com.example.diploma.view.rv_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.diploma.data.CalendarDateModel
import com.example.pigolevmyapplication.R

import com.google.android.material.card.MaterialCardView

class CalendarAdapter(private val listener: (calendarDateModel: CalendarDateModel, position: Int) -> Unit) :
    RecyclerView.Adapter<CalendarAdapter.MyViewHolder>() {
    private val list = ArrayList<CalendarDateModel>()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(calendarDateModel: CalendarDateModel) {
            val calendarDay = itemView.findViewById<TextView>(R.id.tv_calendar_day)
            val calendarDate = itemView.findViewById<TextView>(R.id.tv_calendar_date)
            val cardView = itemView.findViewById<MaterialCardView>(R.id.card_calendar)

            //println(calendarDateModel.calendarDate)

            if (calendarDateModel.isSelected) {
                calendarDay.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.black
                    )
                )
                calendarDate.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.black
                    )
                )
                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.cyan
                    )
                )
                cardView.strokeColor = ContextCompat.getColor(
                    itemView.context,
                    R.color.cyan
                )

            } else {
                calendarDay.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.light_white
                    )
                )
                calendarDate.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.light_white
                    )
                )
                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.light_blue
                    )
                )
                if (calendarDateModel.isCurrent) {
                    cardView.strokeColor = ContextCompat.getColor(
                        itemView.context,
                        R.color.cyan
                    )
                } else {
                    cardView.strokeColor = ContextCompat.getColor(
                        itemView.context,
                        R.color.light_blue
                    )
                }
            }


            calendarDay.text = calendarDateModel.calendarDay
            calendarDate.text = calendarDateModel.calendarDate
            cardView.setOnClickListener {
                listener.invoke(calendarDateModel, adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_calendar_date, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(calendarList: ArrayList<CalendarDateModel>) {
        list.clear()
        list.addAll(calendarList)
        notifyDataSetChanged()
    }

}