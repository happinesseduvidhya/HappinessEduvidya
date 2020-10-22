package com.happiness.eduvidhya.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.happiness.eduvidhya.Interface.RemoveClickListener
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.datamodels.BatchDeatailModel
import com.happiness.eduvidhya.datamodels.StudentEmail
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FragmentAddBatchDetails : Fragment(),RemoveClickListener {


    private lateinit var student_list_constraint: ConstraintLayout
    private lateinit var enter_meeting_name:EditText
    private lateinit var enter_meeting_date:EditText
    private lateinit var enter_meeting_time:EditText
    private lateinit var add_student_name:EditText
    private lateinit var add_student_to_list: ImageView
    private lateinit var add_student_recycler: RecyclerView
    private var mAdapter: AddStudentEmailAdapter? = null
    var myStudentList: ArrayList<StudentEmail> = ArrayList()
    private var format = ""
    val myCalendar = Calendar.getInstance()

    var updatedProgressDilaog = CustomProgressDialog()

    var create_classroom : DocumentReference?=null
    val db = FirebaseFirestore.getInstance()
    val teacher_collection = db.collection("teachers")

    val classes = db.collection("Classes")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_add_batch_details, container, false)

        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        val subject = requireArguments().getString("subject_name")
        val topic = requireArguments().getString("topic_name")
        val position = requireArguments().getString("position")
        val classname = requireArguments().getString("classname")

        val mySharedPreferences =requireActivity().getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
        val email = mySharedPreferences.getString("user_email", "")

        if (classname != null)
        {
            create_classroom = teacher_collection.document(email!!).collection("classrooms").document(classname!!)
        }


        enter_meeting_name=v.findViewById(R.id.enter_meeting_name)
        enter_meeting_time=v.findViewById(R.id.enter_meeting_time)
        enter_meeting_date=v.findViewById(R.id.enter_meeting_date)
        add_student_name=v.findViewById(R.id.enter_student_name)
        add_student_to_list=v.findViewById(R.id.add_student_img)
        add_student_recycler=v.findViewById(R.id.student_list_recycler)
        student_list_constraint=v.findViewById(R.id.student_list_constraint)




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
}