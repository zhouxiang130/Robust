package com.yj.robust.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sobot.chat.utils.ToastUtil;
import com.yj.robust.R;
import com.yj.robust.base.URLBuilder;
import com.yj.robust.model.ShopListEntity;
import com.yj.robust.model.StoreClassEntity;
import com.yj.robust.model.StoreListEntity;
import com.yj.robust.ui.MainActivity;
import com.yj.robust.ui.activity.NormalWebViewActivity;
import com.yj.robust.util.IntentUtils;
import com.yj.robust.widget.WrapContentGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/7/24 0024.
 */

public class StoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final String TAG = "StoreAdapter";
	private List<ShopListEntity.DataBean.ShopArrayBean> mData = null;
	private List<StoreListEntity.DataBean.ProductClassifyListBean> mList = null;
	private Context mContext;
	private ShopListClickListener mItemClickListener;
	private int pos;
	private ViewInterfaces viewInterface;
	private StoreClassEntity storeClassEntity;

	public StoreAdapter(Context mContext,
	                    List<StoreListEntity.DataBean.ProductClassifyListBean> mList,
	                    List<ShopListEntity.DataBean.ShopArrayBean> mData) {
		this.mContext = mContext;
		this.mList = mList;
		this.mData = mData;
		storeClassEntity = new StoreClassEntity();
	}


	public void setOnItemClickListener(ShopListClickListener listener) {
		this.mItemClickListener = listener;
	}

	/**
	 * 单选接口
	 *
	 * @param viewInterfaces
	 */
	public void setViewInterfaces(ViewInterfaces viewInterfaces) {
		this.viewInterface = viewInterfaces;
	}


	/**
	 * 复选框接口
	 */
	public interface ViewInterfaces {
		/**
		 * 组选框状态改变触发的事件
		 */
		void onClicks(int viewId);
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		GridViewHolder gridViewHolder;
		ClassTitleViewHolder classTitleViewHolder;
		StoreItemViewHolder storeItemViewHolder;
		if (viewType == 0) {
			//图标分类
			gridViewHolder = new GridViewHolder(LayoutInflater
					.from(mContext).inflate(R.layout.item_home_gridview, parent, false));
			return gridViewHolder;
		} else if (viewType == 1) {
			//分类标题
			classTitleViewHolder = new ClassTitleViewHolder(LayoutInflater
					.from(mContext).inflate(R.layout.item_class_title, parent, false));
			return classTitleViewHolder;
		} else {
			storeItemViewHolder = new StoreItemViewHolder(LayoutInflater
					.from(mContext).inflate(R.layout.item_store_list_item, parent, false), mItemClickListener);
			return storeItemViewHolder;
		}
	}

	@Override
	public int getItemViewType(int position) {
		switch (position) {
			case 0:
				return 0;
			case 1:
				return 1;
			default:
				return 2;
		}
	}

	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {

		if (viewHolder instanceof GridViewHolder) {
			List<View> mPagerList = new ArrayList<>();
			//每页数量
			int pageSize = 10;
			LayoutInflater inflater = LayoutInflater.from(mContext);
			//页数
			final int pageCount = (int) Math.ceil(mList.size() * 1.0 / pageSize);

			for (int i = 0; i < pageCount; i++) {
				// 每个页面都是inflate出一个新实例
				WrapContentGridView gridView = (WrapContentGridView) inflater.inflate(R.layout.gridview, ((GridViewHolder) viewHolder).mViewpager, false);
				gridView.setAdapter(new StoreGridviewAdapter(mContext, mList, i, pageSize));
				mPagerList.add(gridView);

				gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

						if (TextUtils.isEmpty(mList.get((int) id).getClassify_id()) && TextUtils.isEmpty(mList.get((int) id).getClassify_img())) {
							Intent intent = new Intent(mContext, MainActivity.class);
							intent.putExtra("page", "1");
							mContext.startActivity(intent);
						} else {
							IntentUtils.IntentToGoodsList(mContext, mList.get((int) id).getClassify_id());
						}
					}
				});
				gridView.setFocusable(false);
			}
			//设置适配器
//			LogUtils.i("pagerlist的长度" + mPagerList.size());
			((GridViewHolder) viewHolder).mViewpager.setAdapter(new ViewPagerAdapter(mPagerList));
			((GridViewHolder) viewHolder).mViewpager.setFocusable(false);

		} else if (viewHolder instanceof ClassTitleViewHolder) {

			((ClassTitleViewHolder) viewHolder).tvDefault.setSelected(storeClassEntity.isDefault());
			((ClassTitleViewHolder) viewHolder).tvHighest.setSelected(storeClassEntity.isHighest());
			((ClassTitleViewHolder) viewHolder).tvLately.setSelected(storeClassEntity.isLately());

			((ClassTitleViewHolder) viewHolder).llDefault.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
//					((ClassTitleViewHolder) viewHolder).tvDefault.setSelected(true);
//					((ClassTitleViewHolder) viewHolder).tvHighest.setSelected(false);
//					((ClassTitleViewHolder) viewHolder).tvLately.setSelected(false);
					storeClassEntity.setDefault(true);
					storeClassEntity.setHighest(false);
					storeClassEntity.setLately(false);
					viewInterface.onClicks(1);
				}
			});
			((ClassTitleViewHolder) viewHolder).llHighest.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
