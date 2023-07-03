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
import com.sa.nafhasehaprovider.databinding.ItemImagesBinding
import com.sa.nafhasehaprovider.entity.response.notificationResponse.NotificationResponseData


class ImageOrderDetailsAdapter(var context: Activity, var listImage: List<String>) :
    RecyclerView.Adapter<ImageOrderDetailsAdapter.ViewHolder?>() {


    inner class ViewHolder(binding: ItemImagesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemImagesBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemImagesBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_images, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
        holder.itemView.startAnimation(animation)
        var model = listImage[position]
        onLoadImageFromUrl(
            context, model, holder.binding.ivImage
        )


//        holder.itemView.setOnClickListener {
//            clickItem.Item(position)
//        }
    }

    override fun getItemCount(): Int {
        return listImage.size
    }


}