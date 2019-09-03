package com.yj.robust.ui.activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yj.robust.R;
import com.yj.robust.base.BaseActivity;
import com.yj.robust.ui.adapter.MineOrderTabAdapter;
import com.yj.robust.ui.fragment.MineCollectFrag.MineCollectFrag;
import com.yj.robust.util.LogUtils;
import com.yj.robust.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Suo on 2018/3/15.
 */

public class MineCollectionActivity extends BaseActivity {
	@BindView(R.id.mine_collect_tablayout)
	TabLayout tabLayout;
	@BindView(R.id.viewpager)
	NoScrollViewPager mViewpager;
	@BindView(R.id.title_view)
	View vLine;
	@BindView(R.id.mine_order_title)
	RelativeLayout rlTitle;
	@BindView(R.id.mine_order_vline)
	View vLine2;
	@BindView(R.id.title_ll_iv)
	ImageView ivTitleIcon;
	@BindView(R.id.title_layout)
	LinearLayout lyTitle;
	@BindView(R.id.title_rl_next)
	RelativeLayout reLayout;
	private List<String> mTitle = new ArrayList<String>();
	private List<Fragment> mFragment = new ArrayList<Fragment>();

//	@BindView(R.id.xrecyclerView)
//	SwipeHorXRecyclerView mRecyclerView;
//	@BindView(R.id.progress_layout)
//	ProgressLayout mProgressLayout;


	@Override
	protected int getContentView() {
		return R.layout.activity_mine_collection;
	}

	@Override
	protected void initView() {
		setTitleInfo();
		transTitle();
		vLine.setVisibility(View.GONE);
		mTitle.add("店铺");
		mTitle.add("商品");

		for (int i = 0; i < mTitle.size(); i++) {
		    /*if(i == 2){
		        i++;
            }*/
			LogUtils.i("我添加了" + i);
			mFragment.add(MineCollectFrag.instant(i+1));
		}
		MineOrderTabAdapter adapter = new MineOrderTabAdapter(getSupportFragmentManager(), mTitle, mFragment);
		mViewpager.setAdapter(adapter);
		//为TabLayout设置ViewPager
		mViewpager.setScroll(false);
		tabLayout.setupWithViewPager(mViewpager);
		//使用ViewPager的适配器
		//忘了这句干啥的了. 如果使用过程中有问题.应该就是这句导致的.
		tabLayout.setTabsFromPagerAdapter(adapter);
	}

	private void setTitleInfo() {
		setTitleText("我的收藏");
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
		mViewpager.setCurrentItem(getIntent().getIntExtra("page", 0));
	}

	private void showShadow() {
		if (Build.VERSION.SDK_INT >= 21) {
			rlTitle.setElevation(getResources().getDimension(R.dimen.dis2));
			rlTitle.setOutlineProvider(ViewOutlineProvider.BOUNDS);
			vLine2.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
