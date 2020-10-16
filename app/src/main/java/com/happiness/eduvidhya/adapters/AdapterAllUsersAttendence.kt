package com.happiness.eduvidhya.adapters

import android.content.Context
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
import com.happiness.eduvidhya.datamodels.UserOrFacultyInfo

class AdapterAllUsersAttendence(val context: Context, show_batches: ArrayList<UserOrFacultyInfo>?) : RecyclerView.Adapter<AdapterAllUsersAttendence.ViewHolder>() {

    val db = FirebaseFirestore.getInstance()

    var mArray: ArrayList<UserOrFacultyInfo>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_row_meetings_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val present = "Status : "+"This is <font color='red'>Present</font>"

        holder.meeting_id_txt.setText("User email : "+mArray?.get(position)?.strEmail)
        holder.meeting_name_txt.setText(Html.fromHtml(present))


        holder.meeting_time_txt.visibility = View.GONE
        holder.meeting_status_txt.visibility = View.GONE
        holder.moveForwardImg .visibility = View.GONE




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

    }
}
