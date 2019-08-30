package com.yj.robust.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yj.robust.R;
import com.yj.robust.base.URLBuilder;
import com.yj.robust.model.GoodsListEntity;
import com.yj.robust.widget.MaterialRippleView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Suo on 2017/4/17.
 */

public class StoreGoodsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	public static final int SPAN_COUNT_ONE = 1;
	public static final int SPAN_COUNT_TWO = 2;

	private static final int VIEW_TYPE_ONE = 1;
	private static final int VIEW_TYPE_TWO = 2;

	private Context mContext;
	List<GoodsListEntity.GoodsListData.GoodsListItem> mList;
	SpendDetialClickListener mItemClickListener;
	private GridLayoutManager mLayoutManager;


	public StoreGoodsListAdapter(Context mContext, List<GoodsListEntity.GoodsListData.GoodsListItem> mList, GridLayoutManager layoutManager) {
		this.mContext = mContext;
		this.mList = mList;
		mLayoutManager = layoutManager;
	}


	public void setOnItemClickListener(SpendDetialClickListener listener) {
		this.mItemClickListener = listener;
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		CollectionViewHolder collectionViewHolder;
		View inflate;


		if (viewType == VIEW_TYPE_TWO) {
			inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_goods_list, parent, false);
		} else {
			inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_goods_list_, parent, false);
		}


		collectionViewHolder = new CollectionViewHolder(MaterialRippleView.create(inflate), viewType, mItemClickListener);
		return collectionViewHolder;
	}

	@Override
	public int getItemViewType(int position) {

		int spanCount = mLayoutManager.getSpanCount();

		if (spanCount == SPAN_COUNT_ONE) {
			return VIEW_TYPE_TWO;
		} else {
			return VIEW_TYPE_ONE;
		}
	}

	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof CollectionViewHolder) {
			if (getItemViewType(position) == VIEW_TYPE_TWO) {
				setItemData((CollectionViewHolder) holder, position);
			} else {
				setItemData((CollectionViewHolder) holder, position);
			}
		}
	}

	public void setItemData(CollectionViewHolder holder, int position) {
		holder.tvOp.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		holder.tvTitle.setText(mList.get(position).getProName());
		holder.tvPrice.setText(mList.get(position).getProductCurrent());
		holder.tvOp.setText("￥" + mList.get(position).getProductOrginal());
		holder.tvJudge.setText(mList.get(position).getCommentNum() + "条评论");
		holder.tvReturn.setText("￥" + mList.get(position).getConsumerBackmoney());

//		if (mList.get(position).getShopName() != null) {
//			holder.tvStore.setVisibility(View.VISIBLE);
//			holder.tvStore.setText(mList.get(position).getShopName());
//		}else {
//			holder.tvStore.setVisibility(View.GONE);
//
//		}
		if (mList.get(position).getProductHot().equals("2")) {
			holder.tvHot.setVisibility(View.VISIBLE);
		} else {
			holder.tvHot.setVisibility(View.GONE);
		}
		Glide.with(mContext)
				.load(URLBuilder.getUrl(mList.get(position).getProImg()))
				.asBitmap()
				.centerCrop()
				.error(R.mipmap.default_goods)
				.into(holder.iv);
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	class CollectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		@BindView(R.id.goods_list_tv_op)
		TextView tvOp;
		@BindView(R.id.goods_list_tv_title)
		TextView tvTitle;
		@BindView(R.id.goods_list_tv_price)
		TextView tvPrice;
		@BindView(R.id.goods_list_tv_judge)
		TextView tvJudge;
		@BindView(R.id.goods_list_tv_hot)
		TextView tvHot;

//		@BindView(R.id.goods_list_tv_store)
//		TextView tvStore;
		@BindView(R.id.goods_list_iv)
		ImageView iv;
		@BindView(R.id.goods_list_tv_return)
		TextView tvReturn;
		private SpendDetialClickListener mListener;


		public CollectionViewHolder(View itemView, int viewType, SpendDetialClickListener listener) {
			super(itemView);
			if (viewType == VIEW_TYPE_TWO) {
				ButterKnife.bind(this, itemView);
			} else {
				ButterKnife.bind(this, itemView);
			}

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


	public interface SpendDetialClickListener {
		void onItemClick(View view, int postion);
	}
}