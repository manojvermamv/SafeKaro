package com.safekaro.partner.model.models

import com.google.gson.annotations.SerializedName

data class WalletCreditDebit(
    override val status: String,
    override val message: String,
    val data: List<CreditDebit>? = null,
) : BaseResponse()

data class CreditDebit (
    @SerializedName("_id"             ) var Id              : String? = null,
    @SerializedName("transactionCode" ) var transactionCode : String? = null,
    @SerializedName("accountType"     ) var accountType     : String? = null,
    @SerializedName("credit"          ) var credit          : Long?    = null,
    @SerializedName("debit"           ) var debit           : Long?    = null,
    @SerializedName("accountId"       ) var accountId       : String? = null,
    @SerializedName("accountCode"     ) var accountCode     : String? = null,
    @SerializedName("partnerId"       ) var partnerId       : String? = null,
    @SerializedName("partnerName"     ) var partnerName     : String? = null,
    @SerializedName("partnerBalance"  ) var partnerBalance  : Long?    = null,
    @SerializedName("brokerId"        ) var brokerId        : String? = null,
    @SerializedName("brokerName"      ) var brokerName      : String? = null,
    @SerializedName("remarks"         ) var remarks         : String? = null,
    @SerializedName("policyNumber"    ) var policyNumber    : String? = null,
    @SerializedName("startDate"       ) var startDate       : String? = null,
    @SerializedName("endDate"         ) var endDate         : String? = null,
    @SerializedName("distributedDate" ) var distributedDate : String? = null,
    @SerializedName("employeeId"      ) var employeeId      : String? = null,
    @SerializedName("employeeName"    ) var employeeName    : String? = null,
    @SerializedName("createdOn"       ) var createdOn       : String? = null,
    @SerializedName("createdBy"       ) var createdBy       : String? = null,
    @SerializedName("updatedOn"       ) var updatedOn       : String? = null,
    @SerializedName("__v"             ) var _v              : Long?    = null
)