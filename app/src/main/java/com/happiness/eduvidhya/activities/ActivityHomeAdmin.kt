package com.happiness.eduvidhya.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.utils.Constant
import kotlinx.android.synthetic.main.activity_home_admin.*

class ActivityHomeAdmin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)



        auth = FirebaseAuth.getInstance()

        btnfaculaties.setOnClickListener {

            val intent = Intent(this, ActivityUsers::class.java)
            intent.putExtra("Type","Faculties")
            startActivity(intent)
        }

        usersBtn.setOnClickListener {
            val intent = Intent(this, ActivityUsers::class.java)
            intent.putExtra("Type","Users")
            startActivity(intent)
        }

        logoutBtn.setOnClickListener {
            if (FirebaseAuth.getInstance() != null) {
                val c=auth.currentUser
                if(c!=null)
                {
                    auth.signOut()
                    startActivity(
                        Intent(applicationContext, ActivityStart::class.java))
                }
                Constant.myauth=null
            } else {
                Toast.makeText(applicationContext,"problem", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private lateinit var auth: FirebaseAuth
}