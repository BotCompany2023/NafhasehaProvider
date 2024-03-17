package  com.sa.nafhasehaprovider.ui.generalViewModel

import android.provider.Settings
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.sa.nafhasehaprovider.common.sharedprefrence.PreferencesUtils
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.entity.response.generalResponse.GeneralResponse
import com.sa.nafhasehaprovider.common.Resource
import com.sa.nafhasehaprovider.entity.response.authenticationResponse.AuthResponse
import com.sa.nafhasehaprovider.entity.response.categoriesResponse.CategoriesResponse
import com.sa.nafhasehaprovider.entity.response.vehicleTransporterResponse.VehicleTransporterResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import com.sa.nafhasehaprovider.ui.repository.MainRepo

class AuthenticationViewModel(
    private val sharedPreferences: PreferencesUtils, private val mainRepo: MainRepo
) : ViewModel() {

    var firebaseToken = ""
    val authResponse: MutableLiveData<Resource<AuthResponse>> = MutableLiveData()
    val checkCodeResponse: MutableLiveData<Resource<AuthResponse>> = MutableLiveData()
    val checkCodeResetResponse: MutableLiveData<Resource<GeneralResponse>> = MutableLiveData()
    val checkPhoneResponse: MutableLiveData<Resource<GeneralResponse>> = MutableLiveData()
    val sendActivationCodeResponse: MutableLiveData<Resource<GeneralResponse>> = MutableLiveData()
    val editProfileResponse: MutableLiveData<Resource<AuthResponse>> = MutableLiveData()
    val changePasswordResponse: MutableLiveData<Resource<GeneralResponse>> = MutableLiveData()
    val categoriesResponse: MutableLiveData<Resource<CategoriesResponse>> =MutableLiveData()
    val getVehicleTransporterResponse: MutableLiveData<Resource<VehicleTransporterResponse>> = MutableLiveData()
    val changeDefaultLanguageResponse: MutableLiveData<Resource<GeneralResponse>> = MutableLiveData()


    init {
        getFirebaseToken()
    }


    fun categories(page: Int) {
        if (Utilities.hasInternetConnection()) {
            categoriesResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.categories(page)
                if (response.isSuccessful) {
                    categoriesResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun register(
        provider_type: RequestBody,
        name: RequestBody,
        country_id: RequestBody,
        phone: RequestBody,
        email: RequestBody,
        password: RequestBody,
        address: RequestBody,
        lat: RequestBody,
        long: RequestBody,
        area_id: RequestBody,
        city_id: RequestBody,
        commercialRegister: MultipartBody.Part? = null,
        services_from_home: RequestBody,
        transporter_id: RequestBody,
        categories: List<Int>
    ) {
        if (Utilities.hasInternetConnection()) {
            authResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response =
                    mainRepo.registerUser(
                        provider_type, name,
                        country_id,phone,email,password,
                        address,lat,long,city_id,area_id,
                        commercialRegister,
                        services_from_home,
                        transporter_id,
                        categories)
                if (response.isSuccessful) {
                    authResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun login(phone: String, password: String) {
        if (Utilities.hasInternetConnection()) {
            authResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.loginUser(
                    phone, password, firebaseToken
                )
                if (response.isSuccessful) {
                    authResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }


            }
        }
    }


    fun checkPhone(phone: String, countryId: Int) {
        if (Utilities.hasInternetConnection()) {
            checkPhoneResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.checkPhoneUser(
                    phone, countryId
                )
                try {
                    if (response.isSuccessful) {
                        checkPhoneResponse.postValue(Resource.Success(response.body()!!))
                        // handling if repsonse is succesfully
                        Log.i("TestLoginterVM", "${response.body()}")
                    } else {
                        Resource.Error(response.message())
                        Log.i("TestLoginterVM", " error ${response.code()}")
                    }
                } catch (e: Exception) {
                }

            }
        }
    }



    fun changePassword(old_password: String, new_password: String) {
        if (Utilities.hasInternetConnection()) {
            changePasswordResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.changePassword(
                    old_password, new_password
                )
                try {
                    if (response.isSuccessful) {
                        changePasswordResponse.postValue(Resource.Success(response.body()!!))
                        // handling if repsonse is succesfully
                        Log.i("TestLoginterVM", "${response.body()}")
                    } else {
                        Resource.Error(response.message())
                        Log.i("TestLoginterVM", " error ${response.code()}")
                    }
                } catch (e: Exception) {
                }

            }
        }
    }


    fun checkCode(userId: Int, otpCode: String) {
        if (Utilities.hasInternetConnection()) {
            checkCodeResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.checkOtpCode(
                    userId,otpCode
                )
                try {
                    if (response.isSuccessful) {
                        checkCodeResponse.postValue(Resource.Success(response.body()!!))
                        // handling if repsonse is succesfully
                        Log.i("TestLoginterVM", "${response.body()}")
                    } else {
                        Log.i("TestLoginterVM", " error ${response.code()}")
                        Resource.Error(response.message())

                    }
                }catch (e:Exception){}

            }
        }
    }

    fun checkCodeReset(userId: Int, otpCode: String) {
        if (Utilities.hasInternetConnection()) {
            checkCodeResetResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.checkCodeReset(
                    userId,otpCode
                )
                try {
                    if (response.isSuccessful) {
                        checkCodeResetResponse.postValue(Resource.Success(response.body()!!))
                        // handling if repsonse is succesfully
                        Log.i("TestLoginterVM", "${response.body()}")
                    } else {
                        Log.i("TestLoginterVM", " error ${response.code()}")
                        Resource.Error(response.message())

                    }
                }catch (e:Exception){}

            }
        }
    }



    fun resetPass(userId: Int, newPassword: String) {
        if (Utilities.hasInternetConnection()) {
            checkPhoneResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.resetPasswordUser(
                    userId, newPassword
                )
                try {
                    if (response.isSuccessful) {
                        checkPhoneResponse.postValue(Resource.Success(response.body()!!))
                        // handling if repsonse is succesfully
                        Log.i("TestLoginterVM", "${response.body()}")
                    } else {
                        Resource.Error(response.message())
                        Log.i("TestLoginterVM", " error ${response.code()}")
                    }
                } catch (e: Exception) {
                }

            }
        }
    }


    fun sendActivationCode(userId: Int, type: String) {
        if (Utilities.hasInternetConnection()) {
            sendActivationCodeResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.sendActivationCode(
                    userId, type
                )
                try {
                    if (response.isSuccessful) {
                        sendActivationCodeResponse.postValue(Resource.Success(response.body()!!))
                        // handling if repsonse is succesfully
                        Log.i("TestLoginterVM", "${response.body()}")
                    } else {
                        Resource.Error(response.message())
                        Log.i("TestLoginterVM", " error ${response.code()}")
                    }
                } catch (e: Exception) {
                }

            }
        }
    }


    fun getProfile() {
        if (Utilities.hasInternetConnection()) {
            authResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.getProfileUser()
                try {
                    if (response.isSuccessful) {
                        authResponse.postValue(Resource.Success(response.body()!!))
                        // handling if repsonse is succesfully
                        Log.i("TestGetProfileVM", "${response.body()}")
                    } else {
                        Resource.Error(response.message())
                        Log.i("TestGetProfileVM", " error ${response.code()}")
                    }
                } catch (e: Exception) {
                }

            }
        }
    }

    fun editProfile(
        image: MultipartBody.Part?,
        provider_type: RequestBody,
        name: RequestBody,
        country_id: RequestBody,
        phone: RequestBody,
        email: RequestBody,
        address: RequestBody,
        lat: RequestBody,
        long: RequestBody,
        city_id: RequestBody,
        area_id: RequestBody,
        services_from_home: RequestBody,
        transporter_id: RequestBody,
        categories: List<Int>
    ) {
        if (Utilities.hasInternetConnection()) {
            editProfileResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.editProfile(
                    image,provider_type,name,country_id,
                    phone,email,address,lat,long,city_id,area_id, services_from_home,transporter_id, categories
                )
                if (response.isSuccessful) {
                    editProfileResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    fun changeLanguage(defaultLanguage: String) {
        if (Utilities.hasInternetConnection()) {
            changeDefaultLanguageResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.changeLanguage(defaultLanguage)
                try {
                    if (response.isSuccessful) {
                        changeDefaultLanguageResponse.postValue(Resource.Success(response.body()!!))
                        // handling if repsonse is succesfully
                        Log.i("TestGetProfileVM", "${response.body()}")
                    } else {
                        Log.i("TestGetProfileVM", " error ${response.code()}")
                        Resource.Error(response.message())

                    }
                }catch (e:Exception){}

            }
        }
    }


    fun getDeviceID(): String {
        return Settings.Secure.getString(
            NafhasehaProviderApp.context!!.contentResolver, Settings.Secure.ANDROID_ID
        )
    }

    fun getDeviceName(): String {
        var deviceName = android.os.Build.MODEL
        Log.i("TestName", deviceName)
        var device = android.os.Build.BRAND
        Log.i("TestName", device)
        return device + deviceName
    }

    fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TestFireBase", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result

            firebaseToken = token
            Log.w("TestFireBase", token)


        })
    }

    fun vehicleTransporter() {
        if (Utilities.hasInternetConnection()) {
            getVehicleTransporterResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.getVehicleTransporter()
                if (response.isSuccessful) {
                    getVehicleTransporterResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


}
