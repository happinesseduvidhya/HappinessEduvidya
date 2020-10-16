package com.happiness.eduvidhya.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.datamodels.UserDetailDataModel
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog
import kotlinx.android.synthetic.main.activity_user_or_faculty_add_or_update.*

class ActivityUserOrFacultyAddOrUpdate : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    private var auth: FirebaseAuth? = null


    val faculties_collection = db.collection("Faculties")
    val users_collection = db.collection("Users")

    val progressBar = CustomProgressDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_or_faculty_add_or_update)

        auth = FirebaseAuth.getInstance()



        val type = intent.getStringExtra("Type")
        val Name = intent.getStringExtra("Name")
        val Email = intent.getStringExtra("Email")
        val Pass = intent.getStringExtra("Pass")

        if (type!!.equals("User"))
        {
            signin_heading.setText("User")

        }
        else if (type.equals("Faculty"))
        {
            signin_heading.setText("Faculty")

        }



        if (!Name!!.isEmpty())
        {
            enter_name_edt.setText(Name)
            enter_email_edt.setText(Email)
            enter_password_edt.setText(Pass)
        }


        submit_btn.setOnClickListener {

            val name = enter_name_edt.text.toString()
            val email = enter_email_edt.text.toString()
            val password = enter_password_edt.text.toString()

            if (type.equals("User"))
            {
                mUpdateUser(name,email,password)
            }
            else if (type.equals("Faculty"))
            {
                mUpdateFaculty(name,email,password)
            }
            else{


                if (!name.isEmpty())
                {
                    if (!email.isEmpty())
                    {
                        if (!password.isEmpty())
                        {
                            progressBar.show(this)
                            auth!!.createUserWithEmailAndPassword(email, password)

                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {

                                        progressBar.dialog.dismiss()

                                        val detail_db = UserDetailDataModel(name, email, password)

                                        if (type == "Faculty") {
                                            val particular_user_database = faculties_collection.document(email)
                                            particular_user_database.set(detail_db)

                                        } else if (type == "User") {
                                            val particular_user_database = users_collection.document(email)
                                            particular_user_database.set(detail_db)

                                        }
                                        Toast.makeText(this, type+" registered successfully", Toast.LENGTH_SHORT).show()
                                        submit_btn.isEnabled = false


                                    } else {
                                        // If sign in fails, display a message to the user.
                                        progressBar.dialog.dismiss()
                                        Toast.makeText(baseContext, task.exception.toString(), Toast.LENGTH_SHORT).show()

                                    }

                                }
                        }
                        else{
                            Constant.showMessage(it,"enter password")
                        }
                    }
                    else{
                        Constant.showMessage(it,"enter email")
                    }
                }
                else{
                    Constant.showMessage(it,"enter name")
                }
            }


        }
    }

    private fun mUpdateFaculty(name:String,email:String,password:String)
    {
        progressBar.show(this)

        faculties_collection.document(email).update("name", name,"password",password)
            .addOnSuccessListener{
                progressBar.dialog.dismiss()
                Toast.makeText(this, "values changes successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                progressBar.dialog.dismiss()
                Toast.makeText(this, "Error updating document", Toast.LENGTH_SHORT).show()
            }
    }

    private fun mUpdateUser(name:String,email:String,password:String)
    {
        progressBar.show(this)
        users_collection.document(email).update("name", name,"password",password)
            .addOnSuccessListener{
                progressBar.dialog.dismiss()
                Toast.makeText(this, "values changes successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                progressBar.dialog.dismiss()
                Toast.makeText(this, "Error updating document", Toast.LENGTH_SHORT).show()
            }
    }

}