package com.yj.robust.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.yj.robust.R;
import com.yj.robust.base.BaseActivity;
import com.yj.robust.base.Key;
import com.yj.robust.base.URLBuilder;
import com.yj.robust.model.NormalEntity;
import com.yj.robust.util.LogUtils;
import com.yj.robust.util.SecurityUtil.MD5Utils;
import com.yj.robust.util.ToastUtils;
import com.yj.robust.util.Utils;
import com.yj.robust.widget.Dialog.CustomProgressDialog;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Suo on 2018/3/15.
 */

public class MinePersonalConfirmPwdActivity extends BaseActivity {
    @BindView(R.id.mine_personal_confirm_pwd)
    EditText etPwd;
    @BindView(R.id.title_ll_iv)
    ImageView ivTitleIcon;
    @BindView(R.id.title_layout)
    LinearLayout lyTitle;
    @BindView(R.id.title_rl_next)
    RelativeLayout reLayout;
    CustomProgressDialog loadingDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_personal_confirm;
    }

    @Override
    protected void initView() {
        setTitleInfo();
        transTitle();
    }

    @Override
    protected void initData() {
        loadingDialog = new CustomProgressDialog(this);
    }

    @OnClick({R.id.mine_personal_confirm_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_personal_confirm_btn:
                if (TextUtils.isEmpty(etPwd.getText().toString().trim())) {
                    ToastUtils.showToast(MinePersonalConfirmPwdActivity.this, "请输入登录密码");
                    return;
                }
                doAsyncConfirmPwd(etPwd.getText().toString().trim());
                break;
        }
    }


    private void setTitleInfo() {
        setTitleText("重新绑定");
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


    private void doAsyncConfirmPwd(String pwd) {
        Map<String, String> map = new HashMap<>();
        map.put(Key.userId, mUtils.getUid());
        map.put("userPass", MD5Utils.MD5(pwd));
        LogUtils.i("传输的值===" + URLBuilder.format(map));
        OkHttpUtils.post().url(URLBuilder.URLBaseHeader + "/phone/user/verificationPass.act")
                .addParams(Key.data, URLBuilder.format(map))
                .tag(this).build().execute(new Utils.MyResultCallback<NormalEntity>() {

            @Override
            public void inProgress(float progress) {
                super.inProgress(progress);
                if (loadingDialog == null) {
                    loadingDialog = new CustomProgressDialog(MinePersonalConfirmPwdActivity.this);
                    if (!isFinishing()) {
                        loadingDialog.show();
                    }
                } else {
                    if (!isFinishing()) {
                        loadingDialog.show();
                    }
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
                if (isFinishing()) {
                    return;
                }
                dismissDialog2();
                if (response != null) {
                    if (response.getCode().equals(response.HTTP_OK)) {
                        //返回值为200 说明请求成功
                        etPwd.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MinePersonalConfirmPwdActivity.this, MinePersonalTelActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 400);
                    } else {
                        ToastUtils.showToast(MinePersonalConfirmPwdActivity.this, "验证失败 :)" + response.getMsg());
                    }
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                LogUtils.i("网络请求失败" + e);
                if (call.isCanceled()) {
                    call.cancel();
                } else {
                    ToastUtils.showToast(MinePersonalConfirmPwdActivity.this, "网络故障,请稍后再试");
                }
                dismissDialog2();

            }
        });
    }

    private void dismissDialog2() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog2();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
