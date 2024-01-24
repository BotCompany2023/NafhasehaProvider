package com.sa.nafhasehaprovider.ui.fragment.dialog

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.adapter.ImageLocaleAdapter
import com.sa.nafhasehaprovider.base.BaseBottomDialog
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.common.util.Utilities.Companion.convertFileToMultipartList
import com.sa.nafhasehaprovider.common.util.Utilities.Companion.convertToRequestBody
import com.sa.nafhasehaprovider.databinding.FragmentBottomSheetSubmitReportsBinding
import com.sa.nafhasehaprovider.interfaces.DateAndTimeResult
import com.sa.nafhasehaprovider.interfaces.DeleteImageLocale
import com.sa.nafhasehaprovider.ui.activity.MainActivity
import com.sa.nafhasehaprovider.viewModels.OrdersViewModel
import gun0912.tedimagepicker.builder.TedImagePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


class BottomSheetSubmitReportsFragment :
    BaseBottomDialog<FragmentBottomSheetSubmitReportsBinding>(), DateAndTimeResult,
    DeleteImageLocale {

    override fun getLayoutId(): Int = R.layout.fragment_bottom_sheet_submit_reports

    private var time: String = ""
    private var date: String = ""
    private lateinit var mainActivity: MainActivity
    private var idOrder: Int = 0

    private val viewModel: OrdersViewModel by viewModel()
    private lateinit var bottomSheetDatePickerFragment: BottomSheetDatePickerFragment
    private var bottomSheetTimePickerFragment: BottomSheetTimePickerFragment? = null

    lateinit var imageLocaleAdapter: ImageLocaleAdapter
    lateinit var listImageLocale: ArrayList<String>
    lateinit var imagesList: ArrayList<File>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable=false
        mainActivity = requireActivity() as MainActivity

        imagesList = ArrayList()


        if (arguments != null) {
            val args: BottomSheetSubmitReportsFragmentArgs =
                BottomSheetSubmitReportsFragmentArgs.fromBundle(requireArguments())
            viewModel.showOrder(args.orderId)
            idOrder = args.orderId
        }


        initResponse()
        onClick()

    }

    private fun initResponse() {
        //response submitPriceOffer
        viewModel.submitReportsOrderResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                Utilities.showToastSuccess(requireActivity(), it.message)
                                dismiss()
                                mainActivity.navController!!.navigate(R.id.menuOrders)

                            }
                            CODE403 -> {
                                //unAuthorized()
                                Utilities.showToastError(requireActivity(), it.message)
                                Utilities.logOutApp(requireActivity())

                            }
                            CODE405 -> {
                                Utilities.showToastError(requireActivity(), it.message)

                            }
                            CODE500 -> {
                                Utilities.showToastError(requireActivity(), it.message)
                            }
                            else -> {
                                showProgress(false)
                                Utilities.showToastError(requireActivity(), it.message)
                            }
                        }

                    }

                }
                is Resource.Error -> {
                    // dismiss loading
                    showProgress(false)
                    Log.i("TestVerification", "error")

                }
                is Resource.Loading -> {
                    // show loading
                    Log.i("TestVerification", "loading")
                    showProgress(true)

                }
            }
        })

    }


    private fun onClick() {

        bottomSheetDatePickerFragment = BottomSheetDatePickerFragment()
        bottomSheetTimePickerFragment = BottomSheetTimePickerFragment()
        bottomSheetDatePickerFragment!!.setClickListener(this) // Bind the listener
        bottomSheetTimePickerFragment!!.setClickListener(this) // Bind the listener


        mViewDataBinding.ivBack.setOnClickListener {
            dismiss()
        }

        mViewDataBinding.tvDate.setOnClickListener {
            bottomSheetDatePickerFragment!!.show(
                requireActivity().supportFragmentManager, bottomSheetDatePickerFragment!!.tag
            )

        }


        mViewDataBinding.tvTime.setOnClickListener {
            bottomSheetTimePickerFragment!!.show(
                requireActivity().supportFragmentManager, bottomSheetTimePickerFragment!!.tag
            )
        }


        mViewDataBinding.ivAddImages.setOnClickListener {
            openGallery()
        }


        mViewDataBinding.btnSendReports.setOnClickListener {

            val priceCost = mViewDataBinding.tvDetermineTheCost.text.toString().trim()
            val date = mViewDataBinding.tvDate.text.toString().trim()
            val time = mViewDataBinding.tvTime.text.toString().trim()
            val details = mViewDataBinding.tvDetails.text.toString().trim()

            if (priceCost.isEmpty()) {
                mViewDataBinding.tvDetermineTheCost.error =
                    getString(R.string.enter_the_maintenance_cost)
            } else if (date.isEmpty()) {
                mViewDataBinding.tvDate.error = getString(R.string.date_is_required)
            } else if (time.isEmpty()) {
                mViewDataBinding.tvTime.error = getString(R.string.time_is_required)
            } else if (details.isEmpty()) {
                mViewDataBinding.tvDetails.error = getString(R.string.description_required)
            } else {

                viewModel.submitReports(
                    convertToRequestBody(idOrder.toString()),
                    convertToRequestBody(date),
                    convertToRequestBody(time),
                    convertToRequestBody(details),
                    convertToRequestBody(priceCost),
                    convertFileToMultipartList(imagesList)
                )
            }

        }

    }

    private fun openGallery() {
        listImageLocale = ArrayList()

        Utilities.onPermission(requireActivity())
        TedImagePicker.with(requireActivity()).image()
            // .imageAndVideo()
            // .video()
            .cameraTileImage(R.drawable.ic_defult_camera).showTitle(true).title(R.string.select)
            .backButton(R.drawable.ic_back).buttonText(R.string.done)
            .buttonBackground(R.drawable.shape_bottom)
            .max(5, getString(R.string.you_cannot_add_more_than_5_photos)).drawerAlbum()
            // dropDownAlbum()
            .startMultiImage { uriList ->
                for (i in uriList.indices) {
//                    convertContentUriToFile(uriList[i])
                    listImageLocale.add(convertContentUriToFile(uriList[i]).toString())
                    imageLocaleAdapter =
                        ImageLocaleAdapter(requireActivity(), listImageLocale, this)
                    mViewDataBinding.rvImages.adapter = imageLocaleAdapter
                    imagesList.add(convertContentUriToFile(uriList[i])!!)
                    //convertFileToMultipartList(imagesList)
                }

            }


    }

    fun convertContentUriToFile(contentUri: Uri): File? {
        var inputStream: InputStream? = null
        var fileOutputStream: FileOutputStream? = null
        var file: File? = null
        try {
            val contentResolver = requireActivity().contentResolver
            // إذا لم يتم العثور على الملف باستخدام الطريقة الأولى، استخدم طريقة أخرى للحصول على الملف
            if (file == null) {
                val fileName = "${System.currentTimeMillis()}.jpg"
                val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                file = File(dir, fileName)
                fileOutputStream = FileOutputStream(file)
                inputStream = contentResolver.openInputStream(contentUri)
                val buffer = ByteArray(1024)
                var read = inputStream!!.read(buffer)
                while (read != -1) {
                    fileOutputStream.write(buffer, 0, read)
                    read = inputStream.read(buffer)
                }
                fileOutputStream.flush()

            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
            fileOutputStream?.close()
        }

        return file
    }


    override fun resultDate(dataresult: String) {
        mViewDataBinding.tvDate.text = dataresult
        date = dataresult
    }

    override fun resultTime(timeresult: String) {
        mViewDataBinding.tvTime.text = time
        time = timeresult
    }

    override fun delete(position: Int) {
        listImageLocale.removeAt(position)
        imageLocaleAdapter.notifyDataSetChanged()
    }


    override fun onDestroy() {
        super.onDestroy()
        mainActivity!!.mViewDataBinding.bottomNav.visibility = View.VISIBLE
        mainActivity!!.mViewDataBinding.toolbar.visibility = View.VISIBLE
    }

}