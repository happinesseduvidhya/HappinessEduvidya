package com.happiness.eduvidhya.activities

import android.app.ActionBar
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.happiness.eduvidhya.R


class MainHome:AppCompatActivity() {


    private lateinit var create_meeting_cardview: CardView
    private lateinit var join_meeting_cardview: CardView
    private lateinit var attendance_cardview: CardView
    private lateinit var batch_class_cardview: CardView
    private lateinit var scheduled_meeting_card: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        create_meeting_cardview = findViewById(R.id.create_meeting_card)
        join_meeting_cardview = findViewById(R.id.join_meeting_card)
        attendance_cardview = findViewById(R.id.meetingCardView)
        batch_class_cardview = findViewById(R.id.classrooms_cardview)
        scheduled_meeting_card = findViewById(R.id.scheduled_meeting_card)

        create_meeting_cardview.setOnClickListener {
            val i = Intent(this@MainHome, ActivityBaseForFragment::class.java)
            i.putExtra("checkPage", "create")
            startActivity(i)
        }
        join_meeting_cardview.setOnClickListener {
            val i = Intent(this@MainHome, ActivityBaseForFragment::class.java)
            i.putExtra("checkPage", "join")
            startActivity(i)
        }
        attendance_cardview.setOnClickListener {
            val i = Intent(this@MainHome, ActivityBaseForFragment::class.java)
            i.putExtra("checkPage", "attendance")
            startActivity(i)
        }
        batch_class_cardview.setOnClickListener {
            val i = Intent(this@MainHome, ActivityBaseForFragment::class.java)
            i.putExtra("checkPage", "batch")
            startActivity(i)
        }
        scheduled_meeting_card.setOnClickListener {
            val i = Intent(this@MainHome, ActivityBaseForFragment::class.java)
            i.putExtra("checkPage", "batch")
            startActivity(i)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun getActionBar(): ActionBar? {
        return super.getActionBar()
    }
}
