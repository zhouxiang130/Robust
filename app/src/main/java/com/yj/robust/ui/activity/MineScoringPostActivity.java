package com.yj.robust.ui.activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yj.robust.R;
import com.yj.robust.base.BaseActivity;
import com.yj.robust.base.Key;
import com.yj.robust.base.URLBuilder;
import com.yj.robust.model.NormalEntity;
import com.yj.robust.model.SearchExplainEntity;
import com.yj.robust.util.LogUtils;
import com.yj.robust.util.ToastUtils;
import com.yj.robust.util.Utils;
import com.yj.robust.widget.Dialog.CustomPostDialog;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Suo on 2018/3/28.
 */

public class MineScoringPostActivity extends BaseActivity {
    @BindView(R.id.mine_scoring_post_etmoney)
    EditText etMoney;
    @BindView(R.id.mine_scoring_post_tvmoney)
    TextView tvMoney;
    @BindView(R.id.mine_scoring_post_num)
    TextView tvPostNum;
    @BindView(R.id.text_account_explain)
    TextView tvExplain;
    @BindView(R.id.mine_scoring_post_btnconfirm)
    Button btnConfirm;
    @BindView(R.id.title_ll_iv)
    ImageView ivTitleIcon;
    @BindView(R.id.title_layout)
    LinearLayout lyTitle;
    @BindView(R.id.title_rl_next)
    RelativeLayout reLayout;
    private CustomPostDialog postDialog;
    private String restMoney, upintegral;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_scoring_post;
    }

    @Override
    protected void initView() {
        setTitleInfo();
        transTitle();
        restMoney = getIntent().getStringExtra("money");
        upintegral = getIntent().getStringExtra("upintegral");
        tvMoney.setText("当前总额度" + restMoney + "元");
        etMoney.setHint(restMoney);
    }

    private void setTitleInfo() {
        setTitleText("提升额度");
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
        tvPostNum.setText("（调整以100为单位,消耗积分" + upintegral + "/次）");
        doAsyncGetSearchExplain();

    }

    @OnClick({R.id.mine_scoring_post_btnconfirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_scoring_post_btnconfirm:
                if (TextUtils.isEmpty(etMoney.getText().toString().trim())) {
                    ToastUtils.showToast(MineScoringPostActivity.this, "请输入要申请的额度");
                    return;
                }
                if (Float.parseFloat(etMoney.getText().toString().trim()) < Float.parseFloat(restMoney)) {
                    ToastUtils.showToast(this, "要提升的额度不能小于当前额度");
                    return;
                }
                doAsyncPost(etMoney.getText().toString().trim());
                break;
        }
    }

    private void doAsyncGetSearchExplain() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", mUtils.getUid());
        map.put("type", "2");//@TODO 提现 2.额度提升
        LogUtils.i("传输的值" + URLBuilder.format(map));
        OkHttpUtils.post().url(URLBuilder.URLBaseHeader + "/phone/homePageTwo/searchExplain.act").tag(this)
                .addParams(Key.data, URLBuilder.format(map))
                .build().execute(new Utils.MyResultCallback<SearchExplainEntity>() {

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                if (call.isCanceled()) {
                    call.cancel();
                } else {
                }
            }

            @Override
            public SearchExplainEntity parseNetworkResponse(Response response) throws Exception {
                String json = response.body().string().trim();
                LogUtils.i("json的值" + json);
                return new Gson().fromJson(json, SearchExplainEntity.class);
            }

            @Override
            public void onResponse(SearchExplainEntity response) {
                if (response != null && response.HTTP_OK.equals(response.getCode())) {
                    tvExplain.setText(response.getData().getIntro());
                } else {
                    tvExplain.setVisibility(View.GONE);
                }
            }
        });
    }


    private void doAsyncPost(String money) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", mUtils.getUid());
        map.put("money", money);
        LogUtils.i("传输的值" + URLBuilder.format(map));
        OkHttpUtils.post().url(URLBuilder.URLBaseHeader + "/phone/user/limitMoney.act").tag(this)
                .addParams(Key.data, URLBuilder.format(map))
                .build().execute(new Utils.MyResultCallback<NormalEntity>() {

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                if (call.isCanceled()) {
                    call.cancel();
                } else {
                    ToastUtils.showToast(MineScoringPostActivity.this, "网络故障,请稍后再试");
                }
            }

            @Override
            public NormalEntity parseNetworkResponse(Response response) throws Exception {
                String json = response.body().string().trim();
                LogUtils.i("json的值" + json);
                return new Gson().fromJson(json, NormalEntity.class);
            }

            @Override
            public void onResponse(NormalEntity response) {
                if (response != null && response.HTTP_OK.equals(response.getCode())) {
                    if (postDialog == null) {
                        postDialog = new CustomPostDialog(MineScoringPostActivity.this);
                    }
                    if (!postDialog.isShowing()) {
                        postDialog.show();
                    }
                    btnConfirm.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 600);
                } else {
                    ToastUtils.showToast(MineScoringPostActivity.this, "失败：（" + response.getMsg());
                }
            }
        });
    }

    private void dismissDialog() {
        if (postDialog != null) {
            postDialog.dismiss();
            postDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        dismissDialog();
        OkHttpUtils.getInstance().cancelTag(this);
        super.onDestroy();
    }
}
