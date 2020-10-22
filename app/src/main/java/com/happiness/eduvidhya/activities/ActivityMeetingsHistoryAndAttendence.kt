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
import com.happiness.eduvidhya.adapters.AdapterAllClassesUser
import com.happiness.eduvidhya.adapters.AdapterAllMeetingHistories
import com.happiness.eduvidhya.datamodels.ModelMeetingHistories
import com.happiness.eduvidhya.datamodels.UserOrFacultyInfo
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog
import kotlinx.android.synthetic.main.activity_meetings_faculties.*
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.android.synthetic.main.activity_users.all_users_recyclerview

class ActivityMeetingsHistoryAndAttendence : AppCompatActivity() {

    // top bar
    lateinit var back_top_bar_img: ImageView
    lateinit var title_top_bar_txt: TextView

    val db = FirebaseFirestore.getInstance()
    val faculties_collection = db.collection("Faculties")

    var updatedProgressDilaog = CustomProgressDialog()

    var strHistoryOrAttendence:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meetings_history_and_attendence)


        back_top_bar_img = findViewById(R.id.back_top_bar_img)
        title_top_bar_txt = findViewById(R.id.title_top_bar_txt)
        title_top_bar_txt.setText("Select your Meeting")
        back_top_bar_img.setOnClickListener {
            finish()
        }

        strHistoryOrAttendence = intent.getStringExtra("HistoryOrAttendence")


        val layoutManager = LinearLayoutManager(this@ActivityMeetingsHistoryAndAttendence)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        all_users_recyclerview.setLayoutManager(layoutManager)

        mFaculties(strHistoryOrAttendence)

    }

    var ARRAYS_USERS_HISTORY_OR_ATTENDENCE: ArrayList<ModelMeetingHistories>? = null
    private var mRecyclerAdapter: AdapterAllMeetingHistories? = null

    fun mFaculties(strHistoryOrAttendence:String) {
        updatedProgressDilaog.show(this)
        if (Constant.hasNetworkAvailable(applicationContext)) {

            val mySharedPreferences = getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
            val email = mySharedPreferences.getString("user_email", "")

            faculties_collection.document(email.toString()).collection("MeetingHistory").get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                    override fun onComplete(@NonNull task: Task<QuerySnapshot>) {

                        updatedProgressDilaog.dialog.dismiss()
                        if (task.isSuccessful()) {

                            ARRAYS_USERS_HISTORY_OR_ATTENDENCE = ArrayList<ModelMeetingHistories>()
                            ARRAYS_USERS_HISTORY_OR_ATTENDENCE!!.clear()

                            for (document in task.result!!) {

                                val facultyID = document.id

                                val strMeetingName = document.data.get("strMeetingName")
                                val  strMeetingID = document.data.get("strMeetingID")
                                val  strMeetingDate = document.data.get("strMeetingDate")
                                val  strMeetingTime = document.data.get("strMeetingTime")
                                val  strMeetingStatus = document.data.get("strMeetingStatus")
                                val  strClassName = document.data.get("strClassName")

                                val modelMeetingHistories = ModelMeetingHistories(strMeetingName.toString(), strMeetingID.toString(), strMeetingDate.toString(),strMeetingTime.toString(),strMeetingStatus.toString(),strClassName.toString())
                                ARRAYS_USERS_HISTORY_OR_ATTENDENCE!!.add(modelMeetingHistories)

                            }

                            if (ARRAYS_USERS_HISTORY_OR_ATTENDENCE!!.size == 0)
                            {
                                meetingsCheckTxt.visibility = View.VISIBLE
                            }
                            else{
                                meetingsCheckTxt.visibility = View.GONE
                                mRecyclerAdapter = AdapterAllMeetingHistories(
                                    this@ActivityMeetingsHistoryAndAttendence,ARRAYS_USERS_HISTORY_OR_ATTENDENCE,strHistoryOrAttendence)
                                all_users_recyclerview.adapter = mRecyclerAdapter
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