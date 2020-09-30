package com.happiness.eduvidhya.datamodels

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


@Root(strict = false, name = "response")
data class response
 (
 @field:Element(name = "returncode")
 var returncode: String = "",
// @field:Element(name = "message")
//var message: String? = "",
 @field:Element(name = "meetingID")
 var meetingID:String?=""
// @field:Element(name = "name", required = true)
// var name:String=""


)
