package com.yj.robust.ui.activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
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
import com.yj.robust.ui.fragment.MineAccountCostFrag;
import com.yj.robust.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Suo on 2017/7/31.
 */

public class MineAccountCostActivity extends BaseActivity {
    @BindView(R.id.mine_account_cost_tablayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.title_view)
    View vLine;
    @BindView(R.id.mine_account_cost_rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.mine_account_cost_vline)
    View vLine2;
    @BindView(R.id.title_ll_iv)
    ImageView ivTitleIcon;
    @BindView(R.id.title_layout)
    LinearLayout lyTitle;
    @BindView(R.id.title_rl_next)
    RelativeLayout reLayout;
    private List<String> mTitle = new ArrayList<String>();
    private List<Fragment> mFragment = new ArrayList<Fragment>();

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_account_cost;
    }

    @Override
    protected void initView() {
        setTitleInfo();
        transTitle();
        vLine.setVisibility(View.GONE);
        mTitle.add("收益提现");
        mTitle.add("余额抵扣");
        for (int i = 1; i < mTitle.size() + 1; i++) {
            LogUtils.i("我添加了" + i);
            mFragment.add(MineAccountCostFrag.instant(i));
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
        setTitleText("支出明细");
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

    private void showShadow() {
        if (Build.VERSION.SDK_INT >= 21) {
            rlTitle.setElevation(getResources().getDimension(R.dimen.dis2));
            rlTitle.setOutlineProvider(ViewOutlineProvider.BOUNDS);
            vLine2.setVisibility(View.GONE);
        }
    }
}
