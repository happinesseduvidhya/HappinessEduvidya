package com.happiness.eduvidhya.activities

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.ArrayAdapter
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.adapters.AdapterShowBatches
import com.happiness.eduvidhya.datamodels.ListOfBatchesModel
import com.happiness.eduvidhya.datamodels.ModelUserInfo
import com.happiness.eduvidhya.datamodels.StudentEmail
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog
import kotlinx.android.synthetic.main.activity_add_student_by_faculty.*


class AddUsersInBatchesByFaculty : AppCompatActivity() {

    // top bar
    lateinit var back_top_bar_img: ImageView
    lateinit var title_top_bar_txt: TextView

    var usersAdd : DocumentReference?=null
    var modelUserInfo : ModelUserInfo?=null

    lateinit var spinner: Spinner
    lateinit var spinnerBatches: Spinner
    val mArray = ArrayList<String>()
    var strClass:String ?= ""

    val db = FirebaseFirestore.getInstance()
    val classes = db.collection("Classes")
    val users_collection = db.collection("Users")
    val faculty_collection = db.collection("Faculties")
    var updatedProgressDilaog = CustomProgressDialog()


    var strBatchID:String= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_users_in_batches_by_faculty)

        back_top_bar_img = findViewById(R.id.back_top_bar_img)
        title_top_bar_txt= findViewById(R.id.title_top_bar_txt)

        list_of_batches = ArrayList<String>()

        spinner = findViewById(R.id.spinner)
        spinnerBatches = findViewById(R.id.spinnerBatches)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext, "enter ",Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val mySharedPreferences = getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
                val email = mySharedPreferences.getString("user_email", "")
                strClass = mArray.get(position)
                mBatchesAll(email.toString(),strClass.toString())

            }

        }

        spinnerBatches.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext, "enter ", Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                strBatchID = list_of_batches!!.get(position)

            }

        }

        title_top_bar_txt.setText("Add User")

        back_top_bar_img.setOnClickListener {
            finish()
        }

        mCallGetUsers()




        search_user.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {


            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {

                if (s.toString().contains("@gmail.com"))
                {
                    mUser(s.toString())
                }


            }
        })


        add_student_btn.setOnClickListener {

            if (check_email_txt.equals("ENTER_EMAIL"))
            {
                Toast.makeText(applicationContext, "enter user email", Toast.LENGTH_SHORT).show()
            }
            else if (check_email_txt.equals("user is not found"))
            {
                Toast.makeText(applicationContext, "enter valid user email", Toast.LENGTH_SHORT).show()
            }
            else if (strClass.equals("select your class"))
            {
                Toast.makeText(applicationContext, "select your class", Toast.LENGTH_SHORT).show()
            }
            else if (strBatchID.equals("select your batch"))
            {
                Toast.makeText(applicationContext, "select your batch", Toast.LENGTH_SHORT).show()
            }
            else if (strBatchID.equals(""))
            {
                Toast.makeText(applicationContext, "select your batch", Toast.LENGTH_SHORT).show()
            }
            else{

                if (Constant.hasNetworkAvailable(applicationContext)) {
                    updatedProgressDilaog.show(this)
                    val mySharedPreferences = getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
                    val email = mySharedPreferences.getString("user_email", "")

                    usersAdd = classes.document(email.toString()).collection("classrooms").document(strClass.toString()).collection("Batches").document(strBatchID).collection("Users").document(search_user.text.toString())
                    modelUserInfo?.let { it1 -> usersAdd!!.set(it1) }!!.addOnSuccessListener {

                        val student = StudentEmail("0")

                        val modelUserInfo = ModelUserInfo("",search_user.text.toString(),"",strClass.toString())
                        faculty_collection.document(email.toString()).collection("Users").document(search_user.text.toString()).set(modelUserInfo)
                        users_collection.document(search_user.text.toString()).collection("Faculties").document(email.toString()).set(student)
                        usersAdd = users_collection.document(search_user.text.toString()).collection("Faculties").document(email.toString()).collection("Classes").document(strClass.toString())
                        usersAdd!!.set(student).addOnSuccessListener {
                            add_student_btn.visibility = View.INVISIBLE
                            Toast.makeText(applicationContext, "User added successfully", Toast.LENGTH_SHORT).show()
                            add_student_btn.isEnabled = false
                            updatedProgressDilaog.dialog.dismiss()
                        }.addOnFailureListener {
                            updatedProgressDilaog.dialog.dismiss()
                        }
                        updatedProgressDilaog.dialog.dismiss()
                    }.addOnFailureListener {
                        updatedProgressDilaog.dialog.dismiss()
                    }


                }else {
                    Toast.makeText(applicationContext, "No network available!", Toast.LENGTH_SHORT).show()
                }

            }

        }

    }

    private fun mCallGetUsers()
    {
        val mySharedPreferences = getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
        val email = mySharedPreferences.getString("user_email", "")


        if (Constant.hasNetworkAvailable(applicationContext)) {

            updatedProgressDilaog.show(this)
            classes.document(email.toString()).collection("classrooms").get()

                .addOnSuccessListener { documents ->

                    updatedProgressDilaog.dialog.dismiss()
//                    mArray.clear()
                    mArray.add(0,"select your class")
                    for (document in documents) {
                        mArray.add(document.id)
                    }

                    if (mArray.size ==0)
                    {
//                        Toast.makeText(applicationContext, "No data found", Toast.LENGTH_SHORT).show()
                    }
                    else{

                        val adapter = ArrayAdapter(this, R.layout.custom_spinner,mArray) // where array_name consists of the items to show in Spinner
                        adapter.setDropDownViewResource(R.layout.custom_spinner)
                        spinner.setAdapter(adapter)
                    }


                }.addOnFailureListener { exception ->
                    updatedProgressDilaog.dialog.dismiss()

                }
        } else {
            Toast.makeText(applicationContext, "No network available!", Toast.LENGTH_SHORT).show()
        }
    }



    private fun mUser(email:String)
    {
        if (Constant.hasNetworkAvailable(applicationContext)) {
            users_collection.document(email).get().addOnCompleteListener(object :
                OnCompleteListener<DocumentSnapshot> {
                override fun onComplete(@NonNull task: Task<DocumentSnapshot>) {
                    if (task.isSuccessful()) {
                        updatedProgressDilaog.dialog.dismiss()

                        val document = task.getResult()
                        if (document!!.exists()) {

                            val name = document.get("name")
                            val email = document.get("email")
                            modelUserInfo = ModelUserInfo(name.toString(),email.toString(),"","")
                            check_email_txt.setTextColor(ContextCompat.getColor(applicationContext,R.color.color_green))
                            check_email_txt.setText("user is available")
                            check_email_txt.visibility = View.VISIBLE
                        } else {
                            check_email_txt.visibility = View.VISIBLE
                            check_email_txt.setTextColor(ContextCompat.getColor(applicationContext,R.color.color_red))
                            check_email_txt.setText("user is not found")
                        }
                    } else {
                        Toast.makeText(applicationContext, task.getException().toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } else {
            Toast.makeText(applicationContext, "No network available!", Toast.LENGTH_SHORT).show()
        }
        updatedProgressDilaog.show(this)


    }
    var list_of_batches: ArrayList<String>? = null

    private fun mBatchesAll(email:String,strClass:String)
    {
        if (Constant.hasNetworkAvailable(this)) {

            classes.document(email).collection("classrooms").document(strClass).collection("Batches").addSnapshotListener(object :
                EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    updatedProgressDilaog.dialog.dismiss()
                    list_of_batches!!.clear()
                    list_of_batches!!.add(0,"select your batch")
                    for (data in value!!.documents) {
//                        detail_db = ListOfBatchesModel(.toString())
                        list_of_batches!!.add(data.id)
                    }


                    if (list_of_batches!!.size == 0)
                    {
                        strBatchID = ""
                    }
//                    else{
//                        val adapter = ArrayAdapter(this@AddUsersInBatchesByFaculty, R.layout.custom_spinner,
//                            list_of_batches!!
//                        ) // where array_name consists of the items to show in Spinner
//                        adapter.setDropDownViewResource(R.layout.custom_spinner)
//                        spinnerBatches.setAdapter(adapter)
//                    }
                    val adapter = ArrayAdapter(this@AddUsersInBatchesByFaculty, R.layout.custom_spinner,
                        list_of_batches!!
                    ) // where array_name consists of the items to show in Spinner
                    adapter.setDropDownViewResource(R.layout.custom_spinner)
                    spinnerBatches.setAdapter(adapter)
                }

            })

        }else {
            Toast.makeText(this, "No network available", Toast.LENGTH_SHORT).show()

        }
    }
}