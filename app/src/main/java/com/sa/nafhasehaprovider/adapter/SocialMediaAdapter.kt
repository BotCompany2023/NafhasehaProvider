package com.sa.nafhasehaprovider.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.common.openLink
import com.sa.nafhasehaprovider.databinding.ItemSocialMediaBinding
import com.sa.nafhasehaprovider.common.util.Utilities.Companion.onLoadImageFromUrl
import com.sa.nafhasehaprovider.entity.response.iconSocialMediaResponse.DataIconSocialMediaResponse


class SocialMediaAdapter(
    var context: Activity, var listIcons: List<DataIconSocialMediaResponse>
) : RecyclerView.Adapter<SocialMediaAdapter.ViewHolder?>() {

    inner class ViewHolder(binding:ItemSocialMediaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemSocialMediaBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemSocialMediaBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_social_media, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
        holder.itemView.startAnimation(animation)
        var model = listIcons[position]
        onLoadImageFromUrl(
            context, model.image, holder.binding.ivIcon
        )

        holder.itemView.setOnClickListener {
            openLink(context, model.link)
        }
    }

    override fun getItemCount(): Int {
        return listIcons.size
    }


}