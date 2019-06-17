package com.lwb.jetpack

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.lwb.jetpack.base.adapter.BaseRecycleListAdapter
import com.lwb.jetpack.base.holder.BaseViewHolder

/**
 * author:luwenbo
 * created on date : 2019-06-17
 * content:
 * describe:
 */

class FunctionAdapter(context: Context,mList:List<FunctionBean>) :BaseRecycleListAdapter<FunctionBean>(context, mList) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        var view = mInflater.inflate(R.layout.item_function,parent,false)
       return BaseViewHolder(parent,view,onItemClick)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        var functionBean = mList[position]
        holder.setText(R.id.function_title,functionBean.title)
    }
}