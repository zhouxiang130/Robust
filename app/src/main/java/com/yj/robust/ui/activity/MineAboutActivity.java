package com.yj.robust.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yj.robust.R;
import com.yj.robust.base.BaseActivity;
import com.yj.robust.base.URLBuilder;
import com.yj.robust.util.GetInfoUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Suo on 2018/3/15.
 */

public class MineAboutActivity extends BaseActivity {
    @BindView(R.id.mine_about_tv_version)
    TextView tvVersion;
    @BindView(R.id.title_ll_iv)
    ImageView ivTitleIcon;
    @BindView(R.id.title_layout)
    LinearLayout lyTitle;
    @BindView(R.id.title_rl_next)
    RelativeLayout reLayout;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_about;
    }

    @Override
    protected void initView() {
        setTitleInfo();
        transTitle();
    }


    private void setTitleInfo() {
        setTitleText("关于我们");
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
        tvVersion.setText(GetInfoUtils.getAPPVersion(this));
    }

    @OnClick({R.id.mine_about_rl_company, R.id.mine_about_rl_agreement})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_about_rl_company:
                Intent intentCompany = new Intent(this, NormalWebViewActivity.class);
                intentCompany.putExtra("url", URLBuilder.URLBaseHeader + URLBuilder.CompanyDes);
                intentCompany.putExtra("title", "公司简介");
                startActivity(intentCompany);
                break;
            case R.id.mine_about_rl_agreement:
                Intent intentAgreement = new Intent(this, NormalWebViewActivity.class);
                intentAgreement.putExtra("url", URLBuilder.URLBaseHeader + URLBuilder.Agreement);
                intentAgreement.putExtra("title", "用户协议");
                startActivity(intentAgreement);
                break;
        }
    }
}
