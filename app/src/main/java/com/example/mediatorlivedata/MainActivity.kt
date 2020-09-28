package com.example.mediatorlivedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediatorlivedata.common.ViewModelFactory
import com.example.mediatorlivedata.common.getViewModel
import com.example.mediatorlivedata.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAdapter: MainAdapter
    private val mViewModel: MainViewModel by lazy { getViewModel { MainViewModel(this) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            viewModel = mViewModel
            lifecycleOwner = this@MainActivity
            mBinding = this
        }
        setObserver()
        initiateListener()
        initiateAdapter()
    }

    fun setObserver() {
        mViewModel.newPostTracker.observe(this) {
            newPostExists ->
                if(newPostExists) {
                    Snackbar.make(mBinding.coordinator, "New Item Added", Snackbar.LENGTH_INDEFINITE)
                        .setAction("REFRESH") { mViewModel.refreshItem() }
                        .show()
                }
        }
        mViewModel.displayedItem.observe(this) {
            mAdapter.updateData(it)
        }
        mViewModel.isLoading.observe(this) { mBinding.isLoading = it }
    }

    fun initiateListener() {
        mBinding.fab.setOnClickListener(this)
    }

    fun initiateAdapter() {
        mAdapter = MainAdapter()
        mBinding.rvItem.layoutManager = LinearLayoutManager(this)
        mBinding.rvItem.adapter = mAdapter
        mBinding.rvItem.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) mBinding.fab.extend()
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && mBinding.fab.isExtended()) {
                    mBinding.fab.shrink();
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when(v) {
            mBinding.fab -> mViewModel.insertRandomItem()
        }
    }
}