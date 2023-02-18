package com.teleferik.utils

import android.content.Context
import com.teleferik.AppController
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Constants {
    val NOT_READ: Int = 0
    val DEALS: String = "Deals"
    const val DEVICE_TYPE = "android"
    const val IS_LANG_SELECTED = "is_lang_selected"
    const val IS_FIRST_OPEN = "is_first_open"
    const val BRANCH_NAME = "branchName"
    const val CART_COMMENT = "cart comment"
    const val CART_ITEM_COUNTER = "cart_item_counter"
    const val GOOGLE_LOGIN_REQUEST_CODE = 100
    const val EGYPT_PHONE_CODE = "20"
    const val USER_TOKEN = "user_token"
    const val USER_NAME = "user_name"
    const val USER_PHONE = "user_phone"
    const val USER_EMAIL = "user_email"
    const val NOTIFICATION_TYPE = "notification_type"
    const val NOTIFICATION_URL = "notification_url"
    const val DATE_YYYY_MM_DD_FORMAT = "yyyy-MM-dd"
    const val START_DESTINATION = "START DESTINATION"
    const val ARRIVAL_DESTINATION = "ARRIVAL DESTINATION"

    object ForgetPasswordType {
        const val email = "0"
        const val phone = "1"
    }

    object NOTIFICATIONS_TYPE {
        const val NOTIFICATION_CENTER = 1
        const val SUPPORT_TICKET = 2
        const val WELCOME_NOTIFICATION = 3
    }


    object CATEGORIES_LEVEL {
        const val LEVEL_EMPTY = ""
        const val LEVEL_ONE = "1"
        const val LEVEL_TWO = "2"
        const val LEVEL_THREE = "3"
    }

    object SORT {
        const val DEFAULT = "1"
        const val LOW_HIGH = "2"
        const val HIGH_LOW = "3"
        const val A_Z = "4"
        const val Z_A = "5"
    }

    object ORDER_STATUS_ID {
        const val PENDING = 1
        const val ENDED = 5
    }

    object Network {
        const val AUTHORIZATION = "Authorization"
        const val BEARER = "Bearer"
        const val DEVICE_ID = "deviceId"
        const val GOOGLE_TOKEN_URL = "https://www.googleapis.com/oauth2/v4/token"
    }

    object DeeplinkRoutes {
        const val SELECT_BRANCH = "selectBranch/select_branch"
    }

    object END_POTINS {
        //auth endpoints
        const val LOGIN = "v1/mobile/customer/login"
        const val SEND_OTP = "v1/mobile/customer/send-otp"
        const val REGISTER = "v1/mobile/customer/register"
        const val VERIFY_OTP = "v1/mobile/customer/verify-otp"
        const val SOCIAL_REGISTER = "customer/social-register"
        //const val VERIFY_OTP = "customer/verifyotp"

        //search endpoints
        const val SEARCH_LOCATIONS = "transports/locations"
        const val RESEND_OTP = "customer/resendotp"
        const val SHOW_PROFILE = "customer/showForMobile"
        const val EDIT_USER_PROFILE = "customer/updateCustomerNonMobForMobile"
        const val EDIT_USER_MOBILE = "customer/updateCustomerMobForMobile"
        const val SKY_SCANNER_AIR_PORTS_SEARCH =
            "https://partners.api.skyscanner.net/apiservices/autosuggest/v1.0/EG/EGP/"
        const val CITIES_SEARCH = "https://demo.webusegypt.com/api/cities"
        const val CREATE_TICKET = "ticket/create"
        const val GET_TICKETS_FOR_CUSTOMER = "ticket/getAllForCustomer"
        const val GET_SINGLE_TICKET = "ticket/{ticketId}"
        const val ADD_REPLY = "replies/createForMobile"
        const val USER_NOTIFICATION_LIST = "customer/showNotificationsPerCustomerForMobile"
        const val UPDATE_NOTIFICATION_STATUS = "customer/updateReadStatusForMobile"
        const val HOME_PROMOTIONAL_OFFERS_LIST = "promotionaloffers/list"
        const val CREATE_TICKET_RESERVATION = "ticketreservation/create"
        const val TICKET_RESERVATION_LIST = "ticketreservation/search"

        const val SEARCH_BUS_TRIPS = "transports/trips"
    }

    object LINKS {
        const val TERMS_URL = "http://webview.telefreik.com/terms.html"
        const val FRQ_URL = "http://webview.telefreik.com/faq.html"
    }

    object PARAMS {
        const val MOBILE = "mobile"
        const val PHONE_CODE = "phonecode"
        const val CODE = "code"
        const val SCREEN_TITLE = "screen_title"
        const val SCREEN_URL = "screen_URL"
        const val TICKET_ID = "ticketId"
        const val NOTIFICATION_ID = "notification_id"
        const val NOTIFICATION_READ = "notification_read"

        const val CITY_FROM = "city_from"
        const val CITY_TO = "city_to"
        const val DATE = "date"
    }


    fun getDateHeader(context: Context, date: String): String {
        val formatDate =
            SimpleDateFormat("yyyy-MM-dd", Locale(AppController.localeManager?.language!!))
        formatDate.timeZone = TimeZone.getDefault()
        val mDate = formatDate.parse(date)
        return mDate?.let { formatDate.format(it) }.toString()
    }

    fun datesAreSimilar(firstDate: String, secondDate: String): Boolean {
        val formatDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("en"))
        formatDate.timeZone = TimeZone.getTimeZone("UTC")
        try {
            val mFirstDate = formatDate.parse(firstDate)
            val mSecondDate = formatDate.parse(secondDate)

            val different = mFirstDate!!.time - mSecondDate!!.time
            val secondsInMilli: Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24

            val elapsedDays = different / daysInMilli

            if (elapsedDays == 0L) {
                return true
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return false
    }
}