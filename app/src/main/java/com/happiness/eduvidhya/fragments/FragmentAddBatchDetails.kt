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
import androidx.recyclerview.widget.LinearLayoutManager
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
    private lateinit var enter_meeting_time:EditText
    private lateinit var enter_meeting_date:EditText
    private lateinit var add_student_name:EditText
    private lateinit var add_student_to_list: ImageView
    private lateinit var submit_batch: Button
    private lateinit var add_student_recycler: RecyclerView
    private var mAdapter: AddStudentEmailAdapter? = null
    var myStudentList: ArrayList<StudentEmail> = ArrayList()
    private var format = ""
    val myCalendar = Calendar.getInstance()

    var updatedProgressDilaog = CustomProgressDialog()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_add_batch_details, container, false)

        requireActivity()!!.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        val bundle = Bundle()
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
        submit_batch=v.findViewById(R.id.submit_batch)
        add_student_recycler=v.findViewById(R.id.student_list_recycler)
        student_list_constraint=v.findViewById(R.id.student_list_constraint)

//        mAdapter =AddStudentEmailAdapter(requireActivity(),myStudentList, this)
//        val layoutManager = LinearLayoutManager(activity)
//        layoutManager.orientation = LinearLayoutManager.VERTICAL
//
//        add_student_recycler.setLayoutManager(layoutManager)
//        add_student_recycler.adapter=mAdapter



        if (position == null)
        {
            submit_batch.visibility = View.VISIBLE
            enter_meeting_time.isEnabled = true
            enter_meeting_date.isEnabled = true
        }
        else if (subject == "" && topic =="" && position =="")
        {
            submit_batch.visibility = View.GONE
            Toast.makeText(context, "No meetings found", Toast.LENGTH_SHORT).show()
        }
        else{
            enter_meeting_name.setText(subject)
            enter_meeting_time.setText(position)
            enter_meeting_date.setText(topic)

            enter_meeting_name.isEnabled = false
            enter_meeting_time.isEnabled = false
            enter_meeting_date.isEnabled = false

            submit_batch.visibility = View.GONE
        }


        val date = DatePickerDialog.OnDateSetListener {

                view, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                updateLabel()
            }
        enter_meeting_date.setOnClickListener {

            DatePickerDialog(requireActivity(), date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH], myCalendar[Calendar.DAY_OF_MONTH]).show()
        }
        enter_meeting_time.setOnClickListener {

            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val myFormat = "hh:mm a" //In which you need put here
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(requireActivity(), TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute -> showTime(selectedHour, selectedMinute)

                }, hour, minute, false
            ) //Yes 24 hour time

            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }
        submit_batch.setOnClickListener {

            if (!enter_meeting_name.text.toString().isEmpty())
            {
                if (!enter_meeting_time.text.toString().isEmpty())
                {
                    if (!enter_meeting_date.text.toString().isEmpty())
                    {
                        updatedProgressDilaog.show(requireActivity())

                        val batch_detail = BatchDeatailModel(enter_meeting_name.text.toString(), enter_meeting_date.text.toString(), enter_meeting_time.text.toString())

                        create_classroom!!.collection("Batches").document("Batch"+(position)).set(batch_detail).addOnSuccessListener {

                            updatedProgressDilaog.dialog.dismiss()
                            submit_batch.isEnabled = false
                            Toast.makeText(context, "meeting created", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Constant.showMessage(it,"enter meeting date")
                    }
                }
                else{
                    Constant.showMessage(it,"enter meeting time")
                }
            }
            else{
                Constant.showMessage(it,"enter meeting name")
            }


        }

//        add_student_to_list.setOnClickListener {
//            if(add_student_name.text.toString().isEmpty())
//            {
//                Toast.makeText(activity,"Enter Any Email",Toast.LENGTH_SHORT).show()
//            }
//            else {
//
//                student_list_constraint.visibility = View.VISIBLE
//                val mLog = StudentEmail()
//                mLog.Email = add_student_name.text.toString()
//                myStudentList.add(mLog)
//                mAdapter!!.notifyData(myStudentList)
//                add_student_name.setText("");
//            }
//        }

        return v
    }
    var create_classroom : DocumentReference?=null
    val db = FirebaseFirestore.getInstance()
    val teacher_collection = db.collection("teachers")
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