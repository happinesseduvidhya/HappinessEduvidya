package com.happiness.eduvidhya.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.happiness.eduvidhya.R
import kotlinx.android.synthetic.main.activity_type_batches_or_meetings.*

class ActivityTypeBatchesOrMeetings : AppCompatActivity() {

    var className:String ?= null

    // top bar
    lateinit var back_top_bar_img: ImageView
    lateinit var title_top_bar_txt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type_batches_or_meetings)

        className = intent.getStringExtra("classroom_name")


        back_top_bar_img = findViewById(R.id.back_top_bar_img)
        title_top_bar_txt = findViewById(R.id.title_top_bar_txt)
        title_top_bar_txt.setText("Manages")
        back_top_bar_img.setOnClickListener {
            finish()
        }

        mangesMeetingBtn.setOnClickListener {
            val i = Intent(applicationContext, ActivityAllMeetingsFacultyCanStart::class.java)
            i.putExtra("classroom_name", className)
            startActivity(i)
        }

        managesBatchesBtn.setOnClickListener {

            val i = Intent(applicationContext, ActivityBaseForFragment::class.java)
            i.putExtra("checkPage", "list_of_batches")
            i.putExtra("classroom_name", className)
            startActivity(i)

        }


    }
}