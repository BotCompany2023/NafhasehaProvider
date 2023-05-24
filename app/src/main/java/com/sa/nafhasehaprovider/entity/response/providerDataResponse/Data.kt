package  com.sa.nafhasehaprovider.entity.response.providerDataResponse

import com.sa.nafhasehaprovider.entity.response.authenticationResponse.Area
import com.sa.nafhasehaprovider.entity.response.authenticationResponse.City
import com.sa.nafhasehaprovider.entity.response.cityResponse.Country

data class Data(
    val address: String,
    val area: Area,
    val avg_rate: String,
    val categories: List<CategoryProviderDataResponse>,
    val city: City,
    val country: Country,
    val distance: String,
    val email: String,
    val estimated_time: String,
    val id: Int,
    val image: String,
    val lat: String,
    val long: String,
    val name: String,
    val phone: String,
    val provider_type: String,
    val rates_count: Int
)