package com.sa.nafhasehaprovider.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.databinding.ItemAnswerBinding
import com.sa.nafhasehaprovider.entity.response.fqResponse.Faq


class FaqsDescriptionAdapter(var context: Activity, var list: List<Faq>) :
    RecyclerView.Adapter<FaqsDescriptionAdapter.ViewHolder?>() {


    inner class ViewHolder(binding:ItemAnswerBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemAnswerBinding = binding


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemAnswerBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_answer, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var model = list[position]
        holder.binding.tvQuestions.text = model.title
        holder.binding.tvAnswer.setHtml(model.description)


    }

    override fun getItemCount(): Int {
        return list.size
    }


}