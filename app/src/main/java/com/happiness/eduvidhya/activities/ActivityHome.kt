package com.happiness.eduvidhya.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.firebase.auth.FirebaseAuth
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.fragments.FragmentHome
import com.happiness.eduvidhya.utils.Constant

class ActivityHome : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var home_frame_layout: FrameLayout

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
        val fragmenthome = FragmentHome()
        transcation.replace(R.id.home_frame_layout, fragmenthome)
        transcation.commit()
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navgationImgBtn: ImageView = findViewById(R.id.navgationImgBtn)
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
                                startActivity(
                                    Intent(applicationContext, ActivityStart::class.java))
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