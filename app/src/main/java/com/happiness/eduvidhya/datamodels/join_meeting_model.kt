package com.happiness.eduvidhya.datamodels

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false, name = "response")
data class join_meeting_model
    (
    @field:Element(name = "returncode", required = true)
    var returncode: String = "",
    @field:Element(name = "message", required = true)
    var message: String? = "",
    @field:Element(name = "messageKey", required = true)
    var messageKey: String? = "",
    @field:Element(name = "meeting_id")
    var meeting_id:String?="",
    @field:Element(name = "auth_token")
    var auth_token:String?="",
    @field:Element(name = "session_token")
    var session_token:String?="",
    @field:Element(name = "url")
    var url:String?=""
)