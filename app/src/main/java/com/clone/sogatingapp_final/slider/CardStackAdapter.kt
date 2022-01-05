package com.clone.sogatingapp_final.slider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.clone.sogatingapp_final.R
import com.clone.sogatingapp_final.auth.UserDataModel

class CardStackAdapter(val context : Context, val items : List<UserDataModel>) :
    RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(items[position])
    }

    override fun getItemCount(): Int {
        return items.size;
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById<TextView>(R.id.nameTxt)
        private val age: TextView = itemView.findViewById<TextView>(R.id.ageTxt)
        private val location: TextView = itemView.findViewById<TextView>(R.id.locationTxt)


        fun binding(data: UserDataModel) {
            name.text = data.nickname
            age.text = data.age
            location.text = data.location

        }
    }

}