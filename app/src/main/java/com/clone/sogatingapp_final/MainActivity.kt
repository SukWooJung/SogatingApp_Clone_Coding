package com.clone.sogatingapp_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.clone.sogatingapp_final.auth.IntroActivity
import com.clone.sogatingapp_final.slider.CardStackAdapter
import com.clone.sogatingapp_final.utils.FirebaseAuthUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class MainActivity : AppCompatActivity() {

    lateinit var cardStackAdapter : CardStackAdapter
    lateinit var manager : CardStackLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 로그아웃 버튼
        val logoutBtn = findViewById<Button>(R.id.logoutBtn)
        logoutBtn.setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 카드 스택뷰
        val cardStackView = findViewById<CardStackView>(R.id.cardSTackView)

        manager = CardStackLayoutManager(baseContext, object : CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {
            }

            override fun onCardSwiped(direction: Direction?) {
            }

            override fun onCardRewound() {
            }

            override fun onCardCanceled() {
            }

            override fun onCardAppeared(view: View?, position: Int) {
            }

            override fun onCardDisappeared(view: View?, position: Int) {
            }
        })
        val testList = mutableListOf<String>()
        testList.add("a")
        testList.add("b")
        testList.add("c")
        cardStackAdapter = CardStackAdapter(baseContext, testList)
        cardStackView.layoutManager = manager
        cardStackView.adapter = cardStackAdapter

    }
}