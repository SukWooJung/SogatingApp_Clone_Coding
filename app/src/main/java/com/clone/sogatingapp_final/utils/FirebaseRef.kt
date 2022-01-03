package com.clone.sogatingapp_final.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRef {

    companion object{
        private val database = Firebase.database
        val myRef = database.getReference("userInfo")
    }
}