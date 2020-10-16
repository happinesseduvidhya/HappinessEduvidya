package com.happiness.eduvidhya.fragments

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.RetrofitConnection.ApiCllientXml
import com.happiness.eduvidhya.datamodels.ModelMeetings
import com.happiness.eduvidhya.datamodels.StudentEmail
import com.happiness.eduvidhya.datamodels.response
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import retrofit2.Call
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*


class FragmentCreateMeetingByFaculty : Fragment() {

    private lateinit var enter_meeting_name: EditText
    private lateinit var enter_meeting_date: EditText
    private lateinit var enter_meeting_time: EditText
    private lateinit var display_code_text: TextView
    private lateinit var display_code_value: TextView
    private lateinit var display_name_text: TextView
    private lateinit var display_name_value: TextView
    private lateinit var create_meeting_btn: Button
    private lateinit var share_btn: Button
    private var code: String = "random-4993817"
    private var meeting_id_from_api: String = ""
    private var format = ""
    val myCalendar = Calendar.getInstance()

    var updatedProgressDilaog = CustomProgressDialog()


    lateinit var spinner: Spinner
    val mArray = ArrayList<String>()
    var strClass:String ?= ""

    val db = FirebaseFirestore.getInstance()
    val classes = db.collection("Classes")
    val meetings = db.collection("Meetings")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v: View = inflater.inflate(R.layout.fragment_create_meeting_by_faculty, container, false)


        enter_meeting_name = v.findViewById(R.id.enter_meeting_name)
        enter_meeting_date = v.findViewById(R.id.enter_meeting_date)
        enter_meeting_time = v.findViewById(R.id.enter_meeting_time)
        create_meeting_btn = v.findViewById(R.id.create_meeting_btn)
        display_code_text = v.findViewById(R.id.display_code_text)
        display_code_value = v.findViewById(R.id.display_code_value)
        display_name_text = v.findViewById(R.id.display_name_text)
        display_name_value = v.findViewById(R.id.display_name_value)
        spinner = v.findViewById(R.id.spinner)
        share_btn = v.findViewById(R.id.share_btn)

