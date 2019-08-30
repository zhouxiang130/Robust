package com.yj.robust.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yj.robust.R;
import com.yj.robust.base.URLBuilder;
import com.yj.robust.model.CartsEntity;
import com.yj.robust.util.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Suo on 2017/6/6.
 */

public class MineRefundListGoodsDetailAdaptersss extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


	private static String TAG = "MineRefundListGoodsDetailAdapterss";
	private int pos = 0;
	private Context mContext;
	List<CartsEntity.DataBean.ProCartsBean.ShopProArrayBean> mList;
	private boolean isShow;//是否显示编辑/完成
	private CheckInterface checkInterface;
	private ModifyCountInterface modifyCountInterface;
	private int position;
	ProfitDetialClickListener mItemClickListener;

	public MineRefundListGoodsDetailAdaptersss(Context context, List<CartsEntity.DataBean.ProCartsBean.ShopProArrayBean> data, int pos) {
		this.mContext = context;
		this.mList = data;
		this.pos = pos;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		CartItemViewHolder cartItemViewHolder = new CartItemViewHolder(
				LayoutInflater.from(mContext).inflate(R.layout.item_cart_list_item, viewGroup, false), mItemClickListener);
		return cartItemViewHolder;
	}

	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
		this.position = position;
		((CartItemViewHolder) holder).cbCheck.setChecked(mList.get(position).isChoosed());
		((CartItemViewHolder) holder).tvTitle.setText(mList.get(position).getProName());
		((CartItemViewHolder) holder).tvStyle.setText(mList.get(position).getSkuPropertiesName());
		((CartItemViewHolder) holder).tvPrice.setText(mList.get(position).getSkuPrice());
		((CartItemViewHolder) holder).tvNum.setText("X" + mList.get(position).getNum());
		((CartItemViewHolder) holder).tvChangeNum.setText(mList.get(position).getNum());
		Glide.with(mContext)
				.load(URLBuilder.getUrl(mList.get(position).getProImg()))
				.asBitmap()
				.centerCrop()
				.error(R.mipmap.default_goods)
				.into(((CartItemViewHolder) holder).iv);

		//选择按钮
		((CartItemViewHolder) holder).cbCheck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mList.get(position).setChoosed(((CheckBox) view).isChecked());
				checkInterface.checkGroup(pos, position, ((CheckBox) view).isChecked());//向外暴露接口
			}
		});
		//增加按钮
		((CartItemViewHolder) holder).tvAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (Integer.parseInt(mList.get(position).getNum()) >= Integer.parseInt(mList.get(position).getSkuNum())) {
					ToastUtils.showToast(mContext, "库存不够啦^_^");
					return;
				}
				modifyCountInterface.doIncrease(pos, position, ((CartItemViewHolder) holder).tvChangeNum, ((CartItemViewHolder) holder).cbCheck.isChecked());//暴露增加接口
			}
		});
		//删减按钮
		((CartItemViewHolder) holder).tvMinus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				modifyCountInterface.doDecrease(pos, position, ((CartItemViewHolder) holder).tvChangeNum, ((CartItemViewHolder) holder).cbCheck.isChecked());//暴露删减接口
			}
		});

		//判断是否在编辑状态下
		if (isShow) {
			//可编辑
			Log.i(TAG, "onBindViewHolder: " + holder);
			((CartItemViewHolder) holder).llManage.setVisibility(View.VISIBLE);
			((CartItemViewHolder) holder).tvNum.setVisibility(View.GONE);
//                ((CartItemViewHolder) holder).rlDelete.setVisibility(View.VISIBLE);
		} else {
			//不可编辑
			Log.i(TAG, "onBindViewHolder: " + holder);

			((CartItemViewHolder) holder).llManage.setVisibility(View.GONE);
			((CartItemViewHolder) holder).tvNum.setVisibility(View.VISIBLE);
//                ((CartItemViewHolder) holder).rlDelete.setVisibility(View.GONE);
		}
	}


	@Override
	public int getItemCount() {
		return mList.size();
	}


	class CartItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		@BindView(R.id.item_cart_cb)
		CheckBox cbCheck;
		@BindView(R.id.item_cart_iv)
		ImageView iv;
		@BindView(R.id.item_cart_state)
		TextView tvState;
		@BindView(R.id.item_cart_title)
		TextView tvTitle;
		@BindView(R.id.item_cart_style)
		TextView tvStyle;
		@BindView(R.id.item_cart_price)
		TextView tvPrice;
		@BindView(R.id.item_cart_num)
		TextView tvNum;

		@BindView(R.id.item_cart_ll)
		LinearLayout llManage;
		@BindView(R.id.item_cart_minus)
		TextView tvMinus;
		@BindView(R.id.item_cart_add)
		TextView tvAdd;
		@BindView(R.id.item_cart_changeNum)
		TextView tvChangeNum;

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


	public int getPosition() {
		return position;
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
	 * 改变商品数量接口
	 *
	 * @param modifyCountInterface
	 */
	public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
		this.modifyCountInterface = modifyCountInterface;
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


	/**
	 * 是否显示可编辑
	 *
	 * @param flag
	 */
	public void isShow(boolean flag) {
		this.isShow = flag;
		notifyDataSetChanged();
	}
}