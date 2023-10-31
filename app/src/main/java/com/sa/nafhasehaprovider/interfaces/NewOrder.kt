package  com.sa.nafhasehaprovider.interfaces

import com.sa.nafhasehaprovider.entity.response.getNewOrder.GetNewOrder

interface NewOrder {
    fun newOrder(model: GetNewOrder)
}