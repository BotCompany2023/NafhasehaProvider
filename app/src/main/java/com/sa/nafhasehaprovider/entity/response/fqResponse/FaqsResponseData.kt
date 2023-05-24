package  com.sa.nafhasehaprovider.entity.response.fqResponse

data class FaqsResponseData(
    val faqs: List<Faq>,
    val id: Int,
    val title: String
)