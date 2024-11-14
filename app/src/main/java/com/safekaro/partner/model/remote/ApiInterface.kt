package com.safekaro.partner.model.remote

import com.safekaro.partner.model.data.LoginUserRequest
import com.safekaro.partner.model.models.UserData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {

    @POST("user/login")
    fun loginWithMail(
        @Body loginRequest: LoginUserRequest
    ): Call<UserData>

    @GET("homepage/{uid}")
    fun getUserData(
        @Path("uid") uid: String
    ): Call<UserData>

}