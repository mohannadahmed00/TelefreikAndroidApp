package com.teleferik.ui.more


import com.teleferik.base.BaseRepo
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.models.EditPhoneRequest
import com.teleferik.models.tickets.CreateTicketBodyRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class ProfileRepo(private val api: ApisService) : BaseRepo() {

    suspend fun showProfile() = safeApiCalls {
        api.showProfile()
    }

    suspend fun editUserProfile(name: String , email:String,file : File?) = safeApiCalls {
        val namePart = MultipartBody.Part.createFormData("name", name)
        val emailPart = MultipartBody.Part.createFormData("email", email)
        val filePart = if (file == null) {
            MultipartBody.Part.createFormData("file", "", "".toRequestBody("multipart/form-data".toMediaTypeOrNull()))
        } else {
            MultipartBody.Part.createFormData("file", file.name, file.asRequestBody("multipart/form-data".toMediaTypeOrNull()))
        }
        api.editUserProfile(namePart,emailPart,filePart)
    }

    suspend fun editUserMobile(editPhoneRequest: EditPhoneRequest) = safeApiCalls {
        api.editUserMobile(editPhoneRequest)
    }

    suspend fun createTicket(createTicketBodyRequest: CreateTicketBodyRequest) =
        safeApiCalls { api.createTicket(createTicketBodyRequest) }

    suspend fun getTicketsForCustomer() = safeApiCalls { api.getTicketsForCustomer() }
    suspend fun getSingleTicket(ticketId: String) = safeApiCalls { api.getSingleTicket(ticketId) }

    suspend fun addReply(
        ticket_id: String?,
        user_id: String?,
        file: File?,
        message: String?
    ) = safeApiCalls {

        val ticketIdPart = if (ticket_id == null) {
            MultipartBody.Part.createFormData("ticket_id", "")
        } else {
            MultipartBody.Part.createFormData("ticket_id", ticket_id)
        }

        val userIdPart = if (user_id == null) {
            MultipartBody.Part.createFormData("user_id", "")
        } else {
            MultipartBody.Part.createFormData("user_id", user_id)
        }
        val messagePart = if (message == null) {
            MultipartBody.Part.createFormData("message", "")
        } else {
            MultipartBody.Part.createFormData("message", message)
        }

        val filePart = if (file == null) {
            MultipartBody.Part.createFormData("file", "", "".toRequestBody("multipart/form-data".toMediaTypeOrNull()))
        } else {
            MultipartBody.Part.createFormData("file", file.name, file.asRequestBody("multipart/form-data".toMediaTypeOrNull()))
        }
        api.addReply(ticketIdPart, userIdPart, filePart, messagePart)
    }

    suspend fun notificationsList() = safeApiCalls { api.notificationsList() }

    suspend fun updateNotification(notificationId:String) = safeApiCalls { api.updateNotification(notificationId,"1") }

    suspend fun verifyOTP(OTP:String) = safeApiCalls {
        api.verifyMobileOTP(OTP)
    }

    suspend fun resendOTP() = safeApiCalls {
        api.resendMobileOTP()
    }
}