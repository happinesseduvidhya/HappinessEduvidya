package com.happiness.eduvidhya.fragments

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.activities.ActivityBaseForFragment
import com.happiness.eduvidhya.adapters.AdapterAllClassesUser
import com.happiness.eduvidhya.adapters.AdapterNotification
import com.happiness.eduvidhya.datamodels.notificationModel
import kotlinx.android.synthetic.main.activity_users.*


class FragmentNotification : Fragment() {


    // top bar
    lateinit var back_top_bar_img: ImageView
    lateinit var title_top_bar_txt: TextView


    val db = FirebaseFirestore.getInstance()
    val users_collection = db.collection("Users")
    val notifications_collection = db.collection("Notifications")
    var mArraysFacultiesEmail:ArrayList<String> ?= null
    var mArraysNotifications:ArrayList<notificationModel> ?= null

    private var mRecyclerAdapter: AdapterNotification? = null
    private var all_users_recyclerview: RecyclerView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_notification, container, false)

        all_users_recyclerview = view.findViewById(R.id.all_users_recyclerview)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        all_users_recyclerview!!.setLayoutManager(layoutManager)

        val mySharedPreferences = requireActivity().getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
        val user_email = mySharedPreferences.getString("user_email", "")

        back_top_bar_img = view.findViewById(R.id.back_top_bar_img)
        title_top_bar_txt = view.findViewById(R.id.title_top_bar_txt)
        title_top_bar_txt.setText("All Notifications")
        back_top_bar_img.setOnClickListener {
            requireActivity().finish()
        }


        mArraysFacultiesEmail = null
        mArraysNotifications = null
        mArraysFacultiesEmail = ArrayList()
        mArraysNotifications = ArrayList()


        users_collection.document(user_email.toString()).collection("Faculties").addSnapshotListener(object :
            EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                for (data in value!!.documents) {
                    if (data.exists()) {
                        val facultyIDEmail = data.id
                        mArraysFacultiesEmail!!.add(facultyIDEmail)

                    }
                }
                checkfun()

            }

        })

        return view
    }

    private fun checkfun()
    {
        notifications_collection.addSnapshotListener(object :
            EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?
            ) {
                for (data in value!!.documents) {

                    if (data.exists()) {

                        val emailInfo = data.get("facultyEmail")

                        for (dataFirstLoop in 0 until  mArraysFacultiesEmail!!.size)
                        {
                            val facultyIDEmail = mArraysFacultiesEmail!!.get(dataFirstLoop)
                            if (facultyIDEmail.equals(emailInfo))
                            {
                                val facultyEmail = data.get("facultyEmail")
                                val facultyMeeting = data.get("facultyMeeting")
                                val facultyTime = data.get("meetingTime")
                                val notification_status = data.get("notification_status")
                                val notificationModel = notificationModel(facultyEmail.toString(),facultyMeeting.toString(),facultyTime.toString(),notification_status.toString())
                                mArraysNotifications!!.add(notificationModel)
                            }
                        }

                        mRecyclerAdapter = AdapterNotification(requireContext(),mArraysNotifications,"faculty","")
                        all_users_recyclerview!!.adapter = mRecyclerAdapter

                    }
                }
            }

        })
    }
}