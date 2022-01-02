package com.clone.sogatingapp_final.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseAuthUtils {

    companion object {
        private lateinit var auth: FirebaseAuth

        fun getUid(): String {
            val auth = FirebaseAuth.getInstance()
            return auth.currentUser?.uid.toString()
        }

    }
}