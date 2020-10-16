package com.happiness.eduvidhya.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.adapters.AdapterMeetingsCreatedByFaculties
import com.happiness.eduvidhya.datamodels.ModelMeetings
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog
import kotlinx.android.synthetic.main.activity_all_meetings_see_by_user.*
import kotlinx.android.synthetic.main.activity_all_meetings_see_by_user.all_users_recyclerview
import kotlinx.android.synthetic.main.activity_all_meetings_see_by_user.meetingsCheckTxt
import kotlinx.android.synthetic.main.activity_meeting_start_by_faculty.*
import kotlinx.android.synthetic.main.activity_users.*

class ActivityMeetingStartByFaculty : AppCompatActivity() {

    // top bar
    lateinit var back_top_bar_img: ImageView
    lateinit var title_top_bar_txt: TextView

    val db = FirebaseFirestore.getInstance()
    val meetings_collection = db.collection("Meetings")

    var updatedProgressDilaog = CustomProgressDialog()

    var className:String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_start_by_faculty)

        back_top_bar_img = findViewById(R.id.back_top_bar_img)
        title_top_bar_txt = findViewById(R.id.title_top_bar_txt)
        title_top_bar_txt.setText("All Meetings")
        back_top_bar_img.setOnClickListener {
            finish()
        }

        val layoutManager = LinearLayoutManager(this@ActivityMeetingStartByFaculty)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        meetings_recycler_start_faculty.setLayoutManager(layoutManager)

        className = intent.getStringExtra("classroom_name")

        mAllMeetings()
    }


    var meetingsArrays: ArrayList<ModelMeetings>? = null
    private var mRecyclerAdapter: AdapterMeetingsCreatedByFaculties? = null

    fun mAllMeetings() {
        updatedProgressDilaog.show(this)
        if (Constant.hasNetworkAvailable(applicationContext)) {

            val mySharedPreferences = getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
            val email = mySharedPreferences.getString("user_email", "")

            meetings_collection.document(email.toString()).collection("Classes").document(className.toString()).collection("Meetings").get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                    override fun onComplete(@NonNull task: Task<QuerySnapshot>) {

                        updatedProgressDilaog.dialog.dismiss()
                        if (task.isSuccessful()) {

                            meetingsArrays = ArrayList<ModelMeetings>()
                            meetingsArrays!!.clear()

                            for (document in task.result!!) {

                                val meetingname = document.data.get("meetingName")
                                val meetingDate = document.data.get("meetingDate")
                                val meetingTime = document.data.get("meetingTime")
                                val meetingID = document.data.get("meetingID")
                                val meetingStatus = document.data.get("meetingStatus")

                                val userOrFacultyInfo = ModelMeetings(meetingname.toString(), meetingTime.toString(), meetingDate.toString(),meetingID.toString(),"",meetingStatus.toString())
                                meetingsArrays!!.add(userOrFacultyInfo)

                            }

                            if (meetingsArrays!!.size ==0)
                            {
                                meetingsCheckTxt.visibility = View.VISIBLE
                            }
                            else{
                                meetingsCheckTxt.visibility = View.GONE
                                mRecyclerAdapter = AdapterMeetingsCreatedByFaculties(this@ActivityMeetingStartByFaculty, meetingsArrays,"faculty","")
                                meetings_recycler_start_faculty.adapter = mRecyclerAdapter
                            }


                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException())
                        }
                    }
                }).addOnFailureListener { exception ->
                    updatedProgressDilaog.dialog.dismiss()

                }
        } else {
            Toast.makeText(applicationContext, "No network available!", Toast.LENGTH_SHORT).show()
        }
    }
}