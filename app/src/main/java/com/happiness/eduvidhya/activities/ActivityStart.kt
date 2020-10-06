package com.happiness.eduvidhya.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.happiness.eduvidhya.R

class ActivityStart:AppCompatActivity() {


    private lateinit var login:Button
    private lateinit var register_intent:TextView
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        auth = FirebaseAuth.getInstance()
        register_intent=findViewById(R.id.register_intent)
        login=findViewById(R.id.loginBtn)
        login.setOnClickListener {
            startActivity(Intent(this, ActivityLogin::class.java))
        }
        register_intent.setOnClickListener {
            startActivity(Intent(this, ActivityRegisterAsAdminOrModeratorOrUser::class.java))
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {

            val mySharedPreferences = applicationContext.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
            val type = mySharedPreferences.getString("type", "")

            if (type.equals("Admin"))
            {
                Toast.makeText(this, "already signed in", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ActivityHomeAdmin::class.java))
            }
            else {
                Toast.makeText(this, "already signed in", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ActivityHome::class.java))
            }


        }
    }
}
