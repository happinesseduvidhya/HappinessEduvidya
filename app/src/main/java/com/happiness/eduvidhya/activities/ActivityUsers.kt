package com.happiness.eduvidhya.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.adapters.AdapterAllUsersOrFaculties
import com.happiness.eduvidhya.adapters.AdapterShowBatches
import com.happiness.eduvidhya.datamodels.ListOfBatchesModel
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog
import kotlinx.android.synthetic.main.activity_users.*

class ActivityUsers : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    val teacher_collection = db.collection("teachers")
    val student_collection = db.collection("student")
    // top bar
    lateinit var back_top_bar_img: ImageView
    lateinit var title_top_bar_txt: TextView

    var updatedProgressDilaog = CustomProgressDialog()

    var detail_db: ListOfBatchesModel? = null
    var list_of_batches: ArrayList<ListOfBatchesModel>? = null
    private var mRecyclerAdapter: AdapterAllUsersOrFaculties? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        back_top_bar_img = findViewById(R.id.back_top_bar_img)
        title_top_bar_txt= findViewById(R.id.title_top_bar_txt)

        list_of_batches = ArrayList<ListOfBatchesModel>()

        val layoutManager = LinearLayoutManager(this@ActivityUsers)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        all_users_recyclerview.setLayoutManager(layoutManager)

        val type = intent.getStringExtra("Type")

        if (type!!.equals("Users"))
        {
            title_top_bar_txt.setText("All Users")

            mUsersAll()
        }
        if (type.equals("Faculties"))
        {
            title_top_bar_txt.setText("All Faculties")

            mFaculties()
        }






        back_top_bar_img.setOnClickListener {
            finish()
        }

    }

    fun mUsersAll()
    {
        updatedProgressDilaog.show(this)
        if (Constant.hasNetworkAvailable(applicationContext)) {

            student_collection.get().addOnCompleteListener(object:OnCompleteListener<QuerySnapshot> {
                override fun onComplete(@NonNull task:Task<QuerySnapshot>) {

                    updatedProgressDilaog.dialog.dismiss()
                    if (task.isSuccessful())
                    {

                        val list = ArrayList<String>()
                        for (document in task.result!!)
                        {
                            list.add(document.getId())
                            detail_db = ListOfBatchesModel(document.id)
                            list_of_batches!!.add(detail_db!!)
                        }
                        mRecyclerAdapter = AdapterAllUsersOrFaculties(this@ActivityUsers,list_of_batches,"")
                        all_users_recyclerview.adapter = mRecyclerAdapter
                    }
                    else
                    {
                        Log.d("TAG", "Error getting documents: ", task.getException())
                    }
                }
            }).addOnFailureListener { exception ->
                updatedProgressDilaog.dialog.dismiss()
                Log.w("TAG", "Error getting documents: ", exception)
            }
        }else {
            Toast.makeText(applicationContext, "No network available!", Toast.LENGTH_SHORT).show()
        }
    }

    fun mFaculties()
    {
        updatedProgressDilaog.show(this)
        if (Constant.hasNetworkAvailable(applicationContext)) {

            teacher_collection.get().addOnCompleteListener(object:OnCompleteListener<QuerySnapshot> {
                override fun onComplete(@NonNull task:Task<QuerySnapshot>) {

                    updatedProgressDilaog.dialog.dismiss()
                    if (task.isSuccessful())
                    {

                        val list = ArrayList<String>()
                        for (document in task.result!!)
                        {
                            list.add(document.getId())
                            detail_db = ListOfBatchesModel(document.id)
                            list_of_batches!!.add(detail_db!!)
                        }
                        mRecyclerAdapter = AdapterAllUsersOrFaculties(this@ActivityUsers,list_of_batches,"")
                        all_users_recyclerview.adapter = mRecyclerAdapter
                    }
                    else
                    {
                        Log.d("TAG", "Error getting documents: ", task.getException())
                    }
                }
            }).addOnFailureListener { exception ->
                updatedProgressDilaog.dialog.dismiss()
                Log.w("TAG", "Error getting documents: ", exception)
            }
        }else {
            Toast.makeText(applicationContext, "No network available!", Toast.LENGTH_SHORT).show()
        }
    }

}