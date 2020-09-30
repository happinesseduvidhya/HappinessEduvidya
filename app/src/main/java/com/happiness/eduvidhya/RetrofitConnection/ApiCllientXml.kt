package com.happiness.eduvidhya.RetrofitConnection

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit


object ApiCllientXml {
    var BASE_URL: String = "https://noblekeyz.com/bigbluebutton/api/"
    val timeout = 30000

    val getClientXml:ApiInterface
        get() {
            // base url - url of web site
            val gson = GsonBuilder().setLenient().create()
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().connectTimeout(ApiClient.timeout.toLong(), TimeUnit.MILLISECONDS).addInterceptor(interceptor).build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiInterface::class.java)
        }
}

