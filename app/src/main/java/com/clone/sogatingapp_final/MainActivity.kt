package com.clone.sogatingapp_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.clone.sogatingapp_final.auth.IntroActivity
import com.clone.sogatingapp_final.auth.UserDataModel
import com.clone.sogatingapp_final.setting.SettingActivity
import com.clone.sogatingapp_final.slider.CardStackAdapter
import com.clone.sogatingapp_final.utils.FirebaseRef
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class MainActivity : AppCompatActivity() {

    lateinit var cardStackAdapter: CardStackAdapter
    lateinit var manager: CardStackLayoutManager
    val TAG = "MainActivity"
    val userList = mutableListOf<UserDataModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // SettingPage 가기
        goToSettingPage()

        // 카드 스택뷰
        setCardStackView()
    }

    private fun setCardStackView() {
        val cardStackView = findViewById<CardStackView>(R.id.cardSTackView)

        manager = CardStackLayoutManager(baseContext, object : CardStackListener {
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

        getUserDataList()

        cardStackAdapter = CardStackAdapter(baseContext, userList)
        cardStackView.layoutManager = manager
        cardStackView.adapter = cardStackAdapter
    }

    private fun goToSettingPage() {
        val settingBtn: ImageView = findViewById(R.id.settingBtn)
        settingBtn.setOnClickListener{
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getUserDataList() {
        val userListener = object : ValueEventListener {
            // 경로의 전체 내용을 읽고 변경사항을 수신 대기합니다.
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 우리가 가져온 dataSnapshot 은 JsonArray 형태
                for (dataModel in dataSnapshot.children) {
                    val user = dataModel.getValue<UserDataModel>()
                    // val user = dataModel.getValue(UserDataModel::class.java)
                    userList.add(user!!)
                    Log.d(TAG, user.toString())
                    cardStackAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userInfoRef.addValueEventListener(userListener)

    }
}