package com.sa.nafhasehaprovider.ui.fragment.auth.register

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.adapter.CategoriesAdapter
import com.sa.nafhasehaprovider.adapter.DropDownAreasAdapter
import com.sa.nafhasehaprovider.adapter.DropDownCityAdapter
import com.sa.nafhasehaprovider.adapter.VehicleTransporterAdapter
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.base.BaseFragment
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.common.util.Utilities.Companion.convertFileToMultipart
import com.sa.nafhasehaprovider.common.util.Utilities.Companion.convertToRequestBody
import com.sa.nafhasehaprovider.databinding.FragmentRegisterBinding
import com.sa.nafhasehaprovider.entity.response.areasResponse.AreasResponseData
import com.sa.nafhasehaprovider.entity.response.categoriesResponse.CategoryCategoriesResponse
import com.sa.nafhasehaprovider.entity.response.cityResponse.CityResponseData
import com.sa.nafhasehaprovider.entity.response.vehicleTransporterResponse.DataVehicleTransporterResponse
import com.sa.nafhasehaprovider.interfaces.CheckCategory
import com.sa.nafhasehaprovider.interfaces.ClickItemFilterService
import com.sa.nafhasehaprovider.ui.activity.MainActivity
import com.sa.nafhasehaprovider.ui.activity.MapsActivity
import com.sa.nafhasehaprovider.ui.generalViewModel.AreasViewModel
import com.sa.nafhasehaprovider.ui.generalViewModel.AuthenticationViewModel
import com.sa.nafhasehaprovider.ui.generalViewModel.CityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class RegisterFragment : BaseFragment<FragmentRegisterBinding>() ,
    ClickItemFilterService,
    CheckCategory {

    override fun getLayoutId(): Int = R.layout.fragment_register
    private var vehicleTransporterID: Int? =0
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
    lateinit var listCategory: ArrayList<CategoryCategoriesResponse>


    private var imageFile: File? = null
    var addressStr: String =""
    var homeService: Int = 0
    lateinit var city: String
    var lat = 0.0
    var long: Double = 0.0
    var request_code = 22
    var mProfileUri: Uri? = null
    private val GALLERY_IMAGE_COMMERCAL_REGISTER_REQ_CODE = 102
    private val CAMERA_IMAGE_COMMERCAL_REGISTER_REQ_CODE = 103

    private val GALLERY_IMAGE_NATIONAL_IDENTITY_REQ_CODE = 104
    private val CAMERA_IMAGE_NATIONAL_IDENTITY_REQ_CODE = 105

    private val GALLERY_IMAGE_DRIVING_LICENSE_REQ_CODE = 106
    private val CAMERA_IMAGE_DRIVING_LICENSE_REQ_CODE = 107

    private val GALLERY_IMAGE_GENERAL_LICENSE_REQ_CODE = 108
    private val CAMERA_IMAGE_GENERAL_LICENSE_REQ_CODE = 109

    private val GALLERY_IMAGE_MUNICIPALITY_LICENSE_REQ_CODE = 110
    private val CAMERA_IMAGE_MUNICIPALITY_LICENSE_REQ_CODE = 111



    private lateinit var dialogImage: Dialog

    private var imagePath = ""

    lateinit var vehicleTransporterAdapter: VehicleTransporterAdapter
    private lateinit var listVehicleTransporter: ArrayList<DataVehicleTransporterResponse>



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()

        mViewDataBinding.cbIAgreeToThePrivacyPolicy.paintFlags =
            mViewDataBinding.cbIAgreeToThePrivacyPolicy.paintFlags or Paint.UNDERLINE_TEXT_FLAG

    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        // يتم استدعاء هذه الدالة عندما يتغير حالة الاتصال
        if (isConnected) {
            // يمكنك إجراء أي إجراءات إضافية هنا عند الاتصال بالإنترنت
            initResponse()
            Utilities.dismissDialogNoInternet()
        }
        else{
            Utilities.showDialogNoInternet(requireActivity())
        }

    }


    private fun initResponse() {

        listCategory = ArrayList()
        categoriesAdapter =
            CategoriesAdapter(requireActivity(), listCategory,
                listOf(),mViewDataBinding.constraintVehicleTransporter,this)



        cityDataSource = ArrayList()
        areasDataSource = ArrayList()
        listVehicleTransporter = ArrayList()


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
                                showProgress(false)
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
                                showProgress(false)
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
                                listCategory.addAll(it.data!!.categories!!)
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
                                showProgress(false)
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


        // resend response Vehicle Transporter
        viewModel.getVehicleTransporterResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                //Utilities.showToastSuccess(requireActivity(), it.message)
                                listVehicleTransporter.addAll(it.data!!)
                                vehicleTransporterAdapter = VehicleTransporterAdapter(requireActivity(), listVehicleTransporter, this,vehicleTransporterID!!)
                                mViewDataBinding.rvAllTransportVehicle.adapter = vehicleTransporterAdapter
                                vehicleTransporterAdapter.notifyDataSetChanged()

                            }
                            CODE403 -> {
                                //unAuthorized()
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
                                NafhasehaProviderApp.pref.authToken =it.data!!.access_token


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
                                showProgress(false)
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


        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                    mViewDataBinding.root.findNavController().navigate(action)

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)


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




        mViewDataBinding.ivCommercialRegister.setOnClickListener {
            Utilities.onPermission(requireActivity())
            showDialogImage(  GALLERY_IMAGE_COMMERCAL_REGISTER_REQ_CODE ,CAMERA_IMAGE_COMMERCAL_REGISTER_REQ_CODE )
        }

        mViewDataBinding.ivImageNationalIdentity.setOnClickListener {
            Utilities.onPermission(requireActivity())
            showDialogImage(  GALLERY_IMAGE_NATIONAL_IDENTITY_REQ_CODE ,CAMERA_IMAGE_NATIONAL_IDENTITY_REQ_CODE )
        }


        mViewDataBinding.ivImageDrivingLicense.setOnClickListener {
            Utilities.onPermission(requireActivity())
            showDialogImage(  GALLERY_IMAGE_DRIVING_LICENSE_REQ_CODE ,CAMERA_IMAGE_DRIVING_LICENSE_REQ_CODE )
        }

        mViewDataBinding.ivImageGeneralLicense.setOnClickListener {
            Utilities.onPermission(requireActivity())
            showDialogImage(GALLERY_IMAGE_GENERAL_LICENSE_REQ_CODE,CAMERA_IMAGE_GENERAL_LICENSE_REQ_CODE)
        }

        mViewDataBinding.ivImageMunicipalityLicense.setOnClickListener{
            Utilities.onPermission(requireActivity())
            showDialogImage(GALLERY_IMAGE_MUNICIPALITY_LICENSE_REQ_CODE,CAMERA_IMAGE_MUNICIPALITY_LICENSE_REQ_CODE)
        }


        mViewDataBinding.radioGroupTypeAccount.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_center -> {
                    typeAccount = "ProviderCenter"
                    mViewDataBinding.layoutCommercialRegister.visibility=View.VISIBLE
                    mViewDataBinding.layoutNationalIdentity.visibility=View.VISIBLE
                    mViewDataBinding.layoutDrivingLicense.visibility=View.GONE
                    mViewDataBinding.layoutMunicipalityLicense.visibility=View.VISIBLE
                }
                R.id.radio_person -> {
                    typeAccount = "Provider"
                    mViewDataBinding.layoutCommercialRegister.visibility=View.GONE
                    mViewDataBinding.layoutNationalIdentity.visibility=View.VISIBLE
                    mViewDataBinding.layoutDrivingLicense.visibility=View.VISIBLE
                    mViewDataBinding.layoutMunicipalityLicense.visibility=View.GONE

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

        // استخدام TextWatcher لمراقبة التغييرات في نص المدخل
        mViewDataBinding.tvMobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // لاحاجة لتنفيذ أي شيء هنا
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // لاحاجة لتنفيذ أي شيء هنا
            }

            override fun afterTextChanged(s: Editable?) {
                // تحقق إذا كان أول حرف هو 5 وإذا لم يكن قم بحذفه
                if (!s.isNullOrEmpty() && s[0] != '5') {
                    s.delete(0, 1) // حذف الحرف الأول
                    // عرض رسالة تنبيه للمستخدم
                    Toast.makeText(requireActivity(), getString(R.string.the_mobile_number_must_start_with_the_number_5), Toast.LENGTH_SHORT).show()
                }
            }
        })




        mViewDataBinding.cbIAgreeToThePrivacyPolicy.setOnClickListener {
        val intent=Intent(requireActivity(),MainActivity::class.java)
            intent.putExtra("type","PrivacyPolicy")
            requireActivity().startActivity(intent)
        }


        mViewDataBinding.btnCreateAnAccount.setOnClickListener {
            val nameProvider = mViewDataBinding.tvName.text.toString().trim()
            val phone = mViewDataBinding.tvMobile.text.toString().trim()
            val email = mViewDataBinding.tvEmail.text.toString().trim()
            val password = mViewDataBinding.tvPassword.text.toString().trim()
            val address = mViewDataBinding.tvAddress.text.toString().trim()

            if (nameProvider.isEmpty()) {
                mViewDataBinding.tvName.error = getString(R.string.name_is_required )
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
            } else if (categoriesAdapter.listCategory.size == 0) {
                Utilities.showToastError(
                    requireActivity(), getString(R.string.choose_your_services)
                )
            } else if (imagePath == "") {
                Utilities.showToastError(requireActivity(), getString(R.string.attach_the_photo))
            }
            else if (!mViewDataBinding.cbIAgreeToThePrivacyPolicy.isChecked) {
                Utilities.showToastError(
                    requireActivity(),
                    getString(R.string.you_do_not_agree_to_the_terms_and_conditions))
            }
            else {

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
                    convertFileToMultipart(imageFile!!, "general_license"),
                    convertFileToMultipart(imageFile!!, "municipal_license"),
                    convertFileToMultipart(imageFile!!, "personal_licence"),
                    convertFileToMultipart(imageFile!!, "national_identity"),
                    convertToRequestBody(homeService.toString()),
                    convertToRequestBody(vehicleTransporterID!!.toString()),
                    categoriesAdapter.idCategory)
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
                    GALLERY_IMAGE_COMMERCAL_REGISTER_REQ_CODE -> {
                        mProfileUri = uri!!
                        imageFile = File(mProfileUri!!.path)
                        imagePath = mProfileUri!!.path.toString()

                        Glide.with(requireActivity()).load(imagePath).into(mViewDataBinding.ivCommercialRegister)
                    }
                    CAMERA_IMAGE_COMMERCAL_REGISTER_REQ_CODE -> {
                        mProfileUri = uri!!
                        imageFile = File(mProfileUri!!.path)
                        imagePath = mProfileUri!!.path.toString()

                        Glide.with(requireActivity()).load(imagePath).into(mViewDataBinding.ivCommercialRegister)

                    }
                    GALLERY_IMAGE_NATIONAL_IDENTITY_REQ_CODE -> {
                        mProfileUri = uri!!
                        imageFile = File(mProfileUri!!.path)
                        imagePath = mProfileUri!!.path.toString()

                        Glide.with(requireActivity()).load(imagePath).into(mViewDataBinding.ivImageNationalIdentity)
                    }
                    CAMERA_IMAGE_NATIONAL_IDENTITY_REQ_CODE -> {
                        mProfileUri = uri!!
                        imageFile = File(mProfileUri!!.path)
                        imagePath = mProfileUri!!.path.toString()

                        Glide.with(requireActivity()).load(imagePath).into(mViewDataBinding.ivImageNationalIdentity)

                    }

                    GALLERY_IMAGE_DRIVING_LICENSE_REQ_CODE -> {
                        mProfileUri = uri!!
                        imageFile = File(mProfileUri!!.path)
                        imagePath = mProfileUri!!.path.toString()

                        Glide.with(requireActivity()).load(imagePath).into(mViewDataBinding.ivImageDrivingLicense)
                    }
                    CAMERA_IMAGE_DRIVING_LICENSE_REQ_CODE -> {
                        mProfileUri = uri!!
                        imageFile = File(mProfileUri!!.path)
                        imagePath = mProfileUri!!.path.toString()

                        Glide.with(requireActivity()).load(imagePath).into(mViewDataBinding.ivImageDrivingLicense)

                    }
                    GALLERY_IMAGE_GENERAL_LICENSE_REQ_CODE -> {
                        mProfileUri = uri!!
                        imageFile = File(mProfileUri!!.path)
                        imagePath = mProfileUri!!.path.toString()

                        Glide.with(requireActivity()).load(imagePath).into(mViewDataBinding.ivImageGeneralLicense)
                    }
                    CAMERA_IMAGE_GENERAL_LICENSE_REQ_CODE -> {
                        mProfileUri = uri!!
                        imageFile = File(mProfileUri!!.path)
                        imagePath = mProfileUri!!.path.toString()

                        Glide.with(requireActivity()).load(imagePath).into(mViewDataBinding.ivImageGeneralLicense)

                    }

                    GALLERY_IMAGE_MUNICIPALITY_LICENSE_REQ_CODE -> {
                        mProfileUri = uri!!
                        imageFile = File(mProfileUri!!.path)
                        imagePath = mProfileUri!!.path.toString()

                        Glide.with(requireActivity()).load(imagePath).into(mViewDataBinding.ivImageMunicipalityLicense)
                    }
                    CAMERA_IMAGE_MUNICIPALITY_LICENSE_REQ_CODE -> {
                        mProfileUri = uri!!
                        imageFile = File(mProfileUri!!.path)
                        imagePath = mProfileUri!!.path.toString()

                        Glide.with(requireActivity()).load(imagePath).into(mViewDataBinding.ivImageMunicipalityLicense)

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
                //Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    }


    fun showDialogImage(CAMERA_IMAGE:Int,GALLERY_IMAGE:Int) {
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
                .start(CAMERA_IMAGE)

            dialogImage.dismiss()
        }

        cvGallery.setOnClickListener {

            ImagePicker.with(this) // Crop Image(User can choose Aspect Ratio)
                .crop() // User can only select image from Gallery
                .galleryOnly().galleryMimeTypes(
                    arrayOf<String>("image/png", "image/jpg", "image/jpeg")
                ) // Image resolution will be less than 1080 x 1920
                .maxResultSize(500, 500) // .saveDir(getExternalFilesDir(null))
                .start(GALLERY_IMAGE)
            dialogImage.dismiss()
        }

        val window = dialogImage.window
        window!!.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        try {
            dialogImage.show()
        }
        catch (e: Exception){}
    }
    override fun Item(_vehicleTransporterID: Int) {
        vehicleTransporterID=_vehicleTransporterID
    }
    override fun ItemCheck(selected: Boolean) {
        if (selected == true){
            viewModel.vehicleTransporter()
            mViewDataBinding.constraintVehicleTransporter.visibility= View.VISIBLE
            mViewDataBinding.layoutGeneralLicense.visibility= View.VISIBLE
        }
        else{
            mViewDataBinding.constraintVehicleTransporter.visibility= View.GONE
            mViewDataBinding.layoutGeneralLicense.visibility= View.GONE
            vehicleTransporterID=0
        }
    }


}