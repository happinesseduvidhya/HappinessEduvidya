package com.happiness.eduvidhya.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.datamodels.UserDetailDataModel
import com.happiness.eduvidhya.utils.CustomProgressDialog
import kotlinx.android.synthetic.main.activity_register_as_admin_or_moderator_or_user.*
import java.util.regex.Pattern


class ActivityRegisterAsAdminOrModeratorOrUser : AppCompatActivity() {

    private lateinit var moderator_card: CardView
    private lateinit var attendee_card: CardView
    private lateinit var signin_heading: TextView
    private lateinit var email_edittext: EditText
    private lateinit var password_edittext: EditText
    private lateinit var enter_name: EditText
    private lateinit var signin_btn: Button
    private lateinit var attendee_constraint_lay: ConstraintLayout
    private lateinit var moderator_constraint_lay: ConstraintLayout
    private lateinit var auth: FirebaseAuth
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    val db = FirebaseFirestore.getInstance()

    val teacher_collection = db.collection("teachers")
    val student_collection = db.collection("student")
    val admin_collection = db.collection("admin")
    var user_type: String? = "Teacher"

    val progressBar = CustomProgressDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_as_admin_or_moderator_or_user)

        auth = FirebaseAuth.getInstance()
        moderator_card = findViewById(R.id.moderator_card)
        attendee_card = findViewById(R.id.attendee_card)
        attendee_constraint_lay = findViewById(R.id.attendee_constraint)
        moderator_constraint_lay = findViewById(R.id.moderator_constraint)
        signin_heading = findViewById(R.id.signin_heading)
        email_edittext = findViewById(R.id.enter_email)
        password_edittext = findViewById(R.id.enter_password)
        enter_name = findViewById(R.id.enter_name)
        signin_btn = findViewById(R.id.signin_btn)
        moderator_constraint_lay.setBackgroundResource(R.color.color_light_orange)
        signin_heading.setText("Sign in as Teacher")
        val listener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View) {
                changeColorBack(attendee_constraint_lay)
                changeColorBack(moderator_constraint_lay)
                when (v.getId()) {
                    R.id.attendee_constraint -> changeColor(attendee_constraint_lay, "Student")
                    R.id.moderator_constraint -> changeColor(moderator_constraint_lay, "Teacher")
                }
            }
        }
        attendee_constraint_lay.setOnClickListener(listener)
        moderator_constraint_lay.setOnClickListener(listener)
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()

        signin_btn.setOnClickListener {


            val Expn = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")

            if (enter_name.text.toString().isEmpty()) {
                Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show()
            } else if (enter_email.text.toString().isEmpty()) {
                Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
            } else if (enter_email.text.toString().isEmpty()) {
                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
            }
            else if (enter_password.text.toString().length<=6) {
                Toast.makeText(this, "Enter Password atleast 6 characters", Toast.LENGTH_SHORT).show()
            }
            else if (!isValidEmailId(enter_email.getText().toString().trim())) {
                Toast.makeText(this, "email is not valid", Toast.LENGTH_SHORT).show()
            }
            else {
                val name = enter_name.text.toString()
                val email = enter_email.text.toString()
                val password = enter_password.text.toString()

                progressBar.show(this, "Loading")


                auth.createUserWithEmailAndPassword(email, password)

                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            progressBar.dialog.dismiss()

                            Toast.makeText(this, user_type, Toast.LENGTH_SHORT).show()

                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success")
                            val user = auth.currentUser
                            Toast.makeText(this, "Succesfully Signed", Toast.LENGTH_SHORT).show()
                            val detail_db = UserDetailDataModel(name, email, password)
                            val mySharedPreferences =
                                this.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
                            val editor = mySharedPreferences.edit()
                            editor.putString("user_email", email)
                            editor.putString("user_name", name)
                            editor.putString("user_password", password)
                            editor.putString("type", user_type)
                            editor.apply()
                            if (user_type == "Teacher") {
                                val particular_user_database = teacher_collection.document(email)
                                particular_user_database.set(detail_db)

                                Toast.makeText(this, "Teacher Side", Toast.LENGTH_SHORT).show()

                            } else if (user_type == "Student") {
                                val particular_user_database = student_collection.document(email)
                                particular_user_database.set(detail_db)

                                Toast.makeText(this, "Student Side", Toast.LENGTH_SHORT).show()

                            } else if (user_type == "Admin") {
                                val particular_user_database = admin_collection.document(email)
                                particular_user_database.set(detail_db)
                            }
                            val intent = Intent(applicationContext, ActivityHome::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)

                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.dialog.dismiss()
                            Toast.makeText(baseContext, task.exception.toString(), Toast.LENGTH_SHORT).show()

                        }

                    }
            }
        }
    }

    private fun isValidEmailId(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }

    fun changeColorBack(layout: ConstraintLayout) {
        layout.setBackgroundResource(R.color.colorWhite_LITTLE)
    }

    private fun changeColor(layout: ConstraintLayout, text: String) {



        signin_heading.setText("Sign in as " + text)
        user_type = text
        layout.setBackgroundResource(R.color.color_light_orange)
    }


    private fun verifyEmail() {
        val mUser = mAuth!!.currentUser;
        mUser!!.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Verification email sent to " + mUser.getEmail(),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.e("TAG", "sendEmailVerification", task.exception)
                    Toast.makeText(
                        this,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}

