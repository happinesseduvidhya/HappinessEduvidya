package com.happiness.eduvidhya.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth

import com.happiness.eduvidhya.R

class ActivitySplash : AppCompatActivity(), Animation.AnimationListener {
    private lateinit var auth: FirebaseAuth
    override fun onAnimationRepeat(p0: Animation?) {
    }

    override fun onAnimationEnd(p0: Animation?) {
    }

    override fun onAnimationStart(p0: Animation?) {
    }

    private lateinit var animBlink: Animation
    private lateinit var constraint: ConstraintLayout
    private val SPLASH_DELAY: Long = 2000 //3 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
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
        );
        animBlink.setAnimationListener(this);
        constraint.startAnimation(animBlink);
        Handler().postDelayed({


            startActivity(Intent(this, ActivityStart::class.java))

            finish()
        }, SPLASH_DELAY)
    }


}



