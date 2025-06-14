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
import com.sa.nafhasehaprovider.databinding.ItemNotificationBinding
import com.sa.nafhasehaprovider.common.util.Utilities.Companion.onLoadImageFromUrl
import com.sa.nafhasehaprovider.databinding.ItemImagesBinding
import com.sa.nafhasehaprovider.databinding.ItemPositionBinding
import com.sa.nafhasehaprovider.entity.response.notificationResponse.NotificationResponseData


class PositionOrderDetailsAdapter(var context: Activity, var listPosition: List<String>) :
    RecyclerView.Adapter<PositionOrderDetailsAdapter.ViewHolder?>() {


    inner class ViewHolder(binding: ItemPositionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemPositionBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemPositionBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_position, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
        holder.itemView.startAnimation(animation)
        var model = listPosition[position]

        holder.binding.ivChickItem.setImageResource(R.drawable.icons_chick)
        if (model=="Right")
        {
            holder.binding.tvTitle.text=context.getString(R.string.right)
        }
        else  if (model=="Left")
        {
            holder.binding.tvTitle.text=context.getString(R.string.left)
        }
        else  if (model=="Front")
        {
            holder.binding.tvTitle.text=context.getString(R.string.front)
        }
        else  if (model=="Behind")
        {
            holder.binding.tvTitle.text=context.getString(R.string.behind)
        }

//        holder.itemView.setOnClickListener {
//            clickItem.Item(position)
//        }
    }

    override fun getItemCount(): Int {
        return listPosition.size
    }


}