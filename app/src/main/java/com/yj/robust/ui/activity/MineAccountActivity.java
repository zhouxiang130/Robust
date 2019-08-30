package com.yj.robust.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yj.robust.R;
import com.yj.robust.base.BaseActivity;
import com.yj.robust.base.Key;
import com.yj.robust.base.URLBuilder;
import com.yj.robust.model.AccountEntity;
import com.yj.robust.util.IntentUtils;
import com.yj.robust.util.LogUtils;
import com.yj.robust.util.ToastUtils;
import com.yj.robust.util.Utils;
import com.yj.robust.widget.Dialog.CustomProgressDialog;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Suo on 2018/3/12.
 */

public class MineAccountActivity extends BaseActivity {

    private static final String TAG = "MineAccountActivity";
    @BindView(R.id.mine_Lifting_tv_money)
    TextView tvMoney;
    @BindView(R.id.mine_account_all_money)
    TextView tvAllMoney;

    CustomProgressDialog mDialog;
    @Override
    protected int getContentView() {
        return R.layout.activity_mine_account;
    }

    @Override
    protected void initView() {
        setTitleText("账户余额");
    }

    @Override
    protected void initData() {
        mDialog = new CustomProgressDialog(this);
        doAsyncGetData();
    }


    @OnClick({R.id.mine_account_profit,R.id.mine_account_cost,R.id.mine_account_withdraw})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.mine_account_profit:
                if(mUtils.isLogin()) {
                    Intent intentProfit = new Intent(this, MineAccountProfitActivity.class);
                    startActivity(intentProfit);
                }else{
                    IntentUtils.IntentToLogin(this);
                }
                break;
            case R.id.mine_account_cost:
                if(mUtils.isLogin()) {
                    Intent intentCost = new Intent(this, MineAccountCostActivity.class);
                    startActivity(intentCost);
                }else{
                    IntentUtils.IntentToLogin(this);
                }
                break;
            case R.id.mine_account_withdraw:
                if(mUtils.isLogin()) {
                    Intent intentWithdraw = new Intent(this, MineAccountWithdrawActivity.class);
                    startActivity(intentWithdraw);
                }else{
                    IntentUtils.IntentToLogin(this);
                }
                break;
        }
    }

    private void doAsyncGetData(){
        Map<String,String> map = new HashMap<>();
        map.put("userId",mUtils.getUid());
        LogUtils.i("传输的值"+ URLBuilder.format(map));
        OkHttpUtils.post().url(URLBuilder.URLBaseHeader+"/phone/user/usermoney.act").tag(this)
                .addParams(Key.data,URLBuilder.format(map))
                .build().execute(new Utils.MyResultCallback<AccountEntity>() {
            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                if (mDialog == null) {
                    mDialog = new CustomProgressDialog(MineAccountActivity.this);
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
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                if (call.isCanceled()) {
                    call.cancel();
                }else{
                    ToastUtils.showToast(MineAccountActivity.this,"网络故障,请稍后再试");
                }
                dismissDialog();

            }
            @Override
            public AccountEntity parseNetworkResponse(Response response) throws Exception {
                String json = response.body().string().trim();
                LogUtils.i("json的值"+json);
                return new Gson().fromJson(json,AccountEntity.class);
            }
            @Override
            public void onResponse(AccountEntity response) {
                if (response != null && response.HTTP_OK.equals(response.getCode())) {
                    setData(response.getData());
                } else {
                    ToastUtils.showToast(MineAccountActivity.this,"故障"+response.getMsg());
                }
                dismissDialog();
            }
        });
    }
    private void setData(AccountEntity.AccountData data){
        Log.i(TAG, "setData: "+ data.getUserMoney());
        tvMoney.setText(data.getUserMoney());
        tvAllMoney.setText("￥"+data.getBackmoney());
    }
    private void dismissDialog(){
        if(mDialog != null){
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
