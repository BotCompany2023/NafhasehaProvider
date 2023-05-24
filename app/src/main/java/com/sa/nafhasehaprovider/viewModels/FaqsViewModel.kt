package  com.sa.nafhasehaprovider.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sa.nafhasehaprovider.common.Resource
import com.sa.nafhasehaprovider.common.sharedprefrence.PreferencesUtils
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.entity.response.fqResponse.FaqsResponse
import com.sa.nafhasehaprovider.repository.MainRepo
import kotlinx.coroutines.launch

class FaqsViewModel(
    private val sharedPreferences: PreferencesUtils, private val mainRepo: MainRepo
) : ViewModel() {

    val faqsResponse: MutableLiveData<Resource<FaqsResponse>> = MutableLiveData()


    fun faqs() {
        if (Utilities.hasInternetConnection()) {
            faqsResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.faqs()
                if (response.isSuccessful) {
                    faqsResponse.postValue(Resource.Success(response.body()!!))
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