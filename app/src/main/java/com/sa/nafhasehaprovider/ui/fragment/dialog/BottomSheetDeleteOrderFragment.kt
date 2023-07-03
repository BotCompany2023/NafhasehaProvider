package com.sa.nafhasehaprovider.ui.fragment.dialog

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.base.BaseBottomDialog
import com.sa.nafhasehaprovider.databinding.FragmentBottomSheetAddOfferBinding
import com.sa.nafhasehaprovider.databinding.FragmentBottomSheetDeleteOrderBinding
import com.sa.nafhasehaprovider.databinding.FragmentBottomSheetRatingBinding


class BottomSheetDeleteOrderFragment : BaseBottomDialog<FragmentBottomSheetDeleteOrderBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_bottom_sheet_delete_order


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


}