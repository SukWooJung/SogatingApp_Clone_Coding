package com.clone.sogatingapp_final.auth

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
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
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

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
        val uploadingImageView = findViewById<ImageView>(R.id.getImageBtn)

        // 사진 가져오기
        uploadingImageView.bringToFront()
        val registerForActivityResult = getRegisterForActivityResult(uploadingImageView)
        // 사진 가져오기 버튼 클릭
        uploadingImageView.setOnClickListener {
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
                        val userInfo = User(uid, nickname, gender, location, age)
                        writeNewUser(uid, userInfo)

                        // 이미지 업로드 (storage 에 이미지 저장)
                        uploadImage(uploadingImageView, uid)

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

    private fun getRegisterForActivityResult(getImageBtn: ImageView): ActivityResultLauncher<String> {
        val getAction = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                getImageBtn.setImageURI(it)
            }
        )
        return getAction
    }


    private fun writeNewUser(uid: String, userInfo: User) {
        FirebaseRef.userInfoRef.child(uid).setValue(userInfo)
    }

    private fun uploadImage(imageView: ImageView, uid : String) {
        // storage
        val storage = Firebase.storage
        val storageRef = storage.reference.child(uid + ".png")
        Log.d(TAG, "중간지점")

        // Get the data from an ImageView as bytes
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Log.d(TAG, "Upload is unsuccessful")
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            Log.d(TAG, "Upload is successful")
        }
    }
}