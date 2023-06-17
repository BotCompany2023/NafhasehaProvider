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
import com.sa.nafhasehaprovider.entity.response.homeResponse.NewOrderHomeResponse
import com.sa.nafhasehaprovider.entity.response.walletResponse.DebitWalletResponse
import com.sa.nafhasehaprovider.interfaces.OrderDetails


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
        holder.itemView.startAnimation(animation)
        var model = listNewOrder[position]

//        holder.binding.constraintDistance.visibility=View.GONE
        Utilities.onLoadImageFromUrl(context,model.service!!.image,
        holder.binding.ivLogoService)
        holder.binding.tvNameService.text = model.service!!.title
        holder.binding.tvCodeOrder.text =context.getString(R.string.the_code)+":"+ model.id
        holder.binding.tvDate.text = model.date_at
        holder.binding.tvDistance.text = model.distance +""+context.getString(R.string.km)

        holder.itemView.setOnClickListener {
            orderDetails.sendOrderId(model.id)
        }
    }

    override fun getItemCount(): Int {
        return listNewOrder.size
    }


}