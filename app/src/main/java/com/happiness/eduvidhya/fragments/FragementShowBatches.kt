package com.happiness.eduvidhya.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.adapters.AdapterAllScheduledMeetings
import com.happiness.eduvidhya.adapters.AdapterShowBatches
import com.happiness.eduvidhya.datamodels.ClassroomDetailsModel
import com.happiness.eduvidhya.datamodels.ListOfBatchesModel

class FragementShowBatches : Fragment() {
    private var mRecyclerAdapter: AdapterShowBatches? = null
    private lateinit var show_batches_recycler:RecyclerView
    var list_of_batches: ArrayList<ListOfBatchesModel>? = null
    var detail_db: ListOfBatchesModel? = null
    val db = FirebaseFirestore.getInstance()
    val teacher_collection = db.collection("teachers")

    // top bar
    lateinit var back_top_bar_img: ImageView
    lateinit var title_top_bar_txt: TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_show_batches, container, false)

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

        teacher_collection.document(email!!).collection("classrooms").document(get_classroom.toString()).collection("Batches").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
//                    Log.d("TAG", "${document.id} => ${document.data}")

                    detail_db = ListOfBatchesModel(document.id)
                    list_of_batches!!.add(detail_db!!)

                }

                mRecyclerAdapter = AdapterShowBatches(requireActivity(), list_of_batches)
                show_batches_recycler.adapter = mRecyclerAdapter



            }.addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }
        title_top_bar_txt.setText("All Batches")

        back_top_bar_img.setOnClickListener {
            requireActivity().finish()
        }

        return v
    }}