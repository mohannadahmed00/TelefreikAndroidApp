package com.teleferik.data.network
import androidx.multidex.BuildConfig
import com.teleferik.AppController
import com.teleferik.utils.Constants
import com.teleferik.utils.getDeviceId
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RemoteDataSource {
    private val release:String = "https://api.telefreik.com/api/v1/mobile/"
    private val local:String = "http://192.168.1.2:80/api/v1/mobile/"
    //private val staging:String = "http://telefreik.4fdev.com/api/v1/mobile/"
    private val staging:String = "http://167.99.206.76/api/"//http://167.99.206.76
    private val dev:String = "https://dev.telefreik.com/api/v1/mobile/"
    fun <Api> buildApi(
        api: Class<Api>,
        userToken:String? = AppController.Prefs.getString(Constants.USER_TOKEN,""),
        baseUrl:String = staging
    ): Api {
        return Retrofit.Builder().baseUrl(baseUrl).client(
            OkHttpClient.Builder().readTimeout(50, TimeUnit.SECONDS)
                .connectTimeout(50, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    chain.proceed(chain.request().newBuilder().also {
                        if (!userToken.isNullOrEmpty())
                            it.addHeader(Constants.Network.AUTHORIZATION, "${Constants.Network.BEARER} $userToken")
                        else
                            it.addHeader(Constants.Network.DEVICE_ID, getDeviceId()) // f27dacdd1e5d2a8a
                        it.addHeader("content-language", AppController.localeManager!!.language!!)
                        it.addHeader("Accept","application/json")
                    }.build())
                }
                .also { client ->
                    if (BuildConfig.DEBUG) {
                        val loggingInterceptor = HttpLoggingInterceptor()
                        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                        client.addInterceptor(loggingInterceptor)
                    }
                }.build()
        ).addConverterFactory(GsonConverterFactory.create()).build()
            .create(api)
    }
}