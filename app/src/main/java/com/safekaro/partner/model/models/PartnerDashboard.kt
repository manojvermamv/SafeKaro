package com.safekaro.partner.model.models

import com.google.gson.annotations.SerializedName
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

data class PartnerDashboardRes(
    override val status: String,
    override val message: String,
    val data: List<PartnerDashboard>,
) : BaseResponse()

data class PartnerDashboard(
    val premiums: Premiums,
    val commissions: Commissions,
    val policyCounts: Policy,
    val bookingRequests: BookingRequests,
    val leadCounts: LeadCounts,
)

data class Premiums(
    @SerializedName("Net_Premium") val netPremium: Long,
)

data class Commissions(
    @SerializedName("Monthly_Commission")
    val Monthly_Commission: Long,
    @SerializedName("Total_Commission")
    val Total_Commission: Long,
    @SerializedName("Balance")
    val Balance: Long,
    @SerializedName("Monthly_Paid_Amount")
    val Monthly_Paid_Amount: Long,
    @SerializedName("Total_Paid_Amount")
    val Total_Paid_Amount: Long,
    @SerializedName("Monthly_UnPaid_Amount")
    val Monthly_UnPaid_Amount: Long,
    @SerializedName("Total_UnPaid_Amount")
    val Total_UnPaid_Amount: Long,
) {

    fun monthlyCommissionName() = (this::Monthly_Commission).name.formatValue()
    fun totalCommissionName() = (this::Total_Commission).name.formatValue()
    fun balanceName() = (this::Balance).name.formatValue()
    fun monthlyPaidAmountName() = (this::Monthly_Paid_Amount).name.formatValue()
    fun totalPaidAmountName() = (this::Total_Paid_Amount).name.formatValue()
    fun monthlyUnPaidAmountName() = (this::Monthly_UnPaid_Amount).name.formatValue()
    fun totalUnPaidAmountName() = (this::Total_UnPaid_Amount).name.formatValue()

    fun getSerializedName(): String {
        for (property in Commissions::class.memberProperties) {
            val serializedName = property.annotations.find { it is SerializedName }?.let {
                (it as SerializedName).value
            } ?: property.name // Use the property name if there's no SerializedName
            println("------> ${property.name}: $serializedName")
        }
        return ""
    }

    fun getSerializedNameValue(propertyName: String): String? {
        val property = this::class.memberProperties.find { it.name == propertyName }
        val annotation = property?.javaField?.getAnnotation(SerializedName::class.java)
        println("------> ${property?.name}: ${annotation?.value}")
        return annotation?.value
    }

}

data class Policy(
    @SerializedName("motor")
    val motor: Long,
)

data class BookingRequests(
    @SerializedName("Total_Booking")
    val Total_Booking: Long,
    @SerializedName("Accepted_Booking")
    val Accepted_Booking: Long,
    @SerializedName("Requested_Booking")
    val Requested_Booking: Long,
    @SerializedName("Booked_Booking")
    val Booked_Booking: Long,
    @SerializedName("Rejected_Booking")
    val Rejected_Booking: Long,
) {

    fun totalBookingName() = (this::Total_Booking).name.formatValue()
    fun acceptedBookingName() = (this::Accepted_Booking).name.formatValue()
    fun requestedBookingName() = (this::Requested_Booking).name.formatValue()
    fun bookedBookingName() = (this::Booked_Booking).name.formatValue()
    fun rejectedBookingName() = (this::Rejected_Booking).name.formatValue()

}

data class LeadCounts(
    @SerializedName("Total_Lead")
    val Total_Lead: Long,
) {

    fun totalLeadName() = (this::Total_Lead).name.formatValue()

}

fun String.formatValue(): String {
    return this.replace("_", " ")
}

/*
  fun getSerializedName(): String {
        for (property in Commissions::class.memberProperties) {
            val serializedName = property.annotations.find { it is SerializedName }?.let {
                (it as SerializedName).value
            } ?: property.name // Use the property name if there's no SerializedName
            println("------> ${property.name}: $serializedName")
        }
        return ""
    }

fun getSerializedNameValue(clazz: KClass<*>, propertyName: String): String? {
        val property = clazz.memberProperties.find { it.name == propertyName }
        val annotation = property?.javaField?.getAnnotation(SerializedName::class.java)
        return annotation?.value
    }
* */