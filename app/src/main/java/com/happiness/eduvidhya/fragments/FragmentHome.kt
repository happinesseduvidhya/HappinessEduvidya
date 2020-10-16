package com.happiness.eduvidhya.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.activities.*
import com.happiness.eduvidhya.datamodels.ModelMeetingHistories
import com.happiness.eduvidhya.utils.Constant
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class FragmentHome : Fragment() {

    private lateinit var create_meeting_cardview: CardView
    private lateinit var join_meeting_cardview: CardView
    private lateinit var meetingCardView: CardView
    private lateinit var classrooms_cardview: CardView
    private lateinit var scoreboard_card: CardView
    private lateinit var add_students_cardview: CardView
    private lateinit var attendences_cardview: CardView

    private lateinit var scheduled_meeting_card: CardView
    private lateinit var logoutBtn: Button

    private lateinit var auth: FirebaseAuth
    val REQUEST_MICROPHONE = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.activity_home, container, false)

        val mySharedPreferences = activity?.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
        val type = mySharedPreferences?.getString("type", "")

        auth = FirebaseAuth.getInstance()
        create_meeting_cardview = v.findViewById(R.id.create_meeting_card)
        join_meeting_cardview = v.findViewById(R.id.join_meeting_card)
        meetingCardView = v.findViewById(R.id.meetingCardView)
        classrooms_cardview = v.findViewById(R.id.classrooms_cardview)
        scoreboard_card = v.findViewById(R.id.scoreboard_card)
        attendences_cardview = v.findViewById(R.id.attendences_cardview)

        scheduled_meeting_card = v.findViewById(R.id.scheduled_meeting_card)
        add_students_cardview = v.findViewById(R.id.add_students_cardview)

        logoutBtn = v.findViewById(R.id.logoutBtn)

        if ((getActivity()?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.RECORD_AUDIO)
            } !== PackageManager.PERMISSION_GRANTED))
        {
            getActivity()?.let {

                ActivityCompat.requestPermissions(
                    it,
                    arrayOf<String>(Manifest.permission.RECORD_AUDIO),
                    REQUEST_MICROPHONE)
            }
        }

        create_meeting_cardview.setOnClickListener {

            if (type.equals("User")) {
                Constant.showMessage(it, "You are user, you cannot create meetings")
            } else {
                val i = Intent(activity, ActivityBaseForFragment::class.java)
                i.putExtra("checkPage", "create")
                startActivity(i)
            }

        }

        attendences_cardview.setOnClickListener {

            val i = Intent(activity, ActivityMeetingsHistoryAndAttendence::class.java)
            startActivity(i)
        }

        scoreboard_card.setOnClickListener {
            Constant.showMessage(it, "No story board found")
        }

        join_meeting_cardview.setOnClickListener {

            if (type.equals("User")) {
                Constant.showMessage(it, "you can not join, please join from class")
            } else {
                val i = Intent(activity, ActivityBaseForFragment::class.java)
                i.putExtra("checkPage", "join")
                startActivity(i)
            }


        }
        meetingCardView.setOnClickListener {

            if (type.equals("User")) {

                val i = Intent(activity, ActivityMeetingsFaculties::class.java)
                startActivity(i)

            } else {
                Constant.showMessage(it, "no meetings available")
            }
        }

        classrooms_cardview.setOnClickListener {

            if (type.equals("User")) {
                Constant.showMessage(it, "You are user, you cannot create class")
            } else {
                val i = Intent(activity, ActivityBaseForFragment::class.java)
                i.putExtra("checkPage", "createRoom")
                startActivity(i)
            }
        }
        scheduled_meeting_card.setOnClickListener {

            if (type.equals("User")) {
                Constant.showMessage(it, "you can not see scheduled meetings")
            } else {
                val i = Intent(activity, ActivityBaseForFragment::class.java)
                i.putExtra("checkPage", "all_scheduled_meetings")
                startActivity(i)
            }
        }

        logoutBtn.setOnClickListener {

            val intent = Intent(activity, ActivityStart::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        add_students_cardview.setOnClickListener {

            if (type.equals("User")) {
                Constant.showMessage(it, "You are user, you cannot add users")
            } else {
                val intent = Intent(activity, AddStudentByFaculty::class.java)
                startActivity(intent)
            }
        }


        if (type.equals("User"))
        {
            attendences_cardview.visibility = View.GONE
        }
        else if (type.equals("Faculty")) {
            attendences_cardview.visibility = View.VISIBLE
        }


        return v
    }
}