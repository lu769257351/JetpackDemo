package com.lwb.jetpack.function.paging

import androidx.paging.DataSource
import androidx.lifecycle.MutableLiveData


/**
 * author:luwenbo
 * created on date : 2019-06-06
 * content:
 * describe:
 */

class ConcertFactory : DataSource.Factory<String, PagingBean>() {
    private val mSourceLiveData = MutableLiveData<ConcertDataSource>()

   override fun create(): DataSource<String, PagingBean> {
        val concertDataSource = ConcertDataSource()
        mSourceLiveData.postValue(concertDataSource)
        return concertDataSource
    }

}