package  com.sa.nafhasehaprovider.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sa.nafhaseha.entity.response.canceledReasonsResponse.CanceledReasonsResponse
import com.sa.nafhasehaprovider.common.Resource
import com.sa.nafhasehaprovider.common.sharedprefrence.PreferencesUtils
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.entity.response.cancelOrderResponse.CancelOrderResponse
import com.sa.nafhasehaprovider.entity.response.generalResponse.GeneralResponse
import com.sa.nafhasehaprovider.entity.response.getAllOrdersResponse.AllOrdersResponse
import com.sa.nafhasehaprovider.entity.response.showOrderResponse.ShowOrderResponse
import com.sa.nafhasehaprovider.ui.repository.MainRepo
import kotlinx.coroutines.launch

class OrdersViewModel(
    private val sharedPreferences: PreferencesUtils, private val mainRepo: MainRepo
) : ViewModel() {

    val allOrdersApprovedResponse: MutableLiveData<Resource<AllOrdersResponse>> = MutableLiveData()
    val allOrdersCompletedResponse: MutableLiveData<Resource<AllOrdersResponse>> = MutableLiveData()
    val showOrderResponse: MutableLiveData<Resource<ShowOrderResponse>> = MutableLiveData()
    val submitPriceOfferResponse: MutableLiveData<Resource<ShowOrderResponse>> = MutableLiveData()
    val getCanceledReasonsResponse: MutableLiveData<Resource<CanceledReasonsResponse>> = MutableLiveData()
    val cancelOrderResponse: MutableLiveData<Resource<CancelOrderResponse>> = MutableLiveData()
    val acceptedOrderResponse: MutableLiveData<Resource<GeneralResponse>> = MutableLiveData()


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


    fun showOrder(idOrder: Int) {
        if (Utilities.hasInternetConnection()) {
            showOrderResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.showOrder(idOrder)
                if (response.isSuccessful) {
                    showOrderResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    fun submitPriceOffer(idOrder: Int,price:String) {
        if (Utilities.hasInternetConnection()) {
            submitPriceOfferResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.submitPriceOffer(idOrder,price)
                if (response.isSuccessful) {
                    submitPriceOfferResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }
            }
        }
    }


    fun getCanceledReasons() {
        if (Utilities.hasInternetConnection()) {
            getCanceledReasonsResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.getCanceledReasons()
                if (response.isSuccessful) {
                    getCanceledReasonsResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    fun cancelOrder(idOrder: Int,cancelReasonId: Int) {
        if (Utilities.hasInternetConnection()) {
            cancelOrderResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.cancelOrder(idOrder, cancelReasonId)
                if (response.isSuccessful) {
                    cancelOrderResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun acceptedOrder(idOrder: Int) {
        if (Utilities.hasInternetConnection()) {
            acceptedOrderResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.acceptedOrder(idOrder)
                if (response.isSuccessful) {
                    acceptedOrderResponse.postValue(Resource.Success(response.body()!!))
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