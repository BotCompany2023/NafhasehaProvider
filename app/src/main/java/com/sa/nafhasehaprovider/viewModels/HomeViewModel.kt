package  com.sa.nafhasehaprovider.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sa.nafhasehaprovider.common.Resource
import com.sa.nafhasehaprovider.common.sharedprefrence.PreferencesUtils
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.entity.response.homeResponse.HomeResponse
import com.sa.nafhasehaprovider.ui.repository.MainRepo
import kotlinx.coroutines.launch

class HomeViewModel(
    private val sharedPreferences: PreferencesUtils, private val mainRepo: MainRepo
) : ViewModel() {

    val homeResponse: MutableLiveData<Resource<HomeResponse>> = MutableLiveData()


    fun home(page: Int, countPaginate: Int) {
        if (Utilities.hasInternetConnection()) {
            homeResponse.postValue(Resource.Loading())
            viewModelScope.launch {

                val response = mainRepo.home(page, countPaginate)
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


}