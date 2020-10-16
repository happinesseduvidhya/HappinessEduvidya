package com.happiness.eduvidhya.fragments


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
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


class BatchListRecyclerAdapter(val context: Activity, myList: ArrayList<BatchListDescriptionDataModel>?, listner: RemoveClickListener,classroom_name:String, subject_name:String,topic_name:String
) :
    RecyclerView.Adapter<BatchListRecyclerAdapter.ViewHolder>() {
    private var myList: ArrayList<BatchListDescriptionDataModel>?
    var mLastPosition = 0
    var subjectName:String? =null
    var topicName:String? =null
    var classroom_name:String? =null
    var position: SendPosition? = null
    private val mListner: RemoveClickListener
    var create_classroom : DocumentReference?=null
    val db = FirebaseFirestore.getInstance()
    val classes = db.collection("Classes")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.create_batch_list_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val mySharedPreferences =context.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
        val email = mySharedPreferences.getString("user_email", "")

        val batch_detail =
            BatchDeatailModel("", "", "")

//        create_classroom = classes.document(email!!).collection("Batchers").document("8th")



        classes.document(email!!).collection("classrooms").document(classroom_name.toString()).collection("Batches").document("Batch"+(position+1)).set(batch_detail).addOnSuccessListener {
            Toast.makeText(context, "batch created", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
        val pos=position+1
        holder.etTitleTextView.setText(subjectName+" : "+topicName+" - "+"Batch "+pos)
//        holder.etDescriptionTextView.setText(myList!![position].getDescription())
//        holder.crossImage.setImageResource(R.drawable.cross)
        mLastPosition = position

        holder.etTitleTextView.setOnClickListener {
//                Toast.makeText(
//                    itemView.context,"Position:" + Integer.toString(position),Toast.LENGTH_SHORT).show()
            val i = Intent(context, ActivityBaseForFragment::class.java)
            i.putExtra("checkPage", "batch_details")
            i.putExtra("subjectname",subjectName)
            i.putExtra("topicname","createBatch")
            i.putExtra("classname",classroom_name)
            i.putExtra("position", (position+1).toString())

            context.startActivity(i)
        }

    }

    override fun getItemCount(): Int {
        return if (null != myList) myList!!.size else 0
    }

    fun notifyData(myList: ArrayList<BatchListDescriptionDataModel>) {
        Log.d("notifyData ", myList.size.toString() + "")
        this.myList = myList
        notifyDataSetChanged()
    }

    inner class ViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {
        val etTitleTextView: TextView
        val crossImage: ImageView

        init {

            etTitleTextView = parent.findViewById(R.id.new_batch_item) as TextView
            crossImage = parent.findViewById(R.id.crossImage) as ImageView

            crossImage.setOnClickListener(View.OnClickListener {
                mListner.OnRemoveClick(
                    adapterPosition
                )
            })
        }
    }
    init {
        this.myList = myList
        this.subjectName = subject_name
        this.topicName = topic_name
        this.classroom_name = classroom_name
        mListner = listner
    }
}
