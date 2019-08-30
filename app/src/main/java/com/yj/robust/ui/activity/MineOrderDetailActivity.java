package com.yj.robust.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mm.opensdk.utils.Log;
import com.yj.robust.MyApplication;
import com.yj.robust.R;
import com.yj.robust.base.BaseActivity;
import com.yj.robust.base.Constant;
import com.yj.robust.base.Key;
import com.yj.robust.base.URLBuilder;
import com.yj.robust.base.Variables;
import com.yj.robust.model.AlipayEntity;
import com.yj.robust.model.NormalEntity;
import com.yj.robust.model.OrderDetailEntity;
import com.yj.robust.model.PayResult;
import com.yj.robust.model.WXPayEntity;
import com.yj.robust.ui.activity.storeDetail.StoreDetailActivity;
import com.yj.robust.ui.adapter.MineOrderListGoodsDetailAdapter;
import com.yj.robust.util.IntentUtils;
import com.yj.robust.util.LogUtils;
import com.yj.robust.util.ToastUtils;
import com.yj.robust.util.Utils;
import com.yj.robust.util.payUtil.OrderInfoUtil2_0;
import com.yj.robust.widget.Dialog.CustomNormalContentDialog;
import com.yj.robust.widget.Dialog.CustomProgressDialog;
import com.yj.robust.widget.Dialog.MineOrderPayDialog;
import com.yj.robust.widget.RoundedImageView.RoundedImageView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Suo on 2017/5/9.
 *
 * @TODO 商品订单詳情页面
 */

public class MineOrderDetailActivity extends BaseActivity {
	private static final String TAG = "MineOrderDetailActivity";
	@BindView(R.id.recyclerView_goods)
	RecyclerView recyclerViewGoods;
	//	@BindView(R.id.order_detial_iv_state)
//	ImageView ivState;
	@BindView(R.id.order_detial_tv_state)
	TextView tvState;
	@BindView(R.id.order_detial_tv_state_detial)
	TextView tvStateDetial;
	@BindView(R.id.order_detial_tv_normal_state)
	TextView tvNormalState;
	@BindView(R.id.order_detial_rl_normal)
	RelativeLayout rlNormal;
	@BindView(R.id.order_detial_rl_logistic)
	RelativeLayout rlLogistic;
	@BindView(R.id.order_detial_tel)
	TextView tvTel;
	@BindView(R.id.order_detial_name)
	TextView tvName;
	@BindView(R.id.order_detial_address)
	TextView tvAddress;
	@BindView(R.id.order_detial_rest_)
	TextView tvTicket;
	//	@BindView(R.id.order_detial_goods_num)
//	TextView tvNumber;
//	@BindView(R.id.order_detial_goods_sum)
//	TextView tvGoodsSum;
	@BindView(R.id.order_detial_goods_price)
	TextView tvGoodsPrice;
	@BindView(R.id.order_detial_sendcost)
	TextView tvSendCost;
	@BindView(R.id.order_detial_rest)
	TextView tvRest;
	@BindView(R.id.order_detial_rl_rest)
	RelativeLayout rlRest;
	@BindView(R.id.order_detial_total)
	TextView tvTotal;
	@BindView(R.id.order_detial_num)
	TextView tvOrderNum;
	@BindView(R.id.order_detial_create_time)
	TextView tvTime;
	@BindView(R.id.order_detial_pay_state_)
	TextView tvPayStyle;
	@BindView(R.id.order_detial_payStyle)
	LinearLayout llPayTime;

	@BindView(R.id.order_detial_deliver_goods)
	LinearLayout llDelivesTime;
	@BindView(R.id.order_detial_deliver_goods_time)
	TextView tvDeliverTime;

	@BindView(R.id.order_detial_pay_time)
	TextView tvPaytime;

	@BindView(R.id.mine_order_detial_tv_ftime)
	TextView tvFtime;
	@BindView(R.id.order_detial_tv5)
	TextView tvLogical;

