package com.happiness.eduvidhya.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.happiness.eduvidhya.R

class FragmentCreateBatch : Fragment() {
    private lateinit var batch_name:EditText
    private lateinit var subject_name:EditText
    private lateinit var topic_name:EditText
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            // Inflate the layout for this fragment
            val v: View = inflater.inflate(R.layout.fragment_create_batch, container, false)
            batch_name=v.findViewById(R.id.enter_batch_name)
            batch_name=v.findViewById(R.id.subject_name_edit)
            batch_name=v.findViewById(R.id.topic_name_edit)
            return v
        }
}