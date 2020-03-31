package ie.wit.api

import com.google.gson.GsonBuilder
import ie.wit.markit.ui.ClonMarket.Admin.models.ClonTraderModel
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface TraderService {
    @GET("/clonTraders")
    fun getall(): Call<List<ClonTraderModel>>

    @GET("/clonTraders/{email}")
    fun findall(@Path("email") email: String?)
            : Call<List<ClonTraderModel>>

    @GET("/clonTraders/{email}/{id}")
    fun get(@Path("email") email: String?,
            @Path("id") id: String): Call<ClonTraderModel>

    @DELETE("/clonTraders/{email}/{id}")
    fun delete(@Path("email") email: String?,
               @Path("id") id: String): Call<TraderWrapper>

    @POST("/clonTraders/{email}")
    fun post(@Path("email") email: String?,
             @Body clonTrader: ClonTraderModel)
            : Call<TraderWrapper>

    @PUT("/clonTraders/{email}/{id}")
    fun put(@Path("email") email: String?,
            @Path("id") id: String,
            @Body clonTrader: ClonTraderModel
    ): Call<TraderWrapper>

    companion object {

        val serviceURL = "https://donationweb-hdip-mu-server.herokuapp.com"

        fun create() : TraderService {

            val gson = GsonBuilder().create()

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(serviceURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
            return retrofit.create(TraderService::class.java)
        }
    }
}