	@BindView(R.id.order_detial_bottom)
	RelativeLayout rlBottom;
	@BindView(R.id.order_detial_rl_rest_)
	RelativeLayout rlRest_;
	@BindView(R.id.order_detial_pay)
	TextView tvRight;
	@BindView(R.id.order_detial_delete)
	TextView tvLeft;
	@BindView(R.id.order_detial_line)
	View vLine;
	@BindView(R.id.frag_mine_shop_iv)
	RoundedImageView shopIv;
	@BindView(R.id.frag_mine_shop_detail)
	RelativeLayout shopDetail;
	@BindView(R.id.frag_mine_shop_name)
	TextView shopName;
	@BindView(R.id.image_store_more)
	ImageView ivMore;


	MineOrderListGoodsDetailAdapter goodsAdapter;
	private CustomProgressDialog mDialog;
	CustomNormalContentDialog deleteDialog;

	private OrderDetailEntity.OrderDetialData data;
	private List<OrderDetailEntity.OrderDetialData.OrderDetialItem> mList;
	private List<String> orderState;
	private String oid, flag;
	private Intent resultIntent;
	private MineOrderPayDialog mineOrderPayDialog;
	private int checkedPosition;
	private TextView btnFinish;
	/* private String type;
	 private String style;
	 private String pid;
	 private String pState;
	 private String detialId;
	 private String img;
	 private ArrayList<String> imgs;
	 private String payStyle;

	 private String orderNum;
   */
	private IWXAPI api;

	private String[] payMode = new String[]{
			"账户余额", "支付宝支付", "微信支付"
	};

	private Integer[] payIcon = new Integer[]{
			R.mipmap.yuezhifu,
			R.mipmap.zhifubaozhifu,
			R.mipmap.weixinzhifu,
	};

