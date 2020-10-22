package com.happiness.eduvidhya.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.adapters.AdapterShowBatches
import com.happiness.eduvidhya.datamodels.BatchDeatailModel
import com.happiness.eduvidhya.datamodels.ListOfBatchesModel
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog
import java.text.SimpleDateFormat
import java.util.*

class ActivityCreateBatchByFaculty : AppCompatActivity() {


    lateinit var enter_meeting_name: EditText
    lateinit var enter_meeting_time: EditText
    lateinit var enter_meeting_date: EditText
    lateinit var submit_batch: Button

    val myCalendar = Calendar.getInstance()
    private var format = ""

    var updatedProgressDilaog = CustomProgressDialog()

    val db = FirebaseFirestore.getInstance()
    val classes = db.collection("Classes")

    var batchCheckLast = "0"
    var strEmail: String? = ""
    var strClassName: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_batch_by_faculty)

        enter_meeting_name = findViewById(R.id.enter_meeting_name)
        enter_meeting_time = findViewById(R.id.enter_meeting_time)
        enter_meeting_date = findViewById(R.id.enter_meeting_date)
        submit_batch = findViewById(R.id.submit_batch)

        strClassName = intent.getStringExtra("className")

        val mySharedPreferences = getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
        strEmail = mySharedPreferences.getString("user_email", "")!!

        updatedProgressDilaog.show(this)

        if (Constant.hasNetworkAvailable(applicationContext)) {

            classes.document(strEmail!!).collection("classrooms").document(strClassName.toString())
                .collection("Batches").get()
                .addOnSuccessListener { documents ->
                    updatedProgressDilaog.dialog.dismiss()


                    var batchcount:Int =0
                    for (document in documents) {
                        batchCheckLast = document.id
                    }
                    batchcount = (batchCheckLast.toInt()+1)
                    batchCheckLast = batchcount.toString()


                }.addOnFailureListener { exception ->
                    updatedProgressDilaog.dialog.dismiss()
                    Log.w("TAG", "Error getting documents: ", exception)
                }
        } else {
            Toast.makeText(this, "No network available", Toast.LENGTH_SHORT).show()

        }

        enter_meeting_time.setOnClickListener {

            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val myFormat = "hh:mm a" //In which you need put here
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    showTime(selectedHour, selectedMinute)

                },
                hour,
                minute,
                false
            ) //Yes 24 hour time

            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }
        val date = DatePickerDialog.OnDateSetListener {

                view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            updateLabel()
        }

        enter_meeting_date.setOnClickListener {

            DatePickerDialog(
                this,
                date,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        submit_batch.setOnClickListener {
            createBatches()
        }

    }

    fun showTime(hour: Int, min: Int) {

        val newMin= min

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
        val newValue = fillText(newMin)
        enter_meeting_time.setText(
            StringBuilder().append(hour).append(" : ").append(newValue!!.toString())
                .append(" ").append(format)
        )
    }

    private fun fillText(i: Int): String? {
        return if (i > 9) i.toString() + "" else "0$i"
    }

    private fun updateLabel(): Unit {
        val myFormat = "dd-MM-yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        enter_meeting_date.setText(sdf.format(myCalendar.time))
    }

    private fun createBatches() {

        if (!enter_meeting_name.text.toString().isEmpty()) {
            if (!enter_meeting_time.text.toString().isEmpty()) {
                if (!enter_meeting_date.text.toString().isEmpty()) {
                    updatedProgressDilaog.show(this)

                    val batch_detail = BatchDeatailModel(
                        enter_meeting_name.text.toString(),
                        enter_meeting_date.text.toString(),
                        enter_meeting_time.text.toString()
                    )

                    classes.document(strEmail!!).collection("classrooms")
                        .document(strClassName.toString()).collection("Batches")
                        .document(batchCheckLast.toString()).set(batch_detail).addOnSuccessListener {

                        updatedProgressDilaog.dialog.dismiss()
                        submit_batch.isEnabled = false
                        Toast.makeText(this, "meeting created", Toast.LENGTH_SHORT).show()


                    }.addOnFailureListener {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "enter meeting date", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "enter meeting time", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "enter meeting name", Toast.LENGTH_SHORT).show()
        }

    }
}
