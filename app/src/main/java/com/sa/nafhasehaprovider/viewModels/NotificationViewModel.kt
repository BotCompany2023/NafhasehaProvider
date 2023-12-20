package  com.sa.nafhasehaprovider.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sa.nafhasehaprovider.common.Resource
import com.sa.nafhasehaprovider.common.sharedprefrence.PreferencesUtils
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.entity.response.countNotificationResponse.CountNotificationResponse
import com.sa.nafhasehaprovider.entity.response.generalResponse.GeneralResponse
import com.sa.nafhasehaprovider.entity.response.notificationResponse.NotificationResponse
import com.sa.nafhasehaprovider.ui.repository.MainRepo
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val sharedPreferences: PreferencesUtils, private val mainRepo: MainRepo
) : ViewModel() {

    val notificationResponse: MutableLiveData<Resource<NotificationResponse>> = MutableLiveData()
    val saveTokenResponse: MutableLiveData<Resource<GeneralResponse>> = MutableLiveData()
    val countNotificationResponse: MutableLiveData<Resource<CountNotificationResponse>> = MutableLiveData()
    val changeStatusGetOrdersResponse: MutableLiveData<Resource<GeneralResponse>> = MutableLiveData()


    fun saveToken(fcmToken : String) {
        if (Utilities.hasInternetConnection()) {
            saveTokenResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.saveToken(fcmToken)
                if (response.isSuccessful) {
                    saveTokenResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    fun countNotification() {
        if (Utilities.hasInternetConnection()) {
            countNotificationResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.countNotification()
                if (response.isSuccessful) {
                    countNotificationResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }



    fun notification(page: Int, countPaginate: Int) {
        if (Utilities.hasInternetConnection()) {
            notificationResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.notification(page,countPaginate)
                if (response.isSuccessful) {
                    notificationResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    fun changeStatusGetOrders() {
        if (Utilities.hasInternetConnection()) {
            changeStatusGetOrdersResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.changeStatusGetOrders()
                if (response.isSuccessful) {
                    changeStatusGetOrdersResponse.postValue(Resource.Success(response.body()!!))
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