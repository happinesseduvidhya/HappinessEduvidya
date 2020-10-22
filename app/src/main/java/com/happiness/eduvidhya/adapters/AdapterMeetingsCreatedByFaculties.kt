package com.happiness.eduvidhya.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.happiness.eduvidhya.Interface.CallBackFirst
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.activities.ActivityAllMeetingsSeeByUser
import com.happiness.eduvidhya.activities.ActivityBaseForFragment
import com.happiness.eduvidhya.activities.ActivityUsersAllClasses
import com.happiness.eduvidhya.datamodels.ModelMeetings
import com.happiness.eduvidhya.datamodels.UserOrFacultyInfo

class AdapterMeetingsCreatedByFaculties(
    val context: Context,
    show_batches: ArrayList<ModelMeetings>?,
    strFacultyEmail: String, strClassNameComeFromClasses: String
) : RecyclerView.Adapter<AdapterMeetingsCreatedByFaculties.ViewHolder>() {

    val db = FirebaseFirestore.getInstance()
    var strFacultyEmail: String? = ""
    var ClassNameComeFromClasses: String? = ""
    var mArray: ArrayList<ModelMeetings>? = null

    var callBackFirst: CallBackFirst? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_row_all_meetings, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


//        val mySharedPreferences = context.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
//        val email = mySharedPreferences.getString("user_email", "")
//        var check = teacher_collection.document(email!!).collection("classrooms")
//            .document(mArray?.get(position)?.classroom_name.toString())
//            .collection("Batches")

        holder.meetingNameTxt.setText(mArray?.get(position)?.meetingName)
        holder.meetingDateTxt.setText(mArray?.get(position)?.meetingDate)
        holder.meetingTimeTxt.setText(mArray?.get(position)?.meetingTime)
        holder.meetingIdTxt.setText(mArray?.get(position)?.meetingID)


        if (mArray?.get(position)?.meetingStatus.toString().equals("0"))
        {
            holder.nextBtn.visibility = View.VISIBLE
            holder.nextBtn.setText("Start Meeting")
            holder.meetingStatusTxt.setTextColor(ContextCompat.getColor(context,R.color.color_red))
            holder.meetingStatusTxt.setText("Not started")

        }
        if (mArray?.get(position)?.meetingStatus.toString().equals("1"))
        {
            holder.nextBtn.visibility = View.VISIBLE
            holder.meetingStatusTxt.setTextColor(ContextCompat.getColor(context,R.color.color_green))
            holder.meetingStatusTxt.setText("Running")
        }
        if (mArray?.get(position)?.meetingStatus.toString().equals("2"))
        {
            holder.nextBtn.visibility = View.GONE
            holder.meetingStatusTxt.setText("Completed")
        }


        holder.nextBtn.setOnClickListener {

            val i = Intent(it.context, ActivityBaseForFragment::class.java)
            i.putExtra("checkPage", "join")
            i.putExtra("subjectname", mArray?.get(position)?.meetingID)
            i.putExtra("topicname", strFacultyEmail)
            i.putExtra("classname", ClassNameComeFromClasses)
            it.context.startActivity(i)
        }

    }

    override fun getItemCount(): Int {
        return if (null != mArray) mArray!!.size else 0
    }


    inner class ViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {

        val meetingNameTxt: TextView
        val meetingDateTxt: TextView
        val meetingTimeTxt: TextView
        val meetingIdTxt: TextView
        val meetingStatusTxt: TextView
        val nextBtn: Button

        init {
            meetingNameTxt = parent.findViewById(R.id.meetingNameTxt) as TextView
            meetingDateTxt = parent.findViewById(R.id.meetingDateTxt) as TextView
            meetingTimeTxt = parent.findViewById(R.id.meetingTimeTxt) as TextView
            meetingIdTxt = parent.findViewById(R.id.meetingIdTxt) as TextView
            meetingStatusTxt = parent.findViewById(R.id.meetingStatusTxt) as TextView
            nextBtn = parent.findViewById(R.id.nextBtn) as Button

        }
    }


    init {
        this.mArray = show_batches
        this.strFacultyEmail = strFacultyEmail
        this.ClassNameComeFromClasses = strClassNameComeFromClasses
    }
}
