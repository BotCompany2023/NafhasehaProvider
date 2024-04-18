package com.sa.nafhasehaprovider.common

import com.sa.nafhasehaprovider.app.NafhasehaProviderApp


const val USER_DATA = "user_data"
var BASE_URL = "https://nafhasuha.com/api/provider/V1/"
//var SOCKET_URL = "https://socket.nafhasuha.com:3000"
var SOCKET_URL = "http://135.181.122.200:3200"

const val FIRST_TIME = "first_time"
const val LOGGED = "logged"
const val QUERY_PAGE_SIZE = 15
const val  SHARED_PREFERENCES_FILE_NAME ="APPNAME"
const val  SHARED_PREFERENCES_USER ="USERSHARED"
const val DEVICE_TOKEN = "device_token"
const val USER_TOKEN = "user_token"
const val PROVIDER_RATING = "provider_rating"
const val USER_TOKEN_EXPIRY = "user_token_expiry"
const val USER_ID = "user_id"
const val USER_NAME = "user_name"
const val USER_IMAGE = "user_image"
const val USER_TYPE = "user_type"
const val USER_PROFILE_IMAGE = "user_profile_image"
const val BIRTHDATE = "birthdate"
const val AVATAR = "avatar"
const val GENDER = "gender"
const val SYMBOL = "symbol"
const val COUNTRY_IMAGE = "countryImage"
const val COUNTRY_NAME = "countryName"
const val LOGIN = "LOGIN"
const val FIRSTBUYER = "firstBuyer"
const val VERIFIRD = "verified"
const val TYPE = "type"
const val MOBILE = "mobile"
const val EMAIL = "email"
const val WHATSAPP = "whatsapp"
const val COUNTRY = "country"
const val CITY = "city"
const val LICENCE_TYPE_ID = "license_type_id"
const val ENGINEER = "engineer"
const val DOCTORS = "DOCTORS"
const val COMPANY = "company"
const val GROUP_PAYER = "group"
const val FIRST = "group"
const val LANG = "LANG"
const val BEARER = "Bearer"
const val CODE200 = 200 //Success
const val CODE403 = 403 //unAuthorized
//const val CODE404 = 404 //Not Found
const val CODE405 = 405 //Validation
const val CODE500 = 500 //Server
const val USER = "1"
const val MARKET = "2"
var COUNT_NOTIFICATION = 0


object EmitKeyWord {
    val UPDATELOCATOON = "provider-tracking"
    val NEW_ORDER = "request_range"
    val SEND_OFFER_ORDER = "send-offer-price"
    val ACCEPTED_OFFER_ORDER = "accepted-offer-price"
    val REJECTED_OFFER_ORDER = "remove_request"
    val ACCEPTED_ORDER = "accept-request"

}

