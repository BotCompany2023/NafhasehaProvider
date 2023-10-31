package  com.sa.nafhasehaprovider.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sa.nafhasehaprovider.common.Resource
import com.sa.nafhasehaprovider.common.sharedprefrence.PreferencesUtils
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.entity.response.walletResponse.WalletResponse
import com.sa.nafhasehaprovider.ui.repository.MainRepo
import kotlinx.coroutines.launch

class WalletViewModel(
    private val sharedPreferences: PreferencesUtils, private val mainRepo: MainRepo
) : ViewModel() {

    val walletResponse: MutableLiveData<Resource<WalletResponse>> = MutableLiveData()


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

}