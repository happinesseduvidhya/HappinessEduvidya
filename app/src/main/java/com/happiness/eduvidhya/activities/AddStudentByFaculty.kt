package com.happiness.eduvidhya.activities

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.datamodels.ClassroomDetailsModel
import com.happiness.eduvidhya.datamodels.Faculty
import com.happiness.eduvidhya.datamodels.ModelUserInfo
import com.happiness.eduvidhya.datamodels.StudentEmail
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog
import kotlinx.android.synthetic.main.activity_add_student_by_faculty.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class AddStudentByFaculty : AppCompatActivity(){


    // top bar
    lateinit var back_top_bar_img: ImageView
    lateinit var title_top_bar_txt: TextView


    lateinit var spinner: Spinner
    val mArray = ArrayList<String>()
    var strClass:String ?= ""

    val db = FirebaseFirestore.getInstance()
    val classes = db.collection("Classes")
    val users_collection = db.collection("Users")
    val faculty_collection = db.collection("Faculties")
    var updatedProgressDilaog = CustomProgressDialog()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student_by_faculty)

        back_top_bar_img = findViewById(R.id.back_top_bar_img)
        title_top_bar_txt= findViewById(R.id.title_top_bar_txt)






        spinner = findViewById(R.id.spinner)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext, "enter ", Toast.LENGTH_SHORT).show()

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                strClass = mArray.get(position)
//                Toast.makeText(applicationContext, mArray.get(position), Toast.LENGTH_SHORT).show()

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
            else{
                if (Constant.hasNetworkAvailable(applicationContext)) {
                    updatedProgressDilaog.show(this)
                    val mySharedPreferences = getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
                    val email = mySharedPreferences.getString("user_email", "")
                    val name = mySharedPreferences.getString("user_name", "")

                    usersAdd = classes.document(email.toString()).collection("classrooms").document(strClass.toString()).collection("users").document(search_user.text.toString())
                    modelUserInfo?.let { it1 -> usersAdd!!.set(it1) }!!.addOnSuccessListener {

                        val student = Faculty(name.toString(),email.toString(),strClass.toString())

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

    var usersAdd : DocumentReference?=null
    var modelUserInfo : ModelUserInfo?=null

    private fun mCallGetUsers()
    {
        val mySharedPreferences = getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
        val email = mySharedPreferences.getString("user_email", "")


        if (Constant.hasNetworkAvailable(applicationContext)) {

            updatedProgressDilaog.show(this)
            classes.document(email.toString()).collection("classrooms").get()

                .addOnSuccessListener { documents ->

                    updatedProgressDilaog.dialog.dismiss()

                    for (document in documents) {
                        mArray.add(document.id)
                    }

                    if (mArray.size ==0)
                    {
                        add_student_btn.visibility = View.INVISIBLE
                        Toast.makeText(applicationContext, "No data found", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, mArray)
                        // Set layout to use when the list of choices appear
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        // Set Adapter to Spinner
                        spinner.setAdapter(aa)
                    }


                }.addOnFailureListener { exception ->
                    updatedProgressDilaog.dialog.dismiss()
                    Log.w("TAG", "Error getting documents: ", exception)
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

}