package com.sa.nafhasehaprovider.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.databinding.ItemQuestionsAndAnswerBinding
import com.sa.nafhasehaprovider.entity.response.fqResponse.FaqsResponseData


class FaqsTitleAdapter(var context: Activity, var list: List<FaqsResponseData>) :
    RecyclerView.Adapter<FaqsTitleAdapter.ViewHolder?>() {


    inner class ViewHolder(binding:ItemQuestionsAndAnswerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemQuestionsAndAnswerBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemQuestionsAndAnswerBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_questions_and_answer, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var model = list[position]
        holder.binding.tvTitleName.text = model.title

        var faqsDescriptionAdapter: FaqsDescriptionAdapter =
            FaqsDescriptionAdapter(context, model.faqs)
        holder.binding.rvAnswer.adapter = faqsDescriptionAdapter
        faqsDescriptionAdapter.notifyDataSetChanged()

//        holder.itemView.setOnClickListener {
//            clickItem.Item(position)
//        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}