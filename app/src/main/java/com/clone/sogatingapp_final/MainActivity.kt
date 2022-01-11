package com.clone.sogatingapp_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
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
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class MainActivity : AppCompatActivity() {

    lateinit var myInfo : UserDataModel
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
                    Toast.makeText(baseContext, "좋아요", Toast.LENGTH_SHORT).show()
                    val likeUserUid = userList[userCount].uid.toString()
                    userLikeOtherUser(myInfo.uid.toString(), likeUserUid)
                }

                if(direction == Direction.Left) {
                    Toast.makeText(baseContext, "싫어요", Toast.LENGTH_SHORT).show()
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
        settingBtn.setOnClickListener{
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

        val getMyDataListener = object : ValueEventListener{
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
    private fun userLikeOtherUser(myUid :String, otherUid : String){
        FirebaseRef.userLikeRef.child(myUid).child(otherUid).setValue("true")
    }
}