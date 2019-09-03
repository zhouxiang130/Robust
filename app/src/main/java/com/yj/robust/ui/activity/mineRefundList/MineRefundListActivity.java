package com.yj.robust.ui.activity.mineRefundList;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yj.robust.R;
import com.yj.robust.base.BaseActivity;
import com.yj.robust.base.URLBuilder;
import com.yj.robust.model.MineRefundListEntity;
import com.yj.robust.ui.adapter.MineRefundListAdapter;
import com.yj.robust.util.LogUtils;
import com.yj.robust.util.ToastUtils;
import com.yj.robust.util.Utils;
import com.yj.robust.widget.ProgressLayout;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/6/14 0014.
 *
 * @TODO 退款/售后 2.0添加界面
 */

public class MineRefundListActivity extends BaseActivity {
	@BindView(R.id.xrecyclerView)
	XRecyclerView mRecyclerView;
	@BindView(R.id.progress_layout)
	ProgressLayout mProgressLayout;
	@BindView(R.id.title_ll_iv)
	ImageView ivTitleIcon;
	@BindView(R.id.title_layout)
	LinearLayout lyTitle;
	@BindView(R.id.title_rl_next)
	RelativeLayout reLayout;
	List<MineRefundListEntity.DataBean.ListBean> mList;
	MineRefundListAdapter mAdapter;
	private int page = 1;

	@Override
	protected int getContentView() {
		return R.layout.activity_mine_refund_list;
	}

	@Override
	protected void initView() {
		setTitleInfo();
		transTitle();
		mList = new ArrayList<>();
		LinearLayoutManager layoutManager = new LinearLayoutManager(MineRefundListActivity.this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(layoutManager);
		mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);
		mAdapter = new MineRefundListAdapter(MineRefundListActivity.this, mList, mUtils);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				page = 1;
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						doRefreshData();
					}
				}, 500);
			}

			@Override
			public void onLoadMore() {
				page++;
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						loadMoreData();
						mRecyclerView.setPullRefreshEnabled(false);
					}
				}, 500);
			}
		});
		mRecyclerView.refresh();
	}

	private void setTitleInfo() {
		setTitleText("退款/售后");
//      setTitleLeftImg();
		ivTitleIcon.setImageResource(R.drawable.ic_keyboard_arrow_left_white_24dp);
		setTitleColor(getResources().getColor(R.color.white));
		lyTitle.setBackgroundColor(getResources().getColor(R.color.C50_BD_B5));
		reLayout.setVisibility(View.VISIBLE);
	}

	@TargetApi(21)
	private void transTitle() {
		if (Build.VERSION.SDK_INT >= 21) {
			View decorView = getWindow().getDecorView();
			int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
			decorView.setSystemUiVisibility(option);
			getWindow().setStatusBarColor(Color.TRANSPARENT);
		}
	}

	@Override
	protected void initData() {
	}

	private void doRefreshData() {
		mProgressLayout.showContent();
		Map<String, String> map = new HashMap<>();
		map.put("userId", mUtils.getUid());
		map.put("page", page + "");
		LogUtils.i("传输的值" + URLBuilder.format(map));
		OkHttpUtils.post().url(URLBuilder.URLBaseHeader + "/phone/userUp/userReturnList.act")
				.addParams("data", URLBuilder.format(map))
				.tag(this).build().execute(new Utils.MyResultCallback<MineRefundListEntity>() {

			@Override
			public MineRefundListEntity parseNetworkResponse(Response response) throws Exception {
				String json = response.body().string().trim();
				LogUtils.i("userReturnList json的值" + json);
				return new Gson().fromJson(json, MineRefundListEntity.class);
			}

			@Override
			public void onResponse(MineRefundListEntity response) {
//                dismissDialog();
				if (response != null && response.getCode().equals(response.HTTP_OK)) {
					//返回值为200 说明请求成功
					mList.clear();
					if (response.getData() != null) {
						if (response.getData().getList() != null) {
							if (response.getData().getList().size() != 0) {
								mList.addAll(response.getData().getList());
								mAdapter.notifyDataSetChanged();
								mProgressLayout.showContent();
							} else if (response.getData().getList().size() == 0) {
								mProgressLayout.showNone(new View.OnClickListener() {
									@Override
									public void onClick(View view) {
									}
								});
							}
						} else {
							mProgressLayout.showNone(new View.OnClickListener() {
								@Override
								public void onClick(View view) {
								}
							});
						}
					}

				} else {
					ToastUtils.showToast(MineRefundListActivity.this, "请求失败 :)" + response.getMsg());
					mProgressLayout.showNetError(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							if (mList != null && !mList.isEmpty()) {
								mList.clear();
								mAdapter.notifyDataSetChanged();
							}
							mRecyclerView.refresh();
						}
					});
				}
				mRecyclerView.refreshComplete();
			}

			@Override
			public void onError(Call call, Exception e) {
				super.onError(call, e);
				if (call.isCanceled()) {
					call.cancel();
				} else {
					mProgressLayout.showNetError(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							if (mList != null && !mList.isEmpty()) {
								mList.clear();
								mAdapter.notifyDataSetChanged();
							}
							mRecyclerView.refresh();
						}
					});
				}
				mRecyclerView.refreshComplete();
				LogUtils.i("故障" + e);
			}
		});
	}

	private void loadMoreData() {
		Map<String, String> map = new HashMap<>();
		map.put("userId", mUtils.getUid());
		map.put("page", page + "");
		LogUtils.i("传输的值" + URLBuilder.format(map));
		OkHttpUtils.post().url(URLBuilder.URLBaseHeader + "/phone/userUp/userReturnList.act")
				.addParams("data", URLBuilder.format(map))
				.tag(this).build().execute(new Utils.MyResultCallback<MineRefundListEntity>() {


			@Override
			public MineRefundListEntity parseNetworkResponse(Response response) throws Exception {
				String json = response.body().string().trim();
				LogUtils.i("userReturnList json的值" + json);
				return new Gson().fromJson(json, MineRefundListEntity.class);
			}

			@Override
			public void onResponse(MineRefundListEntity response) {
				if (response != null && response.getCode().equals(response.HTTP_OK)) {
					if (response.getData() != null) {
						if (response.getData().getList().size() != 0) {
							mList.addAll(response.getData().getList());
							mAdapter.notifyDataSetChanged();
							mRecyclerView.loadMoreComplete();
						} else if (response.getData().getList().size() == 0) {
							mRecyclerView.setNoMore(true);
						}
					}else {
						mProgressLayout.showNone(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
							}
						});
					}
					mProgressLayout.showContent();
				} else {
					ToastUtils.showToast(MineRefundListActivity.this, "异常 :)" + response.getMsg());
					mRecyclerView.loadMoreComplete();
				}
				mRecyclerView.setPullRefreshEnabled(true);
			}

			@Override
			public void onError(Call call, Exception e) {
				super.onError(call, e);
				mRecyclerView.loadMoreComplete();
				mRecyclerView.setPullRefreshEnabled(true);
				if (call.isCanceled()) {
					LogUtils.i("我进入到加载更多cancel了");
					call.cancel();
				}

//				else if (pageNum != 1) {
//					LogUtils.i("加载更多的Log");
//					ToastUtils.showToast(MineRefundListActivity.this, "网络故障,请稍后再试");
//					pageNum--;
//				}
			}
		});
	}

}
