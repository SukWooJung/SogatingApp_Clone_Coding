package com.clone.sogatingapp_final.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.clone.sogatingapp_final.MainActivity
import com.clone.sogatingapp_final.R
import com.clone.sogatingapp_final.utils.FirebaseRef
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private companion object {
        const val TAG = "JoinActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val emailText = findViewById<TextInputEditText>(R.id.emailArea)
        val passwordText = findViewById<TextInputEditText>(R.id.passwordArea)
        val nicknameText = findViewById<TextInputEditText>(R.id.nicknameArea)
        val genderText = findViewById<TextInputEditText>(R.id.genderArea)
        val locationText = findViewById<TextInputEditText>(R.id.locationArea)
        val ageText = findViewById<TextInputEditText>(R.id.ageArea)
        val getImageBtn = findViewById<ImageView>(R.id.getImageBtn)

        // 사진 가져오기
        getImageBtn.bringToFront()
        val registerForActivityResult = getRegisterForActivityResult(getImageBtn)
        // 사진 가져오기 버튼 클릭
        getImageBtn.setOnClickListener{
            Log.d(TAG, "버튼 클릭됨")
            registerForActivityResult.launch("image/*")
        }

        // 회원가입 버튼 클릭
        val registerBtn = findViewById<Button>(R.id.registerBtn)
        registerBtn.setOnClickListener {
            val nickname = nicknameText.text.toString()
            val gender = genderText.text.toString()
            val location = locationText.text.toString()
            val age = ageText.text.toString()

            auth.createUserWithEmailAndPassword(
                emailText.text.toString(),
                passwordText.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val uid = auth.currentUser?.uid.toString()

                        // 회원가입 성공시(DB에 정보 저장)
                        val userInfo = UserDataModel(uid, nickname, gender, location, age)
                        writeNewUser(uid, userInfo)
                        // StartActivity 로 이동
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

    private fun getRegisterForActivityResult(getImageBtn : ImageView) : ActivityResultLauncher<String>{
        val getAction = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                getImageBtn.setImageURI(it)
            }
        )
        return getAction
    }


    private fun writeNewUser(uid : String, userInfo : UserDataModel) {
        FirebaseRef.userInfoRef.child(uid).setValue(userInfo)
    }

}