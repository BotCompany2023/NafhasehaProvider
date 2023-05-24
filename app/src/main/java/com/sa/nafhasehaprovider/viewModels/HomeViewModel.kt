package  com.sa.nafhasehaprovider.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sa.nafhasehaprovider.common.Resource
import com.sa.nafhasehaprovider.common.sharedprefrence.PreferencesUtils
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.entity.response.providerDataResponse.ProviderDataResponse
import com.sa.nafhasehaprovider.entity.response.providesMapResponse.ProvidesMapResponse
import com.sa.nafhasehaprovider.repository.MainRepo
import com.sa.nafhasehaprovider.entity.response.homeResponse.HomeResponse
import kotlinx.coroutines.launch

class HomeViewModel(
    private val sharedPreferences: PreferencesUtils, private val mainRepo: MainRepo
) : ViewModel() {

    val homeResponse: MutableLiveData<Resource<HomeResponse>> = MutableLiveData()
    val providesMapResponse: MutableLiveData<Resource<ProvidesMapResponse>> = MutableLiveData()
    val showDataProvideMapResponse: MutableLiveData<Resource<ProviderDataResponse>> =
        MutableLiveData()


    fun home() {
        if (Utilities.hasInternetConnection()) {
            homeResponse.postValue(Resource.Loading())
            viewModelScope.launch {

                val response = mainRepo.home()
                if (response.isSuccessful) {
                    homeResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun providesMap(
        lat: Double, long: Double, service_id: Int
    ) {
        if (Utilities.hasInternetConnection()) {
            providesMapResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                try {

                    val response = mainRepo.providesMap(lat, long, service_id)
                    if (response.isSuccessful) {
                        providesMapResponse.postValue(Resource.Success(response.body()!!))
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


    fun showDataProvides(
        lat: Double, long: Double, idProvider: Int
    ) {
        if (Utilities.hasInternetConnection()) {
            showDataProvideMapResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                try {

                    val response = mainRepo.providesDataMap(lat, long, idProvider)
                    if (response.isSuccessful) {
                        showDataProvideMapResponse.postValue(Resource.Success(response.body()!!))
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

}