package com.lwb.jetpack.function.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
/**
 * author:luwenbo
 * created on date : 2019-06-06
 * content:
 * describe:
 */

class PagingViewModel : ViewModel() {
    private var convertList: LiveData<PagedList<PagingBean>>? = null
    private var concertDataSource: DataSource<String, PagingBean>? = null


    init {
        val concertFactory = ConcertFactory()
        concertDataSource = concertFactory.create()
        convertList = LivePagedListBuilder(concertFactory, 20)
            .setFetchExecutor {
                //TCLog.d("==it:"+it.run())
            }
            .build()
    }


    fun getFunctionList(): LiveData<PagedList<PagingBean>>? {
        //TCLog.d("==getChoiceList:")
        return convertList
    }
}