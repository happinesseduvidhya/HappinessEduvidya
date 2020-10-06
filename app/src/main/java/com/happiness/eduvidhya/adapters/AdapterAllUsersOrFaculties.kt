package com.happiness.eduvidhya.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.activities.ActivityAdminControlClassesAndOthers
import com.happiness.eduvidhya.activities.ActivityBaseForFragment
import com.happiness.eduvidhya.datamodels.BatchDeatailModel
import com.happiness.eduvidhya.datamodels.ListOfBatchesModel
import com.happiness.eduvidhya.utils.Constant

class AdapterAllUsersOrFaculties(val context: Context, show_batches: ArrayList<ListOfBatchesModel>?, classNameBack: String?) : RecyclerView.Adapter<AdapterAllUsersOrFaculties.ViewHolder>() {

    var create_classroom: DocumentReference? = null
    val db = FirebaseFirestore.getInstance()
    var classname: String? = ""
    val teacher_collection = db.collection("teachers")
    var mArray: ArrayList<ListOfBatchesModel>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_all_users_or_faculties, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


//        val mySharedPreferences = context.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
//        val email = mySharedPreferences.getString("user_email", "")
//        var check = teacher_collection.document(email!!).collection("classrooms")
//            .document(mArray?.get(position)?.classroom_name.toString())
//            .collection("Batches")

        holder.etTitleTextView.setText(mArray?.get(position)?.batches_name)


        holder.card_view.setOnClickListener {

            val intent = Intent(it.context, ActivityAdminControlClassesAndOthers::class.java)
            intent.putExtra("Email",mArray?.get(position)?.batches_name.toString())
            it.context.startActivity(intent)

        }


    }

    override fun getItemCount(): Int {
        return if (null != mArray) mArray!!.size else 0
    }


    inner class ViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {
        val etTitleTextView: TextView
        val card_view: CardView
        val mySharedPreferences = context.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
        val email = mySharedPreferences.getString("user_email", "")
        init {
            etTitleTextView = parent.findViewById(R.id.new_batch_item) as TextView
            card_view = parent.findViewById(R.id.card_view) as CardView
        }
    }


    init {
        this.mArray = show_batches
        this.classname = classNameBack
    }
}
