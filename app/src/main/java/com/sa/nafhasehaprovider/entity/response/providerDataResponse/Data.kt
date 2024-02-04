package  com.sa.nafhasehaprovider.entity.response.providerDataResponse

import androidx.annotation.Keep
import com.sa.nafhasehaprovider.entity.response.areasResponse.AreasResponseData
import com.sa.nafhasehaprovider.entity.response.cityResponse.CityResponseData
import com.sa.nafhasehaprovider.entity.response.cityResponse.Country
@Keep
data class Data(
    val address: String,
    val area: AreasResponseData,
    val avg_rate: String,
    val categories: List<CategoryProviderDataResponse>,
    val city: CityResponseData,
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