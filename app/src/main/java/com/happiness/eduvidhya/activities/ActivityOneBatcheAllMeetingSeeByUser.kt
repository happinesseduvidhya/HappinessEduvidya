package com.happiness.eduvidhya.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.adapters.AdapterMeetingFacultyCanStart
import com.happiness.eduvidhya.datamodels.ModelMeetings
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog
import kotlinx.android.synthetic.main.activity_all_meetings_see_by_user.*
import kotlinx.android.synthetic.main.activity_users.all_users_recyclerview

class ActivityOneBatcheAllMeetingSeeByUser : AppCompatActivity() {

    // top bar
    lateinit var back_top_bar_img: ImageView
    lateinit var title_top_bar_txt: TextView

    val db = FirebaseFirestore.getInstance()
    val classes_collection = db.collection("Classes")

    var updatedProgressDilaog = CustomProgressDialog()
    var emailFaculty:String ?= null
    var classType:String ?= null
    var classBatchID:String ?= null
    var meetingsArrays: ArrayList<ModelMeetings>? = null
    private var mRecyclerAdapter: AdapterMeetingFacultyCanStart? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_batch_all_meetings)

        back_top_bar_img = findViewById(R.id.back_top_bar_img)
        title_top_bar_txt = findViewById(R.id.title_top_bar_txt)

        title_top_bar_txt.setText("Batch Meetings")

        back_top_bar_img.setOnClickListener {
            finish()
        }


        emailFaculty = intent.getStringExtra("Email")
        classType = intent.getStringExtra("ClassName")
        classBatchID = intent.getStringExtra("ClassBatchID")

        val layoutManager = LinearLayoutManager(this@ActivityOneBatcheAllMeetingSeeByUser)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        all_users_recyclerview.setLayoutManager(layoutManager)

        mAllMeetings()

    }

    fun mAllMeetings() {

        updatedProgressDilaog.show(this)

        if (Constant.hasNetworkAvailable(applicationContext)) {
//            tagore
            val mySharedPreferences = getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
            val email = mySharedPreferences.getString("user_email", "")

            classes_collection.document(emailFaculty.toString()).collection("classrooms").
            document(classType.toString()).collection("Batches").
            document(classBatchID.toString()).collection("Meetings").addSnapshotListener(object :
                EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    updatedProgressDilaog.dialog.dismiss()
                    meetingsArrays = ArrayList<ModelMeetings>()
                    meetingsArrays!!.clear()

                    for (data in value!!.documents) {

                        val meetingname = data.data!!.get("meetingName")
                        val meetingDate = data.data!!.get("meetingDate")
                        val meetingTime = data.data!!.get("meetingTime")
                        val meetingID = data.data!!.get("meetingID")
                        val meetingStatus = data.data!!.get("meetingStatus")

                        val userOrFacultyInfo = ModelMeetings(
                            meetingname.toString(),
                            meetingTime.toString(),
                            meetingDate.toString(),
                            meetingID.toString(),
                            "",
                            meetingStatus.toString()
                        )
                        meetingsArrays!!.add(userOrFacultyInfo)

                    }

                    if (meetingsArrays!!.size == 0) {
                        meetingsCheckTxt.visibility = View.VISIBLE
                    } else {
                        meetingsCheckTxt.visibility = View.GONE
                        mRecyclerAdapter = AdapterMeetingFacultyCanStart(
                            this@ActivityOneBatcheAllMeetingSeeByUser,
                            meetingsArrays,
                            emailFaculty.toString(),
                            classType.toString()
                        )
                        all_users_recyclerview.adapter = mRecyclerAdapter
                    }
                }
            })
        } else {
            Toast.makeText(applicationContext, "No network available!", Toast.LENGTH_SHORT).show()
        }
    }
}