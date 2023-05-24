package  com.sa.nafhasehaprovider.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sa.nafhasehaprovider.common.Resource
import com.sa.nafhasehaprovider.common.sharedprefrence.PreferencesUtils
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.entity.response.getAllOrdersResponse.GetAllPendingResponse
import com.sa.nafhasehaprovider.repository.MainRepo
import kotlinx.coroutines.launch

class OrdersViewModel(
    private val sharedPreferences: PreferencesUtils, private val mainRepo: MainRepo
) : ViewModel() {

    val orderPendingResponse: MutableLiveData<Resource<GetAllPendingResponse>> = MutableLiveData()


    fun orderPending() {
        if (Utilities.hasInternetConnection()) {
            orderPendingResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.ordersPending()
                if (response.isSuccessful) {
                    orderPendingResponse.postValue(Resource.Success(response.body()!!))
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