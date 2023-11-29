package  com.sa.nafhasehaprovider.interfaces

import com.sa.nafhasehaprovider.entity.response.acceptedOrRejectedOfferSocketResponse.AcceptedOrRejectedOfferSocketResponse
import com.sa.nafhasehaprovider.entity.response.getNewOrder.GetNewOrder

interface AcceptedOrRejectedOffer {
    fun acceptedOrRejectedOffer(model: AcceptedOrRejectedOfferSocketResponse)
}