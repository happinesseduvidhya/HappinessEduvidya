package com.happiness.eduvidhya.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import com.happiness.eduvidhya.datamodels.NotificationModel
import com.happiness.eduvidhya.fragments.FragmentFacultyHome
import com.happiness.eduvidhya.fragments.FragmentHome
import ru.nikartm.support.ImageBadgeView
import java.lang.Exception


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

    var user_email:String = ""
    var strType:String = ""
    var mArraysNotificatioCheck:ArrayList<NotificationModel> ?= null

    var mediaPlayer:MediaPlayer ?=null

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

        user_email = mySharedPreferences.getString("user_email", "")!!
        strType = mySharedPreferences.getString("type", "")!!


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

            users_collection.document(user_email.toString()).collection("Faculties").addSnapshotListener(object :
                EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                    mArraysNotificatioCheck = ArrayList<NotificationModel>()


                    for (data in value!!.documents) {
                        if (data.exists()) {
                            val facultyIDEmail = data.id
                            val classRoom = data.get("facultyClass")
                            val notification = NotificationModel(facultyIDEmail,"","",classRoom.toString(),"")
                            mArraysNotificatioCheck!!.add(notification)

                        }
                    }
                    check()

                }

            })
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.notification)

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

        notification_bell.setOnClickListener {
            val i = Intent(this@ActivityHome, ActivityBaseForFragment::class.java)
            i.putExtra("checkPage", "notification_bell")
            startActivity(i)

        }


    }

    private fun check()
    {

        notifications_collection.addSnapshotListener(object :
            EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?
            ) {
                notificationCount = 0

                try {
                    for (data in value!!.documents) {

                        if (data.exists()) {

                            val emailInfo = data.get("facultyEmail")
                            val className = data.get("meetingClassName")
                            val notificationStatus = data.get("notification_status")

                            if (mArraysNotificatioCheck!!.size !=0)
                            {
                                for (dataFirstLoop in 0 until  mArraysNotificatioCheck!!.size)
                                {
                                    if (notificationStatus!!.equals("0"))
                                    {
                                        val facultyIDEmail = mArraysNotificatioCheck!!.get(dataFirstLoop).facultyEmail
                                        val facultyIDClassName = mArraysNotificatioCheck!!.get(dataFirstLoop).meetingClassName
                                        if (facultyIDEmail.equals(emailInfo) && facultyIDClassName.equals(className))
                                        {
                                            notificationCount++
                                            notification_bell.setBadgeValue(notificationCount)
//                                        mediaPlayer!!.start()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                catch (e:Exception)
                {
                    Toast.makeText(this@ActivityHome, e.message.toString(), Toast.LENGTH_SHORT).show()
                }


            }
        })
    }

    private fun MediaPlayer()
    {
        notification_bell.setBadgeValue(notificationCount)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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