package com.happiness.eduvidhya.fragments

import android.annotation.TargetApi
import android.content.Intent.getIntent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.*
import androidx.fragment.app.Fragment
import com.happiness.eduvidhya.R


class FragmentAttendance : Fragment() {

    private lateinit var webv: WebView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_attedance, container, false)

        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)




//        webv = v.findViewById(R.id.webview)
//        webv.getSettings().setDomStorageEnabled(true)
//        webv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE)
//        webv.getSettings().setAppCacheEnabled(true)
//        webv.getSettings().setAppCachePath()
//            requireActivity().getFilesDir()
//                .getAbsolutePath() + "/cache"
//        )
//        webv.getSettings().setDatabaseEnabled(true)
//        webv.getSettings().setDatabasePath(
//            requireActivity()
//                .getFilesDir()
//                .getAbsolutePath() + "/databases"
//        )
//        webv.getSettings().setSaveFormData(true)
//
//        webv.setWebChromeClient(object : WebChromeClient() {
//            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//            override fun onPermissionRequest(request: PermissionRequest) {
//                request.grant(request.resources)
//            }
//        })
//
//
//        webv.setWebViewClient(MyBrowser())
//        webv.getSettings().setLoadsImagesAutomatically(true)
//        webv.getSettings().setJavaScriptEnabled(true)
//        webv.getSettings().setAllowFileAccess(true)
//        webv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY)
//        webv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
//        webv.getSettings().setLoadWithOverviewMode(true)
//        webv.getSettings().setDefaultTextEncodingName("utf-8")
//        webv.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH)
//        webv.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND)
//
//        webv.getSettings()
//            .setUserAgentString(webv.getSettings().getUserAgentString().replace("; wv", ""));
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            webv.getSettings().setMediaPlaybackRequiresUserGesture(false);
//        }
////        val linkMy = getIntent().extras!!.getString("url")
////
////        val urlMy = linkMy
//        webv.loadUrl("https://noblekeyz.com/bigbluebutton/api/join?fullName=User+4755411&meetingID=random-7554443&password=mp&redirect=true&checksum=e341b5129ca990fbde100c02684267300c5e992d")
        return v
    }

//    private class MyBrowser : WebViewClient() {
//        override fun shouldOverrideUrlLoading(
//            view: WebView,
//            url: String
//        ): Boolean {
//            view.loadUrl(url)
//            return true
//        }
//
//        override fun onPageStarted(
//            view: WebView,
//            url: String,
//            favicon: Bitmap
//        ) {
//            super.onPageStarted(view, url, favicon)
//        }
//
//        override fun onPageFinished(
//            view: WebView,
//            url: String
//        ) {
//            super.onPageFinished(view, url)
////            if (url == "https://happinesseduvidhya.co.in/b/") {
////                webv.clearView()
////                activity.finish()
////            }
//        }
//    }
}