package com.happiness.eduvidhya.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.happiness.eduvidhya.activities.ActivityBaseForFragment
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.adapters.AdapterAllScheduledMeetings
import com.happiness.eduvidhya.adapters.AdapterSmallClassRoom
import com.happiness.eduvidhya.datamodels.ClassroomDetailsModel
import com.happiness.eduvidhya.datamodels.ModelUserInfo
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog


class FragmentSmallClassroom : Fragment() {

    private lateinit var schedule_meeting_recyler: RecyclerView
    var detail_db: ClassroomDetailsModel? = null
    private var mRecyclerAdapter: AdapterSmallClassRoom? = null
    val db = FirebaseFirestore.getInstance()
    val faculties_collection = db.collection("Classes")
    var mArrayBatchesWithMeeting: ArrayList<ClassroomDetailsModel>? = null

    var updatedProgressDilaog = CustomProgressDialog()


    private lateinit var create_batch_btn: Button
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_small_classroom, container, false)

        mArrayBatchesWithMeeting = ArrayList<ClassroomDetailsModel>()

        schedule_meeting_recyler = v.findViewById(R.id.all_classes_recyclerview)

        create_batch_btn = v.findViewById(R.id.create_classroom_btn)

        create_batch_btn.setOnClickListener {
            val i = Intent(activity, ActivityBaseForFragment::class.java)
            i.putExtra("checkPage", "createClassroom")
            startActivity(i)
            //Toast.makeText(activity, "skdnskncfkd", Toast.LENGTH_SHORT).show()

        }


        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        schedule_meeting_recyler.setLayoutManager(layoutManager)

        val mySharedPreferences = requireActivity().getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
        val email = mySharedPreferences.getString("user_email", "")

        updatedProgressDilaog.show(requireActivity())

        if (Constant.hasNetworkAvailable(requireActivity())) {

            faculties_collection.document(email!!).collection("classrooms").addSnapshotListener(object :
                EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    updatedProgressDilaog.dialog.dismiss()
                    detail_db = null
                    mArrayBatchesWithMeeting!!.clear()
                    for (data in value!!.documents) {
                        if (data.exists()) {
                            detail_db = ClassroomDetailsModel(data.id, "0", "0")
                            mArrayBatchesWithMeeting!!.add(detail_db!!)
                        }
                    }
                    mRecyclerAdapter = AdapterSmallClassRoom(activity!!, mArrayBatchesWithMeeting)
                    schedule_meeting_recyler.adapter = mRecyclerAdapter
                }

            })

//            faculties_collection.document(email!!).collection("classrooms").get().addOnCompleteListener(object:
//                OnCompleteListener<QuerySnapshot> {
//                override fun onComplete(@NonNull task: Task<QuerySnapshot>) {
//
//                    updatedProgressDilaog.dialog.dismiss()
//
//                    if (task.isSuccessful())
//                    {
//                        for (document in task.getResult()!!)
//                        {
//                            detail_db = ClassroomDetailsModel(document.id, "0", "0")
//                            mArrayBatchesWithMeeting!!.add(detail_db!!)
//                        }
//                        mRecyclerAdapter = AdapterAllScheduledMeetings(requireActivity(), mArrayBatchesWithMeeting)
//                        schedule_meeting_recyler.adapter = mRecyclerAdapter
//                    }
//                    else
//                    {
//                        Log.d("TAG", "Error getting documents: ", task.getException())
//                    }
//                }
//            })


//            faculties_collection.document(email!!).collection("classrooms").get()
//
//                .addOnSuccessListener { documents ->
//
//                    updatedProgressDilaog.dialog.dismiss()
//
//                    for (document in documents) {
//                        detail_db = ClassroomDetailsModel(document.id, "0", "0")
//                        mArrayBatchesWithMeeting!!.add(detail_db!!)
//                    }
//
//                    mRecyclerAdapter = AdapterAllScheduledMeetings(requireActivity(), mArrayBatchesWithMeeting)
//                    schedule_meeting_recyler.adapter = mRecyclerAdapter
//
//                }.addOnFailureListener { exception ->
//                    updatedProgressDilaog.dialog.dismiss()
//                }
        } else {
            Toast.makeText(activity, "No network available!", Toast.LENGTH_SHORT).show()
        }
        return v


    }
}