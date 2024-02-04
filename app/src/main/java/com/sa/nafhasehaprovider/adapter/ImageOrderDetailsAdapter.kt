package com.sa.nafhasehaprovider.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.common.util.Utilities.Companion.onLoadImageFromUrl
import com.sa.nafhasehaprovider.databinding.ItemImagesBinding
import com.sa.nafhasehaprovider.entity.response.showOrderResponse.Images
import com.stfalcon.imageviewer.StfalconImageViewer
import com.stfalcon.imageviewer.loader.ImageLoader


class ImageOrderDetailsAdapter(var context: Activity, var listImage: List<Images>) :
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
            context, model.url, holder.binding.ivImage
        )


        holder.itemView.setOnClickListener {
            val allImage: MutableList<String> = ArrayList()

            for (i in 0 until listImage.size) {
                allImage.add(listImage[i].url)
            }

            val imageViewLoader: ImageLoader<String> = ImageLoader { imageView, image ->
                imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                Glide.with(context)
                    .load(image)
                    .transition(GenericTransitionOptions.with(R.anim.zoom_in))
                    .apply(RequestOptions.centerInsideTransform())
                    .thumbnail(Glide.with(context).load(R.raw.animation).centerInside())
                    .into(imageView)
            }

            StfalconImageViewer.Builder<String>(context, allImage, imageViewLoader)
                .withStartPosition(holder.absoluteAdapterPosition)
                .withBackgroundColorResource(R.color.blackTrans75)
                // .withBackgroundColorResource(R.color.color)
                // .withOverlayView(R.id.viewPager)
                // .withImagesMargin(R.dimen.album_dp_10)
                // .withImageMarginPixels(margin)
                .withContainerPadding(com.intuit.sdp.R.dimen._16sdp)
                // .withContainerPadding(R.dimen.paddingStart, R.dimen.paddingTop, R.dimen.paddingEnd, R.dimen.paddingBottom)
                // .withContainerPaddingPixels(padding)
                // .withContainerPaddingPixels(paddingStart, paddingTop, paddingEnd, paddingBottom)
                .withHiddenStatusBar(true)
                .allowZooming(true)
                .allowSwipeToDismiss(true)
                // .withTransitionFrom(mViewDataBinding.imageview)
                .withImageChangeListener { position1 -> }
                .withDismissListener { }
                .show(true)
        }


    }

    override fun getItemCount(): Int {
        return listImage.size
    }


}