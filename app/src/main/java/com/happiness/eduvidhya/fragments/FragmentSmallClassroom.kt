package com.happiness.eduvidhya.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.happiness.eduvidhya.activities.ActivityBaseForFragment
import com.happiness.eduvidhya.R


class FragmentSmallClassroom : Fragment() {
private lateinit var create_batch_btn:Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_small_classroom, container, false)
        create_batch_btn=v.findViewById(R.id.create_classroom_btn)
        create_batch_btn.setOnClickListener {
            val i = Intent(activity, ActivityBaseForFragment::class.java)
            i.putExtra("checkPage", "createClassroom")
            startActivity(i)
            //Toast.makeText(activity, "skdnskncfkd", Toast.LENGTH_SHORT).show()
        }
        return v


    }
}