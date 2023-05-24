package  com.sa.nafhasehaprovider.ui.generalViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sa.nafhasehaprovider.common.sharedprefrence.PreferencesUtils
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.entity.response.generalResponse.GeneralResponse
import com.sa.nafhasehaprovider.common.Resource
import kotlinx.coroutines.launch
import com.sa.nafhasehaprovider.repository.MainRepo

class LogOutViewModel(
    private val sharedPreferences: PreferencesUtils,
    private val mainRepo: MainRepo
) : ViewModel() {

    val logOutResponse: MutableLiveData<Resource<GeneralResponse>> = MutableLiveData()


    fun logOut() {
        if (Utilities.hasInternetConnection()) {
            logOutResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.logOut()
                if (response.isSuccessful) {
                    logOutResponse.postValue(Resource.Success(response.body()!!))
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