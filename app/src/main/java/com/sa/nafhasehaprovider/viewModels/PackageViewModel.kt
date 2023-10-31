package  com.sa.nafhasehaprovider.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sa.nafhasehaprovider.common.Resource
import com.sa.nafhasehaprovider.common.sharedprefrence.PreferencesUtils
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.entity.response.showPackageResponse.ShowPackageResponse
import com.sa.nafhasehaprovider.ui.repository.MainRepo
import kotlinx.coroutines.launch

class PackageViewModel(
    private val sharedPreferences: PreferencesUtils, private val mainRepo: MainRepo
) : ViewModel() {

    val packageResponse: MutableLiveData<Resource<ShowPackageResponse>> = MutableLiveData()


    fun shawPackage(packageId: Int) {
        if (Utilities.hasInternetConnection()) {
            packageResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.showPackage(packageId)
                if (response.isSuccessful) {
                    packageResponse.postValue(Resource.Success(response.body()!!))
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