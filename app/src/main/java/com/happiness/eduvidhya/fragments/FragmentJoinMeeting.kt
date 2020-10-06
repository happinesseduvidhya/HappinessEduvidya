package com.happiness.eduvidhya.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.happiness.eduvidhya.activities.ActivityWebView
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils


class FragmentJoinMeeting : Fragment() {

    private lateinit var enter_join_meeting_code: EditText
    private lateinit var join_meeting_btn: Button
    private lateinit var webview_screen: WebView
    private var meeting_id: String? = null

      var updatedProgressDilaog = CustomProgressDialog()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_join_meeting, container, false)

        join_meeting_btn = v.findViewById(R.id.enter_join_meeting_btn)
        enter_join_meeting_code = v.findViewById(R.id.enter_join_meeting_edit)
        join_meeting_btn.setOnClickListener {
            meeting_id = enter_join_meeting_code.text.toString()

            if (meeting_id!!.isEmpty())
            {
                Constant.showMessage(it,"enter the meeting ID")
            }
            else{
                join_meeting(meeting_id.toString())
            }

        }

        return v
    }

    private fun join_meeting(meeting_id: String) {

        if (Constant.hasNetworkAvailable(requireActivity())) {

            updatedProgressDilaog.show(requireActivity())

            val method = "join"
            val parameters = "fullName=gurpreet&meetingID="+ meeting_id +"&password=22&redirect=true"
            //        val checksum = DigestUtils.shaHex(method + parameters + Constant.shared_secret)
            val checksum = String(Hex.encodeHex(DigestUtils.sha(method + parameters + Constant.shared_secret)))
            updatedProgressDilaog.dialog.dismiss()
            val s="https://noblekeyz.com/bigbluebutton/api/join?fullName=gurpreet&meetingID="+meeting_id+"&password=22&redirect=true&checksum="+checksum
            val i = Intent(activity, ActivityWebView::class.java)
            i.putExtra("url", s)
            startActivity(i)

        }
        else{
            Toast.makeText(activity, "No network available!", Toast.LENGTH_SHORT).show()
        }


    }
}


//Recording id
//https://noblekeyz.com/bigbluebutton/api/getRecordings?meetingID=random-294211&recordID=random-669857&checksum=2579ff3d244e6bbc79bae3d31b5d833022fcd41e
//https://noblekeyz.com/bigbluebutton/api/getRecordings?meetingID=random-7554442&recordID=random-7554448&checksum=ad22c3c2c47ab886373c010cd60c5d2483a3d756