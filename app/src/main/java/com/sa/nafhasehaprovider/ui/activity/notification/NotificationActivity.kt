package com.sa.nafhasehaprovider.ui.activity.notification

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.pramonow.endlessrecyclerview.EndlessRecyclerView
import com.pramonow.endlessrecyclerview.EndlessScrollCallback
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.adapter.NewOrderAdapter
import com.sa.nafhasehaprovider.adapter.NotificationAdapter
import com.sa.nafhasehaprovider.base.BaseActivity
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.EndlessRecyclerViewScrollListener
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.databinding.ActivityNotificationBinding
import com.sa.nafhasehaprovider.entity.response.notificationResponse.NotificationResponseData
import com.sa.nafhasehaprovider.viewModels.NotificationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationActivity : BaseActivity<ActivityNotificationBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_notification
    private val viewModel: NotificationViewModel by viewModel()

    lateinit var notificationAdapter: NotificationAdapter
    lateinit var list:ArrayList<NotificationResponseData>

    var currentPage : Int = 1
    var countPage =10
    lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    private var layoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onClick()



        initResponse()


        endlessRecyclerViewScrollListener=object : EndlessRecyclerViewScrollListener(layoutManager!!){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                currentPage=page+1
                viewModel.notification(currentPage, countPage)
            }
        }
        mViewDataBinding.rvNotification.addOnScrollListener(endlessRecyclerViewScrollListener)


    }


    private fun initResponse() {
        list= ArrayList()
        notificationAdapter = NotificationAdapter(this, list)
        layoutManager = LinearLayoutManager(this)
        mViewDataBinding.rvNotification.layoutManager = layoutManager
        mViewDataBinding.rvNotification.adapter = notificationAdapter


        // api response
        viewModel.notification(currentPage, countPage)
        viewModel.notificationResponse.observe(this, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {

                            CODE200 -> {
                             //   Utilities.showToastSuccess(this, it.message)
                                list.addAll(it.data!!)
                                notificationAdapter.notifyDataSetChanged()
                            }
                            CODE403 -> {
                                //unAuthorized()
                                Utilities.logOutApp(this)
                            }
                            CODE405 -> {
                                Utilities.showToastError(this, it.message)
                                showProgress(false)
                            }
                            CODE500 -> {
                                Utilities.showToastError(this, it.message)
                            }
                            else -> {
                                Utilities.showToastError(this, it.message)
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
        mViewDataBinding.ivBack.setOnClickListener {
            onBackPressed()
            finish()
            Animatoo.animateFade(this);

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        Animatoo.animateFade(this);
    }

}