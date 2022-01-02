package com.clone.sogatingapp_final.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.clone.sogatingapp_final.R

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        // 뷰 컴포넌트 가져오기
        val joinBtn = findViewById<Button>(R.id.joinBtn);

        // 액티비티 이동
        val intent = Intent(this, JoinActivity::class.java)
        joinBtn.setOnClickListener {
            startActivity(intent)
        }
    }
}