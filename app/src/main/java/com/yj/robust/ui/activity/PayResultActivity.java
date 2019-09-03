package com.yj.robust.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yj.robust.R;
import com.yj.robust.base.BaseActivity;
import com.yj.robust.ui.MainActivity;
import com.yj.robust.widget.Dialog.CustomProgressDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Suo on 2017/8/7.
 */

public class PayResultActivity extends BaseActivity {
    @BindView(R.id.pay_result_iv_state)
    ImageView ivState;
    @BindView(R.id.pay_result_tv_state)
    TextView tvState;
    @BindView(R.id.pay_result_tv1)
    TextView tv1;
    @BindView(R.id.pay_result_btn_order)
    Button btnOrder;
    @BindView(R.id.pay_result_btn_shopping)
    Button btnShopping;
    @BindView(R.id.title_ll_iv)
    ImageView ivTitleIcon;
    @BindView(R.id.title_layout)
    LinearLayout lyTitle;
    @BindView(R.id.title_rl_next)
    RelativeLayout reLayout;
    private String oid;
    CustomProgressDialog mDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_pay_result;
    }

    @Override
    protected void initView() {

        setTitleInfo();
        transTitle();
        oid = mUtils.getPayOrder();
    }

    private void setTitleInfo() {
        setTitleText("支付成功");
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

    @OnClick({R.id.pay_result_btn_order, R.id.pay_result_btn_shopping})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_result_btn_order:
                Intent intent;
                if (!TextUtils.isEmpty(mUtils.getPayType()) && mUtils.getPayType().equals("goods")) {
                    intent = new Intent(this, MineOrderDetailActivity.class);
                    intent.putExtra("oid", oid);
                } else {
                    intent = new Intent(this, MineOrderActivity.class);
                }
                startActivity(intent);
                finish();
                break;
            case R.id.pay_result_btn_shopping:
                Intent intentShop = new Intent(this, MainActivity.class);
                intentShop.putExtra("page", "0");
                startActivity(intentShop);
                finish();
                break;
        }
    }

    private void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }
}
