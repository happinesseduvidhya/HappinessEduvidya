package com.happiness.eduvidhya.adapters

import android.R.attr.name
import android.R.id
import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.activities.ActivityMeetingAttendUsers
import com.happiness.eduvidhya.datamodels.ModelMeetingHistories
import com.happiness.eduvidhya.datamodels.UserOrFacultyInfo


class AdapterAllMeetingHistories(val context: Context, show_batches: ArrayList<ModelMeetingHistories>?, strHistoryOrAttendence:String) : RecyclerView.Adapter<AdapterAllMeetingHistories.ViewHolder>() {

    val db = FirebaseFirestore.getInstance()

    var mArray: ArrayList<ModelMeetingHistories>? = null

    var HistoryOrAttendence:String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_row_meetings_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val sourceString = "<b>" + "Meeting ID : " + "</b> " + mArray?.get(position)?.strMeetingID
        holder.meeting_id_txt.setText(Html.fromHtml(sourceString))

//        holder.meeting_id_txt.setText("Meeting ID : "+)

        if (mArray!!.get(position).strMeetingName.isEmpty())
        {
            holder.meeting_name_txt.setText("Meeting Name - No name found")
        }
        else{
            holder.meeting_name_txt.setText("Meeting Name - "+mArray!!.get(position).strMeetingName)
        }

        holder.meeting_time_txt.setText("Meeting End - "+mArray!!.get(position).strMeetingTime)

        if (mArray!!.get(position).strMeetingStatus.equals("3"))
        {
            holder.meeting_status_txt.setText("Meeting Status - Completed")
        }
        else{
            holder.meeting_status_txt.setText("Meeting Status - incomplete")
        }



        if (HistoryOrAttendence.equals("History"))
        {
            holder.moveForwardImg.visibility = View.INVISIBLE
        }
        else{
            holder.meeting_name_txt.visibility = View.GONE
            holder.meeting_status_txt.visibility = View.GONE
            holder.moveForwardImg.visibility = View.VISIBLE
        }

        holder.cardview_history_meeting.setOnClickListener {

            if (HistoryOrAttendence.equals("Attendence"))
            {
                val intent = Intent(it.context, ActivityMeetingAttendUsers::class.java)
                intent.putExtra("Email", mArray?.get(position)?.strMeetingID)
                it.context.startActivity(intent)
            }

        }
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

        val mySharedPreferences = context.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)

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
        this.HistoryOrAttendence = strHistoryOrAttendence
    }
}
