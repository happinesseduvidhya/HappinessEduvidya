package com.happiness.eduvidhya.fragments

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.RetrofitConnection.ApiCllientXml
import com.happiness.eduvidhya.datamodels.response
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog
import org.apache.commons.codec.digest.DigestUtils
import retrofit2.Call
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*


class FragmentCreateMeeting : Fragment() {

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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_create_meeting, container, false)


        enter_meeting_name = v.findViewById(R.id.enter_meeting_name)
        enter_meeting_date = v.findViewById(R.id.enter_meeting_date)
        enter_meeting_time = v.findViewById(R.id.enter_meeting_time)
        create_meeting_btn = v.findViewById(R.id.create_meeting_btn)
        display_code_text = v.findViewById(R.id.display_code_text)
        display_code_value = v.findViewById(R.id.display_code_value)
        display_name_text = v.findViewById(R.id.display_name_text)
        display_name_value = v.findViewById(R.id.display_name_value)
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
                createMeeting(enter_meeting_name.text.toString())
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
            DatePickerDialog(
                requireActivity(), date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }
        enter_meeting_time.setOnClickListener {
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val myFormat = "hh:mm a" //In which you need put here
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
        return v
    }

    private fun createMeeting(meeting_name: String) {
        val ran = Random()
        val code = (100000 + ran.nextInt(900000)).toString()
        val meeting_id = "random-" + code.toString()
        val method = "create"
        val parameters =
            "allowStartStopRecording=true&attendeePW=11&autoStartRecording=false&meetingID=" + meeting_id + "&moderatorPW=22&name=" + meeting_name + "&record=true&welcome=Welcometo"
        val checksum = DigestUtils.shaHex(method + parameters + Constant.shared_secret)
        if (Constant.hasNetworkAvailable(requireActivity())) {
            updatedProgressDilaog.show(requireActivity())

            try {
                val call: Call<response> = ApiCllientXml.getClientXml.CreateMeeting(
                    true,
                    "11", false, meeting_id, "22", meeting_name
                    , true, "Welcometo", checksum
                )
                call.enqueue(object : Callback<response> {
                    override fun onFailure(call: Call<response>, t: Throwable) {
                        updatedProgressDilaog.dialog.dismiss()
                        Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<response>,
                        response: retrofit2.Response<response>
                    ) {
                        updatedProgressDilaog.dialog.dismiss()
                        var dataValue: response? = null

                        dataValue = response.body()

                        if (dataValue == null) {

                            Toast.makeText(activity, "No data found", Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(
                                activity,
                                "Successfully Created Meeting",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("status", dataValue.returncode + dataValue.meetingID.toString())
                            meeting_id_from_api = dataValue.meetingID.toString()
                            display_code_text.visibility = View.VISIBLE
                            display_code_value.visibility = View.VISIBLE
                            display_name_text.visibility = View.VISIBLE
                            display_name_value.visibility = View.VISIBLE
                            share_btn.visibility = View.VISIBLE
                            display_code_value.setText(dataValue.meetingID.toString())
                            display_name_value.setText(meeting_name.toString())

                        }
                    }
                })
            } catch (ex: java.lang.Exception) {
                updatedProgressDilaog.dialog.dismiss()
                // updatedProgressDilaog.dialog.dismiss()
                // Toast.makeText(activity, ex.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e("catch_message", ex.message.toString())
            }

        } else {
            Toast.makeText(activity, "No network available!", Toast.LENGTH_SHORT).show()
        }
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

