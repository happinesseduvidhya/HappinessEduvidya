package com.happiness.eduvidhya.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.ArrayAdapter
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.adapters.AdapterShowBatches
import com.happiness.eduvidhya.datamodels.ListOfBatchesModel
import com.happiness.eduvidhya.datamodels.ModelUserInfo
import com.happiness.eduvidhya.datamodels.StudentEmail
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog
import kotlinx.android.synthetic.main.activity_add_student_by_faculty.*
import kotlinx.android.synthetic.main.activity_batches_meeting_see_by_user.*

class ActivityAllBatchesMeetingSeeByUser : AppCompatActivity() {

    // top bar
    lateinit var back_top_bar_img: ImageView
    lateinit var title_top_bar_txt: TextView

    lateinit var spinner_faculty: Spinner
    lateinit var spinner_class: Spinner
    lateinit var spinnerBatches: Spinner
    lateinit var nextBtn: Button
    var mArrayFaculties = ArrayList<String>()
    var mArrayClasses = ArrayList<String>()
    var mArrayBatches = ArrayList<String>()

    val db = FirebaseFirestore.getInstance()
    val classes = db.collection("Classes")
    val users_collection = db.collection("Users")
    var updatedProgressDilaog = CustomProgressDialog()


    var strFacultyEmail: String = ""
    var strFacultyClass: String = ""
    var strFacultyClassBatchID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batches_meeting_see_by_user)

        back_top_bar_img = findViewById(R.id.back_top_bar_img)
        title_top_bar_txt = findViewById(R.id.title_top_bar_txt)

        spinner_faculty = findViewById(R.id.spinner_faculty)
        spinner_class = findViewById(R.id.spinner_class)
        spinnerBatches = findViewById(R.id.spinner_batches)

        nextBtn = findViewById(R.id.nextBtn)

        mArrayFaculties = ArrayList<String>()
        mArrayClasses = ArrayList<String>()
        mArrayBatches = ArrayList<String>()

        spinner_faculty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext, "enter ", Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                strFacultyEmail = mArrayFaculties.get(position)
                mGetFacultiesMakeClassesForUser(strFacultyEmail)

            }

        }
        spinner_class.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext, "enter ", Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                strFacultyClass = mArrayClasses.get(position)
                mGetFacultiesClassesBatchesForUser(strFacultyClass)


            }

        }

        spinnerBatches.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext, "enter ",Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


                strFacultyClassBatchID = mArrayBatches.get(position)


            }

        }

        title_top_bar_txt.setText("Select your batch")

        back_top_bar_img.setOnClickListener {
            finish()
        }

        nextBtn.setOnClickListener {

            if (!strFacultyEmail.equals(""))
            {
                if (!strFacultyClass.equals(""))
                {
                    if (!strFacultyClassBatchID.equals(""))
                    {
                        val intent = Intent(it.context, ActivityOneBatcheAllMeetingSeeByUser::class.java)
                        intent.putExtra("Email", strFacultyEmail)
                        intent.putExtra("ClassName", strFacultyClass)
                        intent.putExtra("ClassBatchID", strFacultyClassBatchID)
                        it.context.startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,"select your batch",Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this,"select your faculty class",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"select your faculty",Toast.LENGTH_SHORT).show()
            }
        }

        mGetFacultiesUser()

    }

    private fun mGetFacultiesUser() {

        if (Constant.hasNetworkAvailable(applicationContext)) {
            updatedProgressDilaog.show(this)
            val mySharedPreferences = getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
            val email = mySharedPreferences.getString("user_email", "")

            users_collection.document(email.toString()).collection("Faculties").addSnapshotListener(object :
                EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    updatedProgressDilaog.dialog.dismiss()
                    mArrayFaculties.clear()
                    for (data in value!!.documents) {
                        if (data.exists()) {
                            val strEmail = data.id
                            mArrayFaculties.add(strEmail)
                        }
                    }
                    val adapter = ArrayAdapter(
                        this@ActivityAllBatchesMeetingSeeByUser, R.layout.custom_spinner,
                        mArrayFaculties
                    ) // where array_name consists of the items to show in Spinner
                    adapter.setDropDownViewResource(R.layout.custom_spinner)
                    spinner_faculty.setAdapter(adapter)
                }

            })
        } else {
            Toast.makeText(applicationContext, "No network available!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun mGetFacultiesMakeClassesForUser(FaultyEmal:String) {

        if (Constant.hasNetworkAvailable(applicationContext)) {

            updatedProgressDilaog.show(this)

            val mySharedPreferences = getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
            val email = mySharedPreferences.getString("user_email", "")

            classes.document(FaultyEmal).collection("classrooms").addSnapshotListener(object :
                EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    updatedProgressDilaog.dialog.dismiss()
                    mArrayClasses.clear()
                    for (data in value!!.documents) {
                        if (data.exists()) {
                            val strEmail = data.id
                            mArrayClasses.add(strEmail)
                        }
                    }
                    val adapter = ArrayAdapter(
                        this@ActivityAllBatchesMeetingSeeByUser, R.layout.custom_spinner,
                        mArrayClasses
                    ) // where array_name consists of the items to show in Spinner
                    adapter.setDropDownViewResource(R.layout.custom_spinner)
                    spinner_class.setAdapter(adapter)
                }

            })
        } else {
            Toast.makeText(applicationContext, "No network available!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun mGetFacultiesClassesBatchesForUser(FaultyClass:String) {

        if (Constant.hasNetworkAvailable(applicationContext)) {

            updatedProgressDilaog.show(this)

            val mySharedPreferences = getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
            val email = mySharedPreferences.getString("user_email", "")

            classes.document(strFacultyEmail).collection("classrooms").document(FaultyClass).collection("Batches").addSnapshotListener(object :
                EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    updatedProgressDilaog.dialog.dismiss()
                    mArrayBatches.clear()
                    for (data in value!!.documents) {
                        if (data.exists()) {
                            val strEmail = data.id
                            mArrayBatches.add(strEmail)
                        }
                    }
                    val adapter = ArrayAdapter(
                        this@ActivityAllBatchesMeetingSeeByUser, R.layout.custom_spinner,
                        mArrayBatches
                    )
                    adapter.setDropDownViewResource(R.layout.custom_spinner)
                    spinner_batches.setAdapter(adapter)
                }

            })
        } else {
            Toast.makeText(applicationContext, "No network available!", Toast.LENGTH_SHORT).show()
        }

    }






}