//					((ClassTitleViewHolder) viewHolder).tvDefault.setSelected(false);
//					((ClassTitleViewHolder) viewHolder).tvHighest.setSelected(true);
//					((ClassTitleViewHolder) viewHolder).tvLately.setSelected(false);
					storeClassEntity.setDefault(false);
					storeClassEntity.setHighest(true);
					storeClassEntity.setLately(false);
					viewInterface.onClicks(2);
				}
			});
			((ClassTitleViewHolder) viewHolder).llLately.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
//					((ClassTitleViewHolder) viewHolder).tvDefault.setSelected(false);
//					((ClassTitleViewHolder) viewHolder).tvHighest.setSelected(false);
//					((ClassTitleViewHolder) viewHolder).tvLately.setSelected(true);
					storeClassEntity.setDefault(false);
					storeClassEntity.setHighest(false);
					storeClassEntity.setLately(true);
					viewInterface.onClicks(3);
				}
			});


		} else if (viewHolder instanceof StoreItemViewHolder) {

			Log.i(TAG, "onBindViewHolder: " + position);
			pos = position - 2;

			if (!mData.get(pos).getReceipt().equals("") && mData.get(pos).getReceipt() != null) {
				if (mData.get(pos).getReceipt().equals("1")) {
					Log.i(TAG, "Receipt:>>>>>>> " + mData.get(pos).getReceipt());
					if (mData.get(pos).getDeliveryDistanceType().equals("1")) {
						((StoreItemViewHolder) viewHolder).tvStoreStates.setVisibility(View.GONE);
					} else {
						((StoreItemViewHolder) viewHolder).tvStoreStates.setVisibility(View.VISIBLE);
						((StoreItemViewHolder) viewHolder).tvStoreStates.setText("超出配送范围");
					}
				} else {
					((StoreItemViewHolder) viewHolder).tvStoreStates.setVisibility(View.VISIBLE);
					((StoreItemViewHolder) viewHolder).tvStoreStates.setText("休息中");
				}
			}


			if (mData.get(pos).getShopType() == 1) {
				((StoreItemViewHolder) viewHolder).tvTitle_.setVisibility(View.GONE);
			} else {
				((StoreItemViewHolder) viewHolder).tvTitle_.setVisibility(View.VISIBLE);
				((StoreItemViewHolder) viewHolder).tvTitle_.setText(mData.get(pos).getShopTypeName());
			}

			((StoreItemViewHolder) viewHolder).tvTitle.setText(mData.get(pos).getShopName());
			Glide.with(mContext)
					.load(URLBuilder.getUrl( mData.get(pos).getShopImg()))
					.asBitmap()
					.centerCrop()
					.error(R.mipmap.default_goods)
					.into(((StoreItemViewHolder) viewHolder).ivIcon);
			((StoreItemViewHolder) viewHolder).tvSales.setText("月售" + mData.get(pos).getDetailNumMonth() + " 单");
			((StoreItemViewHolder) viewHolder).tvExpenses.setText("起送价: ￥" + mData.get(pos).getServiceStartime());
			((StoreItemViewHolder) viewHolder).tvDistance.setText("距离:" + mData.get(pos).getJuli());

		}
	}


	@Override
	public int getItemCount() {
		return mData.size() + 2;
	}

	public void setData(List<ShopListEntity.DataBean.ShopArrayBean> shopArray) {
		this.mData = shopArray;
		notifyDataSetChanged();
	}

	class GridViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.viewpager)
		ViewPager mViewpager;


		public GridViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

	class ClassTitleViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.store_list_ll_default)
		LinearLayout llDefault;
		@BindView(R.id.store_list_ll_lately)
		LinearLayout llLately;
		@BindView(R.id.store_list_ll_highest)
		LinearLayout llHighest;


		@BindView(R.id.store_list_tv_default)
		TextView tvDefault;
		@BindView(R.id.store_list_tv_lately)
		TextView tvLately;
		@BindView(R.id.store_list_tv_highest)
		TextView tvHighest;


		public ClassTitleViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

	class StoreItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		@BindView(R.id.store_list_tv_title)
		TextView tvTitle;
		@BindView(R.id.store_list_tv_sales)
		TextView tvSales;
		@BindView(R.id.store_list_tv_expenses)
		TextView tvExpenses;
		@BindView(R.id.store_list_tv_wuliu)
		TextView tvWulliu;
		@BindView(R.id.store_list_tv_distance)
		TextView tvDistance;
		@BindView(R.id.store_list_tv_title_)
		TextView tvTitle_;
		@BindView(R.id.item_tv_store_states)
		TextView tvStoreStates;

		@BindView(R.id.store_list_iv)
		ImageView ivIcon;

		private ShopListClickListener mListener;

		public StoreItemViewHolder(View itemView, ShopListClickListener listener) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			this.mListener = listener;
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			if (mListener != null) {
				mListener.onItemClick(view, getPosition());
			}
		}


	}

	public interface ShopListClickListener {
		void onItemClick(View view, int postion);
	}

}