	private static final int SDK_PAY_FLAG = 1001;


	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case SDK_PAY_FLAG:
					PayResult payResult = new PayResult((Map<String, String>) msg.obj);
					//同步获取结果
					String resultInfo = payResult.getResult();
					LogUtils.i("我进入支付了=====" + resultInfo);
					String resultStatus = payResult.getResultStatus();
					// 判断resultStatus 为9000则代表支付成功
					if (TextUtils.equals(resultStatus, "9000")) {
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								resultIntent.putExtra("refresh", "refresh");
								Intent intent = new Intent(MineOrderDetailActivity.this, PayResultActivity.class);
								startActivity(intent);
								finish();
							}
						}, 400);
						Toast.makeText(MineOrderDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(MineOrderDetailActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
					}
					break;
			}
		}
	};


	@Override
	protected int getContentView() {
		return R.layout.activity_mine_order_detail;
	}

	@Override
	protected void initView() {
		setTitleText("订单详情");
		mList = new ArrayList<>();
		orderState = new ArrayList<>();
		oid = getIntent().getStringExtra("oid");
		flag = getIntent().getStringExtra("flag");

		recyclerViewGoods.setNestedScrollingEnabled(false);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerViewGoods.setLayoutManager(layoutManager);
		goodsAdapter = new MineOrderListGoodsDetailAdapter(this, mList, oid, flag);
		recyclerViewGoods.setAdapter(goodsAdapter);
		goodsAdapter.setOnItemClickListener(new MineOrderListGoodsDetailAdapter.MineOrderAllInnerClickListener() {
			@Override
			public void onItemClick(View view, int postion) {
				//@TODO  bug添加订单详情页面,单个条目点击事件
				IntentUtils.IntentToGoodsDetial(MineOrderDetailActivity.this, mList.get(postion).getProductId());
			}
		});

		resultIntent = new Intent();
		setResult(Variables.REFRESH_ORDER_LIST, resultIntent);
		api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
		MyApplication.orderListRefresh = false;
	}

	@Override
	protected void initData() {

	}

	@OnClick({R.id.order_detial_rl_logistic, R.id.order_detial_pay, R.id.order_detial_delete})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.order_detial_rl_logistic:
				Intent intent = new Intent(this, MineLogicalDetialActivity.class);
				intent.putExtra("order", data.getOrderId());
				startActivity(intent);
				break;
			case R.id.order_detial_pay:
				if (data.getOrderState().equals("3")) {
					//确认收货
					showConfirmGetDialog();
				} else if (data.getOrderState().equals("4")) {
					//进行评价
					doJudge();
				} else {
					//立即支付
					//@TODO  增加分类支付-------------------------
					showPayDialog(oid, payMode, payIcon, data.getUpOrderMoney());
//					if (!TextUtils.isEmpty(data.getOrderPayStyle())) {}
				}
				break;
			case R.id.order_detial_delete:
				if (data.getOrderState().equals("3") || data.getOrderState().equals("4")) {
					//查看物流
					Intent intentLogistic = new Intent(this, MineLogicalDetialActivity.class);
					intentLogistic.putExtra("order", data.getOrderId());
					startActivity(intentLogistic);
				} else {
					//删除订单+取消订单
					cancelOrder();
				}
				break;
		}
	}


	private void showPayDialog(final String orderId,
	                           String[] payMode,
	                           Integer[] payIcon,
	                           String tvPrice) {

		if (mineOrderPayDialog == null) {
			mineOrderPayDialog = new MineOrderPayDialog(this);
		}
		mineOrderPayDialog.setCustomDialog(payMode, payIcon, tvPrice);
		if (!mineOrderPayDialog.isShowing()) {
			mineOrderPayDialog.show();
		}
		btnFinish = mineOrderPayDialog.getBtnFinish();
		btnFinish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				checkedPosition = mineOrderPayDialog.getCheckedPosition();
				switch (checkedPosition) {
					case 0://账户余额
						payWithAlipay(orderId, "3", btnFinish);
						break;
					case 1://支付宝支付
						payWithAlipay(orderId, "1", btnFinish);
						break;
					case 2://2 微信支付
						doPayWeChat(orderId, "2", btnFinish);
						break;
				}

				Log.i(TAG, "showPayDialog ---- checkedPosition  " + checkedPosition);
				mineOrderPayDialog.dismiss();
			}
		});
		mineOrderPayDialog.setCheckPosition(checkedPosition);
	}


	@Override
	protected void onResume() {
		super.onResume();
		doAsyncGetData();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtils.i("resultCode" + resultCode);
		if (requestCode == Variables.REFRESH_ORDER_LIST && resultCode == Variables.REFRESH_ORDER_LIST) {
			LogUtils.i("我进入resultCode相等了");
			if (data != null && !TextUtils.isEmpty(data.getStringExtra("refresh"))) {
				LogUtils.i("我进入refresh了");
				resultIntent.putExtra("refresh", "refresh");
				tvLeft.setText("删除订单");
				tvRight.setVisibility(View.GONE);
				tvRight.setText("我的评价");
				tvLeft.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						cancelOrder();
					}
				});
				tvRight.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {

					}
				});
			}
		}
	}

	private void doAsyncGetData() {
		Map<String, String> map = new HashMap<>();
		map.put("oid", oid);
		map.put("userId", mUtils.getUid());
		LogUtils.i("changeName传输的值" + URLBuilder.format(map));
		OkHttpUtils.post().url(URLBuilder.URLBaseHeader + "/phone/user/orderDetail.act")
				.addParams(Key.data, URLBuilder.format(map))
				.tag(this).build().execute(new Utils.MyResultCallback<OrderDetailEntity>() {

			@Override
			public void onBefore(Request request) {
				super.onBefore(request);
				if (mDialog == null) {
					mDialog = new CustomProgressDialog(MineOrderDetailActivity.this);
					if (!isFinishing()) {
						mDialog.show();
					}
				} else {
					if (!isFinishing()) {
						mDialog.show();
					}
				}
			}

			@Override
			public OrderDetailEntity parseNetworkResponse(Response response) throws Exception {
				String json = response.body().string().trim();
				LogUtils.i(" OrderDetailEntity ---json的值" + json);
				return new Gson().fromJson(json, OrderDetailEntity.class);
			}

			@Override
			public void onResponse(OrderDetailEntity response) {
				if (isFinishing()) {
					return;
				}
				dismissDialog();
				if (response != null && response.getCode().equals(response.HTTP_OK)) {
					//返回值为200 说明请求成功
					data = response.getData();
					setData(response.getData());
				} else {
					ToastUtils.showToast(MineOrderDetailActivity.this, "请求失败 :)" + response.getMsg());
				}
			}

			@Override
			public void onError(Call call, Exception e) {
				super.onError(call, e);
				dismissDialog();
				if (call.isCanceled()) {
					call.cancel();
				} else {
					ToastUtils.showToast(MineOrderDetailActivity.this, "网络故障,请稍后再试");
				}
				LogUtils.i("网络请求失败" + e);
			}
		});
	}

	private void setData(final OrderDetailEntity.OrderDetialData data) {

		switch (data.getOrderState()) {
			case "1":
				if (data.getProductState().equals("2")) {
					//下架
					tvState.setText("已下架");
					rlNormal.setVisibility(View.VISIBLE);
					rlLogistic.setVisibility(View.GONE);
					tvStateDetial.setText("此商品已下架，去找找别的宝贝吧");
					tvNormalState.setText("此商品已下架，无法支付");
					tvRight.setVisibility(View.GONE);
					tvLeft.setText("删除订单");
				} else if (data.getProductState().equals("3")) {
					//售罄
					tvState.setText("已售罄");
					rlNormal.setVisibility(View.VISIBLE);
					rlLogistic.setVisibility(View.GONE);
					tvStateDetial.setText("此商品已售罄，看来咱们来晚咯");
					tvNormalState.setText("此商品已售罄，无法支付");
					tvRight.setVisibility(View.GONE);
					tvLeft.setText("删除订单");
				} else {
					tvState.setText("待付款");
					rlNormal.setVisibility(View.VISIBLE);
					rlLogistic.setVisibility(View.GONE);
					tvStateDetial.setText("请尽快进行付款哦");
					tvNormalState.setText("订单还没有完成支付，快去结算吧");
//					ivState.setImageResource(R.mipmap.news_fill_z);
				}
				break;
			case "2":
				tvState.setText("待发货");
				rlNormal.setVisibility(View.VISIBLE);
				rlLogistic.setVisibility(View.GONE);
				tvStateDetial.setText("我们会尽快为您配货，请耐心等候");
				tvNormalState.setText("我们会尽快为您发货");
//				ivState.setImageResource(R.mipmap.present_fill_z);
				rlBottom.setVisibility(View.GONE);
				vLine.setVisibility(View.GONE);
				break;
			case "3":
				tvState.setText("待收货");
				rlNormal.setVisibility(View.GONE);
				rlLogistic.setVisibility(View.VISIBLE);
				tvStateDetial.setText("商品正在加紧运输中，请耐心等候");
//				ivState.setImageResource(R.mipmap.deliver_fill_z);
				tvRight.setText("确认收货");
				tvLeft.setText("查看物流");
				if (!TextUtils.isEmpty(data.getContent())) {
					tvLogical.setText(data.getContent());
					tvFtime.setText(data.getTime());
					tvFtime.setVisibility(View.VISIBLE);
				} else {
					tvFtime.setVisibility(View.GONE);
				}
				break;
			case "4":
				tvState.setText("待评价");
				rlNormal.setVisibility(View.VISIBLE);
				rlLogistic.setVisibility(View.GONE);
				tvStateDetial.setText("如果觉着东西不错，给个好评吧");
				tvNormalState.setText("请对您购买的商品进行评价");
//				ivState.setImageResource(R.mipmap.mark_fill_z);
				tvRight.setText("进行评价");
				tvLeft.setText("查看物流");
				break;
			case "5":
				tvState.setText("已完成");
				rlNormal.setVisibility(View.GONE);
				rlLogistic.setVisibility(View.GONE);
				tvStateDetial.setText("您的订单已完成");
				tvRight.setVisibility(View.GONE);
				tvLeft.setText("删除订单");
				break;
			case "6":
				tvState.setText("已失效");
				rlNormal.setVisibility(View.GONE);
				rlLogistic.setVisibility(View.GONE);
				tvStateDetial.setText("您的订单已失效");
				rlBottom.setVisibility(View.GONE);
				vLine.setVisibility(View.GONE);
				break;

			case "7":
				tvState.setText("退款中");
				rlNormal.setVisibility(View.GONE);
				rlLogistic.setVisibility(View.GONE);
				tvStateDetial.setText("您的订单正在退款中");
				rlBottom.setVisibility(View.GONE);
				vLine.setVisibility(View.GONE);
				break;

			case "8":
				tvState.setText("已退款");
				rlNormal.setVisibility(View.GONE);
				rlLogistic.setVisibility(View.GONE);
				tvStateDetial.setText("订单已退款");
				rlBottom.setVisibility(View.GONE);
				vLine.setVisibility(View.GONE);
				break;

			case "9":
				tvState.setText("已驳回");
				rlNormal.setVisibility(View.GONE);
				rlLogistic.setVisibility(View.GONE);
				tvStateDetial.setText("您的订单被驳回");
				rlBottom.setVisibility(View.GONE);
				vLine.setVisibility(View.GONE);
				break;
		}

		tvOrderNum.setText(data.getOrderNum());
		tvTime.setText(data.getTime());
		if (data.getOrderPayStyle().equals("1")) {
			//1支付宝 2微信
			tvPayStyle.setText("支付宝");
		} else if (data.getOrderPayStyle().equals("2")) {
			tvPayStyle.setText("微信");
		} else {
			tvPayStyle.setText("余额支付");
		}
		tvName.setText(data.getReceiverName());
		tvTel.setText(data.getReceiverTel());
		tvAddress.setText(data.getAddress());
		tvTotal.setText("￥" + data.getOrderPaymoney());

		if (!data.getCouponMoney().equals("") && !TextUtils.isEmpty(data.getCouponMoney()) && !data.getCouponMoney().equals("0")) {
			rlRest_.setVisibility(View.VISIBLE);
			tvTicket.setText("-￥" + data.getCouponMoney());
		} else {
			rlRest_.setVisibility(View.GONE);
		}

		if (data.getPayTime() != null && !data.getPayTime().equals("")) {
			llPayTime.setVisibility(View.VISIBLE);
			tvPaytime.setText(data.getPayTime());
		} else {
			llPayTime.setVisibility(View.GONE);
		}


		if (data.getSendTime() != null && !data.getSendTime().equals("")) {
			llDelivesTime.setVisibility(View.VISIBLE);
			tvDeliverTime.setText(data.getSendTime());
		} else {
			llDelivesTime.setVisibility(View.GONE);
		}

		//@TODO  -------------------------
		Glide.with(MineOrderDetailActivity.this)
				.load(URLBuilder.getUrl(data.getShopImg()))
				.error(R.mipmap.default_goods)
				.centerCrop()
				.into(shopIv);

		shopName.setText(data.getShopName());

		if (data.getShopId() != null) {
			ivMore.setVisibility(View.VISIBLE);
			shopDetail.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(MineOrderDetailActivity.this, StoreDetailActivity.class);
					intent.putExtra("shopId", data.getShopId());
					startActivity(intent);
				}
			});

		} else {
			ivMore.setVisibility(View.GONE);
		}


