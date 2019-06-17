package com.lwb.jetpack.function.work

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.Observer
import androidx.work.*
import com.lwb.jetpack.R
import com.lwb.jetpack.base.BaseV2AppActivity
import com.lwb.jetpack.widgets.premissions.PermissionsActivity
import kotlinx.android.synthetic.main.activity_work.*


/**
 * author:luwenbo
 * created on date : 2019-06-13
 * content:
 * describe:
 */

class WorkActivity : BaseV2AppActivity() {
    //val uploadWorkRequest:WorkRequest?=null
    val TAG:String="WorkActivity"
    var time: Long? = 0
    companion object{
        fun startWorkActivity(activity: Activity){
            var intent = Intent(activity,WorkActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun addListener() {
        var con = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        var data = Data.Builder().putString("url","https://www.baidu.com").build()
        var uploadWorkRequest = OneTimeWorkRequestBuilder<SaveWorker>()
            .setConstraints(con)
            .setInputData(data)
            .build()

        start.setOnClickListener {
            if (mPermissionsChecker.lacksPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                PermissionsActivity.startActivityForResult(this,1,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                return@setOnClickListener
            }
            time = System.currentTimeMillis()
            WorkManager.getInstance().enqueue(uploadWorkRequest)
        }

        WorkManager.getInstance().getWorkInfoByIdLiveData(uploadWorkRequest.id).observe(this, Observer {
            when(it.state){
                WorkInfo.State.ENQUEUED -> Log.i(TAG,"==doWork start $time:")
                 WorkInfo.State.SUCCEEDED -> Log.i(TAG,"==doWork succeeded :"+System.currentTimeMillis())
            }
        })
    }

    override fun initData() {
    }

    override fun initView() {
        showBack()
        showTitle("Manager")
    }

    override fun getLayoutView(): Int {
        return R.layout.activity_work

    }

}