package  com.sa.nafhasehaprovider.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sa.nafhasehaprovider.common.Resource
import com.sa.nafhasehaprovider.common.sharedprefrence.PreferencesUtils
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.entity.response.getAllOrdersResponse.AllOrdersResponse
import com.sa.nafhasehaprovider.repository.MainRepo
import kotlinx.coroutines.launch

class OrdersViewModel(
    private val sharedPreferences: PreferencesUtils, private val mainRepo: MainRepo
) : ViewModel() {

    val allOrdersApprovedResponse: MutableLiveData<Resource<AllOrdersResponse>> = MutableLiveData()
    val allOrdersCompletedResponse: MutableLiveData<Resource<AllOrdersResponse>> = MutableLiveData()


    fun ordersApproved(page: Int, countPaginate: Int) {
        if (Utilities.hasInternetConnection()) {
            allOrdersApprovedResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.ordersApproved(page, countPaginate)
                if (response.isSuccessful) {
                    allOrdersApprovedResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    fun ordersCompleted(page: Int, countPaginate: Int) {
        if (Utilities.hasInternetConnection()) {
            allOrdersCompletedResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.ordersCompleted(page, countPaginate)
                if (response.isSuccessful) {
                    allOrdersCompletedResponse.postValue(Resource.Success(response.body()!!))
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