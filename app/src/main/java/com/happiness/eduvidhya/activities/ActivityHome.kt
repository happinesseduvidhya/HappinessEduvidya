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
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.fragments.FragmentHome
import com.happiness.eduvidhya.utils.Constant


class ActivityHome : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var home_frame_layout: FrameLayout
    private lateinit var mLogoutBtn: ImageView

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

        if (savedInstanceState == null)
        {
            val fragmenthome = FragmentHome()
            transcation.replace(R.id.home_frame_layout, fragmenthome)
            transcation.commit()
        }


        val navView: NavigationView = findViewById(R.id.nav_view)
        val headerView: View = navView.getHeaderView(0)


        val navgationImgBtn: ImageView = findViewById(R.id.navgationImgBtn)

        val navUsername = headerView.findViewById(R.id.txt_person) as TextView

        val mySharedPreferences = applicationContext.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
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
                            if (FirebaseAuth.getInstance() != null) {
                                val c=auth.currentUser
                                if(c!=null)
                                {
                                auth.signOut()

                                    val intent = Intent(applicationContext, ActivityStart::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    startActivity(intent)

                                }
                                Constant.myauth=null
                            } else {
Toast.makeText(applicationContext,"problem",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    drawerLayout.closeDrawer(GravityCompat.START)
                    return true
                }
            })
        }


        mLogoutBtn.setOnClickListener {
            if (FirebaseAuth.getInstance() != null) {
                val c=auth.currentUser
                if(c!=null)
                {
                    auth.signOut()
                    val intent = Intent(applicationContext, ActivityStart::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
                Constant.myauth=null
            } else {
                Toast.makeText(applicationContext,"problem",Toast.LENGTH_SHORT).show()
            }
        }
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
    }
}