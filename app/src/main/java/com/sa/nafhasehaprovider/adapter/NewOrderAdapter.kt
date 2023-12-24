package com.sa.nafhasehaprovider.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.databinding.ItemAllOrderBinding
import com.sa.nafhasehaprovider.databinding.ItemTransactionsBinding
import com.sa.nafhasehaprovider.entity.response.getNewOrder.ResponseNewOrder
import com.sa.nafhasehaprovider.entity.response.homeResponse.DataHomeResponse
import com.sa.nafhasehaprovider.entity.response.homeResponse.NewOrderHomeResponse
import com.sa.nafhasehaprovider.entity.response.walletResponse.DebitWalletResponse
import com.sa.nafhasehaprovider.interfaces.OrderDetails
import java.util.*


class NewOrderAdapter(
    var context: Activity, var listNewOrder: List<NewOrderHomeResponse>
    ,var orderDetails: OrderDetails
) : RecyclerView.Adapter<NewOrderAdapter.ViewHolder?>() {



    inner class ViewHolder(binding: ItemAllOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemAllOrderBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemAllOrderBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_all_order, parent, false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
       // holder.itemView.startAnimation(animation)
        var model = listNewOrder[position]

        holder.binding.tvNameService.text = model.category!!.title

        Utilities.onLoadImageFromUrl(context,model.category!!.image,
        holder.binding.ivLogoService)
        holder.binding.tvCodeOrder.text =context.getString(R.string.the_code)+" : "+ model.invoice_no

        holder.binding.layoutAction.visibility=View.VISIBLE
        holder.binding.viewTracking.visibility=View.VISIBLE

        if (model.is_price_request==1){
            holder.binding.constraintOfferPrice.visibility=View.VISIBLE
            holder.binding.tvOfferPrice.text=""+model.price_request +" "+context.getString(R.string.sar)
            holder.binding.btnOffer.text=context.getString(R.string.update_offer)
            holder.binding.btnOffer.setOnClickListener {
                orderDetails.sendOffer(model.id,""+model.price_request!!.toInt(),model.price_type!!)
            }
            holder.binding.tvHideRequest.visibility=View.GONE

        }
        else{
            holder.binding.constraintOfferPrice.visibility=View.GONE
            holder.binding.btnOffer.text=context.getString(R.string.offer)
            holder.binding.btnOffer.setOnClickListener {
                orderDetails.sendOffer(model.id,model.suggested_price,model.price_type!!)
            }

            holder.binding.tvHideRequest.visibility=View.VISIBLE

        }

        if (model.type=="PeriodicInspection")
        {
            holder.binding.tvPrice.text = model.final_total +" "+context.getString(R.string.sar)
            holder.binding.layoutAction.visibility=View.GONE
            holder.binding.btnAcceptOrder.visibility=View.VISIBLE
        }
        else if(model.type=="Petrol"){
            holder.binding.tvPrice.text = model.suggested_price +" "+context.getString(R.string.sar)
            holder.binding.layoutAction.visibility=View.VISIBLE
            holder.binding.btnAcceptOrder.visibility=View.GONE
        }
        else{
            holder.binding.tvPrice.text = model.suggested_price +" "+context.getString(R.string.sar)
            holder.binding.layoutAction.visibility=View.VISIBLE
            holder.binding.btnAcceptOrder.visibility=View.GONE
        }

        holder.itemView.setOnClickListener {
            orderDetails.sendOrderId(model.id)
        }

       holder.binding.tvHideRequest.setOnClickListener {
            orderDetails.cancelOrderId(model.id,position)
        }


       holder.binding.btnAcceptOrder.setOnClickListener {
            orderDetails.acceptOrder(model.id,position)
        }




    }

    override fun getItemCount(): Int {
        return listNewOrder.size
    }


}