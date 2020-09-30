package com.happiness.eduvidhya.adapters


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.happiness.eduvidhya.Interface.RemoveClickListener
import com.happiness.eduvidhya.Interface.SendPosition
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.activities.ActivityBaseForFragment
import com.happiness.eduvidhya.datamodels.BatchDeatailModel
import com.happiness.eduvidhya.datamodels.BatchListDescriptionDataModel
import com.happiness.eduvidhya.datamodels.ClassroomDetailsModel


class AdapterAllScheduledMeetings(val context: Activity, mArrayBatchesWithMeeting: ArrayList<ClassroomDetailsModel>?) : RecyclerView.Adapter<AdapterAllScheduledMeetings.ViewHolder>() {
    var mLastPosition = 0
    var subjectName: String? = null
    var topicName: String? = null
    var classroom_name: String? = null
    var position: SendPosition? = null
    var create_classroom: DocumentReference? = null
    val db = FirebaseFirestore.getInstance()
    val teacher_collection = db.collection("teachers")
    var mArray: ArrayList<ClassroomDetailsModel>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.for_scheduled_meetings_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mySharedPreferences =
            context.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
        val email = mySharedPreferences.getString("user_email", "")
        var check = teacher_collection.document(email!!).collection("classrooms")
            .document(mArray?.get(position)?.classroom_name.toString())
            .collection("Batches")

        holder.etTitleTextView.setText(mArray?.get(position)?.classroom_name)
        holder.card_view.setOnClickListener {
            val i = Intent(context, ActivityBaseForFragment::class.java)
            i.putExtra("checkPage", "list_of_batches")
            i.putExtra("classroom_name",mArray?.get(position)?.classroom_name.toString())
           context. startActivity(i)
                    }

        }


    override fun getItemCount(): Int {
        return if (null != mArray) mArray!!.size else 0
    }

    inner class ViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {
        val etTitleTextView: TextView
        val crossImage: ImageView
        val card_view: CardView

        val mySharedPreferences =context.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
        val email = mySharedPreferences.getString("user_email", "")


        init {
            etTitleTextView = parent.findViewById(R.id.new_batch_item) as TextView
            crossImage = parent.findViewById(R.id.crossImage) as ImageView
            card_view = parent.findViewById(R.id.card_view) as CardView
    }
    }

    init {
        this.mArray = mArrayBatchesWithMeeting
    }
}
