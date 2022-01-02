package com.clone.sogatingapp_final.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.clone.sogatingapp_final.MainActivity
import com.clone.sogatingapp_final.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private companion object{
        const val TAG = "JoinActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val emailText = findViewById<TextInputEditText>(R.id.emailArea)
        val passwordText = findViewById<TextInputEditText>(R.id.passwordArea)
        val nickname = findViewById<TextInputEditText>(R.id.nicknameArea)
        val genderText = findViewById<TextInputEditText>(R.id.genderArea)
        val locationText = findViewById<TextInputEditText>(R.id.locationArea)
        val ageText = findViewById<TextInputEditText>(R.id.ageArea)

        val registerBtn = findViewById<Button>(R.id.registerBtn)

        registerBtn.setOnClickListener {
            auth.createUserWithEmailAndPassword(
                emailText.text.toString(),
                passwordText.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "이미 존재하는 이메일입니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

    }


}