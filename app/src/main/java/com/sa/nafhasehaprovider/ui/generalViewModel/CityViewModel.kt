package  com.sa.nafhasehaprovider.ui.generalViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sa.nafhasehaprovider.common.Resource
import com.sa.nafhasehaprovider.common.sharedprefrence.PreferencesUtils
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.entity.response.cityResponse.CityResponse
import com.sa.nafhasehaprovider.repository.MainRepo
import kotlinx.coroutines.launch

class CityViewModel(
    private val sharedPreferences: PreferencesUtils,
    private val mainRepo: MainRepo
) : ViewModel() {

    val cityResponse: MutableLiveData<Resource<CityResponse>> = MutableLiveData()


    fun city(countryId: Int,countPaginate:String) {
        if (Utilities.hasInternetConnection()) {
            cityResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response=mainRepo.city(countryId,countPaginate)
                if (response.isSuccessful) {
                    cityResponse.postValue(Resource.Success(response.body()!!))
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