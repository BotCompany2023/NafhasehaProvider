package com.sa.nafhasehaprovider.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.databinding.ItemServiceSelectedBinding
import com.sa.nafhasehaprovider.entity.response.authenticationResponse.Category
import com.sa.nafhasehaprovider.entity.response.categoriesResponse.CategoryCategoriesResponse
import com.sa.nafhasehaprovider.interfaces.CheckCategory

class CategoriesAdapter(
    var context: Activity,
    public var listCategory: List<CategoryCategoriesResponse>,
    var checkedItems: List<Category>,
    var constraintLayout:ConstraintLayout
    ,var checkCategory: CheckCategory
) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder?>() {


    var idCategory: ArrayList<Int> = arrayListOf()

    inner class ViewHolder(binding: ItemServiceSelectedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemServiceSelectedBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemServiceSelectedBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_service_selected, parent, false
        )
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
        //  holder.itemView.startAnimation(animation)


        var model = listCategory[position]
        holder.binding.cbSelectService.text = model.title


        checkedItems.forEach {
            if (it.id == model.id) {
                holder.binding.cbSelectService.isChecked = true
                model.isSelected=true
                if (listCategory[position].id==8){
                    checkCategory.ItemCheck(true)
                }
            }
        }



        holder.binding.cbSelectService.setOnCheckedChangeListener { buttonView, isChecked ->
            // تم تحديد أو إلغاء تحديد الصندوق
            if (isChecked) {
                // القيام بإجراء عند تحديد الصندوق
                idCategory.add(model.id)
                model.isSelected=true
                if (listCategory[position].id==8){
                    checkCategory.ItemCheck(true)
                }

            } else {
                // القيام بإجراء عند إلغاء تحديد الصندوق
                idCategory.remove(model.id)
                model.isSelected=false
                if (listCategory[position].id==8){
                    checkCategory.ItemCheck(false)
                }
            }
            }
        }

    override fun getItemCount(): Int {
        return listCategory.size
    }

    override fun getItemViewType(position: Int): Int {
        return position

    }


}