package com.sa.nafhasehaprovider.ui.fragment.auth.myAccount

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
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
import com.sa.nafhasehaprovider.common.util.Utilities.Companion.onLoadImageFromUrl
import com.sa.nafhasehaprovider.common.util.Utilities.Companion.onPermission
import com.sa.nafhasehaprovider.common.util.Utilities.Companion.showToastError
import com.sa.nafhasehaprovider.common.util.Utilities.Companion.showToastSuccess
import com.sa.nafhasehaprovider.databinding.FragmentAccountBinding
import com.sa.nafhasehaprovider.entity.response.areasResponse.AreasResponseData
import com.sa.nafhasehaprovider.entity.response.categoriesResponse.DataCategoriesResponse
import com.sa.nafhasehaprovider.entity.response.cityResponse.CityResponseData
import com.sa.nafhasehaprovider.ui.activity.MainActivity
import com.sa.nafhasehaprovider.ui.activity.MapsActivity
import com.sa.nafhasehaprovider.ui.generalViewModel.AreasViewModel
import com.sa.nafhasehaprovider.ui.generalViewModel.AuthenticationViewModel
import com.sa.nafhasehaprovider.ui.generalViewModel.CityViewModel
import okhttp3.MultipartBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class AccountFragment : BaseFragment<FragmentAccountBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_account

    private var imageFile: File? = null
    private var photo: MultipartBody.Part? = null
    var IDcity: Int? = null
    var IDarea: Int? = null
    var areaId: Int? = null
    var cityid: Int? = null
    var categoriesID: Int? = null
    private var mainActivity: MainActivity? = null
    private val viewModel: CityViewModel by viewModel()
    private val viewModelAreas: AreasViewModel by viewModel()
    private val viewModelAuth: AuthenticationViewModel by viewModel()


    var cityDataSource = ArrayList<CityResponseData>()
    lateinit var dropDownCityAdapter: DropDownCityAdapter


    lateinit var dropDownAreasAdapter: DropDownAreasAdapter
    var areasDataSource = ArrayList<AreasResponseData>()

    lateinit var categoriesAdapter: CategoriesAdapter
    lateinit var listCategory: ArrayList<DataCategoriesResponse>


     var addressStr: String=""
    lateinit var city: String
    var lat = 0.0
    var lon: Double = 0.0
    var request_code = 22
    var mProfileUri: Uri? = null
    private val GALLERY_IMAGE_REQ_CODE = 102
    private val CAMERA_IMAGE_REQ_CODE = 103
    private lateinit var dialogImage: Dialog

    private val REQUEST_CODE_CHOOSE = 100
    private var imagePath = ""
    private lateinit var typeAccount: String
    var homeService: Int=0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        mainActivity!!.mViewDataBinding.bottomNav.visibility = View.GONE
        mainActivity!!.mViewDataBinding.toolbar.visibility = View.GONE

        onPermission(requireActivity())
        initResponse()
        onClick()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initResponse() {
        listCategory = ArrayList()

        //apiResponse city
        cityDataSource.add(CityResponseData(null, 0, getString(R.string.select)))
        viewModel.cityResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                areasDataSource.add(
                                    AreasResponseData(
                                        null, 0, getString(R.string.select)
                                    )
                                )
                                cityDataSource.addAll(it.data)
                                dropDownCityAdapter =
                                    DropDownCityAdapter(requireActivity(), cityDataSource)
                                mViewDataBinding.spCity.adapter = dropDownCityAdapter

                                for (i in cityDataSource.indices) {
                                    if (cityDataSource[i].id == cityid) {
                                        mViewDataBinding.spCity.setSelection(i)
                                    }
                                }

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
                    showProgress(false)

                }
            }
        })


        //apiResponse areas
        viewModelAreas.areasResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                areasDataSource.addAll(it.data)
                                dropDownAreasAdapter =
                                    DropDownAreasAdapter(requireActivity(), areasDataSource)
                                mViewDataBinding.spAreas.adapter = dropDownAreasAdapter

                                for (i in areasDataSource.indices) {
                                    if (areasDataSource[i].id == areaId) {
                                        mViewDataBinding.spAreas.setSelection(i)
                                    }
                                }

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
        viewModelAuth.categories(1)
        viewModelAuth.categoriesResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                listCategory.addAll(it.data!!)
                                viewModelAuth.getProfile()
//                                categoriesAdapter =
//                                    CategoriesAdapter(requireActivity(), listCategory,
//                                         listOf(),  //it.data?.provider?.categories ?:
//                                        this)
//                                mViewDataBinding.rvAllService.adapter = categoriesAdapter
//                                categoriesAdapter.notifyDataSetChanged()

