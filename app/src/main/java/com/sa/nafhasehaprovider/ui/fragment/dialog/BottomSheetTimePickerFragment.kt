package com.sa.nafhasehaprovider.ui.fragment.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.base.BaseBottomDialog
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.databinding.FragmentBottomSheetTimePickerBinding
import com.sa.nafhasehaprovider.interfaces.DateAndTimeResult
import java.text.SimpleDateFormat
import java.util.*


class BottomSheetTimePickerFragment : BaseBottomDialog<FragmentBottomSheetTimePickerBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_bottom_sheet_time_picker
    private lateinit var msg: String
    lateinit var timeListener: DateAndTimeResult
    private var resultTime: String = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        onClick()
    }

    fun setClickListener(dateListener: DateAndTimeResult) {
        this.timeListener = dateListener
    }

    private fun onClick() {
        mViewDataBinding.btnClose.setOnClickListener {
            dismiss()
        }

        mViewDataBinding.btnConfirm.setOnClickListener {

            if (mViewDataBinding.tvResultTime.text.toString().isEmpty()) {
                Utilities.showToastError(
                    requireActivity(), getString(R.string.please_select_time)
                )
            } else {
                timeListener.resultTime(mViewDataBinding.tvResultTime.text.toString())
                timeListener.resultTime(resultTime)
                dismiss()
            }


        }
    }


    private fun init() {

        val currentTimeMillis = System.currentTimeMillis()
        val date = Date(currentTimeMillis)
        val timeFormat = SimpleDateFormat("hh:mm", Locale.ENGLISH)
        val formattedTime = timeFormat.format(date)

        mViewDataBinding.tvResultTime.text = formattedTime
        Log.d("Current time", formattedTime)


        mViewDataBinding.timePicker.setOnTimeChangedListener { _, hour, minute ->
            var hour = hour
            var am_pm = ""
            // AM_PM decider logic
            when {
                hour == 0 -> {
                    hour += 12
                    am_pm = "AM"
                }
                hour == 12 -> am_pm = "PM"
                hour > 12 -> {
                    hour -= 12
                    am_pm = "PM"
                }
                else -> am_pm = "AM"
            }
            if (mViewDataBinding.tvResultTime.text != null) {
                val hour = if (hour < 10) "0" + hour else hour
                val min = if (minute < 10) "0" + minute else minute
                // display format of time
                msg = "$hour : $min "
                mViewDataBinding.tvResultTime.text = msg
            }
            resultTime = msg

        }
    }


}