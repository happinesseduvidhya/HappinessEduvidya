package com.happiness.eduvidhya.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.happiness.eduvidhya.adapters.AdapterAllClassesUser
import com.happiness.eduvidhya.datamodels.UserOrFacultyInfo
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog
import kotlinx.android.synthetic.main.activity_users.*

class ActivityUsersAllClasses : AppCompatActivity() {

    // top bar
    lateinit var back_top_bar_img: ImageView
    lateinit var title_top_bar_txt: TextView


    val db = FirebaseFirestore.getInstance()
    val users_collection = db.collection("Users")

    var updatedProgressDilaog = CustomProgressDialog()

    var facultyEmail:String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_all_classes)


        back_top_bar_img = findViewById(R.id.back_top_bar_img)
        title_top_bar_txt = findViewById(R.id.title_top_bar_txt)
        title_top_bar_txt.setText("My Classes")
        back_top_bar_img.setOnClickListener {
            finish()
        }

        facultyEmail = intent.getStringExtra("Email")


        val layoutManager = LinearLayoutManager(this@ActivityUsersAllClasses)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        all_users_recyclerview.setLayoutManager(layoutManager)

        myClasses()


    }
    var users_faculty: ArrayList<UserOrFacultyInfo>? = null
    private var mRecyclerAdapter: AdapterAllClassesUser? = null

    fun myClasses() {

        updatedProgressDilaog.show(this)

        if (Constant.hasNetworkAvailable(applicationContext)) {

            val mySharedPreferences = getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
            val email = mySharedPreferences.getString("user_email", "")

            users_collection.document(email.toString()).collection("Faculties").document(facultyEmail.toString()).collection("Classes").get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                    override fun onComplete(@NonNull task: Task<QuerySnapshot>) {

                        updatedProgressDilaog.dialog.dismiss()
                        if (task.isSuccessful()) {
                            users_faculty = ArrayList<UserOrFacultyInfo>()
                            users_faculty!!.clear()
                            for (document in task.result!!) {

                                val facultyID = document.id
                                val userOrFacultyInfo =
                                    UserOrFacultyInfo("", facultyID.toString(), "")
                                users_faculty!!.add(userOrFacultyInfo)

                            }
                            mRecyclerAdapter = AdapterAllClassesUser(
                                this@ActivityUsersAllClasses,
                                users_faculty,"AllUsersClasses",facultyEmail.toString())
                            all_users_recyclerview.adapter = mRecyclerAdapter

                        }
                    }
                }).addOnFailureListener { exception ->
                    updatedProgressDilaog.dialog.dismiss()

                }
        } else {
            Toast.makeText(applicationContext, "No network available!", Toast.LENGTH_SHORT).show()
        }
    }
}