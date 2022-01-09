package com.clone.sogatingapp_final.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.clone.sogatingapp_final.R
import com.clone.sogatingapp_final.auth.UserDataModel
import com.clone.sogatingapp_final.utils.FirebaseAuthUtils
import com.clone.sogatingapp_final.utils.FirebaseRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.w3c.dom.Text

class MyPageActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MyPageActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        getMyInfo()

    }

    fun getMyInfo(){
        val uid = FirebaseAuthUtils.getUid()

        val getMyDataListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, snapshot.toString())
                val myInfo = snapshot.getValue<UserDataModel>()
                showMyInfo(myInfo)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "get User Data Fail")
            }
        }

        FirebaseRef.userInfoRef.child(uid).addValueEventListener(getMyDataListener)
    }

    fun showMyInfo(myInfo : UserDataModel?){
        val uidTxt: TextView = findViewById(R.id.uidTxt)
        val nameTxt: TextView = findViewById(R.id.nameTxt)
        val genderTxt: TextView = findViewById(R.id.genderTxt)
        val locationTxt: TextView = findViewById(R.id.locationTxt)
        val ageTxt: TextView = findViewById(R.id.ageTxt)

        uidTxt.text = myInfo?.uid
        nameTxt.text = myInfo?.nickname
        genderTxt.text = myInfo?.gender
        locationTxt.text = myInfo?.location
        ageTxt.text = myInfo?.age

        // 이미지 처리
        val myImage: ImageView = findViewById(R.id.myImage)
        val storage = Firebase.storage
        val storageRef = storage.reference.child(myInfo?.uid + ".png")

        storageRef.downloadUrl.addOnCompleteListener{
            if (it.isSuccessful) {
                Glide.with(applicationContext)
                    .load(it.result)
                    .into(myImage)
            }
        }
    }

}