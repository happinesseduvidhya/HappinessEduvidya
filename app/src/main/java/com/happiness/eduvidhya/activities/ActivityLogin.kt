package com.happiness.eduvidhya.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog


class ActivityLogin : AppCompatActivity() {


    private lateinit var email_edittext: EditText
    private lateinit var password_edittext: EditText
    private lateinit var type_edt: EditText
    private lateinit var login_btn: Button
    private lateinit var mAuth: FirebaseAuth

    val progressBar = CustomProgressDialog()

    val db = FirebaseFirestore.getInstance()
    val teacher_collection = db.collection("teachers")
    val student_collection = db.collection("student")
    val admin_collection = db.collection("admin")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        email_edittext = findViewById(R.id.enter_email)
        password_edittext = findViewById(R.id.enter_password)
        type_edt = findViewById(R.id.type_edt)

        login_btn = findViewById(R.id.login_btn)
        login_btn.setOnClickListener {

            loginUserAccount(it)

        }

        type_edt.setOnClickListener {

            val popup = PopupMenu(this@ActivityLogin, type_edt)
            popup.getMenuInflater().inflate(R.menu.types, popup.getMenu())

            val liveitem: MenuItem = popup.menu.getItem(0)
            val s = SpannableString(liveitem.title.toString())
            s.setSpan(ForegroundColorSpan(ContextCompat.getColor(applicationContext!!,R.color.color_red)), 0, s.length, 0)
            liveitem.title = s


            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    val types = item.getTitle()
                    if (types.equals("Select your type")) {
                        Constant.showMessage(it, "select your type")
                        return false
                    } else {
                        type_edt.setText(item.getTitle())
                        return true
                    }
                }
            })
            popup.show()

        }

    }

    private fun loginUserAccount(view: View) {
        val email = email_edittext.text.toString()
        val password = password_edittext.text.toString()
        val type = type_edt.text.toString()
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter email...", Toast.LENGTH_LONG)
                .show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_LONG)
                .show()
            return
        }
        if (TextUtils.isEmpty(type)) {
            Toast.makeText(applicationContext, "Please enter type!", Toast.LENGTH_LONG)
                .show()
            return
        }

        progressBar.show(this, "Loading")

        mAuth.signInWithEmailAndPassword(email.toString().trim(), password.trim())
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult> { task ->
                    if (!task.isSuccessful) {
                        progressBar.dialog.dismiss()
                        Constant.showMessage(view, "Password or email is not correct")
                    } else {

                        if (type.equals("Admin")) {
                            admin_collection.document(email).get().addOnCompleteListener(object :
                                OnCompleteListener<DocumentSnapshot> {
                                override fun onComplete(@NonNull task: Task<DocumentSnapshot>) {
                                    if (task.isSuccessful()) {
                                        progressBar.dialog.dismiss()

                                        val document = task.getResult()
                                        if (document!!.exists()) {
                                            val name = document.get("name")
                                            mLoginSucess(email,name.toString(),password,type)

                                        } else {
                                            Constant.showMessage(view,"type is not valid")
                                        }
                                    } else {
                                        Constant.showMessage(view,task.getException().toString())
                                    }
                                }
                            })

                        } else if (type.equals("Teacher")) {

                            val docRef = db.collection("teachers").document(email)
                            docRef.get().addOnCompleteListener(object :
                                OnCompleteListener<DocumentSnapshot> {
                                override fun onComplete(@NonNull task: Task<DocumentSnapshot>) {
                                    if (task.isSuccessful()) {
                                        progressBar.dialog.dismiss()

                                        val document = task.getResult()
                                        if (document!!.exists()) {
                                            val name = document.get("name")
                                            mLoginSucess(email,name.toString(),password,type)

                                        } else {
                                            Constant.showMessage(view,"type is not valid")
                                        }
                                    } else {
                                        Constant.showMessage(view,task.getException().toString())
                                    }
                                }
                            })

                        } else if (type.equals("Student")) {

                            student_collection.document(email).get().addOnCompleteListener(object :
                                OnCompleteListener<DocumentSnapshot> {
                                override fun onComplete(@NonNull task: Task<DocumentSnapshot>) {
                                    if (task.isSuccessful()) {
                                        progressBar.dialog.dismiss()

                                        val document = task.getResult()
                                        if (document!!.exists()) {
                                            val name = document.get("name")
                                            mLoginSucess(email,name.toString(),password,type)

                                        } else {
                                            Constant.showMessage(view,"type is not valid")
                                        }
                                    } else {
                                        Constant.showMessage(view,task.getException().toString())
                                    }
                                }
                            })
                        }


                    }
                })
    }

    fun mLoginSucess(email:String,name:String,password:String,type:String)
    {
        val mySharedPreferences = getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
        val editor = mySharedPreferences.edit()
        editor.putString("user_email", email)
        editor.putString("user_name", name)
        editor.putString("user_password", password)
        editor.putString("type", type)
        editor.apply()


        val type = type_edt.text.toString()
        if (type.equals("Admin")) {
            startActivity(Intent(this, ActivityHomeAdmin::class.java))
        }
        else{
            startActivity(Intent(this, ActivityHome::class.java))
        }

    }


}