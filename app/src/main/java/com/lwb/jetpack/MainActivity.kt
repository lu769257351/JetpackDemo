package com.lwb.jetpack

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lwb.jetpack.base.BaseV2AppActivity
import com.lwb.jetpack.base.RecycleViewDrivider
import com.lwb.jetpack.function.work.WorkActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseV2AppActivity() {
    var TAG:String = "MainActivity"
    val titles = arrayOf("paging","WorkManager")
    var functionAdapter:FunctionAdapter?=null
    val functions = arrayListOf<FunctionBean>()
    override fun addListener() {
        functionAdapter!!.setOnItemClick { v, position ->
            when(position){
                0 ->Log.i(TAG,"$position")
                1 -> WorkActivity.startWorkActivity(this)
            }

        }
    }

    @SuppressLint("WrongConstant")
    override fun initData() {
        for (i in 0 until titles.size){
            var functionBean = FunctionBean()
            functionBean.title = titles[i]
            functions.add(functionBean)
        }
       functionAdapter =  FunctionAdapter(this,functions)
        recyclerView.adapter = functionAdapter
        recyclerView!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.addItemDecoration(RecycleViewDrivider(this, LinearLayoutManager.VERTICAL))

    }

    override fun initView() {
        titleLayout.visibility = View.GONE
       // setImmerseLayout(titleLayout)
    }

    override fun getLayoutView(): Int {
        return R.layout.activity_main
    }

}
