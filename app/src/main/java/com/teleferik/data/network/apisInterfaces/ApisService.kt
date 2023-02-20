package com.teleferik.data.network.apisInterfaces

import com.teleferik.models.BaseResponse
import com.teleferik.models.EditPhoneRequest
import com.teleferik.models.RegisterRequest
import com.teleferik.models.editPhoneResponse.EditPhoneResponse
import com.teleferik.models.login.LoginResponse
import com.teleferik.models.notificationList.NotificationListResponse
import com.teleferik.models.promotionalOffer.PromotionalOffer
import com.teleferik.models.showProfile.ShowProfileResponse
import com.teleferik.models.skyscanner.airPorts.AirPortsResponse
import com.teleferik.models.skyscanner.searchResults.FlightSearchResultsResponse
import com.teleferik.models.ticketReservation.TicketReservation
import com.teleferik.models.tickets.CreateTicketBodyRequest
import com.teleferik.models.tickets.SingleTicketResponse
import com.teleferik.models.tickets.Ticket
import com.teleferik.models.tickets.TicketsForCustomerResponse
import com.teleferik.models.bus.searchResults.TripsSearchResponseItem
import com.teleferik.utils.Constants
import com.teleferik.utils.Constants.PARAMS.TICKET_ID
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface ApisService {

    @FormUrlEncoded
    //@Headers("Accept-Language: ar")
    @POST("api/v1/mobile/customer/login")//(Constants.END_POTINS.LOGIN)
    suspend fun login(
        @Field(Constants.PARAMS.MOBILE) phone: String,
        @Field(Constants.PARAMS.PHONE_CODE) code: String
    ): BaseResponse<LoginResponse>

    @FormUrlEncoded
    //@Headers("Accept-Language: en")
    @POST("api/v1/mobile/customer/send-otp")//(Constants.END_POTINS.SEND_OTP)
    suspend fun sendOTP(
        @Field(Constants.PARAMS.MOBILE) phone: String,
        @Field(Constants.PARAMS.PHONE_CODE) code: String
    ): BaseResponse<LoginResponse>

    @FormUrlEncoded
    @POST("api/v1/mobile/customer/verify-otp")//(Constants.END_POTINS.VERIFY_OTP)
    suspend fun verifyOTP(
        @Field(Constants.PARAMS.MOBILE) phone: String,
        @Field(Constants.PARAMS.PHONE_CODE) phoneCode: String,
        @Field(Constants.PARAMS.CODE) code: String
    ): BaseResponse<LoginResponse>

    @POST("api/v1/mobile/customer/register")//(Constants.END_POTINS.REGISTER)
    suspend fun register(@Body registerRequest: RegisterRequest): BaseResponse<LoginResponse>

    /*@FormUrlEncoded
    @POST(Constants.END_POTINS.VERIFY_OTP)
    suspend fun oldVerifyOTP(
        @Field(Constants.PARAMS.MOBILE) phone: String,
        @Field(Constants.PARAMS.PHONE_CODE) phoneCode: String,
        @Field("code") code: String
    ):  BaseResponse<LoginResponse>*/



    @POST(Constants.END_POTINS.SOCIAL_REGISTER)
    suspend fun socialRegister(@Body registerRequest: RegisterRequest): BaseResponse<LoginResponse>



    @FormUrlEncoded
    @POST(Constants.END_POTINS.RESEND_OTP)
    suspend fun resendOTP(): BaseResponse<LoginResponse>

    @FormUrlEncoded
    @POST(Constants.END_POTINS.VERIFY_OTP)
    suspend fun verifyMobileOTP(@Field("code") OTP: String): BaseResponse<EditPhoneResponse>

    @FormUrlEncoded
    @POST(Constants.END_POTINS.RESEND_OTP)
    suspend fun resendMobileOTP(): BaseResponse<EditPhoneResponse>

//    @Multipart
//    @POST(Constants.END_POTINS.EDIT_USER_PROFILE)
//    suspend fun editUserProfile(
//        @PartMap() partMap: MutableMap<String, RequestBody>,
//        @Part file: MultipartBody.Part?): BaseResponse<LoginResponse>
    @Multipart
    @POST(Constants.END_POTINS.EDIT_USER_PROFILE)
    suspend fun editUserProfile(
    @Part name: MultipartBody.Part,
    @Part email: MultipartBody.Part,
    @Part file: MultipartBody.Part?): BaseResponse<LoginResponse>

    @POST(Constants.END_POTINS.EDIT_USER_MOBILE)
    suspend fun editUserMobile(@Body editPhoneRequest: EditPhoneRequest): BaseResponse<EditPhoneResponse>

    @GET(Constants.END_POTINS.SHOW_PROFILE)
    suspend fun showProfile(): BaseResponse<ShowProfileResponse>

    // ================= SKY SCANNER ==================
    @FormUrlEncoded
    @POST
    @Headers("Content-Type: application/x-www-form-urlencoded")
    fun createSesstion(
        @Url url: String,
        @Field("cabinclass") cabinclass: String,
        @Field("country") country: String,
        @Field("currency") currency: String,
        @Field("locale") locale: String,
        @Field("locationSchema") locationSchema: String,
        @Field("originplace") originplace: String,
        @Field("destinationplace") destinationplace: String,
        @Field("outbounddate") outbounddate: String,
        @Field("inbounddate") inbounddate: String,
        @Field("adults") adults: Int,
        @Field("children") children: Int,
        @Field("infants") infants: Int,
        @Field("apikey") apikey: String
    ): Call<Any>


    @GET
    suspend fun searchAirPorts(
        @Url url:String
    ):AirPortsResponse


    @GET
    suspend fun getFlighTripsSearchResults(
        @Url url:String
    ):FlightSearchResultsResponse
    // ================= Bus Trips ==================

    @GET("api/transports/locations")//(Constants.END_POTINS.SEARCH_LOCATIONS)
    suspend fun searchLocations(): BaseResponse<List<LocationsResponseItem>>

    @FormUrlEncoded
    @POST("api/transports/trips")
    suspend fun getBusTripsSearchResults(
        @Field("city_from") city_from: String,
        @Field("city_to") city_to: String,
        @Field("date") date: String
        /*,
        @Field("station_from") station_from: String,
        @Field("station_to") station_to: String,*/
    ):BaseResponse<List<TripsSearchResponseItem>>
    // ================= TICKETS ==================
    @POST(Constants.END_POTINS.CREATE_TICKET)
    suspend fun createTicket(@Body createTicketBodyRequest: CreateTicketBodyRequest): BaseResponse<Ticket>

    @GET(Constants.END_POTINS.GET_TICKETS_FOR_CUSTOMER)
    suspend fun getTicketsForCustomer(): BaseResponse<TicketsForCustomerResponse>

    @GET(Constants.END_POTINS.GET_SINGLE_TICKET)
    suspend fun getSingleTicket(@Path(TICKET_ID) ticketId: String): BaseResponse<SingleTicketResponse>

    @Multipart
    @POST(Constants.END_POTINS.ADD_REPLY)
    suspend fun addReply(
        @Part ticket_id: MultipartBody.Part,
        @Part user_id: MultipartBody.Part,
        @Part file: MultipartBody.Part,
        @Part message: MultipartBody.Part): BaseResponse<Ticket>

    @GET(Constants.END_POTINS.USER_NOTIFICATION_LIST)
    suspend fun notificationsList(): BaseResponse<NotificationListResponse>

    @FormUrlEncoded
    @POST(Constants.END_POTINS.UPDATE_NOTIFICATION_STATUS)
    suspend fun updateNotification(
        @Field(Constants.PARAMS.NOTIFICATION_ID) notificationId:String,
        @Field(Constants.PARAMS.NOTIFICATION_READ) notificationRead:String,
    ): BaseResponse<Any>

    @GET(Constants.END_POTINS.HOME_PROMOTIONAL_OFFERS_LIST)
    suspend fun promotionalOffersList(): BaseResponse<PromotionalOffer>

    @POST(Constants.END_POTINS.CREATE_TICKET_RESERVATION)
    suspend fun createTicketReservation(): BaseResponse<TicketReservation>

    @GET(Constants.END_POTINS.TICKET_RESERVATION_LIST)
    suspend fun ticketReservationsList(@Query("status") status:String?): BaseResponse<TicketReservation>
}