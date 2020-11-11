package com.happiness.eduvidhya.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog
import kotlinx.android.synthetic.main.activity_admin_control_classes_and_others.*

class ActivityAdminControlClassesAndOthers : AppCompatActivity() {

    // top bar
    lateinit var back_top_bar_img: ImageView
    lateinit var title_top_bar_txt: TextView

    lateinit var enableDisableSwitch: Switch


    val db = FirebaseFirestore.getInstance()
    val faculties = db.collection("Faculties")
    val student_collection = db.collection("Users")
    var updatedProgressDilaog = CustomProgressDialog()


    var email:String ?= null
    var strType:String ?= null
    var strKey:String ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_control_classes_and_others)

        back_top_bar_img = findViewById(R.id.back_top_bar_img)
        title_top_bar_txt = findViewById(R.id.title_top_bar_txt)

        email = intent.getStringExtra("Email")
        strType = intent.getStringExtra("Type")

        if (strType.equals("Users"))
        {
            card_view.visibility = View.GONE
        }

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

        enableDisableSwitch = findViewById(R.id.enableDisableSwitch)


        enableDisableSwitch_add_student.setOnClickListener {

            Constant.showMessage(it,"not working...")
            enableDisableSwitch_add_student.isChecked = false

        }

        switch_case_login_approval.setOnClickListener {

            if (switch_case_login_approval.isChecked)
            {
                mActiveLoginApproval(it)
            }
            else{
                mDeActiveLoginApproval(it)
            }

        }


        enableDisableSwitch.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
            }
        })
        mCallApiFireStore()

    }

    fun mActiveLoginApproval(buttonView:View)
    {
        updatedProgressDilaog.show(this@ActivityAdminControlClassesAndOthers)

        if (Constant.hasNetworkAvailable(applicationContext)) {
            val lucknowRef = db.collection(strType.toString()).document(email.toString())
            lucknowRef.update("admin_approvable", "1").addOnSuccessListener {
                updatedProgressDilaog.dialog.dismiss()
                Constant.showMessage(buttonView, "login enabled successfully")
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

    fun mDeActiveLoginApproval(buttonView:View)
    {
        updatedProgressDilaog.show(this@ActivityAdminControlClassesAndOthers)
        if (Constant.hasNetworkAvailable(applicationContext)) {
            val lucknowRef = db.collection(strType.toString()).document(email.toString())
            lucknowRef.update("admin_approvable", "0").addOnSuccessListener {
                updatedProgressDilaog.dialog.dismiss()
                Constant.showMessage(buttonView, "login disabled successfully")
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

    fun mActiveTheClass(buttonView:View)
    {
        updatedProgressDilaog.show(this@ActivityAdminControlClassesAndOthers)
        if (Constant.hasNetworkAvailable(applicationContext)) {
            val lucknowRef = db.collection(strType.toString()).document(email.toString())
            lucknowRef.update("class_status", "1").addOnSuccessListener {
                updatedProgressDilaog.dialog.dismiss()
                Constant.showMessage(buttonView, "class enabled successfully")
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
            val lucknowRef = db.collection(strType.toString()).document(email.toString())
            lucknowRef.update("class_status", "0").addOnSuccessListener {
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


    fun mCallApiFireStore()
    {
        updatedProgressDilaog.show(this)

        val type = db.collection(strType.toString()).document(email.toString())
        type.get().addOnCompleteListener(object :
            OnCompleteListener<DocumentSnapshot> {
            override fun onComplete(@NonNull task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    updatedProgressDilaog.dialog.dismiss()

                    val document = task.getResult()
                    if (document!!.exists()) {
                        val class_status = document.get("class_status")
                        val admin_approvable = document.get("admin_approvable")

                        if (admin_approvable!!.equals("1")){
                            switch_case_login_approval.isChecked = true
                        }
                        else{
                            switch_case_login_approval.isChecked = false
                        }

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