        val date =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel()
            }
        create_meeting_btn.setOnClickListener {
            if (enter_meeting_name.text.toString().isEmpty()) {
                Toast.makeText(activity, "Enter Meeting Name", Toast.LENGTH_SHORT).show()
            } else {
                createMeeting(enter_meeting_name.text.toString().replace("\\s".toRegex(), ""))
            }
        }
        share_btn.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareBody = meeting_id_from_api.toString()
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, "Share via"))
        }

        enter_meeting_date.setOnClickListener {

            val mDate = activity?.let { it1 ->
                DatePickerDialog(it1, date, 2016, 2, 24) }
            mDate?.getDatePicker()?.setMinDate(System.currentTimeMillis() - 1000)
            mDate?.show()

//            DatePickerDialog(
//                requireActivity(), date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
//                myCalendar[Calendar.DAY_OF_MONTH]
//            ).show()
        }
        enter_meeting_time.setOnClickListener {
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val myFormat = "hh:mm" //In which you need put here
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(
                requireActivity(),
                OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    showTime(selectedHour, selectedMinute)

                }, hour, minute, false
            ) //Yes 24 hour time

            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }



        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(activity, "enter", Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                strClass = mArray.get(position)
            }

        }

        mCallGetUsers()

        return v
    }

    private fun createMeeting(meeting_name: String) {



        val ran = Random()
        val code = (100000 + ran.nextInt(900000)).toString()
        val meeting_id = "random-" + code.toString()
        val method = "create"
        val parameters = "allowStartStopRecording=true&attendeePW=ap&autoStartRecording=false&meetingID=" + meeting_id + "&moderatorPW=mp&name=" + meeting_name + "&record=true&welcome=Welcometo"
//        val checksum = DigestUtils.shaHex(method + parameters + Constant.shared_secret)

        val checksum = String(Hex.encodeHex(DigestUtils.sha(method + parameters + Constant.shared_secret)))

        if (Constant.hasNetworkAvailable(requireActivity())) {
            updatedProgressDilaog.show(requireActivity())



            try {
                val call: Call<response> = ApiCllientXml.getClientXml.CreateMeeting(
                    true, "ap", false, meeting_id, "mp", meeting_name.trim(),true, "Welcometo", checksum)
                call.enqueue(object : Callback<response> {
                    override fun onFailure(call: Call<response>, t: Throwable) {
                        updatedProgressDilaog.dialog.dismiss()
                        Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<response>,
                        response: retrofit2.Response<response>
                    ) {

                        var dataValue: response? = null

                        dataValue = response.body()

                        if (dataValue == null) {
                            updatedProgressDilaog.dialog.dismiss()
                            Toast.makeText(activity, "No data found", Toast.LENGTH_SHORT).show()

                        } else {
                            meeting_id_from_api = dataValue.meetingID.toString()
                            display_code_text.visibility = View.VISIBLE
                            display_code_value.visibility = View.VISIBLE
                            display_name_text.visibility = View.VISIBLE
                            display_name_value.visibility = View.VISIBLE
                            share_btn.visibility = View.VISIBLE
                            display_code_value.setText(dataValue.meetingID.toString())
                            display_name_value.setText(meeting_name)

                            val meetingsModel = ModelMeetings(enter_meeting_name.text.toString(),enter_meeting_time.text.toString(),enter_meeting_date.text.toString(),display_code_value.text.toString(),strClass.toString(),"0")

                            val mySharedPreferences = requireActivity().getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
                            val email = mySharedPreferences.getString("user_email", "")

                            val model = StudentEmail(email)
                            meetings.document(email.toString()).set(model)
                            meetings.document(email.toString()).collection("Classes").document(strClass.toString()).set(model)
                            val meetings = meetings.document(email.toString()).collection("Classes").document(strClass.toString()).collection("Meetings").document(display_code_value.text.toString())

                            meetings.set(meetingsModel).addOnSuccessListener {
                                updatedProgressDilaog.dialog.dismiss()
                                create_meeting_btn.isEnabled = false
                                Toast.makeText(activity, "Successfully Created Meeting", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener {

                            }
                        }
                    }
                })
            } catch (ex: java.lang.Exception) {
                updatedProgressDilaog.dialog.dismiss()
            }

        } else {
            Toast.makeText(activity, "No network available!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateLabel(): Unit {
        val myFormat = "dd-MM-yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        enter_meeting_date.setText(sdf.format(myCalendar.time))
    }

    fun showTime(hour: Int, min: Int) {

        var newMin= min

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
//        if(newMin<10)
//        {
//            newMin="0"+newMin
//        }

        val newValue = fillText(newMin)
        enter_meeting_time.setText(StringBuilder().append(hour).append(" : ").append(newValue!!.toString()).append(" ").append(format))
    }

    private fun fillText(i: Int): String? {
        return if (i > 9) i.toString() + "" else "0$i"
    }

    private fun mCallGetUsers()
    {
        val mySharedPreferences = requireActivity().getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
        val email = mySharedPreferences.getString("user_email", "")


        if (Constant.hasNetworkAvailable(requireActivity())) {

            updatedProgressDilaog.show(requireActivity())
            classes.document(email.toString()).collection("classrooms").get()

                .addOnSuccessListener { documents ->

                    updatedProgressDilaog.dialog.dismiss()

                    for (document in documents) {
                        mArray.add(document.id)
                    }

                    if (mArray.size ==0)
                    {
                        create_meeting_btn.visibility = View.GONE
                        Toast.makeText(requireActivity(), "No classes found", Toast.LENGTH_SHORT).show()
                    }
                    else{

                        create_meeting_btn.visibility = View.VISIBLE
                        val aa = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, mArray)
                        // Set layout to use when the list of choices appear
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        // Set Adapter to Spinner
                        spinner.setAdapter(aa)
                    }


                }.addOnFailureListener { exception ->
                    updatedProgressDilaog.dialog.dismiss()
                }
        } else {
            Toast.makeText(requireActivity(), "No network available!", Toast.LENGTH_SHORT).show()
        }
    }

}
