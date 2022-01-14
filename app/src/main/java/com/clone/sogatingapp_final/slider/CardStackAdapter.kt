package com.clone.sogatingapp_final.slider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.clone.sogatingapp_final.R
import com.clone.sogatingapp_final.auth.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class CardStackAdapter(val context : Context, val items : List<User>) :
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
        private val image: ImageView = itemView.findViewById(R.id.profileImageArea)

        fun binding(data: User) {
            name.text = data.nickname
            age.text = data.age
            location.text = data.location

            // 이미지
            val storageRef = Firebase.storage.reference.child(data.uid + ".png")
            storageRef.downloadUrl.addOnCompleteListener{ // OnCompleteListener 람다
                if (it.isSuccessful) {
                    Glide.with(context)
                        .load(it.result)
                        .into(image)
                }
            }
        }
    }

}