package com.clone.sogatingapp_final.message

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

class MyLikeAdapter(val context : Context, val likes : MutableList<User>) : RecyclerView.Adapter<MyLikeAdapter.ViewHolder>(){

    lateinit var itemClickListener: ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.like_person_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setItem(likes[position])
    }

    override fun getItemCount(): Int {
        return likes.size
    }

    fun setClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onClick(view : View, position: Int)
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener{
                val position = adapterPosition
                itemClickListener?.onClick(it,position)
            }
        }
        private  val likeImage = itemView.findViewById<ImageView>(R.id.likeImage)
        private  val likeName = itemView.findViewById<TextView>(R.id.likeName)
        private  val likeAge = itemView.findViewById<TextView>(R.id.likeAge)
        private  val likeCity = itemView.findViewById<TextView>(R.id.likeCity)



        fun setItem(liked: User) {
            likeName.text = liked.nickname
            likeAge.text = liked.age
            likeCity.text = liked.location

            // 이미지파일
            val storageRef = Firebase.storage.reference.child(liked.uid + ".png")
            storageRef.downloadUrl.addOnCompleteListener{
                if (it.isSuccessful) {
                    Glide.with(context)
                        .load(it.result)
                        .into(likeImage)
                }
            }
        }
    }
}