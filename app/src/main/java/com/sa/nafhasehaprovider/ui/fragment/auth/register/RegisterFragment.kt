package com.sa.nafhasehaprovider.ui.fragment.auth.register

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.adapter.CategoriesAdapter
import com.sa.nafhasehaprovider.adapter.DropDownAreasAdapter
import com.sa.nafhasehaprovider.adapter.DropDownCityAdapter
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.base.BaseFragment
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.common.util.Utilities.Companion.convertFileToMultipart
import com.sa.nafhasehaprovider.common.util.Utilities.Companion.convertToRequestBody
import com.sa.nafhasehaprovider.databinding.FragmentRegisterBinding
import com.sa.nafhasehaprovider.entity.response.areasResponse.AreasResponseData
import com.sa.nafhasehaprovider.entity.response.categoriesResponse.DataCategoriesResponse
import com.sa.nafhasehaprovider.entity.response.cityResponse.CityResponseData
import com.sa.nafhasehaprovider.interfaces.CheckCategory
import com.sa.nafhasehaprovider.ui.activity.MapsActivity
import com.sa.nafhasehaprovider.ui.generalViewModel.AreasViewModel
import com.sa.nafhasehaprovider.ui.generalViewModel.AuthenticationViewModel
import com.sa.nafhasehaprovider.ui.generalViewModel.CityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_register
    private lateinit var typeAccount: String
    private var IDcity: Int = 0
    private val viewModel: AuthenticationViewModel by viewModel()


    private val viewModelCity: CityViewModel by viewModel()
    private val viewModelAreas: AreasViewModel by viewModel()
    lateinit var cityDataSource: ArrayList<CityResponseData>
    lateinit var dropDownCityAdapter: DropDownCityAdapter
    lateinit var dropDownAreasAdapter: DropDownAreasAdapter
    lateinit var areasDataSource: ArrayList<AreasResponseData>

    lateinit var categoriesAdapter: CategoriesAdapter
    lateinit var listCategory: ArrayList<DataCategoriesResponse>


    private var imageFile: File? = null
    var addressStr: String =""
    var homeService: Int = 0
    lateinit var city: String
    var lat = 0.0
    var long: Double = 0.0
    var request_code = 22
    var request_path = 23
    var mProfileUri: Uri? = null
    private val GALLERY_IMAGE_REQ_CODE = 102
    private val CAMERA_IMAGE_REQ_CODE = 103
    private lateinit var dialogImage: Dialog

    private val REQUEST_CODE_CHOOSE = 100
    private var imagePath = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
        initResponse()

        mViewDataBinding.cbIAgreeToThePrivacyPolicy.paintFlags =
            mViewDataBinding.cbIAgreeToThePrivacyPolicy.paintFlags or Paint.UNDERLINE_TEXT_FLAG

    }


    private fun initResponse() {
        cityDataSource = ArrayList()
        areasDataSource = ArrayList()
        listCategory = ArrayList()

        //apiResponse city
        viewModelCity.city(1, "ALL")
        viewModelCity.cityResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                cityDataSource.add(
                                    CityResponseData(
                                        null, 0, getString(R.string.select)
                                    )
                                )
                                cityDataSource.addAll(it.data)
                                dropDownCityAdapter =
                                    DropDownCityAdapter(requireActivity(), cityDataSource)
                                mViewDataBinding.spCity.adapter = dropDownCityAdapter

                            }
                            CODE403 -> {
                                //unAuthorized()
                                NafhasehaProviderApp.pref.clearSharedPref()
                            }
                            CODE405 -> {
                                Utilities.showToastError(requireActivity(), it.message)

                            }
                            CODE500 -> {
                                Utilities.showToastError(requireActivity(), it.message)
                            }
                            else -> {

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

        //apiResponse areas
        viewModelAreas.areasResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    areasDataSource.clear()
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                areasDataSource.add(
                                    AreasResponseData(
                                        null, 0, getString(R.string.select)
                                    )
                                )
                                areasDataSource.addAll(it.data)
                                dropDownAreasAdapter =
                                    DropDownAreasAdapter(requireActivity(), areasDataSource)
                                mViewDataBinding.spAreas.adapter = dropDownAreasAdapter

                            }
                            CODE403 -> {
                                //unAuthorized()
                                NafhasehaProviderApp.pref.clearSharedPref()
                            }
                            CODE405 -> {
                                Utilities.showToastError(requireActivity(), it.message)

                            }
                            CODE500 -> {
                                Utilities.showToastError(requireActivity(), it.message)
                            }
                            else -> {

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


        //apiResponse categories
        viewModel.categories(1)
        viewModel.categoriesResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                listCategory.addAll(it.data!!)
                                categoriesAdapter =
                                    CategoriesAdapter(requireActivity(), listCategory,
                                        listOf() )
                                mViewDataBinding.rvAllService.adapter = categoriesAdapter

                            }
                            CODE403 -> {
                                //unAuthorized()
                                NafhasehaProviderApp.pref.clearSharedPref()
                            }
                            CODE405 -> {
                                Utilities.showToastError(requireActivity(), it.message)

                            }
                            CODE500 -> {
                                Utilities.showToastError(requireActivity(), it.message)
                            }
                            else -> {

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


        //apiResponse register
        viewModel.authResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {

                                Utilities.showToastSuccess(requireActivity(), it.message)
                                // saving token
                                //NafhasehaApp.pref.saveUserData(requireActivity(),USER_DATA,it)

                                //  NafhasehaProviderApp.pref.authToken =it.data!!.access_token

                                val action =
                                    RegisterFragmentDirections.actionRegisterFragmentToVerifyOtpFragment(
                                        mViewDataBinding.tvMobile.text.toString(),
                                        it.data!!.provider!!.id!!,
                                        "REGISTER"
                                    )
                                mViewDataBinding.root.findNavController().navigate(action)
                            }
                            CODE403 -> {
                                //unAuthorized()
                                NafhasehaProviderApp.pref.clearSharedPref()
                            }
                            CODE405 -> {
                                Utilities.showToastError(requireActivity(), it.message)

                            }
                            CODE500 -> {
                                Utilities.showToastError(requireActivity(), it.message)
                            }
                            else -> {

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

        mViewDataBinding.spCity.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    if (position != 0) {
                        IDcity = cityDataSource[position].id
                        viewModelAreas.areas(IDcity!!, "ALL")
                    } else {

                    }
                }

            }


        mViewDataBinding.tvAddress.setOnClickListener {
            Utilities.onPermission(requireActivity())
            startActivityForResult(
                Intent(requireActivity(), MapsActivity::class.java), request_code
            )
        }


        mViewDataBinding.ivImage.setOnClickListener {
            Utilities.onPermission(requireActivity())
            showDialogImage()
        }



        mViewDataBinding.radioGroupTypeAccount.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_center -> {
                    typeAccount = "ProviderCenter"
                }
                R.id.radio_person -> {
                    typeAccount = "Provider"
                }
            }
        }

        mViewDataBinding.cbDoYouHaveHomeService.setOnCheckedChangeListener { buttonView, isChecked ->
            // تم تحديد أو إلغاء تحديد الصندوق
            if (isChecked) {
                // القيام بإجراء عند تحديد الصندوق
                homeService = 1
            } else {
                // القيام بإجراء عند إلغاء تحديد الصندوق
                homeService = 0
            }
        }


        mViewDataBinding.btnCreateAnAccount.setOnClickListener {
            val nameProvider = mViewDataBinding.tvName.text.toString().trim()
            val phone = mViewDataBinding.tvMobile.text.toString().trim()
            val email = mViewDataBinding.tvEmail.text.toString().trim()
            val password = mViewDataBinding.tvPassword.text.toString().trim()
            val address = mViewDataBinding.tvAddress.text.toString().trim()

            if (nameProvider.isEmpty()) {
                mViewDataBinding.tvName.error = getString(R.string.mobile_number_is_required)
            } else if (phone.isEmpty()) {
                mViewDataBinding.tvName.error = getString(R.string.mobile_number_is_required)
            } else if (password.isEmpty()) {
                mViewDataBinding.tvPassword.error = getString(R.string.password_is_required)
            } else if (mViewDataBinding.tvPassword.length() < 4) {
                mViewDataBinding.tvPassword.error =
                    getString(R.string.the_password_is_not_less_than_four_letters_numbers_or_letters_and_numbers)
            } else if (mViewDataBinding.spCity.selectedItemPosition == 0) {
                Utilities.showToastError(
                    requireActivity(), getString(R.string.please_selected_city)
                )

            } else if (mViewDataBinding.spAreas.selectedItemPosition == 0) {
                Utilities.showToastError(
                    requireActivity(), getString(R.string.please_selected_aras)
                )

            } else if (address.isEmpty()) {
                mViewDataBinding.tvAddress.error = getString(R.string.address_is_required)
            } else if (typeAccount == "") {
                Utilities.showToastError(
                    requireActivity(), getString(R.string.please_select_an_account_type)
                )
            } else if (categoriesAdapter.idCategory.size == 0) {
                Utilities.showToastError(
                    requireActivity(), getString(R.string.choose_your_services)
                )
            } else if (imagePath == "") {
                Utilities.showToastError(requireActivity(), getString(R.string.attach_the_photo))
            } else if (!mViewDataBinding.cbIAgreeToThePrivacyPolicy.isChecked) {
                Utilities.showToastError(
                    requireActivity(),
                    getString(R.string.you_do_not_agree_to_the_terms_and_conditions)
                )
            } else {
                viewModel.register(
                    convertToRequestBody(typeAccount),
                    convertToRequestBody(nameProvider),
                    convertToRequestBody(1.toString()),
                    convertToRequestBody(phone),
                    convertToRequestBody(email),
                    convertToRequestBody(password),
                    convertToRequestBody(addressStr),
                    convertToRequestBody(lat.toString()),
                    convertToRequestBody(long.toString()),
                    convertToRequestBody(mViewDataBinding.spCity.selectedItemPosition.toString()),
                    convertToRequestBody(mViewDataBinding.spAreas.selectedItemPosition.toString()),
                    convertFileToMultipart(imageFile!!, "commercial_register"),
                    convertToRequestBody(homeService.toString()),
                    categoriesAdapter.idCategory
                )
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                // Uri object will not be null for RESULT_OK
                val uri = data!!.data
                when (requestCode) {
                    GALLERY_IMAGE_REQ_CODE -> {
                        mProfileUri = uri!!
                        imageFile = File(mProfileUri!!.path)
                        imagePath = mProfileUri!!.path.toString()

                        Glide.with(requireActivity()).load(imagePath).into(mViewDataBinding.ivImage)
                    }
                    CAMERA_IMAGE_REQ_CODE -> {
                        mProfileUri = uri!!
                        imageFile = File(mProfileUri!!.path)
                        imagePath = mProfileUri!!.path.toString()

                        Glide.with(requireActivity()).load(imagePath).into(mViewDataBinding.ivImage)

                    }
                    request_code -> {
                        addressStr = data!!.getStringExtra("ADDRESS")!!
                        city = data.getStringExtra("CITY")!!
                        lat = data.getDoubleExtra("LAT", 0.0)
                        long = data.getDoubleExtra("LON", 0.0)
                        mViewDataBinding.tvAddress.text = addressStr
                    }
                }
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    }


    fun showDialogImage() {
        dialogImage = Dialog(requireActivity(), R.style.customDialogTheme)
        dialogImage.setCancelable(false)
        val inflater = requireActivity().layoutInflater
        val v: View = inflater.inflate(R.layout.dialog_image, null)
        dialogImage.setContentView(v)
        dialogImage.setCancelable(false)

        var cvCamera: ConstraintLayout = v.findViewById(R.id.constraint_camera)
        var cvGallery: ConstraintLayout = v.findViewById(R.id.constraint_gallery)
        var ivClose: ImageView = v.findViewById(R.id.iv_close_dialog)



        ivClose.setOnClickListener {
            dialogImage.dismiss()
        }

        cvCamera.setOnClickListener {

            ImagePicker.with(this) // Crop Image(User can choose Aspect Ratio)
                .crop() // User can only select image from Gallery
                .cameraOnly()
                // Image resolution will be less than 1080 x 1920
                .maxResultSize(500, 500) // .saveDir(getExternalFilesDir(null))
                .start(CAMERA_IMAGE_REQ_CODE)

            dialogImage.dismiss()
        }

        cvGallery.setOnClickListener {

            ImagePicker.with(this) // Crop Image(User can choose Aspect Ratio)
                .crop() // User can only select image from Gallery
                .galleryOnly().galleryMimeTypes(
                    arrayOf<String>(
                        "image/png", "image/jpg", "image/jpeg"
                    )
                ) // Image resolution will be less than 1080 x 1920
                .maxResultSize(500, 500) // .saveDir(getExternalFilesDir(null))
                .start(GALLERY_IMAGE_REQ_CODE)
            dialogImage.dismiss()
        }


        val window = dialogImage.window
        window!!.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        try {
            dialogImage.show()
        } catch (e: Exception) {
        }
    }


}