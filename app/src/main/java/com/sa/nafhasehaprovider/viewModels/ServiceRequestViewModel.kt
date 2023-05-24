package  com.sa.nafhasehaprovider.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sa.nafhasehaprovider.common.Resource
import com.sa.nafhasehaprovider.common.sharedprefrence.PreferencesUtils
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.entity.response.categoriesResponse.DataCategoriesResponse
import com.sa.nafhasehaprovider.entity.response.checkCouponResponse.CheckCouponResponse
import com.sa.nafhasehaprovider.entity.response.generalResponse.GeneralResponse
import com.sa.nafhasehaprovider.entity.response.homeOrCenterResponse.HomeOrCenterResponse
import com.sa.nafhasehaprovider.entity.response.vehicleTransporterResponse.VehicleTransporterResponse
import com.sa.nafhasehaprovider.repository.MainRepo
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ServiceRequestViewModel(
    private val sharedPreferences: PreferencesUtils, private val mainRepo: MainRepo
) : ViewModel() {

    val categoriesResponse: MutableLiveData<Resource<DataCategoriesResponse>> =
        MutableLiveData()
    val getVehicleTransporterResponse: MutableLiveData<Resource<VehicleTransporterResponse>> =
        MutableLiveData()
    val checkCouponCodeResponse: MutableLiveData<Resource<CheckCouponResponse>> = MutableLiveData()
    val homeOrCenterResponse: MutableLiveData<Resource<HomeOrCenterResponse>> = MutableLiveData()
    val periodicInspectionResponse: MutableLiveData<Resource<GeneralResponse>> = MutableLiveData()
    val transporterResponse: MutableLiveData<Resource<GeneralResponse>> = MutableLiveData()
    val maintenanceResponse: MutableLiveData<Resource<GeneralResponse>> = MutableLiveData()




    fun homeOrCenter() {
        if (Utilities.hasInternetConnection()) {
            homeOrCenterResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.homeOrCenter()
                if (response.isSuccessful) {
                    homeOrCenterResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun vehicleTransporter() {
        if (Utilities.hasInternetConnection()) {
            getVehicleTransporterResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.getVehicleTransporter()
                if (response.isSuccessful) {
                    getVehicleTransporterResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun checkCouponCode(coupon_code: String, service_id: Int) {
        if (Utilities.hasInternetConnection()) {
            checkCouponCodeResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.checkCouponCode(coupon_code, service_id)
                if (response.isSuccessful) {
                    checkCouponCodeResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    //خدمة الفحص الدوري
    fun periodicInspection(
        vehicle_id: Int, date_at: String, time_at: String, city_id: Int,
        coupon_code: String
    ) {
        if (Utilities.hasInternetConnection()) {
            periodicInspectionResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response =
                    mainRepo.periodicInspection(vehicle_id, date_at, time_at, city_id, coupon_code)
                if (response.isSuccessful) {
                    periodicInspectionResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    //خدمةالسطحة
    fun transporter(
        transporter_id: Int,
        date_at: String,
        time_at: String,
        address: String,
        lat: Double,
        long: Double,
        address_to: String,
        lat_to: Double,
        long_to: Double,
        details: String,
        coupon_code: String
    ) {
        if (Utilities.hasInternetConnection()) {
            transporterResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.transporter(
                    transporter_id,
                    date_at,
                    time_at,
                    address,
                    lat,
                    long,
                    address_to,
                    lat_to,
                    long_to,
                    details,
                    coupon_code
                )
                if (response.isSuccessful) {
                    transporterResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    //خدمةالصيانة
    fun maintenance(
        vehicle_id: RequestBody,
        category_id: RequestBody,
        type_from: RequestBody,
        date_at: RequestBody,
        time_at: RequestBody,
        lat: RequestBody,
        long: RequestBody,
        address: RequestBody,
        details: RequestBody,
        coupon_code: RequestBody,
        images: List<MultipartBody.Part>
    ) {
        if (Utilities.hasInternetConnection()) {
            maintenanceResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.maintenance(
                    vehicle_id,
                    category_id,
                    type_from,
                    date_at,
                    time_at,
                    lat,
                    long,
                    address,
                    details,
                    coupon_code,
                    images
                )
                if (response.isSuccessful) {
                    maintenanceResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    //خدمة استشارة الاعطال
    fun consultation(
        vehicle_id: RequestBody,
        category_id: RequestBody,
        city_id: RequestBody,
        details: RequestBody,
        coupon_code: RequestBody,
        images: List<MultipartBody.Part>
    ) {
        if (Utilities.hasInternetConnection()) {
            maintenanceResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.consultation(
                    vehicle_id,
                    category_id,
                    city_id,
                    details,
                    coupon_code,
                    images
                )
                if (response.isSuccessful) {
                    maintenanceResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    //خدمة حواجز السيارات
    fun vehicleBarriers(
        vehicle_id: Int,
        position: List<String>,
        date_at: String,
        time_at: String,
        city_id: Int,
        coupon_code: String,
    ) {
        if (Utilities.hasInternetConnection()) {
            transporterResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.vehicleBarriers(
                    vehicle_id,
                    position,
                    date_at,
                    time_at,
                    city_id,
                    coupon_code
                )
                if (response.isSuccessful) {
                    transporterResponse.postValue(Resource.Success(response.body()!!))
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