package com.example.mycontactonlyan_3.data.sourse.remote
//
//import android.content.Context
//import android.text.Spannable.Factory
//import com.chuckerteam.chucker.api.ChuckerInterceptor
//import com.example.mycontactonlyan_3.data.sourse.remote.api.ContactApi
//import com.example.mycontactonlyan_3.utils.NetworkStatusValidator
//import okhttp3.Cache
//import okhttp3.OkHttpClient
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import java.util.concurrent.TimeUnit
//
//object ApiClient {
//    lateinit var api: ContactApi
//    fun init(context: Context) {
//        val cacheSize = 50 * 1024 * 1024
//        val cache = Cache(context.cacheDir, cacheSize.toLong())
//        val maxStale = 60 * 60 * 24 * 30
//        val okHttpClient = OkHttpClient.Builder()
//            .addInterceptor(ChuckerInterceptor(context))
//            .addInterceptor { chain ->
//                if (!NetworkStatusValidator.hasNetwork) {
//                    val newRequest = chain.request()
//                        .newBuilder()
//                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                        .removeHeader("Pragma")
//                        .build()
//
//                    chain.proceed(newRequest)
//                } else chain.proceed(chain.request())
//            }
//            .readTimeout(30, TimeUnit.SECONDS)
//            .connectTimeout(30, TimeUnit.SECONDS)
//            .writeTimeout(30, TimeUnit.SECONDS)
//            .cache(cache)
//            .build()
//
//        val retrofit = Retrofit
//            .Builder()
//            .baseUrl("https://8e5f-195-158-16-140.ngrok-free.app/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(okHttpClient)
//            .build()
//
//
//        api = retrofit.create(ContactApi::class.java)
//    }
//
//}
