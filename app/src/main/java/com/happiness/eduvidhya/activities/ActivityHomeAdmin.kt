package com.happiness.eduvidhya.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.happiness.eduvidhya.R
import kotlinx.android.synthetic.main.activity_home_admin.*

class ActivityHomeAdmin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)


        managesFacultiesRl.setOnClickListener {

            val intent = Intent(this, ActivityListUsersOrFaculty::class.java)
            intent.putExtra("Type","Faculties")
            startActivity(intent)
        }

        managesUsersRl.setOnClickListener {

            val intent = Intent(this, ActivityListUsersOrFaculty::class.java)
            intent.putExtra("Type","Users")
            startActivity(intent)
        }

        mLogoutBtn.setOnClickListener {

            val mySharedPreferences = getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
            val editor = mySharedPreferences.edit()
            editor.putString("user_email", "")
            editor.putString("user_name", "")
            editor.putString("user_password", "")
            editor.putString("type", "")
            editor.putString("check","all things")
            editor.putString("check","all things")
            editor.apply()

            val intent = Intent(this, ActivityStart::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

            finish()

        }


        createUserRl.setOnClickListener {
            val intent = Intent(this, ActivityUserOrFacultyAddOrUpdate::class.java)
            intent.putExtra("Type","User")
            intent.putExtra("Name","")
            intent.putExtra("Email","")
            intent.putExtra("Pass","")
            startActivity(intent)
        }

        createFacultyRl.setOnClickListener {

            val intent = Intent(this, ActivityUserOrFacultyAddOrUpdate::class.java)
            intent.putExtra("Type","Faculty")
            intent.putExtra("Name","")
            intent.putExtra("Email","")
            intent.putExtra("Pass","")
            startActivity(intent)

        }
    }
}