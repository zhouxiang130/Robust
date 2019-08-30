package com.yj.robust.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.yj.robust.R;
import com.yj.robust.ViewHolder.ChildViewHolder;
import com.yj.robust.ViewHolder.GroupViewHolder;
import com.yj.robust.ViewHolder.NormalViewHolder;
import com.yj.robust.bean.ChildItemBean;
import com.yj.robust.bean.DemoItemBean;
import com.yj.robust.bean.GroupItemBean;
import com.yj.robust.bean.NormalItemBean;
import com.yj.robust.helper.ParseHelper;
import com.yj.robust.model.CartsEntity;

import java.util.List;

/**
 * Created by Horrarndoo on 2017/11/22.
 * <p>
 */

public class DemoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

//    private List<DemoItemBean> mDatas;
    private Context mContext;
    private OnCheckChangeListener onCheckChangeListener;
    private final List<CartsEntity.DataBean.ProCartsBean> mLists;

    public void setOnCheckChangeListener(OnCheckChangeListener l) {
        onCheckChangeListener = l;
    }

    public DemoAdapter(Context context,List<CartsEntity.DataBean.ProCartsBean> mList) {
        mContext = context;
//        mDatas = datas;
        mLists = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.w("tag", "onCreateViewHolder");
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder = null;

        switch (viewType) {
            case DemoItemBean.TYPE_NORMAL:
                View v = mInflater.inflate(R.layout.item_normal, parent, false);
                holder = new NormalViewHolder(v);
                break;
            case DemoItemBean.TYPE_GROUP:
                View v1 = mInflater.inflate(R.layout.item_group, parent, false);
                holder = new GroupViewHolder(v1);
                break;
            case DemoItemBean.TYPE_CHILD:
                View v2 = mInflater.inflate(R.layout.item_child, parent, false);
                holder = new ChildViewHolder(v2);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Log.w("tag", "onBindViewHolder");

        if (holder instanceof NormalViewHolder) {
            NormalViewHolder nHolder = (NormalViewHolder) holder;
            nHolder.bindData(mLists.get(position));
//            nHolder.tvNormal.setText(mLists.get(position).get);
            nHolder.cbNormal.setOnCheckedChangeListener(new OnCheckedChangeListener(position, DemoItemBean.TYPE_NORMAL));

//            nHolder.cbNormal.setChecked(mDatas.get(position).isChecked());
        } else if (holder instanceof GroupViewHolder) {

            GroupViewHolder gHolder = (GroupViewHolder) holder;
            gHolder.bindData(mLists.get(position));
//            gHolder.tvGroup.setText(mLists.get(position).getTitle());
            gHolder.cbGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(position, DemoItemBean.TYPE_GROUP));

//            gHolder.cbGroup.setChecked(mLists.get(position).isChecked());

        } else if (holder instanceof ChildViewHolder) {

            ChildViewHolder cHolder = (ChildViewHolder) holder;
            cHolder.bindData( mLists.get(position));
//            cHolder.tvChild.setText(mLists.get(position).getTitle());
            cHolder.cbChild.setOnCheckedChangeListener(new OnCheckedChangeListener(position, DemoItemBean.TYPE_CHILD));
//            cHolder.cbChild.setChecked(mLists.get(position).isChecked());

        }
    }

    @Override
    public int getItemViewType(int position) {
        return mLists.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    /**
     * CheckBox CheckedChangeListener
     */
    private class OnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        int mPosition, mItemType;

        public OnCheckedChangeListener(int position, int itemType) {
            mPosition = position;
            mItemType = itemType;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (onCheckChangeListener != null)
                onCheckChangeListener.onCheckedChanged(mLists, mPosition, isChecked, mItemType);
        }
    }


    /**
     * 删除选中item
     */
    public void removeChecked() {
        int iMax = mLists.size() - 1;
        //这里要倒序，因为要删除mDatas中的数据，mDatas的长度会变化
//        for (int i = iMax; i >= 0; i--) {
//            if (mLists.get(i).isChecked()) {
//                mLists.remove(i);
//                notifyItemRemoved(i);
//                notifyItemRangeChanged(i, mLists.size());
//            }
//        }
    }
}
