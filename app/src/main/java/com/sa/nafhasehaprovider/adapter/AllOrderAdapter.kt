package com.sa.nafhasehaprovider.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.common.util.Utilities.Companion.onLoadImageFromUrl
import com.sa.nafhasehaprovider.databinding.ItemAllOrderBinding
import com.sa.nafhasehaprovider.entity.response.getAllOrdersResponse.DataAllOrdersResponse
import com.sa.nafhasehaprovider.interfaces.OrderDetails

class AllOrderAdapter(
    var context: Activity, var list: List<DataAllOrdersResponse>, var orderDetails: OrderDetails
) : RecyclerView.Adapter<AllOrderAdapter.ViewHolder?>() {


    inner class ViewHolder(binding: ItemAllOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemAllOrderBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemAllOrderBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_all_order, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
        holder.itemView.startAnimation(animation)
        var model = list[position]
        onLoadImageFromUrl(
            context, model.service!!.image, holder.binding.ivLogoService
        )
        onLoadImageFromUrl(
            context, model.provider!!.image, holder.binding.ivProvider)

        onLoadImageFromUrl(
            context, model.user!!.image, holder.binding.ivClient)

        holder.binding.tvNameService.text = model.service!!.title
        holder.binding.tvCodeOrder.text = context.getString(R.string.code) + " : " + model.invoice_no
        holder.binding.tvTime.text = model.time_at
        holder.binding.tvDate.text = model.date_at
        holder.binding.tvDistance.text = model.distance + "" + context.getString(R.string.km)


        holder.itemView.setOnClickListener {
            orderDetails.sendOrderId(model.id!!)
        }

        //صيانة
        if (model.status == "Maintenance") {

        }
        //استشارة الاعطال
        else if (model.status == "Consultation") {

        }
        //الفحص الدوري
        else if (model.status == "PeriodicInspection") {

        }
        //حواجز السيارات
        else if (model.status == "VehicleBarrier") {

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}