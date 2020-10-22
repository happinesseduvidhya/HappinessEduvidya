package com.happiness.eduvidhya.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.happiness.eduvidhya.R

class ActivitySplash : AppCompatActivity(), Animation.AnimationListener {



    override fun onAnimationRepeat(p0: Animation?) {
    }

    override fun onAnimationEnd(p0: Animation?) {
    }

    override fun onAnimationStart(p0: Animation?) {
    }

    private lateinit var animBlink: Animation
    private lateinit var constraint: ConstraintLayout
    private val SPLASH_DELAY: Long = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_splash)

        constraint = findViewById(R.id.splash_constraint)

        animBlink = AnimationUtils.loadAnimation(
            this,
            R.anim.blink
        )
        animBlink.setAnimationListener(this)
        constraint.startAnimation(animBlink)

        Handler().postDelayed({

            val mySharedPreferences = applicationContext.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
            val type = mySharedPreferences.getString("type", "")

            if (type.equals("Admin"))
            {
                val intent = Intent(this, ActivityHomeAdmin::class.java)
                startActivity(intent)
                finish()
            }
            else if (type.equals("User")) {
                val intent = Intent(this, ActivityHome::class.java)
                intent.putExtra("Type","User")
                startActivity(intent)
                finish()

            }
            else if (type.equals("Faculty")) {
                val intent = Intent(this, ActivityHome::class.java)
                intent.putExtra("Type","Faculty")
                startActivity(intent)
                finish()
            }
            else{
                val intent = Intent(this, ActivityStart::class.java)
                startActivity(intent)
                finish()
            }

        }, SPLASH_DELAY)




    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}



