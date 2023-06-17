package  com.sa.nafhasehaprovider.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sa.nafhasehaprovider.common.Resource
import com.sa.nafhasehaprovider.common.sharedprefrence.PreferencesUtils
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.entity.response.addCarResponse.AddCarResponse
import com.sa.nafhasehaprovider.entity.response.carBrandsResponse.CarBrandsResponse
import com.sa.nafhasehaprovider.entity.response.carModelsResponse.CarModelsResponse
import com.sa.nafhasehaprovider.entity.response.carYearsResponse.CarYearsResponse
import com.sa.nafhasehaprovider.entity.response.generalResponse.GeneralResponse
import com.sa.nafhasehaprovider.entity.response.myCarResponse.MyCarResponse
import com.sa.nafhasehaprovider.entity.response.typeCarResponse.CarTypeResponse
import com.sa.nafhasehaprovider.repository.MainRepo
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MyCarViewModel(
    private val sharedPreferences: PreferencesUtils, private val mainRepo: MainRepo
) : ViewModel() {

    val getCarResponse: MutableLiveData<Resource<MyCarResponse>> = MutableLiveData()
    val getCarTypeResponse: MutableLiveData<Resource<CarTypeResponse>> = MutableLiveData()
    val getCarBrandsResponse: MutableLiveData<Resource<CarBrandsResponse>> = MutableLiveData()
    val getCarModelsResponse: MutableLiveData<Resource<CarModelsResponse>> = MutableLiveData()
    val getCarYearsResponse: MutableLiveData<Resource<CarYearsResponse>> = MutableLiveData()
    val addCarResponse: MutableLiveData<Resource<AddCarResponse>> = MutableLiveData()
    val updateCarResponse: MutableLiveData<Resource<AddCarResponse>> = MutableLiveData()
    val showDataCarResponse: MutableLiveData<Resource<AddCarResponse>> = MutableLiveData()
    val deleteCarResponse: MutableLiveData<Resource<GeneralResponse>> = MutableLiveData()


    fun getCar(page: Int, countPaginate: Int) {
        if (Utilities.hasInternetConnection()) {
            getCarResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.getCar(page, countPaginate)
                if (response.isSuccessful) {
                    getCarResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun carType(page: Int, countPaginate: String) {
        if (Utilities.hasInternetConnection()) {
            getCarTypeResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.getCarType(page, countPaginate)
                if (response.isSuccessful) {
                    getCarTypeResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    fun carBrands(typeId: Int, page: Int, countPaginate: String) {
        if (Utilities.hasInternetConnection()) {
            getCarBrandsResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.getCarBrands(typeId, page, countPaginate)
                if (response.isSuccessful) {
                    getCarBrandsResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun carModels(brandId: Int, page: Int, countPaginate: String) {
        if (Utilities.hasInternetConnection()) {
            getCarModelsResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.getCarModels(brandId, page, countPaginate)
                if (response.isSuccessful) {
                    getCarModelsResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun carCarYears(page: Int, countPaginate: String) {
        if (Utilities.hasInternetConnection()) {
            getCarYearsResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.getCarYears(page, countPaginate)
                if (response.isSuccessful) {
                    getCarYearsResponse.postValue(Resource.Success(response.body()!!))
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