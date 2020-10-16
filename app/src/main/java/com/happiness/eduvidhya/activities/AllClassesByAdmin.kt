package com.happiness.eduvidhya.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.adapters.AdapterAllScheduledMeetings
import com.happiness.eduvidhya.datamodels.ClassroomDetailsModel
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog

class AllClassesByAdmin : AppCompatActivity() {

    private lateinit var schedule_meeting_recyler: RecyclerView
    var detail_db: ClassroomDetailsModel? = null
    private var mRecyclerAdapter: AdapterAllScheduledMeetings? = null
    val db = FirebaseFirestore.getInstance()
    val classes = db.collection("Faculties")
    var mArrayBatchesWithMeeting: ArrayList<ClassroomDetailsModel>? = null

    var updatedProgressDilaog = CustomProgressDialog()

    // top bar
    lateinit var back_top_bar_img: ImageView
    lateinit var title_top_bar_txt: TextView

    lateinit var no_data_found: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_classes_by_admin)

        mArrayBatchesWithMeeting = ArrayList<ClassroomDetailsModel>()

        val strEmail = intent.getStringExtra("email")

        schedule_meeting_recyler = findViewById(R.id.schedule_meeting_recycler)
        back_top_bar_img = findViewById(R.id.back_top_bar_img)
        title_top_bar_txt= findViewById(R.id.title_top_bar_txt)
        no_data_found= findViewById(R.id.no_data_found)

        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        schedule_meeting_recyler.setLayoutManager(layoutManager)

        updatedProgressDilaog.show(this)

        if (Constant.hasNetworkAvailable(this)) {


            classes.document(strEmail!!.toString()).collection("classrooms").get()

                .addOnSuccessListener { documents ->

                    updatedProgressDilaog.dialog.dismiss()

                    for (document in documents) {

                        val classID = document.id
                        classes.document(strEmail).collection("classrooms").document(classID.toString()).collection("Batches").get()
                            .addOnSuccessListener { documents ->
                                updatedProgressDilaog.dialog.dismiss()
                                for (document in documents) {
//                                    detail_db = ListOfBatchesModel(document.id)
//                                    list_of_batches!!.add(detail_db!!)
                                }

//                                mRecyclerAdapter = AdapterShowBatches(requireActivity(),list_of_batches,get_classroom.toString())
//                                show_batches_recycler.adapter = mRecyclerAdapter

                            }.addOnFailureListener { exception ->
                                updatedProgressDilaog.dialog.dismiss()
                                Log.w("TAG", "Error getting documents: ", exception)
                            }
                        detail_db = ClassroomDetailsModel(document.id, "0", "0")
                        mArrayBatchesWithMeeting!!.add(detail_db!!)
                    }

                    if (mArrayBatchesWithMeeting!!.size == 0)
                    {
                        no_data_found.visibility = View.VISIBLE
                    }
                    else{
                        mRecyclerAdapter = AdapterAllScheduledMeetings(this, mArrayBatchesWithMeeting)
                        schedule_meeting_recyler.adapter = mRecyclerAdapter
                    }
                }.addOnFailureListener { exception ->
                    updatedProgressDilaog.dialog.dismiss()

                }
        } else {
            Toast.makeText(applicationContext, "No network available!", Toast.LENGTH_SHORT).show()
        }

        title_top_bar_txt.setText("All Classrooms")

        back_top_bar_img.setOnClickListener {
           finish()
        }
    }
}