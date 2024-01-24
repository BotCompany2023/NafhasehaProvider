package com.sa.nafhasehaprovider.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.databinding.ItemAllVehicleTransporterBinding
import com.sa.nafhasehaprovider.entity.response.vehicleTransporterResponse.DataVehicleTransporterResponse
import com.sa.nafhasehaprovider.interfaces.ClickItemFilterService

class VehicleTransporterAdapter(
    var context: Activity,
    var list: ArrayList<DataVehicleTransporterResponse>,
    var clickItemFilterService: ClickItemFilterService,
    var vehicleTransporterID:Int
) : RecyclerView.Adapter<VehicleTransporterAdapter.ViewHolder?>() {

    private var selectedItem: Int =-1

    inner class ViewHolder(binding: ItemAllVehicleTransporterBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemAllVehicleTransporterBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemAllVehicleTransporterBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_all_vehicle_transporter, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
        //holder.itemView.startAnimation(animation)
        var model = list[position]
        Utilities.onLoadImageFromUrl(
            context, model.image, holder.binding.ivImageCar
        )



        holder.itemView.setOnClickListener {
            selectedItem = holder.layoutPosition
//            clickItemFilterService.Item(model.id)
            notifyDataSetChanged()
        }

        if (selectedItem == position) {
            clickItemFilterService.Item(model.id)
            holder.binding.ivChickCar.visibility = View.VISIBLE
        }
        else {
            holder.binding.ivChickCar.visibility = View.GONE

            if (vehicleTransporterID==model.id) {
                // clickItemFilterService.Item(model.id)
                holder.binding.ivChickCar.visibility = View.VISIBLE
            }

        }

    }
    override fun getItemCount(): Int {
        return list.size
    }


}