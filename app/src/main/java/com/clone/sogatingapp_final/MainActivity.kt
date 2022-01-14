package com.clone.sogatingapp_final

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.clone.sogatingapp_final.auth.IntroActivity
import com.clone.sogatingapp_final.auth.UserDataModel
import com.clone.sogatingapp_final.setting.MyPageActivity
import com.clone.sogatingapp_final.setting.SettingActivity
import com.clone.sogatingapp_final.slider.CardStackAdapter
import com.clone.sogatingapp_final.utils.FirebaseAuthUtils
import com.clone.sogatingapp_final.utils.FirebaseRef
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class MainActivity : AppCompatActivity() {

    lateinit var myInfo: UserDataModel
    lateinit var cardStackAdapter: CardStackAdapter
    lateinit var manager: CardStackLayoutManager
    val TAG = "MainActivity"
    val userList = mutableListOf<UserDataModel>()

    private var userCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 나의 정보를 받아오기
        getMyInfo()

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

            // 넘겼을 때 방향 설정
            override fun onCardSwiped(direction: Direction?) {
                if (direction == Direction.Right) {
                    // 좋아요한 유저 uid 가져오기
                    val likeUserUid = userList[userCount].uid.toString()
                    // 좋아요한 유저 정보 DB에 반영
                    userLikeOtherUser(myInfo.uid.toString(), likeUserUid)
                    // 좋아요한 유저 매칭
                    checkOtherUserLikeMe(myInfo.uid.toString(), likeUserUid)
                    // 매칭 메세지 보내기
                    createNotificationChannel()
                    sendMatchingSuccessNotification()
                }

                if (direction == Direction.Left) {

                }

                userCount += 1
                // 모든 유저의 정보를 확인했으면, 다시 모든 회원정보 받아오기
                if (userCount == userList.size) {
                    userCount = 0;
                    userList.clear()
                    getDifferentGenderUserDataList()
                    Toast.makeText(baseContext, "유저를 새롭게 받아 옵니다", Toast.LENGTH_SHORT).show()
                }
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

        getDifferentGenderUserDataList()

        cardStackAdapter = CardStackAdapter(baseContext, userList)
        cardStackView.layoutManager = manager
        cardStackView.adapter = cardStackAdapter
    }

    private fun goToSettingPage() {
        val settingBtn: ImageView = findViewById(R.id.settingBtn)
        settingBtn.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getDifferentGenderUserDataList() {
        val userListener = object : ValueEventListener {
            // 경로의 전체 내용을 읽고 변경사항을 수신 대기합니다.
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 우리가 가져온 dataSnapshot 은 JsonArray 형태
                for (dataModel in dataSnapshot.children) {
                    // val user = dataModel.getValue(UserDataModel::class.java)
                    val user = dataModel.getValue<UserDataModel>()
                    Log.d(TAG, user.toString())
                    if (user?.gender != myInfo.gender) {
                        userList.add(user!!)
                    }
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

    private fun getMyInfo() {
        val uid = FirebaseAuthUtils.getUid()

        val getMyDataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, snapshot.toString())
                myInfo = snapshot.getValue<UserDataModel>()!!
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "get User Data Fail")
            }
        }

        FirebaseRef.userInfoRef.child(uid).addValueEventListener(getMyDataListener)
    }

    // 자신의 UID와 좋아요한 다른 사람의 UID 값이 저장되어야 하겠지
    private fun userLikeOtherUser(myUid: String, otherUid: String) {
        FirebaseRef.userLikeRef.child(myUid).child(otherUid).setValue("true")
    }

    // 내가 좋아하는 사람의 좋아요 정보를 가져오기
    private fun checkOtherUserLikeMe(myUid: String, otherUid: String) {
        val getOtherLikeDataListener = object : ValueEventListener {
            // 있다면 true 데이터를 들고 왔을 것
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val likeUid = data.key.toString()
                    if (likeUid == myUid) {
                        Toast.makeText(this@MainActivity, "매칭성공", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }
        FirebaseRef.userLikeRef.child(otherUid).addValueEventListener(getOtherLikeDataListener)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "소개팅앱"
            val descriptionText = "매칭이 선사되었습니다 !!"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("TEST_CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendMatchingSuccessNotification(){
        var builder = NotificationCompat.Builder(this, "TEST_CHANNEL_ID")
            .setSmallIcon(R.drawable.profile_img)
            .setContentTitle("매칭 완료")
            .setContentText("매칭이 완료되었습니다 ~!!")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("매칭이 완료되었습니다 ~!!\n자세한 사항은 탭을 눌러 확인하세요 !"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(123, builder.build())
        }

    }
}