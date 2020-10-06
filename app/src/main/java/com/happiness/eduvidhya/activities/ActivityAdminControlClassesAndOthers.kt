package com.happiness.eduvidhya.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog

class ActivityAdminControlClassesAndOthers : AppCompatActivity() {

    // top bar
    lateinit var back_top_bar_img: ImageView
    lateinit var title_top_bar_txt: TextView

    lateinit var enableDisableSwitch: Switch

    val db = FirebaseFirestore.getInstance()
    val teacher_collection = db.collection("teachers")
    val student_collection = db.collection("student")
    var updatedProgressDilaog = CustomProgressDialog()


    var email:String ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_control_classes_and_others)

        back_top_bar_img = findViewById(R.id.back_top_bar_img)
        title_top_bar_txt = findViewById(R.id.title_top_bar_txt)

        email = intent.getStringExtra("Email")


        enableDisableSwitch = findViewById(R.id.enableDisableSwitch)

        title_top_bar_txt.setText("All Controls")
        back_top_bar_img.setOnClickListener {
            finish()
        }

        enableDisableSwitch.setOnClickListener {

            if (enableDisableSwitch.isChecked)
            {
                mActiveTheClass(it)
            }
            else{
                mDeActiveTheClass(it)
            }
        }

        enableDisableSwitch.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {



            }
        })
        mCallApiFireStore()

    }

    fun mActiveTheClass(buttonView:View)
    {
        updatedProgressDilaog.show(this@ActivityAdminControlClassesAndOthers)
        if (Constant.hasNetworkAvailable(applicationContext)) {
            val lucknowRef = db.collection("teachers").document(email.toString())
            lucknowRef.update("class_status", "1").addOnSuccessListener {
                updatedProgressDilaog.dialog.dismiss()
                Constant.showMessage(buttonView, "class disabled successfully")
            }
                .addOnFailureListener {
                    updatedProgressDilaog.dialog.dismiss()
                    Constant.showMessage(buttonView, "Error updating document")
                }
        }
        else {
            Toast.makeText(applicationContext, "No network available!", Toast.LENGTH_SHORT).show()
        }
    }

    fun mDeActiveTheClass(buttonView:View)
    {
        updatedProgressDilaog.show(this@ActivityAdminControlClassesAndOthers)
        if (Constant.hasNetworkAvailable(applicationContext)) {
            val lucknowRef = db.collection("teachers").document(email.toString())
            lucknowRef.update("class_status", "0").addOnSuccessListener {
                updatedProgressDilaog.dialog.dismiss()
                Constant.showMessage(buttonView, "class enable successfully")
            }
                .addOnFailureListener {
                    updatedProgressDilaog.dialog.dismiss()
                    Constant.showMessage(buttonView, "Error updating document")
                }
        }
        else {
            Toast.makeText(applicationContext, "No network available!", Toast.LENGTH_SHORT).show()
        }
    }


    fun mCallApiFireStore()
    {
        updatedProgressDilaog.show(this)

        teacher_collection.document(email.toString()).get().addOnCompleteListener(object :
            OnCompleteListener<DocumentSnapshot> {
            override fun onComplete(@NonNull task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    updatedProgressDilaog.dialog.dismiss()

                    val document = task.getResult()
                    if (document!!.exists()) {
                        val class_status = document.get("class_status")

                        if (class_status!!.equals("0"))
                        {
                            enableDisableSwitch.isChecked = false
                        }
                        else{
                            enableDisableSwitch.isChecked = true
                        }


                    } else {
                        Toast.makeText(applicationContext, "type is not valid", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, task.getException().toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}