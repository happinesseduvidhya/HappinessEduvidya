package com.happiness.eduvidhya.RetrofitConnection


import com.happiness.eduvidhya.datamodels.join_meeting_model
import com.happiness.eduvidhya.datamodels.response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("create")
    fun CreateMeeting(
        @Query("allowStartStopRecording") allowStartStopRecording: Boolean,
        @Query("attendeePW") attendeePW: String,
        @Query("autoStartRecording") autoStartRecording: Boolean,
        @Query("meetingID") meeting_id: String,
        @Query("moderatorPW") moderatorPW: String,
        @Query("name") meeting_name: String,
        @Query("record") record: Boolean,
        @Query("welcome") welcome: String,
        @Query("checksum") checksum: String
    ): Call<response>


    @GET("join")
    fun JoinMeeting(
        @Query("fullName") fullName: String,
        @Query("meetingID") meetingID: String,
        @Query("password") password: String,
        @Query("redirect") redirect: Boolean,
        @Query("checksum") checksum: String
    ): Call<join_meeting_model>
}
