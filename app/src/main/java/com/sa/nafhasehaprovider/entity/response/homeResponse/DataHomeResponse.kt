package  com.sa.nafhasehaprovider.entity.response.homeResponse


data class DataHomeResponse(
    val packages: List<PackageHomeResponse>,
    val services: List<ServiceHomeResponse>,
    val sliders: List<SliderHomeResponse>
)