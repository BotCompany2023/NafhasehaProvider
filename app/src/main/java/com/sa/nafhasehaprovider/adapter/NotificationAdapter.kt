package com.sa.nafhasehaprovider.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.databinding.ItemNotificationBinding
import com.sa.nafhasehaprovider.common.util.Utilities.Companion.onLoadImageFromUrl
import com.sa.nafhasehaprovider.entity.response.notificationResponse.NotificationResponseData


class NotificationAdapter(var context: Activity, var list: List<NotificationResponseData>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder?>() {


    inner class ViewHolder(binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemNotificationBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemNotificationBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_notification, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
        holder.itemView.startAnimation(animation)
        var model = list[position]

        if ( model.image !=null)
        {
            onLoadImageFromUrl(
                context, model.image!!, holder.binding.ivUser
            )
        }

        holder.binding.tvTitle!!.text = model.title
        holder.binding.tvTimeData!!.text = model.body


//        holder.itemView.setOnClickListener {
//            clickItem.Item(position)
//        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}