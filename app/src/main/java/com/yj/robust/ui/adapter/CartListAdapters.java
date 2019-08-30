package com.yj.robust.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yj.robust.R;
import com.yj.robust.base.URLBuilder;
import com.yj.robust.model.CartsEntity;
import com.yj.robust.widget.RoundedImageView.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Suo on 2017/4/17.
 */

public class CartListAdapters extends RecyclerView.Adapter<RecyclerView.ViewHolder>
		implements MineRefundListGoodsDetailAdaptersss.CheckInterface,
		MineRefundListGoodsDetailAdaptersss.ModifyCountInterface {

	private static final String TAG = "CartListAdapter";
	private Context mContext;
	//    List<CartEntity.CartData.CartItem> mList;
	List<CartsEntity.DataBean.ProCartsBean> mList;
	private String limit;
	List<MineRefundListGoodsDetailAdaptersss> mAdapters;
//	private boolean isShow = false;//是否显示编辑/完成


	ProfitDetialClickListener mItemClickListener;
	private CheckInterface checkInterface;
	private CheckInterfaces checkInterfaces;
	private ModifyCountInterface modifyCountInterface;
	private MineRefundListGoodsDetailAdaptersss adapters;
	private int position;
	private boolean isShow;
	private boolean check;
	private int positions;
	private boolean allCheck;


	public CartListAdapters(Context mContext, List<CartsEntity.DataBean.ProCartsBean> mList) {
		this.mContext = mContext;
		this.mList = mList;
		mAdapters = new ArrayList();


	}

	public void setOnItemClickListener(ProfitDetialClickListener listener) {
		this.mItemClickListener = listener;
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		CartHeadViewHolder cartHeadViewHolder;
		CartItemViewHolder cartItemViewHolder;
		if (viewType == 0) {
			cartHeadViewHolder = new CartHeadViewHolder(LayoutInflater
					.from(mContext).inflate(R.layout.item_cart_list_head, parent, false), mItemClickListener);
			return cartHeadViewHolder;
		} else {
			cartItemViewHolder = new CartItemViewHolder(LayoutInflater
					.from(mContext).inflate(R.layout.item_cart_gridview_item, parent, false), mItemClickListener);
			return cartItemViewHolder;
		}
	}



	@Override
	public int getItemViewType(int position) {
		if (TextUtils.isEmpty(limit)) {
			return 1;
		} else {
			if (position == 0) {
				return 0;
			} else {
				return 1;
			}
		}
	}

	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof CartHeadViewHolder) {
			((CartHeadViewHolder) holder).tvLimit.setText("满" + limit + "元免运费");
		} else if (holder instanceof CartItemViewHolder) {
			final int pos;
			if (TextUtils.isEmpty(limit)) {
				pos = position;
			} else {
				pos = position - 1;
			}
			this.position = pos;
			((CartItemViewHolder) holder).tvShopName.setText(mList.get(pos).getShopName());

			Glide.with(mContext)
					.load(URLBuilder.getUrl( mList.get(pos).getShopImg()))
					.asBitmap()
					.centerCrop()
					.error(R.mipmap.default_goods)
					.into(((CartItemViewHolder) holder).ivShopIcon);

			if (positions == pos) {
				((CartItemViewHolder) holder).cbCheck.setChecked(check);
			}


			//选择按钮
			((CartItemViewHolder) holder).cbCheck.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					mList.get(pos).getShopProArray().get(pos).setChoosed(((CheckBox) view).isChecked());
					checkInterfaces.checkGroup(pos, ((CheckBox) view).isChecked());//向外暴露接口
				}
			});


			LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
			((CartItemViewHolder) holder).gridView.setLayoutManager(layoutManager);
			Log.i(TAG, "onBindViewHolder: " + isShow);

			if (!isShow) {
				adapters = new MineRefundListGoodsDetailAdaptersss(mContext, mList.get(pos).getShopProArray(), pos);
				mAdapters.add(adapters);
			}

			((CartItemViewHolder) holder).gridView.setAdapter(adapters);
			adapters.setCheckInterface(this);
			adapters.setModifyCountInterface(this);
		}
	}


	@Override
	public int getItemCount() {
		if (mList != null && mList.size() > 0) {
			if (TextUtils.isEmpty(limit)) {
				return mList.size();
			} else {
				return mList.size() + 1;
			}
		}
		return 0;
	}


	public void setLimit(String limit) {
		this.limit = limit;
	}

	public void setCheck(int pos, boolean check) {
		this.positions = pos;
		this.check = check;
	}

	public void setAllCheck(boolean allCheck) {
		this.allCheck = allCheck;
	}

	public void setNotifyDataSetChanged() {
		for (MineRefundListGoodsDetailAdaptersss adapter : mAdapters) {
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void checkGroup(int pos, int position, boolean isChecked) {
		checkInterface.checkGroup(pos, position, isChecked);//向外暴露接口
	}


	@Override
	public void doIncrease(int pos, int position, View showCountView, boolean isChecked) {
		modifyCountInterface.doIncrease(pos, position, showCountView, isChecked);
	}

	@Override
	public void doDecrease(int pos, int position, View showCountView, boolean isChecked) {
		modifyCountInterface.doDecrease(pos, position, showCountView, isChecked);
	}

	@Override
	public void childDelete(int position) {
		modifyCountInterface.childDelete(position);
	}

	public int getPosition() {
		return position;
	}

	public int getGoodPosition() {
		return adapters.getPosition();
	}


	class CartHeadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		@BindView(R.id.cart_list_head_tv_limit)
		TextView tvLimit;
		@BindView(R.id.cart_list_head_tv_tag)
		TextView tvTag;

		private ProfitDetialClickListener mListener;

		public CartHeadViewHolder(View itemView, ProfitDetialClickListener listener) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			this.mListener = listener;
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (mListener != null) {
				mListener.onItemClick(v, getPosition());
			}
		}
	}

	class CartItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@BindView(R.id.refound_detial_tv_normal_states)
		TextView tvShopName;

		@BindView(R.id.frag_mine_login_iv)
		RoundedImageView ivShopIcon;
		@BindView(R.id.item_cart_cb)
		CheckBox cbCheck;

		@BindView(R.id.gridView)
		RecyclerView gridView;
