package com.lwb.jetpack.function.work

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.lwb.jetpack.BuildConfig
import com.lwb.jetpack.R
import kotlinx.coroutines.withContext
import java.io.File
import java.io.RandomAccessFile

/**
 * author:luwenbo
 * created on date : 2019-06-13
 * content:
 * describe:
 */
class SaveWorker :Worker{
    private val TAG:String = "SaveWorkers"
    private var url:String = ""
    val path:String = Environment.getExternalStorageDirectory().toString() + "/myApplication/file/"
    private var context:Context?=null
    constructor(context: Context,workerParameters: WorkerParameters) : super(context,workerParameters) {
            this.context = context
    }
    override fun doWork(): Result {
        var url = inputData.getString("url")
        var file = File(path)
        if (!file.exists()){
            file.mkdirs()
        }
        var files =  makeFilePath(path,"test.txt")
        if (files !=null){
            for (i in 0 until 5000){
                //Log.i(TAG,"==1:$i")
                writeTxtToFile("i=$i",path,"test.txt")
            }
        }
        Log.i(TAG,"==doWork:"+System.currentTimeMillis()+"/url:"+url)
        var notification = NotificationCompat.Builder(context)
            .setContentText("数据保存成功")
            .setContentTitle("wokr测试数据")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setPriority(Notification.PRIORITY_HIGH)

        val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT > 25) {
            val channel = NotificationChannel(
                "jet_pack",
                BuildConfig.APPLICATION_ID,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        val id = System.currentTimeMillis().toString() + ""
        if (id.length >= 9) {
            notificationManager.notify(Integer.parseInt(id.substring(id.length - 9)), notification.build())
        } else {
            notificationManager.notify(Integer.parseInt(id), notification.build())
        }
        return Result.success()
    }

    // 将字符串写入到文本文件中
    private fun writeTxtToFile(strcontent: String, filePath: String, fileName: String) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName)

        val strFilePath = filePath + fileName
        // 每次写入时，都换行写
        val strContent = strcontent + "\r\n"
        try {
            val file = File(strFilePath)
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:$strFilePath")
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            val raf = RandomAccessFile(file, "rwd")
            raf.seek(file.length())
            raf.write(strContent.toByteArray())
            raf.close()
        } catch (e: Exception) {
            Log.e("TestFile", "Error on write File:$e")
        }

    }

    // 生成文件
    fun makeFilePath(filePath: String, fileName: String): File? {
        var file: File? = null
       // makeRootDirectory(filePath)
        try {
            file = File(filePath + fileName)
            if (!file.exists()) {
                file.createNewFile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return file
    }


    override fun hashCode(): Int {
        return url.hashCode()
    }

}