//                                for (i in listCategory.indices) {
//                                    if (listCategory[i].id == categoriesID) {
//
////                                        listCategory.add(DataCategoriesResponse(
////                                            listCategory[i].id, "", null,"",true))
//                                    }
//                                }
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
                    showProgress(false)
                }
            }
        })


        // apiResponse getProfile
        viewModelAuth.authResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {

                            // dismiss loading
                            CODE200 -> {

                                categoriesAdapter =
                                    CategoriesAdapter(requireActivity(), listCategory,
                                        it.data?.provider?.categories ?:   listOf())
                                mViewDataBinding.rvAllService.adapter = categoriesAdapter
                                categoriesAdapter.notifyDataSetChanged()

                                viewModel.city(1, "ALL")


                                //showToastSuccess(requireActivity(), it.message)
                                if ( it.data!!.provider!!.image !=null){
                                    onLoadImageFromUrl(
                                        requireActivity(), it.data!!.provider!!.image!!,
                                        mViewDataBinding.ivImageUser)
                                }

                                mViewDataBinding.tvName.setText(it.data.provider!!.name)
                                mViewDataBinding.tvEmail.setText(it.data.provider.email)
                                mViewDataBinding.tvMobile.setText(it.data.provider.phone)
                                mViewDataBinding.tvAddress.text = it.data.provider.address
                                if (it.data.provider.services_from_home!! == 1) {
                                    mViewDataBinding.cbDoYouHaveHomeService.isChecked = true
                                }
                                else {
                                    mViewDataBinding.cbDoYouHaveHomeService.isChecked = false
                                }

                                if (it.data.provider.provider_type!! == "ProviderCenter") {
                                    mViewDataBinding.radioCenter.isChecked=true
                                    mViewDataBinding.radioPerson.isChecked=false

                                } else if (it.data.provider.provider_type!! == "Provider") {
                                    mViewDataBinding.radioPerson.isChecked=true
                                    mViewDataBinding.radioCenter.isChecked=false
                                }
                                try {
                                    areaId = it.data.provider.area!!.id!!
                                    cityid = it.data.provider.city!!.id!!


                                } catch (e: Exception) {
                                }

                            }
                            CODE403 -> {
                                //unAuthorized()
                                Utilities.logOutApp(requireActivity())
                                Utilities.showToastError(requireActivity(), it.message)
                            }
                            CODE405 -> {
                                Utilities.showToastError(requireActivity(), it.message)

                            }
                            CODE500 -> {
                                Utilities.showToastError(requireActivity(), it.message)
                            }
                            else -> {
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


        // apiResponse editProfile
        viewModelAuth.editProfileResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                showToastSuccess(requireActivity(), it.message)
                                NafhasehaProviderApp.pref.saveUserData(
                                    requireActivity(), USER_DATA, it)

                            }
                            CODE403 -> {
                                //unAuthorized()
                                Utilities.logOutApp(requireActivity())
                                showToastError(requireActivity(), it.message)
                            }
                            CODE405 -> {
                                Utilities.showToastError(requireActivity(), it.message)

                            }
                            CODE500 -> {
                                Utilities.showToastError(requireActivity(), it.message)
                            }
                            else -> {
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
        mViewDataBinding.spCity?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    if (position != 0) {
                        IDcity = cityDataSource.get(position).id
                        viewModelAreas.areas(IDcity!!, "ALL")
                    } else {

                    }
                }

            }


        mViewDataBinding.spAreas?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    if (position != 0) {
                        IDarea = areasDataSource.get(position).id
                    } else {

                    }
                }

            }

        mViewDataBinding.ivProfileOrBack.setOnClickListener {
            mainActivity!!.navController.popBackStack()
            onDestroy()
        }


        mViewDataBinding.tvAddress.setOnClickListener {
            onPermission(requireActivity())
            startActivityForResult(
                Intent(
                    requireActivity(), MapsActivity::class.java
                ), request_code
            )
        }


        mViewDataBinding.radioGroupTypeAccount.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_center -> {
                    typeAccount ="ProviderCenter"
                }
                R.id.radio_person -> {
                    typeAccount ="Provider"
                }
            }
        }

        mViewDataBinding.cbDoYouHaveHomeService.setOnCheckedChangeListener { buttonView, isChecked ->
            // تم تحديد أو إلغاء تحديد الصندوق
            if (isChecked) {
                // القيام بإجراء عند تحديد الصندوق
                homeService=1
            } else {
                // القيام بإجراء عند إلغاء تحديد الصندوق
                homeService=0
            }
        }

        mViewDataBinding.ivUploadImage.setOnClickListener {

            //showDialogImage()
            openGallery()

        }

        mViewDataBinding.btnUpdate.setOnClickListener {

            val nameProvider = mViewDataBinding.tvName.text.toString().trim()
            val phone = mViewDataBinding.tvMobile.text.toString().trim()
            val email = mViewDataBinding.tvEmail.text.toString().trim()
            val address = mViewDataBinding.tvAddress.text.toString().trim()

            if (nameProvider.isEmpty()) {
                mViewDataBinding.tvName.error = getString(R.string.mobile_number_is_required)
            } else if (phone.isEmpty()) {
                mViewDataBinding.tvName.error = getString(R.string.mobile_number_is_required)
            }else if (mViewDataBinding.spCity.selectedItemPosition==0) {
                Utilities.showToastError(requireActivity(), getString(R.string.please_selected_city))

            }else if (mViewDataBinding.spAreas.selectedItemPosition==0) {
                Utilities.showToastError(requireActivity(), getString(R.string.please_selected_aras))

            }else if (address.isEmpty()) {
                mViewDataBinding.tvAddress.error= getString(R.string.address_is_required)
            }
            else if (typeAccount=="") {
                Utilities.showToastError(requireActivity(), getString(R.string.please_select_an_account_type))
            }
            else if (categoriesAdapter.idCategory.size==0) {
                Utilities.showToastError(requireActivity(), getString(R.string.choose_your_services))
            }


            else {
                val ids = ArrayList<Int>()
                categoriesAdapter.idCategory.forEach{
                    ids.add(it)
                }

                viewModelAuth.editProfile(
                    convertFileToMultipart(imageFile, "image"),
                    convertToRequestBody(typeAccount),
                    convertToRequestBody(nameProvider),
                    convertToRequestBody(1.toString()),
                    convertToRequestBody(phone),
                    convertToRequestBody(email),
                    convertToRequestBody(address),
                    convertToRequestBody(lat.toString()),
                    convertToRequestBody(lon.toString()),
                    convertToRequestBody(IDcity.toString()),
                    convertToRequestBody(IDarea.toString()),
                    convertToRequestBody(homeService.toString()),
                    ids)
            }
        }
    }


    private fun openGallery() {
        onPermission(requireActivity())
        showDialogImage()
//        TedImagePicker.with(requireActivity())
//            .image()
//            // .imageAndVideo()
//            // .video()
//            .cameraTileImage(R.drawable.ic_defult_camera).showTitle(true).title(R.string.select)
//            .backButton(R.drawable.ic_back)
//            .buttonText(R.string.done)
//            .buttonBackground(R.drawable.shape_bottom)
//            //.max(1, "")
//            .drawerAlbum()
//            // dropDownAlbum()
//            .start { uriList ->
//                image_url=uriList.path.toString()
//                Log.d("sdsdadadasds",uriList.path.toString())
//                Toast.makeText(requireActivity(), uriList.toString(), Toast.LENGTH_SHORT).show()
//                Glide.with(requireActivity()).load(uriList.toString().trim())
//                    .into(mViewDataBinding.ivImageUser)
//            }
//            .startMultiImage { uriList ->
//                Log.d("sdsdadadasds", uriList.toString())
//                Toast.makeText(requireActivity(), uriList.toString(), Toast.LENGTH_SHORT).show()
//                Glide.with(requireActivity()).load(uriList.get(0).toString().trim())
//                    .into(mViewDataBinding.ivImageUser)
//            }


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

                        Glide.with(requireActivity()).load(imagePath)
                            .into(mViewDataBinding.ivImageUser)
                    }
                    CAMERA_IMAGE_REQ_CODE -> {
                        mProfileUri = uri!!
                        imageFile = File(mProfileUri!!.path)
                        imagePath = mProfileUri!!.path.toString()

                        Glide.with(requireActivity()).load(imagePath)
                            .into(mViewDataBinding.ivImageUser)

                    }
                    request_code -> {
                        addressStr = data!!.getStringExtra("ADDRESS")!!
                        city = data.getStringExtra("CITY")!!
                        lat = data.getDoubleExtra("LAT", 0.0)
                        lon = data.getDoubleExtra("LON", 0.0)
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

    override fun onDestroy() {
        super.onDestroy()
        mainActivity!!.mViewDataBinding.bottomNav.visibility = View.VISIBLE
        mainActivity!!.mViewDataBinding.toolbar.visibility = View.VISIBLE
    }



}