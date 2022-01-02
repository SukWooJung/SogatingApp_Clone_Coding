package com.clone.sogatingapp_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.clone.sogatingapp_final.auth.IntroActivity
import com.clone.sogatingapp_final.utils.FirebaseAuthUtils
import com.google.firebase.auth.FirebaseAuth
import javax.xml.datatype.DatatypeConstants.DURATION

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val DURATION: Long = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val uid = FirebaseAuthUtils.getUid()

        if (uid != "null") { // 이미 로그인한 UID 기록이 있으면
            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }, DURATION)
        } else {
            Handler().postDelayed({
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }, DURATION)
        }

    }

}