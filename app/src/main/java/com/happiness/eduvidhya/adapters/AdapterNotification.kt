package com.happiness.eduvidhya.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.happiness.eduvidhya.Interface.CallBackFirst
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.activities.ActivityAllMeetingsSeeByUser
import com.happiness.eduvidhya.activities.ActivityUsersAllClasses
import com.happiness.eduvidhya.datamodels.UserOrFacultyInfo
import com.happiness.eduvidhya.datamodels.notificationModel

class AdapterNotification(
    val context: Context,
    show_batches: ArrayList<notificationModel>?,
    strTypeFacultyOrClasses: String,
    emailBackSide: String
) : RecyclerView.Adapter<AdapterNotification.ViewHolder>() {

    val db = FirebaseFirestore.getInstance()
    var strFacultyOrClasses: String? = ""
    var strFacultyEmail: String? = ""
    var mArray: ArrayList<notificationModel>? = null

    var callBackFirst: CallBackFirst? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_row_meetings_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.meeting_id_txt.setText("Faculty Email : "+mArray?.get(position)?.facultyEmail)
        holder.meeting_name_txt.setText("Meeting Time : "+mArray?.get(position)?.meetingTime)
        holder.meeting_time_txt.setText("Meeting Status : Not started")

    }

    override fun getItemCount(): Int {
        return if (null != mArray) mArray!!.size else 0
    }


    inner class ViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {

        val meeting_id_txt: TextView
        val meeting_name_txt: TextView
        val meeting_time_txt: TextView
        val meeting_status_txt: TextView
        val moveForwardImg: ImageView
        val cardview_history_meeting: CardView

        init {
            meeting_id_txt = parent.findViewById(R.id.meeting_id_txt) as TextView
            meeting_name_txt = parent.findViewById(R.id.meeting_name_txt) as TextView
            meeting_time_txt = parent.findViewById(R.id.meeting_time_txt) as TextView
            meeting_status_txt = parent.findViewById(R.id.meeting_status_txt) as TextView
            moveForwardImg = parent.findViewById(R.id.moveForwardImg) as ImageView
            cardview_history_meeting = parent.findViewById(R.id.cardview_history_meeting) as CardView
        }
    }


    init {
        this.mArray = show_batches
        this.strFacultyOrClasses = strTypeFacultyOrClasses
        this.strFacultyEmail = emailBackSide
    }
}
