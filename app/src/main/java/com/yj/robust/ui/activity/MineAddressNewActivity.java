package com.yj.robust.ui.activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.yj.robust.R;
import com.yj.robust.base.BaseActivity;
import com.yj.robust.base.Key;
import com.yj.robust.base.URLBuilder;
import com.yj.robust.model.JsonBean;
import com.yj.robust.model.NormalEntity;
import com.yj.robust.util.GetJsonDataUtil;
import com.yj.robust.util.KeyBoardUtils;
import com.yj.robust.util.LogUtils;
import com.yj.robust.util.MatchUtils;
import com.yj.robust.util.ToastUtils;
import com.yj.robust.util.Utils;
import com.yj.robust.widget.Dialog.CustomProgressDialog;
import com.yj.robust.widget.Dialog.ValidateAddressDialogs;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Suo on 2017/4/28.
 */

public class MineAddressNewActivity extends BaseActivity {
    private static final String TAG = " MineAddressNewActivity ";
    @BindView(R.id.address_new_address)
    TextView tvAddress;
    @BindView(R.id.address_new_cb)
    CheckBox cbCheck;
    /* @BindView(R.id.address_news_save)
     Button btnSave;*/
    @BindView(R.id.address_new_etName)
    EditText etName;
    @BindView(R.id.address_new_tel)
    EditText etTel;
    @BindView(R.id.address_new_etDetial)
    EditText etDetial;
    @BindView(R.id.title_rl_next)
    RelativeLayout rlNext;
    @BindView(R.id.title_tv_next)
    TextView tvNext;
    @BindView(R.id.title_ll_iv)
    ImageView ivTitleIcon;
    @BindView(R.id.title_layout)
    LinearLayout lyTitle;
    private String addressId;

    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private boolean isLoaded = false;

