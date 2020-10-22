package com.happiness.eduvidhya.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.datamodels.ModelUserInfo
import com.happiness.eduvidhya.fragments.FragmentFacultyHome
import com.happiness.eduvidhya.fragments.FragmentHome
import com.happiness.eduvidhya.utils.Constant
import ru.nikartm.support.ImageBadgeView


class ActivityHome : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var home_frame_layout: FrameLayout
    private lateinit var mLogoutBtn: ImageView
    private lateinit var notification_bell: ImageBadgeView

    val db = FirebaseFirestore.getInstance()
    val users_collection = db.collection("Users")
    val notifications_collection = db.collection("Notifications")

    var notificationCount:Int = 0


    var mArraysFacultiesEmail:ArrayList<String> ?= null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home2)

        auth = FirebaseAuth.getInstance()
        val transcation = supportFragmentManager.beginTransaction()
        transcation.isAddToBackStackAllowed
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        home_frame_layout = findViewById(R.id.home_frame_layout)
        mLogoutBtn = findViewById(R.id.mLogoutBtn)
        notification_bell = findViewById(R.id.notificationImgBtn)

        val type = intent.getStringExtra("Type")

        val mySharedPreferences = applicationContext.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)

        val user_email = mySharedPreferences.getString("user_email", "")


        if (type!!.equals("Faculty"))
        {
            notification_bell.visibility = View.INVISIBLE
            if (savedInstanceState == null)
            {
                val fragmentFacultyHome = FragmentFacultyHome()
                transcation.replace(R.id.home_frame_layout, fragmentFacultyHome)
                transcation.commit()
            }
        }
        else{
            if (savedInstanceState == null)
            {
                val fragmenthome = FragmentHome()
                transcation.replace(R.id.home_frame_layout, fragmenthome)
                transcation.commit()
            }
        }

        mArraysFacultiesEmail = null
        mArraysFacultiesEmail = ArrayList()

        users_collection.document(user_email.toString()).collection("Faculties").addSnapshotListener(object :
            EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                for (data in value!!.documents) {
                    if (data.exists()) {

//                        Toast.makeText(applicationContext,"First Loop"+data.id.toString(),Toast.LENGTH_SHORT).show()
                        val facultyIDEmail = data.id
                        mArraysFacultiesEmail!!.add(facultyIDEmail)

                    }
                }
                check()

            }

        })



        val navView: NavigationView = findViewById(R.id.nav_view)
        val headerView: View = navView.getHeaderView(0)


        val navgationImgBtn: ImageView = findViewById(R.id.navgationImgBtn)

        val navUsername = headerView.findViewById(R.id.txt_person) as TextView

        val name = mySharedPreferences.getString("user_name", "")
        navUsername.setText(name)

        navgationImgBtn.setOnClickListener {

            drawerLayout.openDrawer(Gravity.START)
            navView.itemIconTintList = null
            navView.setNavigationItemSelectedListener(object :
                NavigationView.OnNavigationItemSelectedListener {

                override fun onNavigationItemSelected(p0: MenuItem):Boolean {

                    when (p0.itemId) {
                        R.id.edit_profile -> {

                        }
                        R.id.logout -> {

                            Toast.makeText(applicationContext,"check things",Toast.LENGTH_SHORT).show()

                            val mySharedPreferences = getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
                            val editor = mySharedPreferences.edit()
                            editor.putString("user_email", "")
                            editor.putString("user_name", "")
                            editor.putString("user_password", "")
                            editor.putString("type", "")
                            editor.apply()

                            val intent = Intent(this@ActivityHome, ActivityStart::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                            finish()
                        }
                    }
                    drawerLayout.closeDrawer(GravityCompat.START)
                    return true
                }
            })
        }


        mLogoutBtn.setOnClickListener {

            val mySharedPreferences = getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
            val editor = mySharedPreferences.edit()
            editor.putString("user_email", "")
            editor.putString("user_name", "")
            editor.putString("user_password", "")
            editor.putString("type", "")
            editor.apply()

            val intent = Intent(this@ActivityHome, ActivityStart::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()

        }
    }

    private fun check()
    {

        notifications_collection.addSnapshotListener(object :
            EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?
            ) {
                notificationCount = 0
                for (data in value!!.documents) {


                    if (data.exists()) {

                        val emailInfo = data.get("facultyEmail")

                        for (dataFirstLoop in 0 until  mArraysFacultiesEmail!!.size)
                        {
                            val facultyIDEmail = mArraysFacultiesEmail!!.get(dataFirstLoop)
                            if (facultyIDEmail.equals(emailInfo))
                            {
//                                Toast.makeText(applicationContext,"Second Loop"+notificationCount.toString(),Toast.LENGTH_SHORT).show()
                                notificationCount++
                            }
                        }


                    }

                }
//                Toast.makeText(applicationContext,notificationCount.toString(),Toast.LENGTH_SHORT).show()
                notification_bell.setBadgeValue(notificationCount)
                notification_bell.setOnClickListener {
                    val i = Intent(this@ActivityHome, ActivityBaseForFragment::class.java)
                    i.putExtra("checkPage", "notification_bell")
                    startActivity(i)
                }

            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.activity_home, menu)
        return true
    }
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        }
        finish()
    }
}