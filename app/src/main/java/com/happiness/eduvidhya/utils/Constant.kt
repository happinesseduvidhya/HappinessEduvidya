package com.happiness.eduvidhya.utils

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

object Constant {

    fun hasNetworkAvailable(context: Context): Boolean {
        val service = Context.CONNECTIVITY_SERVICE
        val manager = context.getSystemService(service) as ConnectivityManager?
        val network = manager?.activeNetworkInfo
        return (network != null)
    }

    var shared_secret = "KhYssBmjq3Q4OouoVGB1TorbBKnpxBSXGRDLbpNKsY"
    var myauth:FirebaseAuth?=null

    var mConstantType =""

    fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction("Action", null).show()
    }
}