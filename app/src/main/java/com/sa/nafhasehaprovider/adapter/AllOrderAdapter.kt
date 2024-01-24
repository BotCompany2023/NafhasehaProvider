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
import com.sa.nafhasehaprovider.entity.response.ordersResponse.DataOrdersResponse
import com.sa.nafhasehaprovider.interfaces.OrderDetails


class AllOrderAdapter(
    var context: Activity, var list: List<DataOrdersResponse>, var orderDetails: OrderDetails
) : RecyclerView.Adapter<AllOrderAdapter.ViewHolder?>() {


    private  var imageUser: String=""

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
            holder.binding.viewTracking.visibility=View.GONE
            holder.binding.layoutTraking.visibility=View.GONE
            holder.binding.tvCancelOrder.visibility=View.GONE
            holder.binding.tvStatus.text = context.getString(R.string.approved)
            holder.binding.tvStatus.setBackgroundResource(R.drawable.shape_pending)

            if (model.user!!.image !=null){
                imageUser=model.user!!.image!!
            }
            holder.binding.tvTraking.setOnClickListener {
                orderDetails.trackingUser(model.id,
                    model.user!!.id,
                model.lat!!.toFloat(),
                model.long!!.toFloat(),
                    imageUser!!,
                    model.user!!.name!!,
                    model.user!!.phone!!,
                model.distance!!,
                "")
            }

            //سطحه
            if (model.type == "TransportVehicle") {
                holder.binding.tvStatus.text = context.getString(R.string.the_client_is_waiting)
            }


        }
        //مرفوض
        else if (model.status == "canceled") {
            holder.binding.tvStatus.visibility=View.VISIBLE
            holder.binding.tvStatus.text = context.getString(R.string.canceled)
            holder.binding.tvStatus.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.tvStatus.setBackgroundResource(R.drawable.shape_cancel)

        }
        //مكتمل
        else if (model.status == "completed") {
            holder.binding.tvStatus.text = context.getString(R.string.completed)
            holder.binding.tvStatus.setBackgroundResource(R.drawable.shape_abrroved)
            holder.binding.tvStatus.setTextColor(context.resources.getColor(R.color.green))

        }

        //جاري فحص السياره في مركز الصيانه
        else if (model.status == "PickUp") {


            //صيانه
            if (model.type == "Maintenance") {
                if (model.is_report==true)
                {

                    holder.binding.tvStatus.setBackgroundResource(R.drawable.shape_pending)
                    holder.binding.tvStatus.text = context.getString(R.string.waiting_for_customer_approval)
                    holder.binding.viewTracking.visibility=View.GONE
                    holder.binding.btnSubmitReports.visibility=View.GONE

                    if (model.report!!.status=="Accept")
                    {
                        holder.binding.tvStatus.text =context.getString(R.string.under_maintenance)
                    }
                }

                holder.binding.btnSubmitReports.setOnClickListener {
                    orderDetails.submitReports(model.id)
                }
            }
            else{
                holder.binding.tvStatus.text = context.getString(R.string.the_car_is_being_moved)
                holder.binding.tvStatus.setBackgroundResource(R.drawable.shape_pending)
                holder.binding.btnSubmitReports.visibility=View.GONE
                holder.binding.viewTracking.visibility=View.GONE
            }




        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}