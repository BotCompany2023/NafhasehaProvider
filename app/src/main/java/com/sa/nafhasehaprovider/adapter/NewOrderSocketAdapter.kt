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
import com.sa.nafhasehaprovider.entity.response.homeResponse.NewOrderHomeResponse
import com.sa.nafhasehaprovider.entity.response.walletResponse.DebitWalletResponse
import com.sa.nafhasehaprovider.interfaces.OrderDetails
import java.util.*


class NewOrderSocketAdapter(
    var context: Activity, var listNewOrder: List<ResponseNewOrder>
    ,var orderDetails: OrderDetails
) : RecyclerView.Adapter<NewOrderSocketAdapter.ViewHolder?>() {


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
        holder.itemView.startAnimation(animation)
        var model = listNewOrder[position]


//            if (Locale.getDefault().displayLanguage == "English" || Locale.getDefault().displayLanguage == "الانجليزية"|| Locale.getDefault().displayLanguage == "en") {
//                holder.binding.tvNameService.text = model.category_name!![position].en!!
//            } else if (Locale.getDefault().displayLanguage == "Arabic" || Locale.getDefault().displayLanguage == "العربية"|| Locale.getDefault().displayLanguage == "ar") {
//                holder.binding.tvNameService.text = model.category_name!![position].ar!!
//            }


        if (Locale.getDefault().language == "en") {
            holder.binding.tvNameService.text = model.category_name_en!!
        } else if (Locale.getDefault().language == "ar") {
            holder.binding.tvNameService.text = model.category_name_ar
        }

        Utilities.onLoadImageFromUrl(context,"https://nafhasuha.com/assets/images/"+model.category_image,
        holder.binding.ivLogoService)
        holder.binding.tvCodeOrder.text =context.getString(R.string.the_code)+" : "+ model.invoice_no
        holder.binding.tvPrice.text =""+ model.suggested_price +" "+context.getString(R.string.sar)

        holder.binding.layoutAction.visibility=View.VISIBLE
        holder.binding.viewTracking.visibility=View.VISIBLE


        if (model.type=="PeriodicInspection")
        {
            holder.binding.layoutAction.visibility=View.GONE
            holder.binding.btnAcceptOrder.visibility=View.VISIBLE
        }
        else if (model.type=="Maintenance")
        {
            holder.binding.layoutAction.visibility=View.GONE
            holder.binding.btnAcceptOrder.visibility=View.VISIBLE
        }
        else{
            holder.binding.layoutAction.visibility=View.VISIBLE
            holder.binding.btnAcceptOrder.visibility=View.GONE
        }



        holder.itemView.setOnClickListener {
            orderDetails.sendOrderId(model.order_id)
        }

        holder.binding.btnOffer.setOnClickListener {
            orderDetails.sendOffer(model.order_id,
                ""+model.suggested_price.toInt(),model.price_type!!)
        }

        holder.binding.btnAcceptOrder.setOnClickListener {
            orderDetails.acceptOrder(model.order_id,position)
        }

    }

    override fun getItemCount(): Int {
        return listNewOrder.size
    }


}