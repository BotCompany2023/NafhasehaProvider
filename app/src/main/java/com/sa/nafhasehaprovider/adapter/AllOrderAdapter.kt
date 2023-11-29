package com.sa.nafhasehaprovider.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
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
            context, model.category!!.image, holder.binding.ivLogoService
        )


        holder.binding.tvNameService.text = model.category!!.title
        holder.binding.tvCodeOrder.text = context.getString(R.string.code) + " : " + model.invoice_no
        holder.binding.tvPrice.text = model.final_total +" "+context.getString(R.string.sar)


        holder.itemView.setOnClickListener {
            orderDetails.sendOrderId(model.id!!)
        }
        holder.binding.tvCancelOrder.setOnClickListener {
            orderDetails.cancelOrderId(model.id,position)
        }

        //صيانة
        if (model.type == "Maintenance") {

        }
        //استشارة الاعطال
        else if (model.type == "Consultation") {

        }
        //الفحص الدوري
        else if (model.type == "PeriodicInspection") {

        }
        //حواجز السيارات
        else if (model.type == "VehicleBarrier") {

        }



        //جديد
        if (model.status == "pending") {
            holder.binding.tvStatus.visibility=View.VISIBLE
            holder.binding.tvStatus.text = context.getString(R.string.news)
        }
        //تم الموافقة عليه
        else if (model.status == "approved") {
            holder.binding.tvStatus.visibility=View.VISIBLE
            holder.binding.viewTracking.visibility=View.VISIBLE
            holder.binding.layoutTraking.visibility=View.VISIBLE
            holder.binding.tvStatus.text = context.getString(R.string.approved)
            holder.binding.tvStatus.setBackgroundResource(R.drawable.shape_abrroved)

            holder.binding.tvTraking.setOnClickListener {
                orderDetails.trackingUser(model.id,
                    model.user!!.id,
                model.lat!!.toFloat(),
                model.long!!.toFloat(),
                    model.user!!.image!!,
                    model.user!!.name!!,
                    model.user!!.phone!!,
                model.distance!!,
                "")
            }
        }
        //مرفوض
        else if (model.status == "canceled") {
            holder.binding.tvStatus.visibility=View.VISIBLE
            holder.binding.tvStatus.text = context.getString(R.string.canceled)
            holder.binding.tvStatus.setTextColor(context.resources.getColor(`in`.aabhasjindal.otptextview.R.color.red))
            holder.binding.tvStatus.setBackgroundResource(R.drawable.shape_cancel)

        }

        //منتهي
        else if (model.status == "PeriodicInspection") {

        }
        //مكتمل
        else if (model.status == "VehicleBarrier") {

        }

    }

    override fun getItemCount(): Int {
        return list.size
    }


}