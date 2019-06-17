package com.lwb.jetpack.function.paging

import android.content.Context
import android.widget.Toast
import androidx.paging.PageKeyedDataSource

/**
 * author:luwenbo
 * created on date : 2019-06-06
 * content:
 * describe:
 */

class ConcertDataSource : PageKeyedDataSource<String, PagingBean>() {
    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, PagingBean>
    ) {
        var url = ""
        getData(url, object : Callback {
            override fun onResult(choicenesses: List<PagingBean>, paging: String) {
                callback.onResult(choicenesses, "1", paging)

            }

        })
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, PagingBean>) {
        //callback.onResult(fetchItems(params.key, params.loadSize))
        if (params.key != "") {
            getData(params.key, object : Callback {
                override fun onResult(choicenesses: List<PagingBean>, paging: String) {
                    callback.onResult(choicenesses, paging)
                }

            })
        } else {
            Toast.makeText(context,"沒有更多加載!",Toast.LENGTH_LONG).view
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, PagingBean>) {

    }

    var context: Context? = null

    fun init(context: Context) {
        this.context = context
    }

    private fun getData(url: String, callback: Callback) {

    }

    private interface Callback {
        fun onResult(choicenesses: List<PagingBean>, paging: String)
    }

}