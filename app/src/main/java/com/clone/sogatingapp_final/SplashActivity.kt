package com.clone.sogatingapp_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.clone.sogatingapp_final.auth.IntroActivity
import javax.xml.datatype.DatatypeConstants.DURATION

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val DURATION : Long = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val intent = Intent(this, IntroActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        },DURATION)

    }
    override fun onBackPressed() {
        super.onBackPressed()
    }

}