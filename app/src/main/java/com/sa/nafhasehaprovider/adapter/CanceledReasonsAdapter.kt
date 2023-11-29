package com.sa.nafhasehaprovider.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sa.nafhaseha.entity.response.canceledReasonsResponse.DataCanceledReasonsResponse
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.databinding.ItemReasonBinding
import kotlinx.android.synthetic.main.custom_spinner_item.view.*

class CanceledReasonsAdapter(
    var context: Activity, var list: List<DataCanceledReasonsResponse>) : RecyclerView.Adapter<CanceledReasonsAdapter.ViewHolder?>() {


    var idReason :Int=0
    private var selectedItem: Int=-1

    inner class ViewHolder(binding: ItemReasonBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemReasonBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemReasonBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_reason, parent, false
        )
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
       // holder.itemView.startAnimation(animation)
        var model = list[position]

        holder.binding.tvTitle.text = model!!.title

        holder.itemView.setOnClickListener {
            selectedItem = holder.layoutPosition
            notifyDataSetChanged()

        }

        holder.itemView.setOnClickListener {
            selectedItem = holder.layoutPosition
            idReason=model.id
            notifyDataSetChanged()

        }

        when (selectedItem) {
//            0 -> {
//                holder.binding.cbTitle.setBackgroundResource(
//                    R.drawable.icons_checkbox_unchecked)
//            }
            position -> {
                holder.binding.cbTitle.setBackgroundResource(
                    R.drawable.icons_checkbox_checked)
                holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.black))

            }
            else -> {
                holder.binding.cbTitle.setBackgroundResource(
                    R.drawable.icons_checkbox_unchecked)
                holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.grey))

            }
        }



    }

    override fun getItemCount(): Int {
        return list.size
    }


}