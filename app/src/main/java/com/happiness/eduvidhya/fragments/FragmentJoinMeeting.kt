package com.happiness.eduvidhya.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.happiness.eduvidhya.activities.ActivityWebView
import com.happiness.eduvidhya.R
import com.happiness.eduvidhya.utils.Constant
import com.happiness.eduvidhya.utils.CustomProgressDialog
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils


class FragmentJoinMeeting : Fragment() {

    private lateinit var image_back_arrow: ImageView
    private lateinit var enter_join_meeting_code: EditText
    private lateinit var join_meeting_btn: Button
    private lateinit var webview_screen: WebView
    private var meeting_id: String? = null
    private var classname: String? = null
    private var facultyEmailWhenUserComeOnThisScreen: String? = null

    var updatedProgressDilaog = CustomProgressDialog()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
// Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_join_meeting, container, false)


        image_back_arrow = v.findViewById(R.id.image_back_arrow)
        join_meeting_btn = v.findViewById(R.id.enter_join_meeting_btn)
        enter_join_meeting_code = v.findViewById(R.id.enter_join_meeting_edit)

        val joinMeetingID = requireArguments().getString("subject_name")
        facultyEmailWhenUserComeOnThisScreen = requireArguments().getString("topic_name")
        classname = requireArguments().getString("classname")



        val james = requireActivity()

        if (joinMeetingID != null)
        {
            enter_join_meeting_code.setText(joinMeetingID)
            enter_join_meeting_code.isEnabled = false
        }

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

        image_back_arrow.setOnClickListener {
            requireActivity().finish()
        }

        return v
    }

    private fun join_meeting(meeting_id: String) {

        if (Constant.hasNetworkAvailable(requireActivity())) {

            updatedProgressDilaog.show(requireActivity())

            val mySharedPreferences = requireActivity().getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
            val strUserName = mySharedPreferences.getString("user_name", "")
            val type = mySharedPreferences.getString("type", "")
            val name = strUserName!!.replace(" ","")
            val method = "join"

            var parameters:String = ""
            if (type.equals("Faculty"))
            {
                parameters = "fullName="+name.trim()+"&meetingID="+ meeting_id +"&password=mp&redirect=true"
            }
            else{
                parameters = "fullName="+name.trim()+"&meetingID="+ meeting_id +"&password=ap&redirect=true"
            }

            // val checksum = DigestUtils.shaHex(method + parameters + Constant.shared_secret)
            val checksum = String(Hex.encodeHex(DigestUtils.sha(method + parameters + Constant.shared_secret)))
            updatedProgressDilaog.dialog.dismiss()

             var strUrl:String = ""
             if (type.equals("Faculty"))
             {
             strUrl="https://noblekeyz.com/bigbluebutton/api/join?fullName="+name+"&meetingID="+meeting_id+"&password=mp&redirect=true&checksum="+checksum

             }
             else if (type.equals("User"))
             {
             strUrl="https://noblekeyz.com/bigbluebutton/api/join?fullName="+name+"&meetingID="+meeting_id+"&password=ap&redirect=true&checksum="+checksum
             }

//            val s="https://noblekeyz.com/bigbluebutton/api/join?fullName="+name+"&meetingID="+meeting_id+"&password=mp&redirect=true&checksum="+checksum
            val i = Intent(activity, ActivityWebView::class.java)
            i.putExtra("url", strUrl)
            i.putExtra("meetingID", meeting_id)
            i.putExtra("facultyEmailWhenUserComeOnThisScreen", facultyEmailWhenUserComeOnThisScreen)
            i.putExtra("classname", classname)
            startActivity(i)

        }
        else{
            Toast.makeText(activity, "No network available!", Toast.LENGTH_SHORT).show()
        }


    }
//noblekeyz.com/bigbluebutton/api/join?fullName=User+8198480&meetingID=random-6648035&password=mp&redirect=true&checksum=2ab5e7e747f137f510e93abc59f90793cd6d3f16
//noblekeyz.com/bigbluebutton/api/join?fullName=User+8198480&meetingID=random-6648035&password=ap&redirect=true&checksum=94287993dcf6097b01dd2e118014f752ae58c65a
//noblekeyz.com/bigbluebutton/api/join?fullName=userGaurav&meetingID=random-816074&password=ap&redirect=true&checksum=df331ffec0cddb9c93d21509236f8d140336aaba
}


//Recording id
//https://noblekeyz.com/bigbluebutton/api/getRecordings?meetingID=random-294211&recordID=random-669857&checksum=2579ff3d244e6bbc79bae3d31b5d833022fcd41e
//https://noblekeyz.com/bigbluebutton/api/getRecordings?meetingID=random-7554442&recordID=random-7554448&checksum=ad22c3c2c47ab886373c010cd60c5d2483a3d756