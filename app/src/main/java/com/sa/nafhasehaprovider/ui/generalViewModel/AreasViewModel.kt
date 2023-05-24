package com.sa.nafhasehaprovider.ui.generalViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sa.nafhasehaprovider.common.Resource
import com.sa.nafhasehaprovider.common.sharedprefrence.PreferencesUtils
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.entity.response.areasResponse.AreasResponse
import com.sa.nafhasehaprovider.repository.MainRepo
import kotlinx.coroutines.launch

class AreasViewModel(
    private val sharedPreferences: PreferencesUtils,
    private val mainRepo: MainRepo
) : ViewModel() {

    val areasResponse: MutableLiveData<Resource<AreasResponse>> = MutableLiveData()


    fun areas(cityId: Int,countPaginate:String) {
        if (Utilities.hasInternetConnection()) {
            areasResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response=mainRepo.areas(cityId,countPaginate)
                if (response.isSuccessful) {
                    areasResponse.postValue(Resource.Success(response.body()!!))
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