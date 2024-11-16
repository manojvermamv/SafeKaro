package com.safekaro.partner.model.remote

import com.safekaro.partner.model.data.LoginUserRequest
import com.safekaro.partner.model.models.PartnerDashboardRes
import com.safekaro.partner.model.models.RankData
import com.safekaro.partner.model.models.UserData
import com.safekaro.partner.model.models.WalletCreditDebit
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @POST("user/login")
    fun loginWithMail(
        @Body loginRequest: LoginUserRequest
    ): Call<UserData>

    @GET("partner-dashboard/app")
    fun getPartnerDashboard(
        @Query("startDate") startDate: String,      // 2024-06-01
        @Query("endDate") endDate: String,          // 2024-10-30
        @Query("partnerId") partnerId: String       // 6698b308a54843d8431795ac%27
    ): Call<PartnerDashboardRes>

    @GET("ranks/badge/{partnerId}")
    fun getRankData(
        @Path("partnerId") partnerId: String
    ): Call<RankData>

    @GET("credit-debit/partner-id")
    fun getWalletCreditDebit(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("partnerId") partnerId: String
    ): Call<WalletCreditDebit>

    @GET("homepage/{uid}")
    fun getUserData(
        @Path("uid") uid: String
    ): Call<UserData>

}