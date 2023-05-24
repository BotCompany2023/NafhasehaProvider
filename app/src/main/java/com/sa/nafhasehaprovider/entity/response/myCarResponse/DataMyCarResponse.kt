package  com.sa.nafhasehaprovider.entity.response.myCarResponse


data class DataMyCarResponse(
    val first_image: String,
    val id: Int,
    val images: List<ImageMyCarResponse>? =null,
    val letters_ar: String,
    val letters_en: String,
    val numbers_ar: String,
    val numbers_en: String,
    val periodic_inspection: String,
    val status: Int,
    val vehicle_brand: VehicleBrand? =null,
    val vehicle_manufacture_year: VehicleManufactureYear? =null,
    val vehicle_model: VehicleModel? =null,
    val vehicle_type: VehicleType? =null
)