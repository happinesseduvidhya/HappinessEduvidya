package com.happiness.eduvidhya.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.happiness.eduvidhya.Interface.CallBackFirst
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.activities.ActivityCreateBatchByFaculty
import com.happiness.eduvidhya.adapters.AdapterAllUsersOrFaculties
import com.happiness.eduvidhya.adapters.AdapterShowBatches
import com.happiness.eduvidhya.datamodels.ListOfBatchesModel
import com.happiness.eduvidhya.datamodels.ModelUserInfo
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog
import kotlinx.android.synthetic.main.activity_users.*

class FragementShowBatches : Fragment() {

    private var mRecyclerAdapter: AdapterShowBatches? = null
    private lateinit var show_batches_recycler:RecyclerView
    var list_of_batches: ArrayList<ListOfBatchesModel>? = null
    var detail_db: ListOfBatchesModel? = null
    val db = FirebaseFirestore.getInstance()
    val classes = db.collection("Classes")


    // top bar
    lateinit var back_top_bar_img: ImageView
    lateinit var title_top_bar_txt: TextView
    lateinit var no_batches_txt: TextView
    lateinit var createbatchBtn: Button

    var updatedProgressDilaog = CustomProgressDialog()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_show_batches, container, false)

        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        list_of_batches = ArrayList<ListOfBatchesModel>()
        show_batches_recycler = v.findViewById(R.id.show_batches_recycler)
        back_top_bar_img = v.findViewById(R.id.back_top_bar_img)
        title_top_bar_txt= v.findViewById(R.id.title_top_bar_txt)
        no_batches_txt= v.findViewById(R.id.no_batches_txt)
        createbatchBtn= v.findViewById(R.id.createbatchBtn)

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        show_batches_recycler.setLayoutManager(layoutManager)
        val mySharedPreferences =
            requireActivity().getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
        val email = mySharedPreferences.getString("user_email", "")
        val get_classroom=requireArguments().getString("get_class_name")

        createbatchBtn.setOnClickListener {
            val intent = Intent(requireActivity(), ActivityCreateBatchByFaculty::class.java)
            intent.putExtra("className",get_classroom.toString())
            startActivity(intent)
        }

        updatedProgressDilaog.show(requireActivity())

        if (Constant.hasNetworkAvailable(requireActivity())) {

            classes.document(email!!).collection("classrooms").document(get_classroom.toString()).collection("Batches").addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    updatedProgressDilaog.dialog.dismiss()
                    list_of_batches!!.clear()
                    for (data in value!!.documents) {
                        detail_db = ListOfBatchesModel(data.id)
                        list_of_batches!!.add(detail_db!!)
                    }

                    if (list_of_batches!!.size == 0)
                    {
                        no_batches_txt.visibility = View.VISIBLE

                    }
                    else{
                        no_batches_txt.visibility = View.GONE
                        mRecyclerAdapter = AdapterShowBatches(activity!!,list_of_batches,get_classroom.toString())
                        show_batches_recycler.adapter = mRecyclerAdapter
                    }
                }

            })

        }else {
            Toast.makeText(activity, "No network available", Toast.LENGTH_SHORT).show()

        }

        title_top_bar_txt.setText("All Batches")

        back_top_bar_img.setOnClickListener {
            requireActivity().finish()

        }

        return v
    }}