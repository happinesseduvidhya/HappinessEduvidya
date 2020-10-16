package com.happiness.eduvidhya.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.fragments.*

class ActivityBaseForFragment: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_fragment)

        if (savedInstanceState == null) {
            val transcation = supportFragmentManager.beginTransaction()
            transcation.isAddToBackStackAllowed

            val checkPage = intent.extras!!.getString("checkPage")

            val subject=  intent.getStringExtra("subjectname")
            val topic= intent.getStringExtra("topicname")
            val position= intent.getStringExtra("position")
            val classname= intent.getStringExtra("classname")
            val get_class_name= intent.getStringExtra("classroom_name")


            val bundle = Bundle()
            bundle.putString("subject_name", subject)
            bundle.putString("topic_name", topic)
            bundle.putString("position", position)
            bundle.putString("classname", classname)
            bundle.putString("get_class_name", get_class_name)


            if (checkPage!!.equals("create")) {
                val fragmentCreate = FragmentCreateMeetingByFaculty()
                transcation.replace(R.id.frame_layout_base_fragment, fragmentCreate)
                transcation.commit()
            }
            if (checkPage.equals("join")) {
                val fragmentJoin = FragmentJoinMeeting()
                fragmentJoin.setArguments(bundle);
                transcation.replace(R.id.frame_layout_base_fragment, fragmentJoin)
                transcation.commit()
            }
            if (checkPage.equals("attendance")) {
                val fragmentAttendance = FragmentAttendance()
                transcation.replace(R.id.frame_layout_base_fragment, fragmentAttendance)
                transcation.commit()
            }
            if (checkPage.equals("createRoom")) {
                val fragmentBatchClass = FragmentSmallClassroom()
                transcation.replace(R.id.frame_layout_base_fragment, fragmentBatchClass)
                transcation.commit()
            }
            if (checkPage.equals("createClassroom")) {
                val fragmentCreateBatch = FragmentCreateClassRoom()
                transcation.replace(R.id.frame_layout_base_fragment, fragmentCreateBatch)
                transcation.commit()
            }
            if (checkPage.equals("batch_details")) {
                val fragmenBatchDetails = FragmentAddBatchDetails()
                fragmenBatchDetails.setArguments(bundle)
                transcation.replace(R.id.frame_layout_base_fragment, fragmenBatchDetails)
                transcation.commit()
            }
            if (checkPage.equals("all_scheduled_meetings")) {
                val fragmentScheduledMeetings = FragmentScheduleMeeting()
                fragmentScheduledMeetings.setArguments(bundle);
                transcation.replace(R.id.frame_layout_base_fragment, fragmentScheduledMeetings)
                transcation.commit()
            }
            if (checkPage.equals("list_of_batches")) {
                val fragment_show_batches = FragementShowBatches()
                fragment_show_batches.setArguments(bundle);
                transcation.replace(R.id.frame_layout_base_fragment, fragment_show_batches)
                transcation.commit()
            }
        }

    }
}