package com.sa.nafhasehaprovider.ui.fragment.dialog

import android.os.Bundle
import android.view.View
import android.widget.CalendarView.OnDateChangeListener
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.base.BaseBottomDialog
import com.sa.nafhasehaprovider.databinding.FragmentBottomSheetDatePickerBinding
import com.sa.nafhasehaprovider.interfaces.DateAndTimeResult
import java.text.SimpleDateFormat
import java.util.*


class BottomSheetDatePickerFragment : BaseBottomDialog<FragmentBottomSheetDatePickerBinding>() {

    private var resultDate: String = ""
    lateinit var dataListener: DateAndTimeResult

    override fun getLayoutId(): Int = R.layout.fragment_bottom_sheet_date_picker


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        onClick()
    }

    fun setClickListener(dateListener: DateAndTimeResult) {
        this.dataListener = dateListener
    }


    private fun onClick() {
        mViewDataBinding.btnClose.setOnClickListener {
            dismiss()
        }


        mViewDataBinding.btnConfirm.setOnClickListener {

            dataListener.resultDate(mViewDataBinding.tvResultDate.text.toString())
            dismiss()


        }
    }


    private fun init() {


        val d = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
        val formattedDate = df.format(d.time)
        // formattedDate have current date/time
        mViewDataBinding.tvResultDate.text = formattedDate
        mViewDataBinding.datePicker.setMinDate(System.currentTimeMillis() - 1000)
        mViewDataBinding.datePicker.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth ->
            mViewDataBinding.tvResultDate.text =
                year.toString() + "/" + (month + 1) + "/" + dayOfMonth
            resultDate = year.toString() + "/" + (month + 1) + "/" + dayOfMonth

        })


    }


}