package com.sa.nafhasehaprovider.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.databinding.ItemTransactionsBinding
import com.sa.nafhasehaprovider.entity.response.walletResponse.DebitWalletResponse


class DebitWalletAdapter(
    var context: Activity, var listDebit: List<DebitWalletResponse>
) : RecyclerView.Adapter<DebitWalletAdapter.ViewHolder?>() {


    inner class ViewHolder(binding: ItemTransactionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemTransactionsBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemTransactionsBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_transactions, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
        holder.itemView.startAnimation(animation)
        var model = listDebit[position]

        holder.binding.imageView4.setImageResource(R.drawable.icons_debit)
        holder.binding.tvTitle.text = model.title
        holder.binding.tvTimeDate.text = model.date + " - " + model.time


//        holder.itemView.setOnClickListener {
//            clickItem.Item(position)
//        }
    }

    override fun getItemCount(): Int {
        return listDebit.size
    }


}