package com.clone.sogatingapp_final.message

import android.content.res.Resources
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clone.sogatingapp_final.R
import com.clone.sogatingapp_final.auth.User
import com.clone.sogatingapp_final.utils.FirebaseAuthUtils
import com.clone.sogatingapp_final.utils.FirebaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

// 내가 좋아요한 사람들이 나를 좋아요했는 경우(매칭)의 목록 리스트
class MyMatchingActivity : AppCompatActivity() {

    var myLikeUidList = mutableListOf<String>()
    private var myLikeList = mutableListOf<User>()
    private lateinit var myLikeAdapter: MyLikeAdapter
    private lateinit var myLikeManager: RecyclerView.LayoutManager

    companion object {
        const val TAG = "MyMatchingActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_likes)

        // 내가 좋아요한 사람들 정보 가져오기
        getMyLikes(FirebaseAuthUtils.getUid())

        // RecyclerView 설정
        setMyLikeRecyclerView()
    }

    private fun setMyLikeRecyclerView() {
        val likeRecyclerView = findViewById<RecyclerView>(R.id.myLikesRecyclerView)
        myLikeManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        myLikeAdapter = MyLikeAdapter(this, myLikeList)
        likeRecyclerView.layoutManager = myLikeManager
        likeRecyclerView.adapter = myLikeAdapter

        // clickListener 설정 매칭된 회원인지 알려주기
        val itemClickListener = object : MyLikeAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int) {
                val liked = myLikeAdapter.likes[position]
                checkOtherUserLikeMe(liked.uid.toString(), view, position)
            }
        }
        myLikeAdapter.setClickListener(itemClickListener)
    }

    // 내가 좋아하는 사람들의 모든 UID 가져와서, UID로 사람 정보 가져오기
    private fun getMyLikes(myUid: String) {
        val getMyLikesDataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val uid = data.key.toString()
                    myLikeUidList.add(uid)
                }
                getLikeUserData()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "내가 좋아하는 사람들의 모든 UID 가져오기 실패")
            }
        }
        FirebaseRef.userLikeRef.child(myUid).addValueEventListener(getMyLikesDataListener)
    }

    // 내가 좋아하는 사람들의 UID로 User 가져오기
    private fun getLikeUserData() {
        val getMyLikesUserDataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val user = data.getValue<User>()
                    if (user != null && myLikeUidList.contains(user?.uid.toString())) {
                        myLikeList.add(user)
                    }
                    myLikeAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "내가 좋아하는 사람들의 모든 데이터 가져오기 실패")
            }
        }
        FirebaseRef.userInfoRef.addValueEventListener(getMyLikesUserDataListener)
    }

    // 내가 좋아하는 사람의 좋아요 정보를 가져와서, 날 좋아했는지 매칭 확인하기
    private fun checkOtherUserLikeMe(otherUid: String, view: View, position: Int) {
        val getOtherLikeDataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val likeToken = view.findViewById<ImageView>(R.id.likeToken)
                var check = false;
                for (data in snapshot.children) {
                    val likeUid = data.key.toString()
                    if (likeUid == FirebaseAuthUtils.getUid()) {
                        Toast.makeText(baseContext, "나와 매칭된 회원입니당 !!", Toast.LENGTH_SHORT)
                            .show()
                        likeToken.setBackgroundColor(Color.GREEN)
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    Toast.makeText(baseContext, "나와 매칭된 회원이 아닙니다 ㅠㅠ", Toast.LENGTH_SHORT)
                        .show()
                    likeToken.setBackgroundColor(Color.RED)
                }
            }


            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "다른사람의 UID 가져오기 실패")
            }

        }
        FirebaseRef.userLikeRef.child(otherUid).addValueEventListener(getOtherLikeDataListener)
    }

//    // 매칭된 사람들의 리스트 반환
//    private fun getMyMatchingList(myLikes: MutableList<String>) {
//        for (myLike in myLikes) {
//            if (checkOtherUserLikeMe(myLike)) {
//                myLikeList.add(myLike)
//            }
//        }
//        for (s in myLikeList) {
//            println("매칭된인간 = ${s}")
//        }
//    }
}