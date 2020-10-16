package com.happiness.eduvidhya.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.adapters.AdapterShowBatches
import com.happiness.eduvidhya.datamodels.ListOfBatchesModel
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog

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

    var updatedProgressDilaog = CustomProgressDialog()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_show_batches, container, false)

        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        list_of_batches = ArrayList<ListOfBatchesModel>()
        show_batches_recycler = v.findViewById(R.id.show_batches_recycler)
        back_top_bar_img = v.findViewById(R.id.back_top_bar_img)
        title_top_bar_txt= v.findViewById(R.id.title_top_bar_txt)

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        show_batches_recycler.setLayoutManager(layoutManager)
        val mySharedPreferences =
            requireActivity().getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
        val email = mySharedPreferences.getString("user_email", "")
        val get_classroom=requireArguments().getString("get_class_name")

        updatedProgressDilaog.show(requireActivity())

        if (Constant.hasNetworkAvailable(requireActivity())) {

            classes.document(email!!).collection("classrooms").document(get_classroom.toString()).collection("Batches").get()
                .addOnSuccessListener { documents ->
                    updatedProgressDilaog.dialog.dismiss()
                    for (document in documents) {
                        detail_db = ListOfBatchesModel(document.id)
                        list_of_batches!!.add(detail_db!!)
                    }

                    mRecyclerAdapter = AdapterShowBatches(requireActivity(),list_of_batches,get_classroom.toString())
                    show_batches_recycler.adapter = mRecyclerAdapter

                }.addOnFailureListener { exception ->
                    updatedProgressDilaog.dialog.dismiss()
                    Log.w("TAG", "Error getting documents: ", exception)
                }
        }else {
            Toast.makeText(activity, "No network available", Toast.LENGTH_SHORT).show()

        }

        title_top_bar_txt.setText("All Batches")

        back_top_bar_img.setOnClickListener {
            requireActivity().finish()

        }

        return v
    }}