package com.happiness.eduvidhya.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.activities.ActivityBaseForFragment
import com.happiness.eduvidhya.activities.ActivityStart
import com.happiness.eduvidhya.utils.Constant
import kotlinx.android.synthetic.main.activity_home.*

class FragmentHome : Fragment() {

    private lateinit var create_meeting_cardview: CardView
    private lateinit var join_meeting_cardview: CardView
    private lateinit var attendance_cardview: CardView
    private lateinit var batch_class_cardview: CardView

    private lateinit var scheduled_meeting_card: CardView
    private lateinit var logoutBtn: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.activity_home, container, false)

        val mySharedPreferences = activity?.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
        val type = mySharedPreferences?.getString("type", "")



        auth = FirebaseAuth.getInstance()
        create_meeting_cardview = v.findViewById(R.id.create_meeting_card)
        join_meeting_cardview = v.findViewById(R.id.join_meeting_card)
        attendance_cardview = v.findViewById(R.id.Attendance_card)
        batch_class_cardview = v.findViewById(R.id.batch_class_card)
        scheduled_meeting_card = v.findViewById(R.id.scheduled_meeting_card)
        logoutBtn = v.findViewById(R.id.logoutBtn)
        create_meeting_cardview.setOnClickListener {
            if (type.equals("Student"))
            {
                Constant.showMessage(it,"You are user, you cannot create meetings")
            }
            else{
                val i = Intent(activity, ActivityBaseForFragment::class.java)
                i.putExtra("checkPage", "create")
                startActivity(i)
            }

        }



        join_meeting_cardview.setOnClickListener {
            val i = Intent(activity, ActivityBaseForFragment::class.java)
            i.putExtra("checkPage", "join")
            startActivity(i)
        }
        attendance_cardview.setOnClickListener {
            val i = Intent(activity, ActivityBaseForFragment::class.java)
            i.putExtra("checkPage", "attendance")
            startActivity(i)
        }
        batch_class_cardview.setOnClickListener {
            if (type.equals("Student"))
            {
                Constant.showMessage(it,"You are user, you cannot create class")
            }
            else{
                val i = Intent(activity, ActivityBaseForFragment::class.java)
                i.putExtra("checkPage", "batch")
                startActivity(i)
            }

        }
        scheduled_meeting_card.setOnClickListener {


            val i = Intent(activity, ActivityBaseForFragment::class.java)
            i.putExtra("checkPage", "all_scheduled_meetings")
            startActivity(i)
        }

        logoutBtn.setOnClickListener {

            if (FirebaseAuth.getInstance() != null) {

                val c=auth.currentUser
                if(c!=null)
                {
                    auth.signOut()
                    startActivity(Intent(activity, ActivityStart::class.java))
                }
                Constant.myauth=null
            } else {
                Toast.makeText(activity,"problem", Toast.LENGTH_SHORT).show()
            }
        }

        return v
    }
}