//	orderPaymoney 实付金额       proMoneyAll  商品应付金额

//		tvNumber.setText("共" + data.getProNumber() + "件商品");
//		tvGoodsSum.setText(data.getOrderMoney());

		tvGoodsPrice.setText("￥" + data.getProMoneyAll());

		if (TextUtils.isEmpty(data.getOrderSendCosts()) || data.getOrderSendCosts().equals("0") || data.getOrderSendCosts().equals("免运费")) {
			tvSendCost.setText("免运费");
		} else {
			tvSendCost.setText("+￥" + data.getOrderSendCosts());
		}
		if (TextUtils.isEmpty(data.getOrderBalance()) || data.getOrderBalance().equals("0")) {
			rlRest.setVisibility(View.GONE);
		} else {
			rlRest.setVisibility(View.VISIBLE);
			tvRest.setText("-￥" + data.getOrderBalance());
		}

		mList.clear();
		mList.addAll(data.getItems());
		goodsAdapter.setOrderState(data.getOrderState());
		goodsAdapter.setRefundType(data.getRefundType());
		//getOrderMoney 总价  getOrderSendCosts 运费  //getOrderBalance 余额

		android.util.Log.i(TAG, "setData:>>>>>>>>>>>> "
				+ "OrderPaymoney : " + data.getOrderPaymoney()
				+ " OrderSendCosts ： " + data.getOrderSendCosts()
				+ " OrderBalance : " + data.getOrderBalance());

		goodsAdapter.setOrderRefundPic(data.getOrderPaymoney(), data.getOrderSendCosts(), data.getOrderBalance());
		goodsAdapter.notifyDataSetChanged();
	}

	private void cancelOrder() {
		if (deleteDialog == null) {
			deleteDialog = new CustomNormalContentDialog(this);
		}
		if (!deleteDialog.isShowing()) {
			deleteDialog.show();
		}
		deleteDialog.getTvTitle().setText("确认删除该订单吗?");
		deleteDialog.getTvContent().setText("确定取消订单，取消后可在票务里重新下单");
		deleteDialog.getTvConfirm().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doDeleteOrder();
				deleteDialog.dismiss();
			}
		});
	}

	private void showConfirmGetDialog() {
		if (deleteDialog == null) {
			deleteDialog = new CustomNormalContentDialog(this);
		}
		if (!deleteDialog.isShowing()) {
			deleteDialog.show();
		}
		deleteDialog.getTvTitle().setText("确认收货吗?");
		deleteDialog.getTvContent().setText("请确保您已签收");
		deleteDialog.getTvConfirm().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doConfirmGetGoods();
				deleteDialog.dismiss();
			}
		});
	}

	private void doJudge() {
		Intent intent = new Intent(this, PostJudgeGoodsActivity.class);
		intent.putExtra("oid", oid);
		startActivityForResult(intent, Variables.REFRESH_ORDER_LIST);

	}


	private void doDeleteOrder() {
		//订单和订单列表删除订单传oid type传1
		Map<String, String> map = new HashMap<>();
		map.put("orderNum", oid);
		map.put("type", "1");
		map.put("userId", mUtils.getUid());
		LogUtils.i("传输的值" + URLBuilder.format(map));
		OkHttpUtils.post().url(URLBuilder.URLBaseHeader + "/phone/user/cancleOrder.act")
				.addParams("data", URLBuilder.format(map))
				.tag(this).build().execute(new Utils.MyResultCallback<NormalEntity>() {
			@Override
			public NormalEntity parseNetworkResponse(Response response) throws Exception {
				String json = response.body().string().trim();
				LogUtils.i("json的值" + json);
				return new Gson().fromJson(json, NormalEntity.class);
			}

			@Override
			public void onResponse(NormalEntity response) {
				if (response != null && response.getCode().equals(response.HTTP_OK)) {
					//返回值为200 说明请求成功
					ToastUtils.showToast(MineOrderDetailActivity.this, "删除订单成功");
					resultIntent.putExtra("refresh", "refresh");
					tvLeft.postDelayed(new Runnable() {
						@Override
						public void run() {
							finish();
						}
					}, 400);
				} else {
					ToastUtils.showToast(MineOrderDetailActivity.this, response.getCode() + " :)" + response.getMsg());
				}
			}

			@Override
			public void onError(Call call, Exception e) {
				super.onError(call, e);
				if (call.isCanceled()) {
					call.cancel();
				} else {
					ToastUtils.showToast(MineOrderDetailActivity.this, "网络故障,请稍后再试");
				}
//                disMissDialog();

			}
		});
	}

	private void doConfirmGetGoods() {
		Map<String, String> map = new HashMap<>();
		map.put("oid", oid);
		map.put("userId", mUtils.getUid());
		LogUtils.i("传输的值" + URLBuilder.format(map));
		OkHttpUtils.post().url(URLBuilder.URLBaseHeader + "/phone/user/confrimOrders.act")
				.addParams("data", URLBuilder.format(map))
				.tag(this).build().execute(new Utils.MyResultCallback<NormalEntity>() {
			@Override
			public NormalEntity parseNetworkResponse(Response response) throws Exception {
				String json = response.body().string().trim();
				LogUtils.i("json的值" + json);
				return new Gson().fromJson(json, NormalEntity.class);
			}

			@Override
			public void onResponse(NormalEntity response) {
				if (response != null && response.getCode().equals(response.HTTP_OK)) {
					//返回值为200 说明请求成功
					ToastUtils.showToast(MineOrderDetailActivity.this, "确认收货成功");
					tvRight.setText("评价");
					tvRight.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							doJudge();
						}
					});
				} else {
					ToastUtils.showToast(MineOrderDetailActivity.this, response.getCode() + " :)" + response.getMsg());
				}
			}

			@Override
			public void onError(Call call, Exception e) {
				super.onError(call, e);
				if (call.isCanceled()) {
					call.cancel();
				} else {
					ToastUtils.showToast(MineOrderDetailActivity.this, "网络故障,请稍后再试");
				}
//                disMissDialog();

			}
		});
	}


	private void doPayWeChat(final String oid, final String type, final TextView btnFinish) {
		tvRight.setEnabled(false);
		btnFinish.setEnabled(false);
		Map<String, String> map = new HashMap<>();
		map.put("oid", oid);
		map.put("pay", type);
		LogUtils.i("传输的值" + URLBuilder.format(map));
		OkHttpUtils.post().url(URLBuilder.URLBaseHeader + "/phone/homePage/alipayPay.act")
				.addParams("data", URLBuilder.format(map))
				.tag(this).build().execute(new Utils.MyResultCallback<WXPayEntity>() {

			@Override
			public void inProgress(float progress) {
				super.inProgress(progress);
				if (mDialog == null) {
					mDialog = new CustomProgressDialog(MineOrderDetailActivity.this);
					if (!isFinishing()) {
						mDialog.show();
					}
				} else {
					if (!isFinishing()) {
						mDialog.show();
					}
				}
			}

			@Override
			public WXPayEntity parseNetworkResponse(Response response) throws Exception {
				String json = response.body().string().trim();
				LogUtils.i("json的值" + json);
				return new Gson().fromJson(json, WXPayEntity.class);
			}

			@Override
			public void onResponse(WXPayEntity response) {
				disMissDialogs();
				if (response != null && response.getCode().equals("200")) {
					if (response.getData() != null && !TextUtils.isEmpty(response.getData().getAppPayJson().getAppid())) {
						PayReq req = new PayReq();
						//req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
						req.appId = response.getData().getAppPayJson().getAppid();
						req.partnerId = response.getData().getAppPayJson().getPartnerid();
						req.prepayId = response.getData().getAppPayJson().getPrepayid();
						req.nonceStr = response.getData().getAppPayJson().getNoncestr();
						req.timeStamp = response.getData().getAppPayJson().getTimestamp();
						req.packageValue = "Sign=WXPay";
						req.sign = response.getData().getAppPayJson().getSign();
						req.extData = "app data"; // optional
						api.sendReq(req);
						mUtils.savePayOrder(oid);
						mUtils.savePayType("goods");
						resultIntent.putExtra("refresh", "refresh");
						MyApplication.orderDetial = false;
					} else {
						//请求成功 但是没返回微信数据 很有可能是余额的问题
						ToastUtils.showToast(MineOrderDetailActivity.this, "无法获取支付信息");
					}
				} else {
					ToastUtils.showToast(MineOrderDetailActivity.this, response.getCode() + ":)" + response.getMsg());
				}
				tvRight.setEnabled(true);
				btnFinish.setEnabled(false);
			}

			@Override
			public void onError(Call call, Exception e) {
				super.onError(call, e);
				disMissDialogs();
				tvRight.setEnabled(true);
				btnFinish.setEnabled(false);
				if (call.isCanceled()) {
					call.cancel();
				} else {
					ToastUtils.showToast(MineOrderDetailActivity.this, "网络故障,请稍后再试");
				}
//                disMissDialog();

			}
		});
	}


	private void payWithAlipay(final String oid, final String type, final TextView btnFinish) {
		tvRight.setEnabled(false);
		btnFinish.setEnabled(false);
		Map<String, String> map = new HashMap<>();
		map.put("oid", oid);
		map.put("pay", type);
		LogUtils.i("传输的值" + URLBuilder.format(map));
		OkHttpUtils.post().url(URLBuilder.URLBaseHeader + "/phone/homePage/alipayPay.act")
				.addParams("data", URLBuilder.format(map))
				.tag(this).build().execute(new Utils.MyResultCallback<AlipayEntity>() {


			@Override
			public void inProgress(float progress) {
				super.inProgress(progress);
				if (mDialog == null) {
					mDialog = new CustomProgressDialog(MineOrderDetailActivity.this);
					if (!isFinishing()) {
						mDialog.show();
					}
				} else {
					if (!isFinishing()) {
						mDialog.show();
					}
				}
			}


			@Override
			public AlipayEntity parseNetworkResponse(Response response) throws Exception {
				String json = response.body().string().trim();
				LogUtils.i("json的值" + json);
				NormalEntity normalEntity = new Gson().fromJson(json, NormalEntity.class);
				if (normalEntity.getData().equals("") && !normalEntity.getCode().equals("200")) {
					return new AlipayEntity(normalEntity.getCode(), normalEntity.getMsg());
				} else {
					return new Gson().fromJson(json, AlipayEntity.class);
				}
			}

			@Override
			public void onResponse(AlipayEntity response) {
				disMissDialogs();
				if (response != null && response.getCode().equals(response.HTTP_OK)) {
					if (response.getData() != null && response.getData().getAppPayJson() != null && !TextUtils.isEmpty(response.getData().getAppPayJson().getAPPID())) {
						mUtils.savePayOrder(oid);
						mUtils.savePayType("goods");
						alipay(response.getData().getAppPayJson());
					} else {
						if (response.getData() != null && response.getData().getCode().equals(response.HTTP_OK)) {
							ToastUtils.showToast(MineOrderDetailActivity.this, "支付成功");
							Intent intent = new Intent(MineOrderDetailActivity.this, PayResultActivity.class);
							mUtils.savePayOrder(oid);
							mUtils.savePayType("goods");
							startActivity(intent);
						} else {
							ToastUtils.showToast(MineOrderDetailActivity.this, "无法获取支付信息，请稍后再试");
						}
					}
				} else {
					ToastUtils.showToast(MineOrderDetailActivity.this, response.getCode() + " :)" + response.getMsg());
				}
				tvRight.setEnabled(true);
				btnFinish.setEnabled(false);
			}

			@Override
			public void onError(Call call, Exception e) {
				super.onError(call, e);
				disMissDialogs();
				tvRight.setEnabled(true);
				btnFinish.setEnabled(false);
				if (call.isCanceled()) {
					call.cancel();
				} else {
					ToastUtils.showToast(MineOrderDetailActivity.this, "网络故障,请稍后再试 ");
				}
				LogUtils.i("网络请求失败 获取轮播图错误" + e);

			}
		});
	}

	private void alipay(final AlipayEntity.AlipayData.AlipayJson data) {
		//秘钥验证的类型 true:RSA2 false:RSA
		boolean rsa2 = true;

		//构造支付订单参数列表
		Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(data.getAPPID(), rsa2, data);
		LogUtils.i("params的值" + params);
		//构造支付订单参数信息
		String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
		LogUtils.i("orderParam的值" + orderParam);
		//对支付参数信息进行签名
		LogUtils.i("RSA_PRIVATE_KEY的值" + data.getRSA_PRIVATE_KEY());
		String sign = OrderInfoUtil2_0.getSign(params, data.getRSA_PRIVATE_KEY(), rsa2);
		LogUtils.i("sing的值" + sign);
		//订单信息
		final String orderInfo = orderParam + "&" + sign;
		LogUtils.i("orderInfo的值" + orderInfo);
		//异步处理
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				//新建任务
				PayTask alipay = new PayTask(MineOrderDetailActivity.this);
				//获取支付结果
				Map<String, String> result = alipay.payV2(orderInfo, true);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		dismissDialog();
		disMissDialogs();
		OkHttpUtils.getInstance().cancelTag(this);
		if (MyApplication.orderListRefresh) {
			resultIntent.putExtra("refresh", "refresh");
		}
	}


	private void disMissDialogs() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}

	private void dismissDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}

}
