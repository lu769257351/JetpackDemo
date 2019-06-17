package com.lwb.jetpack.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.lwb.jetpack.base.holder.BaseViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class BaseRecycleListAdapter<T> extends  RecyclerView.Adapter<BaseViewHolder>{
    protected List<T> mList; // Arraylist数据集合
    protected Context mContext; // 上下文对像
    protected LayoutInflater mInflater;
    protected BaseViewHolder.OnItemClick onItemClick;
    public BaseRecycleListAdapter(Context context, List<T> mList) {
        this.mContext = context;
        mInflater= LayoutInflater.from(mContext);
        setList(mList);
    }

    /**
     * 构造方法
     *
     * @param context
     *            上下文对像
     * @param mList
     *            数据集合
     */
    public BaseRecycleListAdapter(Context context, ArrayList<T> mList) {
        this.mContext = context;
        mInflater= LayoutInflater.from(mContext);
        setList(mList);
    }

    /**
     *
     * @param context
     */
    public BaseRecycleListAdapter(Context context, T[] mArray) {
        this.mContext = context;
        mInflater= LayoutInflater.from(mContext);
        setList(mArray);
    }


    @NonNull
    @Override
    public abstract BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType);


    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 设置数据集合
     *
     * @param mList2
     */
    private void setList(List<T> mList2) {
        this.mList = mList2 != null ? mList2 : new ArrayList<T>();
    }

    /**
     *
     * @param mArray
     */
    private void setList(T[] mArray) {
        List<T> list = new ArrayList<T>();
        if (mArray != null) {
            for (T t : mArray) {
                list.add(t);
            }
        }
        setList(list);
    }

    /**
     * 更新数据
     *
     * @param mList
     *            ArrayList<T>
     */
    public void changeData(List<T> mList) {
        setList(mList);
        this.notifyDataSetChanged();
    }

    /**
     * 更新数据
     *
     * @param mArray
     */
    public void changeData(T[] mArray) {
        setList(mArray);
        this.notifyDataSetChanged();
    }

    /**
     * 增加数据
     *
     * @param t
     */
    public void add(T t) {
        this.mList.add(t);
        this.notifyDataSetChanged();
    }

    /**
     * 增加集合的数
     *
     * @param mList
     */
    public void addAll(List<T> mList) {
        int size = mList.size();
        for (int i = 0; i < size; i++) {
            T t = mList.get(i);
            if(!this.mList.contains(t)){
                this.mList.add(t);
            }
        }
        this.notifyDataSetChanged();
    }

    /**
     * 插入数据
     *
     * @param position
     *            插入的位
     * @param t
     *            数据对象
     */
    public void insert(int position, T t) {
        this.mList.add(position, t);
        this.notifyDataSetChanged();
    }

    /**
     * 删除�?��数据
     *
     * @param t
     *            数据对象
     * @return 删除是否成功
     */
    public boolean remove(T t) {
        boolean removed = this.mList.remove(t);
        this.notifyDataSetChanged();
        return removed;
    }

    /**
     * 删除指定位置的数�?
     *
     * @param position
     *            要删除的位置
     * @return 被删除的对象
     */
    public T remove(int position) {
        T t = this.mList.remove(position);
        this.notifyDataSetChanged();
        return t;
    }

    /**
     * 更新指定位置的对�?
     *
     * @param position
     *            指定位置
     * @param t
     *            数据对象
     */
    public void set(int position, T t) {
        this.mList.set(position, t);
        this.notifyDataSetChanged();
    }

    /**
     * 根据指定的比较器进行排序
     *
     * @param comparator
     *            比较
     */
    public void sort(Comparator<T> comparator) {
        Collections.sort(mList, comparator);
        this.notifyDataSetChanged();
    }

    public BaseViewHolder.OnItemClick getOnItemClick() {
        return onItemClick;
    }

    public void setOnItemClick(BaseViewHolder.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
}
