package  com.sa.nafhasehaprovider.interfaces

interface OrderDetails {
    fun sendOrderId(idOrder:Int)
    fun cancelOrderId(idOrder:Int,pot:Int)
    fun acceptOrder(idOrder:Int,pot:Int)
    fun sendOffer(
        idOrder: Int,offerPrice: String,priceType:Int
    )
    fun trackingUser(
        orderId:Int,
        userID:Int,orderLat:Float,orderLong:Float,userImage:String,userName:String,userPhone:String,
    distance:String,estimatedTime:String)
}