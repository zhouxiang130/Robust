package com.yj.robust.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.yj.robust.R;
import com.yj.robust.base.URLBuilder;
import com.yj.robust.model.MineCollectEntity;
import com.yj.robust.model.NormalEntity;
import com.yj.robust.model.ShopDetailEntity;
import com.yj.robust.util.LogUtils;
import com.yj.robust.util.ToastUtils;
import com.yj.robust.util.UserUtils;
import com.yj.robust.util.Utils;
import com.yj.robust.widget.Dialog.CustomNormalContentDialog;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Suo on 2017/4/17.
 */

public class StoreTelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private final ShopDetailEntity datas;
	private int flag = 0;
	private Context mContext;
	SpendDetialClickListener mItemClickListener;
	private String TAG = "MineCollectionAdapter";

	public StoreTelAdapter(Context mContext, ShopDetailEntity datas) {
		this.mContext = mContext;
		this.datas = datas;

	}

	public void setOnItemClickListener(SpendDetialClickListener listener) {
		this.mItemClickListener = listener;
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		CollectionViewHolder collectionViewHolder;
		collectionViewHolder = new CollectionViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_store_tel_list, parent, false), mItemClickListener);
		return collectionViewHolder;
	}


	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
		if (holder instanceof CollectionViewHolder) {
			((CollectionViewHolder) holder).tvTel.setText(datas.getData().getShopTel().get(position));
		}
	}

	@Override
	public int getItemCount() {
		return datas.getData().getShopTel().size();
	}

	class CollectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		@BindView(R.id.text_tel_info)
		TextView tvTel;


		private SpendDetialClickListener mListener;

		public CollectionViewHolder(View itemView, SpendDetialClickListener listener) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			this.mListener = listener;
			//@TODO 布局文件 添加点击事件
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (mListener != null) {
				mListener.onItemClick(v, getPosition(), flag);
			}
		}
	}

	public interface SpendDetialClickListener {
		void onItemClick(View view, int postion, int flag);
	}


}