    private CustomProgressDialog loadingDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_address_new;
    }

    @Override
    protected void initView() {
        setTitleInfo();
        transTitle();
//        setEnable();
        loadingDialog = new CustomProgressDialog(this);
        rlNext.setVisibility(View.VISIBLE);
        tvNext.setTextColor(getResources().getColor(R.color.C1E_1E_1E));
        tvNext.setTextSize(13);
        tvNext.setText("保存");
        if (getIntent().getStringExtra("tag").equals("new")) {
            setTitleText("新建收货地址");
        } else {
            setTitleText("修改收货地址");
            addressId = getIntent().getStringExtra("addressId");
            etName.setText(getIntent().getStringExtra("name"));
            etTel.setText(getIntent().getStringExtra("tel"));
            tvAddress.setText(getIntent().getStringExtra("area"));
            etDetial.setText(getIntent().getStringExtra("detial"));
            if (getIntent().getStringExtra("default").equals("1")) {
                cbCheck.setChecked(true);
            } else {
                cbCheck.setChecked(false);
            }
        }

    }

    private void setTitleInfo() {
//      setTitleLeftImg();
        ivTitleIcon.setImageResource(R.drawable.ic_keyboard_arrow_left_white_24dp);
        setTitleColor(getResources().getColor(R.color.white));
        lyTitle.setBackgroundColor(getResources().getColor(R.color.C50_BD_B5));
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
        if (thread == null) {//如果已创建就不再重新创建子线程了
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // 写子线程中的操作,解析省市区数据
                    initJsonData();
                }
            });
            thread.start();
        }
    }

    @OnClick({R.id.address_new_area, R.id.address_new_checkrl/*,R.id.address_news_save*/, R.id.title_rl_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.address_new_area:
                KeyBoardUtils.hintKb(this);
                if (isLoaded) {
                    ShowPickerView();
                }
                break;
            case R.id.address_new_checkrl:
                cbCheck.setChecked(!cbCheck.isChecked());
                break;
            /*case R.id.address_news_save:*/
            case R.id.title_rl_next:
                if (TextUtils.isEmpty(etName.getText().toString().trim())) {
                    ToastUtils.showToast(MineAddressNewActivity.this, "请输入收货人");
                    return;
                }
                if (TextUtils.isEmpty(etTel.getText().toString().trim())) {
                    ToastUtils.showToast(MineAddressNewActivity.this, "请输入收货电话");
                    return;
                }
                if (TextUtils.isEmpty(tvAddress.getText().toString().trim())) {
                    ToastUtils.showToast(MineAddressNewActivity.this, "请选择收货地区");
                    return;
                }
                if (TextUtils.isEmpty(etDetial.getText().toString().trim())) {
                    ToastUtils.showToast(MineAddressNewActivity.this, "请输入收货详细地址");
                    return;
                }


                if (!MatchUtils.isValidPhoneNumber(etTel.getText().toString().trim())) {
                    ToastUtils.showToast(this, "请输入正确的手机号码");
                } else {
                    //地址验证
                    doAsynAddress();
                    //手机号格式正确

//					doAsyncEditAddress();
                }
                break;
        }
    }

    private void doAsynAddress() {
        Map<String, String> map = new HashMap<>();
        map.put("addressArea", tvAddress.getText().toString());
        map.put("addressDetail", etDetial.getText().toString());
        LogUtils.i("地址传输的值" + URLBuilder.format(map));
        OkHttpUtils.post().url(URLBuilder.URLBaseHeader + "/phone/userUp/editAddressValidation.act").tag(this)
                .addParams(Key.data, URLBuilder.format(map))
                .build().execute(new Utils.MyResultCallback<NormalEntity>() {
            @Override
            public void inProgress(float progress) {
                super.inProgress(progress);
                if (loadingDialog == null) {
                    loadingDialog = new CustomProgressDialog(MineAddressNewActivity.this);
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
            public void onError(Call call, Exception e) {
                LogUtils.i("网络故障" + e);
                if (call.isCanceled()) {
                    call.cancel();
                } else {
                    ToastUtils.showToast(MineAddressNewActivity.this, "网络故障,请稍后再试");
                }
                dismissDialog2();

                super.onError(call, e);
            }

            @Override
            public NormalEntity parseNetworkResponse(Response response) throws Exception {
                String json = response.body().string().trim();
                LogUtils.i("editAddressValidation json的值" + json);
                return new Gson().fromJson(json, NormalEntity.class);
            }

            @Override
            public void onResponse(NormalEntity response) {
                dismissDialog2();
                if (response.getCode().equals(response.HTTP_OK)) {

                    if (response.getMsg() != null && !response.getMsg().equals("")) {
                        if ("成功".equals(response.getMsg())) {
                            doAsyncEditAddress();
                        } else {
                            showDeleteDialog(response.getMsg());
                        }
                    } else {

                    }
                } else {
                    ToastUtils.showToast(MineAddressNewActivity.this, response.getCode() + " :) " + response.getMsg());
                }
            }

        });

    }

    ValidateAddressDialogs deleteDialog;

    private void showDeleteDialog(String msg) {

        if (deleteDialog == null) {
            deleteDialog = new ValidateAddressDialogs(MineAddressNewActivity.this);
        }
        if (!deleteDialog.isShowing()) {
            deleteDialog.show();
        }
        deleteDialog.getTvTitle().setText(msg);

        deleteDialog.getTvConfirm().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAsyncEditAddress();
                deleteDialog.dismiss();
            }
        });

        deleteDialog.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
            }
        });
    }


    private void
    doAsyncEditAddress() {
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(addressId)) {
            map.put("addressId", addressId);
        }
        map.put("usersId", mUtils.getUid());
        map.put("receiverName", etName.getText().toString().trim());
        map.put("receiverTel", etTel.getText().toString().trim());
        map.put("addressArea", tvAddress.getText().toString());
        map.put("addressDetail", etDetial.getText().toString());
		/*if(!TextUtils.isEmpty("postCode")){
		    map.put("postCode",etPost.getText().toString().trim());
        }*/
        if (cbCheck.isChecked()) {
            map.put("addressDefault", "1");
        } else {
            map.put("addressDefault", "2");
        }
        LogUtils.i("地址传输的值" + URLBuilder.format(map));
        OkHttpUtils.post().url(URLBuilder.URLBaseHeader + "/phone/user/addUpdateUserAddress.act").tag(this)
                .addParams(Key.data, URLBuilder.format(map))
                .build().execute(new Utils.MyResultCallback<NormalEntity>() {
            @Override
            public void inProgress(float progress) {
                super.inProgress(progress);
                if (loadingDialog == null) {
                    loadingDialog = new CustomProgressDialog(MineAddressNewActivity.this);
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
            public void onError(Call call, Exception e) {
                LogUtils.i("网络故障" + e);
                if (call.isCanceled()) {
                    call.cancel();
                } else {
                    ToastUtils.showToast(MineAddressNewActivity.this, "网络故障,请稍后再试");
                }
                dismissDialog2();

                super.onError(call, e);
            }

            @Override
            public NormalEntity parseNetworkResponse(Response response) throws Exception {
                String json = response.body().string().trim();
                LogUtils.i("addUpdateUserAddress json的值" + json);
                return new Gson().fromJson(json, NormalEntity.class);
            }

            @Override
            public void onResponse(NormalEntity response) {
                if (isFinishing()) {
                    return;
                }
                dismissDialog2();
                if (response.getCode().equals(response.HTTP_OK)) {
                    ToastUtils.showToast(MineAddressNewActivity.this, "地址保存成功");
                    rlNext.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 400);
                } else {
                    ToastUtils.showToast(MineAddressNewActivity.this, response.getCode() + " :) " + response.getMsg());
                }
            }
        });
    }


    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
        isLoaded = true;

    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() + " " +
                        options2Items.get(options1).get(options2) + " " +
                        options3Items.get(options1).get(options2).get(options3);

                tvAddress.setText(tx);
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void dismissDialog() {
        if (deleteDialog != null) {
            deleteDialog.dismiss();
            deleteDialog = null;

        }
    }


    private void dismissDialog2() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        dismissDialog2();
        dismissDialog();
        OkHttpUtils.getInstance().cancelTag(this);
        super.onDestroy();
    }

}
