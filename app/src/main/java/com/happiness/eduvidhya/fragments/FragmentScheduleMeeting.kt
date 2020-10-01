package com.happiness.eduvidhya.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.adapters.AdapterAllScheduledMeetings
import com.happiness.eduvidhya.datamodels.ClassroomDetailsModel
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog


class FragmentScheduleMeeting : Fragment() {

    private lateinit var schedule_meeting_recyler: RecyclerView
    var detail_db: ClassroomDetailsModel? = null
    private var mRecyclerAdapter: AdapterAllScheduledMeetings? = null
    val db = FirebaseFirestore.getInstance()
    val teacher_collection = db.collection("teachers")
    var mArrayBatchesWithMeeting: ArrayList<ClassroomDetailsModel>? = null

    var updatedProgressDilaog = CustomProgressDialog()

    // top bar
    lateinit var back_top_bar_img: ImageView
    lateinit var title_top_bar_txt: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_schedule_meeting, container, false)



        mArrayBatchesWithMeeting = ArrayList<ClassroomDetailsModel>()

        schedule_meeting_recyler = v.findViewById(R.id.schedule_meeting_recycler)
        back_top_bar_img = v.findViewById(R.id.back_top_bar_img)
        title_top_bar_txt= v.findViewById(R.id.title_top_bar_txt)

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        schedule_meeting_recyler.setLayoutManager(layoutManager)
        val mySharedPreferences = requireActivity().getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
        val email = mySharedPreferences.getString("user_email", "")

        updatedProgressDilaog.show(requireActivity())

        if (Constant.hasNetworkAvailable(requireActivity())) {

            teacher_collection.document(email!!).collection("classrooms").get()

                .addOnSuccessListener { documents ->

                    updatedProgressDilaog.dialog.dismiss()

                    for (document in documents) {
                        detail_db = ClassroomDetailsModel(document.id, "0", "0", "0")
                        mArrayBatchesWithMeeting!!.add(detail_db!!)
                    }

                    mRecyclerAdapter = AdapterAllScheduledMeetings(requireActivity(), mArrayBatchesWithMeeting)
                    schedule_meeting_recyler.adapter = mRecyclerAdapter

                }.addOnFailureListener { exception ->
                    updatedProgressDilaog.dialog.dismiss()
                    Log.w("TAG", "Error getting documents: ", exception)
                }
        } else {
            Toast.makeText(activity, "No network available!", Toast.LENGTH_SHORT).show()
        }

        title_top_bar_txt.setText("All Classrooms")

        back_top_bar_img.setOnClickListener {
            requireActivity().finish()
        }


        return v
    }
}