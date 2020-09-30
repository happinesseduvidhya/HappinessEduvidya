package com.happiness.eduvidhya.fragments

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.happiness.eduvidhya.Interface.RemoveClickListener
import com.happiness.eduvidhya.Interface.SendPosition
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.activities.ActivityBaseForFragment
import com.happiness.eduvidhya.datamodels.BatchListDescriptionDataModel
import com.happiness.eduvidhya.datamodels.StudentEmail

class AddStudentEmailAdapter(val context: Activity, myStudentList: ArrayList<StudentEmail>?, listner: RemoveClickListener
) :
    RecyclerView.Adapter<AddStudentEmailAdapter.ViewHolder>() {
    private var myList: ArrayList<StudentEmail>?
    var mLastPosition = 0
    var position: SendPosition? = null
    private val mListner: RemoveClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.add_student_list_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("onBindViewHoler ", myList!!.size.toString() + "")
        holder.etTitleTextView.setText(myList!!.get(position).Email);
//        holder.etDescriptionTextView.setText(myList!![position].getDescription())
//        holder.crossImage.setImageResource(R.drawable.cross)
        mLastPosition = position
    }

    override fun getItemCount(): Int {
        return if (null != myList) myList!!.size else 0
    }

    fun notifyData(myList: ArrayList<StudentEmail>) {
        Log.d("notifyData ", myList.size.toString() + "")
        this.myList = myList
        notifyDataSetChanged()
    }

    inner class ViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {
        val etTitleTextView: TextView
        val crossImage: ImageView

        init {
            etTitleTextView = parent.findViewById(R.id.new_email_item) as TextView
            crossImage = parent.findViewById(R.id.crossImage) as ImageView
//            etTitleTextView.setOnClickListener {
////                Toast.makeText(
////                    itemView.context,"Position:" + Integer.toString(position),Toast.LENGTH_SHORT).show()
//                val i = Intent(context, ActivityBaseForFragment::class.java)
//                i.putExtra("checkPage", "join")
//                context.startActivity(i)
//            }

            crossImage.setOnClickListener(View.OnClickListener {
                mListner.OnRemoveClick(
                    adapterPosition
                )
            })
        }
    }
    init {
        this.myList = myStudentList
        mListner = listner
    }


}
