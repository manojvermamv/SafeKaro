package com.safekaro.partner.model.repository

import android.util.Log
import com.safekaro.partner.model.local.ContentDataSource
import com.safekaro.partner.model.models.ContactItem
import com.safekaro.partner.model.models.ErrorResponse
import com.safekaro.partner.model.models.UserData
import com.safekaro.partner.model.remote.RetrofitClient
import com.safekaro.partner.utils.Resource
import com.google.gson.Gson
import com.safekaro.partner.model.data.LoginUserRequest
import com.safekaro.partner.model.models.PartnerDashboardRes
import com.safekaro.partner.model.models.RankData
import com.safekaro.partner.model.models.WalletCreditDebit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import retrofit2.awaitResponse

class MainRepository(private val source: ContentDataSource) {

    private val api = RetrofitClient.apiInterface

    suspend fun fetchContacts(): List<ContactItem> {
        return withContext(Dispatchers.IO) {
            source.fetchContacts()
        }
    }

    suspend fun loginWithMail(loginRequest: LoginUserRequest): Resource<UserData> {
        return withContext(Dispatchers.IO) {
            delay(20)
            makeApiCall { api.loginWithMail(loginRequest) }
        }
    }

    suspend fun getPartnerDashboard(startDate: String, endDate: String, partnerId: String): Resource<PartnerDashboardRes> {
        return withContext(Dispatchers.IO) {
            delay(20)
            makeApiCall { api.getPartnerDashboard(startDate, endDate, partnerId) }
        }
    }

    suspend fun getRankData(partnerId: String): Resource<RankData> {
        return withContext(Dispatchers.IO) {
            delay(20)
            makeApiCall { api.getRankData(partnerId) }
        }
    }

    suspend fun getWalletCreditDebit(startDate: String, endDate: String, partnerId: String): Resource<WalletCreditDebit> {
        return withContext(Dispatchers.IO) {
            delay(20)
            makeApiCall { api.getWalletCreditDebit(startDate, endDate, partnerId) }
        }
    }

    suspend fun getUserDataApiCall(): Resource<UserData> {
        return withContext(Dispatchers.IO) {
            delay(20)
            makeApiCall { api.getUserData("1") }
        }
    }

//    suspend fun getUpcomingMatchesApiCall(pageIndex: Int): Resource<UpcomingMatches> {
//        return withContext(Dispatchers.IO) {
//            delay(if (pageIndex == 1) 20 else 250)
//            makeApiCall { api.getUpcomingMatches(pageIndex.toString()) }
//        }
//    }

}

/**
 * Helper function to make API call and handle exceptions
 * */
suspend inline fun <reified T> makeApiCall(apiCall: () -> Call<T>): Resource<T> {
    return try {
        val response = apiCall().awaitResponse()
        if (response.isSuccessful) {
            Resource.Success(response.body()!!)
        } else {
            // Handle API error (e.g., non-200 status code)
            Resource.Error(Exception(response.catchError()))
        }
    } catch (e: Exception) {
        // Handle network or other exceptions
        Log.d("DEBUG", "API error: ${e.message}")
        Resource.Error(e)
    }
}

inline fun <reified T> Response<T>.catchError(): String {
    return when (code()) {
        500 -> {
            val errorResponse = Gson().fromJson(errorBody()?.string(), ErrorResponse::class.java)
            Log.d("DEBUG", "Custom error ${errorResponse.status}: ${errorResponse.message}")
            errorResponse?.message ?: "Internal Server Error"
        }
        else -> {
            val errorResponse = Gson().fromJson(errorBody()?.string(), ErrorResponse::class.java)
            Log.d("DEBUG", "Custom error ${errorResponse.status}: ${errorResponse.message}")
            errorResponse?.message ?: "Custom error: ${code()} - ${message()}"
        }
    }
}