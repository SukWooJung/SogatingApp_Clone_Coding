package com.clone.sogatingapp_final.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.clone.sogatingapp_final.R
import com.clone.sogatingapp_final.auth.IntroActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        // 로그아웃
        logout()

        // 마이페이지로
        goToMyPage()
    }

    private fun goToMyPage() {
        val myPageBtn = findViewById<Button>(R.id.myPageBtn)
        myPageBtn.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun logout() {
        val logoutBtn = findViewById<Button>(R.id.logoutBtn)
        logoutBtn.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}