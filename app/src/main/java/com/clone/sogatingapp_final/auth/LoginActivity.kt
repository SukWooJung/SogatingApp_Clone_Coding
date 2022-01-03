package com.clone.sogatingapp_final.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.clone.sogatingapp_final.MainActivity
import com.clone.sogatingapp_final.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    companion object{
        const val TAG = "LoginActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val loginBtn = findViewById<Button>(R.id.loginCompleteBtn)
        val emailTv = findViewById<TextInputEditText>(R.id.emailArea)
        val passwordTv = findViewById<TextInputEditText>(R.id.passwordArea)

        loginBtn.setOnClickListener{

            auth.signInWithEmailAndPassword(emailTv.text.toString(), passwordTv.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "로그인에 실패하였습니다",
                            Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }
}