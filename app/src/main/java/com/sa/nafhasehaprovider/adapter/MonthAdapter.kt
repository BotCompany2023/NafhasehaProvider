package com.sa.nafhasehaprovider.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.databinding.ItemMonthBinding
import com.sa.nafhasehaprovider.entity.response.monthResponse.MonthResponse
import com.sa.nafhasehaprovider.interfaces.ClickItem


class MonthAdapter(
    var context: Activity, var listMonth: List<MonthResponse>, var clickItem: ClickItem
) : RecyclerView.Adapter<MonthAdapter.ViewHolder?>() {


    private var selectedItem: Int = 0

    inner class ViewHolder(binding: ItemMonthBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemMonthBinding = binding

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemMonthBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_month, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
        holder.itemView.startAnimation(animation)
        val model = listMonth[position]

        holder.binding.tvNumber.text = model.number.toString()
        holder.binding.tvText.text = model.text


        holder.itemView.setOnClickListener {
            selectedItem = holder.layoutPosition
            clickItem.Item(model.number!! , position , selectedItem)
            notifyDataSetChanged()

        }


        when (selectedItem) {
            0 -> {
                holder.binding.cvMonth.setBackgroundResource(
                    R.drawable.shape_text)
                holder.binding.tvText.setTextColor(context.resources.getColor(R.color.black))
                holder.binding.tvNumber.setTextColor(context.resources.getColor(R.color.black))



            }
            position -> {
                holder.binding.cvMonth.setBackgroundResource(
                    R.drawable.shape_bottom2)
                holder.binding.tvText.setTextColor(context.resources.getColor(R.color.white))
                holder.binding.tvNumber.setTextColor(context.resources.getColor(R.color.white))
            }
            else -> {
                holder.binding.cvMonth.setBackgroundResource(
                    R.drawable.shape_text)
                holder.binding.tvText.setTextColor(context.resources.getColor(R.color.black))
                holder.binding.tvNumber.setTextColor(context.resources.getColor(R.color.black))

            }
        }
    }

    override fun getItemCount(): Int {
        return listMonth.size
    }


}