package com.happiness.eduvidhya.fragments

import android.R.attr.description
import android.app.Activity
import android.content.Context
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.happiness.eduvidhya.Interface.RemoveClickListener
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.datamodels.BatchListDescriptionDataModel
import com.happiness.eduvidhya.datamodels.ClassroomDetailsModel
import com.happiness.eduvidhya.datamodels.UserDetailDataModel
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog


class FragmentCreateClassRoom : Fragment(), RemoveClickListener {

    private lateinit var enter_classroom_name: EditText
    private lateinit var subject_name_edit: EditText
    private lateinit var topic_name_edit: EditText
    private lateinit var add_batch: ImageView
    private lateinit var batch_list_recycler: RecyclerView
    private lateinit var create_classroom_btn: Button
    private lateinit var create_batch_text: TextView
    val db = FirebaseFirestore.getInstance()
    val teacher_collection = db.collection("teachers")
    var create_classroom :DocumentReference?=null
    private var mRecyclerAdapter: BatchListRecyclerAdapter? = null
    var myList: ArrayList<BatchListDescriptionDataModel> = ArrayList()


    var updatedProgressDilaog = CustomProgressDialog()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v: View = inflater.inflate(R.layout.fragment_create_classroom, container, false)

        enter_classroom_name = v.findViewById(R.id.enter_classroom_name)
        subject_name_edit = v.findViewById(R.id.subject_name_edit)
        topic_name_edit = v.findViewById(R.id.topic_name_edit)
        add_batch = v.findViewById(R.id.add_batch_img)
        batch_list_recycler = v.findViewById(R.id.recycler_view)
        create_classroom_btn = v.findViewById(R.id.create_classroom_btn)
        create_batch_text = v.findViewById(R.id.create_batch_text)

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        batch_list_recycler.setLayoutManager(layoutManager)

        create_classroom_btn.setOnClickListener {

            if (enter_classroom_name.text.toString().isEmpty()) {
                showMessage("Enter Classroom Name")
            } else if (subject_name_edit.text.toString().isEmpty()) {
                showMessage("Enter Subject Name")
            } else if (topic_name_edit.text.toString().isEmpty()) {
                showMessage("Enter Topic")
            } else {

                val mySharedPreferences = requireActivity().getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
                val email = mySharedPreferences.getString("user_email", "")
                mCallApiFireStore(email.toString())




            }

        }
        add_batch.setOnClickListener {
            mRecyclerAdapter = BatchListRecyclerAdapter(requireActivity(), myList, this,enter_classroom_name.text.toString(), subject_name_edit.text.toString(), topic_name_edit.text.toString())
            batch_list_recycler.adapter = mRecyclerAdapter
            val mLog = BatchListDescriptionDataModel()
          //  mLog.title = "Batch 1"
            myList.add(mLog)
           mRecyclerAdapter!!.notifyData(myList)

        }
        return v
    }

    fun mCallApiFireStore(email:String)
    {
        activity?.let { updatedProgressDilaog.show(it) }

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
                            val classroom_name = enter_classroom_name.text.toString()
                            val subject_name = subject_name_edit.text.toString()
                            val topic_name = topic_name_edit.text.toString()
                            val classroom_detail = ClassroomDetailsModel(classroom_name, subject_name, topic_name,"")

                            if (Constant.hasNetworkAvailable(requireActivity())) {

                                updatedProgressDilaog.show(requireActivity())

                                create_classroom = teacher_collection.document(email!!).collection("classrooms").document(enter_classroom_name.text.toString())
                                create_classroom!!.set(classroom_detail).addOnSuccessListener {

                                    updatedProgressDilaog.dialog.dismiss()
                                    create_batch_text.visibility = View.VISIBLE

                                    create_classroom_btn.isEnabled = false
                                    add_batch.visibility = View.VISIBLE
                                    Toast.makeText(context, "Successfully created class", Toast.LENGTH_SHORT).show() }.addOnFailureListener {
                                }
                            }
                            else
                            {
                                Toast.makeText(activity, "No network available!", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            Toast.makeText(activity!!, "You have not permission to create a class", Toast.LENGTH_SHORT).show()
                        }


                    } else {
                        Toast.makeText(activity!!, "type is not valid", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(activity!!, task.getException().toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun OnRemoveClick(index: Int) {
        myList.removeAt(index)
        mRecyclerAdapter!!.notifyData(myList)
    }

    fun showMessage(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}