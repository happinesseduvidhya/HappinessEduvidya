package com.happiness.eduvidhya.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.happiness.eduvidhya.Interface.CallBackFirst
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.adapters.AdapterAllUsersOrFaculties
import com.happiness.eduvidhya.datamodels.ListOfBatchesModel
import com.happiness.eduvidhya.datamodels.ModelUserInfo
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog
import kotlinx.android.synthetic.main.activity_users.*


class ActivityListUsersOrFaculty : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    val faculties_collection = db.collection("Faculties")
    val users_collection = db.collection("Users")

    // top bar
    lateinit var back_top_bar_img: ImageView
    lateinit var title_top_bar_txt: TextView

    var updatedProgressDilaog = CustomProgressDialog()

    var mArrayModelUserInfo: ModelUserInfo? = null
    var list_of_batches: ArrayList<ListOfBatchesModel>? = null


    var listOfUsers: ArrayList<ModelUserInfo>? = null

    var type: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        back_top_bar_img = findViewById(R.id.back_top_bar_img)
        title_top_bar_txt = findViewById(R.id.title_top_bar_txt)

        list_of_batches = ArrayList<ListOfBatchesModel>()

        val layoutManager = LinearLayoutManager(this@ActivityListUsersOrFaculty)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        all_users_recyclerview.setLayoutManager(layoutManager)

        type = intent.getStringExtra("Type")

        if (type.equals("Users")) {
            title_top_bar_txt.setText("All Users")

            mUsersAll()
        }
        if (type.equals("Faculties")) {
            title_top_bar_txt.setText("All Faculties")

            mFaculties()
        }

        back_top_bar_img.setOnClickListener {
            finish()
        }

    }

    fun mUsersAll() {
        updatedProgressDilaog.show(this)
        if (Constant.hasNetworkAvailable(applicationContext)) {

//            users_collection.get()
//                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
//                    override fun onComplete(@NonNull task: Task<QuerySnapshot>) {
//
//                        updatedProgressDilaog.dialog.dismiss()
//                        if (task.isSuccessful()) {
//
//                            listOfUsers = ArrayList<ModelUserInfo>()
//                            listOfUsers!!.clear()
//
//                            for (document in task.result!!) {
//
//                                val data = document.data
//                                val emai = data.get("email")
//                                val name = data.get("name")
//                                val class_status = data.get("class_status")
//                                val password = data.get("password")
//                                val modelUserInfo = ModelUserInfo(name.toString(),emai.toString(),password.toString(),class_status.toString())
//                                listOfUsers!!.add(modelUserInfo)
//                            }
//                            mRecyclerAdapter = AdapterAllUsersOrFaculties(
//                                this@ActivityListUsersOrFaculty,
//                                listOfUsers,
//                                type
//                            )
//                            all_users_recyclerview.adapter = mRecyclerAdapter
//                            mRecyclerAdapter!!.callBackFirst = object : CallBackFirst {
//                                override fun mCallBack(
//                                    position: String,
//                                    firstString: String,
//                                    secondString: String
//                                ) {
//
//                                    if (secondString.equals("delete")) {
//                                        mAlertShowDelete(firstString,type.toString())
//
//                                    } else {
//                                        mCallApiFireStoreGetUsers(secondString)
//                                    }
//
//                                }
//
//                            }
//                        } else {
//                            Log.d("TAG", "Error getting documents: ", task.getException())
//                        }
//                    }
//                }).addOnFailureListener { exception ->
//                updatedProgressDilaog.dialog.dismiss()
//                Log.w("TAG", "Error getting documents: ", exception)
//        }

            users_collection.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    updatedProgressDilaog.dialog.dismiss()
                    listOfUsers = ArrayList<ModelUserInfo>()
                    listOfUsers!!.clear()
                    for (data in value!!.documents) {
                        if (data.exists()) {

                            val emai = data.get("email")
                            val name = data.get("name")
                            val class_status = data.get("class_status")
                            val password = data.get("password")
                            val modelUserInfo = ModelUserInfo(
                                name.toString(),
                                emai.toString(),
                                password.toString(),
                                class_status.toString()
                            )
                            listOfUsers!!.add(modelUserInfo)
                        }
                    }
                    val mRecyclerAdapter = AdapterAllUsersOrFaculties(
                        this@ActivityListUsersOrFaculty,
                        listOfUsers,
                        type
                    )
                    all_users_recyclerview.adapter = mRecyclerAdapter
                    mRecyclerAdapter!!.callBackFirst = object : CallBackFirst {
                        override fun mCallBack(
                            position: String,
                            firstString: String,
                            secondString: String
                        ) {
                            if (secondString.equals("delete")) {
                                mAlertShowDelete(firstString, type.toString())

                            } else {
                                mCallApiFireStoreGetUsers(secondString)
                            }

                        }

                    }
                }

            })

        } else {
            Toast.makeText(applicationContext, "No network available!", Toast.LENGTH_SHORT).show()
        }
    }

    fun mFaculties() {
        updatedProgressDilaog.show(this)
        if (Constant.hasNetworkAvailable(applicationContext)) {

//            faculties_collection.get()
//                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
//                    override fun onComplete(@NonNull task: Task<QuerySnapshot>) {
//
//                        updatedProgressDilaog.dialog.dismiss()
//                        if (task.isSuccessful()) {
//
//                            listOfUsers = ArrayList<ModelUserInfo>()
//                            listOfUsers!!.clear()
//                            for (document in task.result!!) {
//
//
//                                val data = document.data
//                                val emai = data.get("email")
//                                val name = data.get("name")
//                                val class_status = data.get("class_status")
//                                val password = data.get("password")
//                                val modelUserInfo = ModelUserInfo(name.toString(),emai.toString(),password.toString(),class_status.toString())
//                                listOfUsers!!.add(modelUserInfo)
//
//
//                            }
//                            mRecyclerAdapter = AdapterAllUsersOrFaculties(
//                                this@ActivityListUsersOrFaculty,
//                                listOfUsers,
//                                type
//                            )
//                            all_users_recyclerview.adapter = mRecyclerAdapter
//                            mRecyclerAdapter!!.callBackFirst = object : CallBackFirst {
//                                override fun mCallBack(
//                                    position: String,
//                                    firstString: String,
//                                    secondString: String
//                                ) {
//                                    if (secondString.equals("delete")) {
//                                        mAlertShowDelete(firstString,type.toString())
//
//                                    } else {
//                                        mCallApiFireStoreGetFaculties(secondString)
//                                    }
//                                }
//
//                            }
//                        } else {
//                            Log.d("TAG", "Error getting documents: ", task.getException())
//                        }
//                    }
//                }).addOnFailureListener { exception ->
//                updatedProgressDilaog.dialog.dismiss()
//                Log.w("TAG", "Error getting documents: ", exception)
//            }

            faculties_collection.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    updatedProgressDilaog.dialog.dismiss()
                            listOfUsers = ArrayList<ModelUserInfo>()
                            listOfUsers!!.clear()

                    for (data in value!!.documents) {
                        if (data.exists()) {

                            val emai = data.get("email")
                            val name = data.get("name")
                            val class_status = data.get("class_status")
                            val password = data.get("password")
                            val modelUserInfo = ModelUserInfo(
                                name.toString(),
                                emai.toString(),
                                password.toString(),
                                class_status.toString()
                            )
                            listOfUsers!!.add(modelUserInfo)
                        }
                    }
                    val mRecyclerAdapter = AdapterAllUsersOrFaculties(
                        this@ActivityListUsersOrFaculty,
                        listOfUsers,
                        type
                    )
                    all_users_recyclerview.adapter = mRecyclerAdapter
                            mRecyclerAdapter.callBackFirst = object : CallBackFirst {
                                override fun mCallBack(
                                    position: String,
                                    firstString: String,
                                    secondString: String
                                ) {
                                    if (secondString.equals("delete")) {
                                        mAlertShowDelete(firstString,type.toString())

                                    } else {
                                        mCallApiFireStoreGetFaculties(secondString)
                                    }
                                }

                            }
                }

            })


        } else {
            Toast.makeText(applicationContext, "No network available!", Toast.LENGTH_SHORT).show()
        }
    }




    fun mCallApiFireStoreGetFaculties(email: String) {

        updatedProgressDilaog.show(this)

        faculties_collection.document(email.toString()).get().addOnCompleteListener(object :
            OnCompleteListener<DocumentSnapshot> {
            override fun onComplete(@NonNull task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    updatedProgressDilaog.dialog.dismiss()

                    val document = task.getResult()
                    if (document!!.exists()) {
                        val name = document.get("name")
                        val email = document.get("email")
                        val password = document.get("password")

                        val intent =
                            Intent(applicationContext, ActivityUserOrFacultyAddOrUpdate::class.java)
                        intent.putExtra("Type", "Faculty")
                        intent.putExtra("Name", name.toString())
                        intent.putExtra("Email", email.toString())
                        intent.putExtra("Pass", password.toString())
                        startActivity(intent)


                    } else {
                        Toast.makeText(applicationContext, "type is not valid", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        task.getException().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })



        faculties_collection.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                updatedProgressDilaog.dialog.dismiss()
                listOfUsers = ArrayList<ModelUserInfo>()
                listOfUsers!!.clear()
                for (data in value!!.documents) {
                    if (data.exists()) {

                        val emai = data.get("email")
                        val name = data.get("name")
                        val class_status = data.get("class_status")
                        val password = data.get("password")
                        val modelUserInfo = ModelUserInfo(
                            name.toString(),
                            emai.toString(),
                            password.toString(),
                            class_status.toString()
                        )
                        listOfUsers!!.add(modelUserInfo)
                    }
                }
                val mRecyclerAdapter = AdapterAllUsersOrFaculties(
                    this@ActivityListUsersOrFaculty,
                    listOfUsers,
                    type
                )
                all_users_recyclerview.adapter = mRecyclerAdapter
                all_users_recyclerview.adapter = mRecyclerAdapter
                mRecyclerAdapter.callBackFirst = object : CallBackFirst {
                    override fun mCallBack(
                        position: String,
                        firstString: String,
                        secondString: String
                    ) {
                        if (secondString.equals("delete")) {
                            mAlertShowDelete(firstString,type.toString())

                        } else {
                            mCallApiFireStoreGetFaculties(secondString)
                        }
                    }

                }
            }

        })

    }

    fun mCallApiFireStoreGetUsers(email: String) {
        updatedProgressDilaog.show(this)

        users_collection.document(email.toString()).get().addOnCompleteListener(object :
            OnCompleteListener<DocumentSnapshot> {
            override fun onComplete(@NonNull task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    updatedProgressDilaog.dialog.dismiss()

                    val document = task.getResult()
                    if (document!!.exists()) {
                        val name = document.get("name")
                        val email = document.get("email")
                        val password = document.get("password")

                        val intent =
                            Intent(applicationContext, ActivityUserOrFacultyAddOrUpdate::class.java)
                        intent.putExtra("Type", "User")
                        intent.putExtra("Name", name.toString())
                        intent.putExtra("Email", email.toString())
                        intent.putExtra("Pass", password.toString())
                        startActivity(intent)


                    } else {
                        Toast.makeText(applicationContext, "type is not valid", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        task.getException().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }


    private fun mAlertShowDelete(email: String,type:String) {


        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle(R.string.dialogTitle)
        //set message for alert dialog
        builder.setMessage(R.string.dialogMessage)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->

            if (type.equals("Users"))
            {
                mDeleteFunUserOrFunction(email)
            }
            else{
                mDeleteFaculty(email)
            }

        }

        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            Toast.makeText(applicationContext, "clicked No", Toast.LENGTH_LONG).show()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun mDeleteFunUserOrFunction(email: String) {
        updatedProgressDilaog.show(this)
        users_collection.document(email).delete().addOnSuccessListener {
            updatedProgressDilaog.dialog.dismiss()
                Toast.makeText(applicationContext, "Deleted successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
            updatedProgressDilaog.dialog.dismiss()
                Toast.makeText(applicationContext, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun mDeleteFaculty(email: String) {
        updatedProgressDilaog.show(this)
        faculties_collection.document(email).delete().addOnSuccessListener {
            updatedProgressDilaog.dialog.dismiss()
            Toast.makeText(applicationContext, "Deleted successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            updatedProgressDilaog.dialog.dismiss()
            Toast.makeText(applicationContext, it.message.toString(), Toast.LENGTH_SHORT).show()
        }

    }


}