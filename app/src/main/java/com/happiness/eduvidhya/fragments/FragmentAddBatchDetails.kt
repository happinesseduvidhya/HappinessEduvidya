package com.happiness.eduvidhya.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.happiness.eduvidhya.Interface.RemoveClickListener
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.adapters.AdapterMeetingFacultyCanStart
import com.happiness.eduvidhya.datamodels.ModelMeetings
import com.happiness.eduvidhya.datamodels.StudentEmail
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog
import kotlinx.android.synthetic.main.activity_meetings_faculties.meetingsCheckTxt
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FragmentAddBatchDetails : Fragment(),RemoveClickListener {


    private lateinit var enter_meeting_name:EditText
    private lateinit var enter_meeting_date:EditText
    private lateinit var enter_meeting_time:EditText
    private lateinit var meetingsUnder:TextView
    private var mAdapter: AddStudentEmailAdapter? = null
    var myStudentList: ArrayList<StudentEmail> = ArrayList()
    private var format = ""
    val myCalendar = Calendar.getInstance()

    var updatedProgressDilaog = CustomProgressDialog()

    var create_classroom : DocumentReference?=null
    val db = FirebaseFirestore.getInstance()
    val teacher_collection = db.collection("teachers")

    val classes = db.collection("Classes")

    private var mRecyclerAdapter: AdapterMeetingFacultyCanStart? = null
    private var all_users_recyclerview: RecyclerView? = null


    var strBatchID = ""
    var classname = ""
    var meetingsArrays: ArrayList<ModelMeetings>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_add_batch_details, container, false)

        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        val subject = requireArguments().getString("subject_name")
        val topic = requireArguments().getString("topic_name")
        val position = requireArguments().getString("position")
        classname = requireArguments().getString("classname")!!
        strBatchID = requireArguments().getString("get_class_name")!!

        val mySharedPreferences =requireActivity().getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
        val email = mySharedPreferences.getString("user_email", "")

        if (classname != null)
        {
            create_classroom = teacher_collection.document(email!!).collection("classrooms").document(classname!!)
        }


        enter_meeting_name=v.findViewById(R.id.enter_meeting_name)
        enter_meeting_time=v.findViewById(R.id.enter_meeting_time)
        enter_meeting_date=v.findViewById(R.id.enter_meeting_date)
        all_users_recyclerview=v.findViewById(R.id.all_users_recyclerview)
        meetingsUnder=v.findViewById(R.id.meetingsUnder)

        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        all_users_recyclerview!!.setLayoutManager(layoutManager)

        if (position == null)
        {
            enter_meeting_time.isEnabled = true
            enter_meeting_date.isEnabled = true
        }
        else if (subject == "" && topic =="" && position =="")
        {
            Toast.makeText(context, "No meetings found", Toast.LENGTH_SHORT).show()
        }
        else{

            if (topic.equals("createBatch"))
            {
                enter_meeting_time.isEnabled = true
                enter_meeting_date.isEnabled = true
            }
            else{
                enter_meeting_name.setText(subject)
                enter_meeting_time.setText(position)
                enter_meeting_date.setText(topic)

                enter_meeting_name.isEnabled = false
                enter_meeting_time.isEnabled = false
                enter_meeting_date.isEnabled = false
            }
        }

        mFaculties()
        return v
    }

    override fun OnRemoveClick(index: Int) {
        myStudentList.removeAt(index)
        mAdapter!!.notifyData(myStudentList)
    }
    private fun updateLabel(): Unit {
        val myFormat = "dd-MM-yy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        enter_meeting_date.setText(sdf.format(myCalendar.time))
    }

    fun showTime(hour: Int, min: Int) {
        var hour = hour
        if (hour == 0) {
            hour += 12
            format = "AM"
        } else if (hour == 12) {
            format = "PM"
        } else if (hour > 12) {
            hour -= 12
            format = "PM"
        } else {
            format = "AM"
        }
        enter_meeting_time.setText(
            StringBuilder().append(hour).append(" : ").append(min)
                .append(" ").append(format)
        )
    }



    fun mFaculties() {
        updatedProgressDilaog.show(requireContext())
        if (Constant.hasNetworkAvailable(requireContext())) {

            val mySharedPreferences = requireContext().getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
            val email = mySharedPreferences.getString("user_email", "")

            classes.document(email.toString()).collection("classrooms").document(classname.toString()).collection("Batches").document(strBatchID.toString()).collection("Meetings").get()
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
                                meetingsUnder.visibility = View.GONE
                            }
                            else{
                                meetingsUnder.visibility = View.VISIBLE
                                meetingsCheckTxt.visibility = View.GONE
                                mRecyclerAdapter = AdapterMeetingFacultyCanStart(requireContext(), meetingsArrays,"faculty",classname.toString())
                                all_users_recyclerview!!.adapter = mRecyclerAdapter
                            }

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException())
                        }
                    }
                }).addOnFailureListener { exception ->
                    updatedProgressDilaog.dialog.dismiss()

                }
        } else {
            Toast.makeText(requireContext(), "No network available!", Toast.LENGTH_SHORT).show()
        }
    }

}