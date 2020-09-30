package com.happiness.eduvidhya.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog


class ActivityLogin : AppCompatActivity() {


    private lateinit var email_edittext: EditText
    private lateinit var password_edittext: EditText
    private lateinit var login_btn: Button
    private lateinit var mAuth: FirebaseAuth

    val progressBar = CustomProgressDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        email_edittext = findViewById(R.id.enter_email)
        password_edittext = findViewById(R.id.enter_password)
        login_btn = findViewById(R.id.login_btn)
        login_btn.setOnClickListener {

            loginUserAccount(it)

        }

    }

    private fun loginUserAccount(view:View) {
        var email = email_edittext.text.toString()
        var password = password_edittext.text.toString()
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

        progressBar.show(this, "Loading")
        mAuth.signInWithEmailAndPassword(email.toString().trim(), password.trim())
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult> { task ->
                    if (!task.isSuccessful) {
                        progressBar.dialog.dismiss()
                        Constant.showMessage(view,"Password or email is not correct")
                    } else {
                        progressBar.dialog.dismiss()
                        Constant.showMessage(view,"successfully Logged in")
                        startActivity(Intent(this, ActivityHome::class.java))
                    }
                })
    }


}