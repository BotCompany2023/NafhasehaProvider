package  com.sa.nafhasehaprovider.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sa.nafhasehaprovider.common.Resource
import com.sa.nafhasehaprovider.common.sharedprefrence.PreferencesUtils
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.entity.response.generalResponse.GeneralResponse
import com.sa.nafhasehaprovider.entity.response.getBanksResponse.GetBanksResponse
import com.sa.nafhasehaprovider.entity.response.walletResponse.WalletResponse
import com.sa.nafhasehaprovider.ui.repository.MainRepo
import kotlinx.coroutines.launch

class WalletViewModel(
    private val sharedPreferences: PreferencesUtils, private val mainRepo: MainRepo
) : ViewModel() {

    val walletResponse: MutableLiveData<Resource<WalletResponse>> = MutableLiveData()
    val banksResponse: MutableLiveData<Resource<GetBanksResponse>> = MutableLiveData()
    val requestWithdrawalResponse: MutableLiveData<Resource<GeneralResponse>> = MutableLiveData()


    fun wallet(page: Int, countPaginate: Int) {
        if (Utilities.hasInternetConnection()) {
            walletResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.wallet(page, countPaginate)
                if (response.isSuccessful) {
                    walletResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun getBanks() {
        if (Utilities.hasInternetConnection()) {
            banksResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.getBanks()
                if (response.isSuccessful) {
                    banksResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    fun requestWithdrawal(bank_id:Int,full_name:String,Iban:String,amount:Int) {
        if (Utilities.hasInternetConnection()) {
            requestWithdrawalResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.requestWithdrawal(bank_id, full_name,Iban, amount)
                if (response.isSuccessful) {
                    requestWithdrawalResponse.postValue(Resource.Success(response.body()!!))
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