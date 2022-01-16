package com.clone.sogatingapp_final

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.clone.sogatingapp_final.auth.IntroActivity
import com.clone.sogatingapp_final.utils.FirebaseAuthUtils

class SplashActivity : AppCompatActivity() {

    companion object {
        val TAG = "SplashActivity"
        private const val DURATION: Long = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val uid = FirebaseAuthUtils.getUid()

        if (uid != "null") { // 이미 로그인한 UID 기록이 있으면 MainActivity 로
            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }, DURATION)
        } else { // 로그인한 UID 기록이 없으면 IntroActivity 로
            Handler().postDelayed({
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }, DURATION)
        }

    }
}