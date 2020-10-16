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

class AdapterAllClassesUser(
    val context: Context,
    show_batches: ArrayList<UserOrFacultyInfo>?,
    strTypeFacultyOrClasses: String,
    emailBackSide: String
) : RecyclerView.Adapter<AdapterAllClassesUser.ViewHolder>() {

    val db = FirebaseFirestore.getInstance()
    var strFacultyOrClasses: String? = ""
    var strFacultyEmail: String? = ""
    var mArray: ArrayList<UserOrFacultyInfo>? = null

    var callBackFirst: CallBackFirst? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_all_users_or_faculties, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


//        val mySharedPreferences = context.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
//        val email = mySharedPreferences.getString("user_email", "")
//        var check = teacher_collection.document(email!!).collection("classrooms")
//            .document(mArray?.get(position)?.classroom_name.toString())
//            .collection("Batches")

        holder.etTitleTextView.setText(mArray?.get(position)?.strEmail)


        holder.etTitleTextView.setOnClickListener {

            if (strFacultyOrClasses.equals("faculty")) {

                val intent = Intent(it.context, ActivityUsersAllClasses::class.java)
                intent.putExtra("Email", mArray?.get(position)?.strEmail.toString())
                it.context.startActivity(intent)
            }
            else if (strFacultyOrClasses.equals("AllUsersClasses")) {
                val intent = Intent(it.context, ActivityAllMeetingsSeeByUser::class.java)
                intent.putExtra("Email", strFacultyEmail)
                intent.putExtra("ClassName", mArray?.get(position)?.strEmail.toString())
                it.context.startActivity(intent)
            }

        }


    }

    override fun getItemCount(): Int {
        return if (null != mArray) mArray!!.size else 0
    }


    inner class ViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {

        val etTitleTextView: TextView
        val card_view: CardView
        val edit_img: ImageView
        val mDeleteImg: ImageView
        val mySharedPreferences =
            context.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
        val email = mySharedPreferences.getString("user_email", "")

        init {
            etTitleTextView = parent.findViewById(R.id.new_batch_item) as TextView
            card_view = parent.findViewById(R.id.card_view) as CardView
            edit_img = parent.findViewById(R.id.edit_img) as ImageView
            mDeleteImg = parent.findViewById(R.id.mDeleteImg) as ImageView
        }
    }


    init {
        this.mArray = show_batches
        this.strFacultyOrClasses = strTypeFacultyOrClasses
        this.strFacultyEmail = emailBackSide
    }
}