//		WrapContentGridView gridView;

		private ProfitDetialClickListener mListener;

		public CartItemViewHolder(View itemView, ProfitDetialClickListener listener) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			this.mListener = listener;
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (mListener != null) {
				mListener.onItemClick(v, getPosition());
			}
		}
	}

	public interface ProfitDetialClickListener {
		void onItemClick(View view, int postion);
	}

	/**
	 * 单选接口
	 *
	 * @param checkInterface
	 */
	public void setCheckInterface(CheckInterface checkInterface) {
		this.checkInterface = checkInterface;
	}

	/**
	 * 单选接口
	 *
	 * @param checkInterfaces
	 */
	public void setCheckInterfaces(CheckInterfaces checkInterfaces) {
		this.checkInterfaces = checkInterfaces;
	}

	/**
	 * 改变商品数量接口
	 *
	 * @param modifyCountInterface
	 */
	public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
		this.modifyCountInterface = modifyCountInterface;
	}

	/**
	 * 是否显示可编辑
	 *
	 * @param flag
	 */
	public void isShow(boolean flag) {
		this.isShow = flag;
		for (int i = 0; i < mList.size(); i++) {
			mAdapters.get(i).isShow(flag);
		}
//		notifyDataSetChanged();
	}

	/**
	 * 复选框接口
	 */
	public interface CheckInterface {
		/**
		 * 组选框状态改变触发的事件
		 *
		 * @param position  元素位置
		 * @param isChecked 元素选中与否
		 */
		void checkGroup(int pos, int position, boolean isChecked);
	}

	/**
	 * 复选框接口
	 */
	public interface CheckInterfaces {
		/**
		 * 组选框状态改变触发的事件
		 *
		 * @param pos       元素位置
		 * @param isChecked 元素选中与否
		 */
		void checkGroup(int pos, boolean isChecked);
	}


	/**
	 * 改变数量的接口
	 */
	public interface ModifyCountInterface {
		/**
		 * 增加操作
		 *
		 * @param pos
		 * @param position      组元素位置
		 * @param showCountView 用于展示变化后数量的View
		 * @param isChecked     子元素选中与否
		 */
		void doIncrease(int pos, int position, View showCountView, boolean isChecked);

		/**
		 * 删减操作
		 *
		 * @param pos
		 * @param position      组元素位置
		 * @param showCountView 用于展示变化后数量的View
		 * @param isChecked     子元素选中与否
		 */
		void doDecrease(int pos, int position, View showCountView, boolean isChecked);

		/**
		 * 删除子item
		 *
		 * @param position
		 */
		void childDelete(int position);
	}
}