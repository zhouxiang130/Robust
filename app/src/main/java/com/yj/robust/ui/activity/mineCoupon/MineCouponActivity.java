package com.yj.robust.ui.activity.mineCoupon;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yj.robust.R;
import com.yj.robust.base.BaseActivity;
import com.yj.robust.ui.adapter.MineOrderTabAdapter;
import com.yj.robust.ui.fragment.MineCouponDetail.MineCouponDetailFrag;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/7 0007.
 *
 * @TODO 我的优惠券 2.0版本的添加页面
 */

public class MineCouponActivity extends BaseActivity implements MineCoupon_Contract.View {
	@BindView(R.id.mine_scoring_detial_tablayout)
	TabLayout tabLayout;
	@BindView(R.id.title_view)
	View vLine;
	@BindView(R.id.viewpager)
	ViewPager mViewpager;
	@BindView(R.id.title_ll_iv)
	ImageView ivTitleIcon;
	@BindView(R.id.mine_scoring_detial_rl_title)
	RelativeLayout rlTitle;
	@BindView(R.id.mine_scoring_detial_vline)
	View vLine2;
	@BindView(R.id.title_layout)
	LinearLayout lyTitle;
	@BindView(R.id.title_rl_next)
	RelativeLayout reLayout;

	private List<String> mTitle = new ArrayList<>();
	private List<Fragment> mFragment = new ArrayList<>();

	private MineCoupon_Contract.Presenter couponPresenter = new MineCoupon_Presenter(this);

	@Override
	protected int getContentView() {
		return R.layout.activity_mine_coupon;
	}

	@Override
	protected void initView() {
		setTitleInfo();
		transTitle();
		vLine.setVisibility(View.GONE);
		mTitle.add("优惠券");
		mTitle.add("已使用");
		mTitle.add("已失效");
		for (int i = 0; i < mTitle.size(); i++) {
			mFragment.add(MineCouponDetailFrag.instant(i));
		}
		MineOrderTabAdapter adapter = new MineOrderTabAdapter(getSupportFragmentManager(), mTitle, mFragment);
		mViewpager.setAdapter(adapter);
		//为TabLayout设置ViewPager
		tabLayout.setupWithViewPager(mViewpager);
		//使用ViewPager的适配器
		//忘了这句干啥的了. 如果使用过程中有问题.应该就是这句导致的.
		tabLayout.setTabsFromPagerAdapter(adapter);

	}

	private void setTitleInfo() {
		setTitleText("我的优惠券");
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
		showShadow();
	}


	/**
	 * 为了得到传回的数据，必须在前面的Activity中（指MainActivity类）重写onActivityResult方法
	 * <p>
	 * requestCode 请求码，即调用startActivityForResult()传递过去的值
	 * resultCode 结果码，结果码用于标识返回数据来自哪个新Activity
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		String result = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
//		Log.i(TAG, "onActivityResult>>>>>>>>>>>>>");
		doFragRefresh();
	}


	public void doFragRefresh() {
		MineCouponDetailFrag frag = (MineCouponDetailFrag) mFragment.get(mViewpager.getCurrentItem());
		if (frag != null) {
			frag.doRefresh();
		}
	}

	private void showShadow() {
		if (Build.VERSION.SDK_INT >= 21) {
			rlTitle.setElevation(getResources().getDimension(R.dimen.dis2));
			rlTitle.setOutlineProvider(ViewOutlineProvider.BOUNDS);
			vLine2.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO: add setContentView(...) invocation
		ButterKnife.bind(this);
	}
}
