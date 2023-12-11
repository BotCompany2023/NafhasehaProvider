package  com.sa.nafhasehaprovider.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksa.smartcarb.entity.response.versionUpdateResponse.VersionUpdateResponse
import com.sa.nafhasehaprovider.common.Resource
import com.sa.nafhasehaprovider.common.sharedprefrence.PreferencesUtils
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.entity.response.generalResponse.GeneralResponse
import com.sa.nafhasehaprovider.entity.response.iconSocialMediaResponse.IconSocialMediaResponse
import com.sa.nafhasehaprovider.entity.response.infoResponse.InfoResponse
import com.sa.nafhasehaprovider.ui.repository.MainRepo
import kotlinx.coroutines.launch

class InfoViewModel(
    private val sharedPreferences: PreferencesUtils, private val mainRepo: MainRepo
) : ViewModel() {

    val infoResponse: MutableLiveData<Resource<InfoResponse>> = MutableLiveData()
    val socialMediaResponse: MutableLiveData<Resource<IconSocialMediaResponse>> = MutableLiveData()
    val contactUsResponse: MutableLiveData<Resource<GeneralResponse>> = MutableLiveData()
    val versionUpdateResponse: MutableLiveData<Resource<VersionUpdateResponse>> = MutableLiveData()


    fun info(type: String) {
        if (Utilities.hasInternetConnection()) {
            infoResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.infos(type)
                if (response.isSuccessful) {
                    infoResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun socialMedia() {
        if (Utilities.hasInternetConnection()) {
            socialMediaResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.iconsSocialMedia()
                if (response.isSuccessful) {
                    socialMediaResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun contactUs(
        title: String, country_id: Int, phone: String, note: String
    ) {
        if (Utilities.hasInternetConnection()) {
            contactUsResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.contactUs(title, country_id, phone, note)
                if (response.isSuccessful) {
                    contactUsResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    fun versionUpdate(deviceType: String, currentVersion: String) {
        if (Utilities.hasInternetConnection()) {
            versionUpdateResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.versionUpdate(deviceType,currentVersion)
                if (response.isSuccessful) {
                    versionUpdateResponse.postValue(Resource.Success(response.body()!!))
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