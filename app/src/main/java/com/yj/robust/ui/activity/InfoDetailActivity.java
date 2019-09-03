package com.yj.robust.ui.activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yj.robust.R;
import com.yj.robust.base.BaseActivity;
import com.yj.robust.base.URLBuilder;
import com.yj.robust.model.InfoDetailEntity;
import com.yj.robust.util.LogUtils;
import com.yj.robust.util.ToastUtils;
import com.yj.robust.util.Utils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Suo on 2018/3/30.
 */

public class InfoDetailActivity extends BaseActivity {
    @BindView(R.id.info_detial_tv_content)
    TextView tvContent;
    @BindView(R.id.info_detial_tv_date)
    TextView tvDate;
    @BindView(R.id.info_detial_tv_state)
    TextView tvState;
    @BindView(R.id.title_ll_iv)
    ImageView ivTitleIcon;
    @BindView(R.id.title_layout)
    LinearLayout lyTitle;
    @BindView(R.id.title_rl_next)
    RelativeLayout reLayout;
    private String mid;

    @Override
    protected int getContentView() {
        return R.layout.activity_info_detail;
    }

    @Override
    protected void initView() {
        setTitleInfo();
        transTitle();
        mid = getIntent().getStringExtra("mid");
    }

    private void setTitleInfo() {
        setTitleText("消息详情");
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
        doAsyncGetData();
    }

    private void doAsyncGetData() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", mUtils.getUid());
        map.put("mid", mid);
        LogUtils.i("传输的值" + URLBuilder.format(map));
        OkHttpUtils.post().url(URLBuilder.URLBaseHeader + "/phone/user/msgDetail.act")
                .addParams("data", URLBuilder.format(map))
                .tag(this).build().execute(new Utils.MyResultCallback<InfoDetailEntity>() {

            @Override
            public InfoDetailEntity parseNetworkResponse(Response response) throws Exception {
                String json = response.body().string().trim();
                LogUtils.i("json的值" + json);
                return new Gson().fromJson(json, InfoDetailEntity.class);
            }

            @Override
            public void onResponse(InfoDetailEntity response) {
                if (response != null && response.getCode().equals(response.HTTP_OK)) {
                    //返回值为200 说明请求成功
                    setData(response.getData());
                } else {
                    ToastUtils.showToast(InfoDetailActivity.this, "网络故障 " + response.getMsg());
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                LogUtils.i("网络请求失败 获取轮播图错误" + e);
                if (call.isCanceled()) {
                    call.cancel();
                } else {
                    ToastUtils.showToast(InfoDetailActivity.this, "网络故障,请稍后再试 ");
                }

            }
        });
    }

    private void setData(InfoDetailEntity.InfoDetialData data) {
        tvContent.setText(data.getContent());
        tvState.setText(data.getTitle());
        tvDate.setText(data.getTime());
    }

    @Override
    protected void onDestroy() {
        OkHttpUtils.getInstance().cancelTag(this);
        super.onDestroy();
    }
}
