package com.yj.robust.ui.activity;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.yj.robust.R;
import com.yj.robust.base.BaseActivity;
import com.yj.robust.base.Key;
import com.yj.robust.base.URLBuilder;
import com.yj.robust.model.NormalEntity;
import com.yj.robust.ui.activity.sotreList.StoreListActivity;
import com.yj.robust.ui.activity.storeDetail.StoreDetailActivity;
import com.yj.robust.ui.adapter.MineOrderTabAdapters;
import com.yj.robust.ui.fragment.HomeGoodsListFrag.HomeGoodsListFrag;
import com.yj.robust.util.IntentUtils;
import com.yj.robust.util.LogUtils;
import com.yj.robust.util.Utils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author Suo
 * @date 2018/3/16
 */

public class HomeGoodsListActivity extends BaseActivity {
    @BindView(R.id.goods_list_tv_default)
    TextView tvDefault;
    @BindView(R.id.goods_list_tv_count)
    TextView tvCount;
    @BindView(R.id.rl_all_info)
    LinearLayout rl_all_info;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.goods_list_tv_price)
    TextView tvPrice;
    @BindView(R.id.goods_list_iv_count)
    ImageView ivCount;
    @BindView(R.id.goods_list_iv_icon)
    ImageView ivClassIcon;
    @BindView(R.id.goods_list_iv_price)
    ImageView ivPrice;
    @BindView(R.id.goods_list_tv_info)
    TextView tvInfo;
    @BindView(R.id.ll_bottom_more)
    LinearLayout llButtomMore;
    @BindView(R.id.store_list_iv)
    ImageView ivShopIcon;
    @BindView(R.id.item_tv_store_states)
    TextView tvStoreStates;
    @BindView(R.id.store_list_tv_title_)
    TextView tvTitle_;
    @BindView(R.id.store_list_tv_title)
    TextView tvTitle;
    @BindView(R.id.store_list_tv_expenses)
    TextView tvExpenses;
    @BindView(R.id.store_list_tv_sales)
    TextView tvSales;
    @BindView(R.id.rl_store_info)
    RelativeLayout rlStoreInfo;
    @BindView(R.id.frag_home_v_head)
    View vHead;

    private String countTag = "default";
    private String priceTag = "default";
    private String orderby = "recommend";

    private List<Fragment> mFragment = new ArrayList<Fragment>();
    private HomeGoodsListFrag instant;
    private String shopId, classifyId, type, name, TAG, productIds;
    private MyReceiver myReceiver;

    @Override
    protected int getContentView() {
        return R.layout.activity_home_good_list_;
    }

    @Override
    protected void initView() {
        shopId = getIntent().getStringExtra("shopId");
        classifyId = getIntent().getStringExtra("classifyId");
        productIds = getIntent().getStringExtra("productIds");
        type = getIntent().getStringExtra("type");
        name = getIntent().getStringExtra("name");
        TAG = getIntent().getStringExtra("TAG");
        instant = HomeGoodsListFrag.instant(shopId, classifyId, type, name, TAG, productIds);
        tvDefault.setSelected(true);
        mFragment.add(instant);
        MineOrderTabAdapters adapter = new MineOrderTabAdapters(getSupportFragmentManager(), mFragment);
        viewPager.setAdapter(adapter);
//		if (shopId != null) {
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("CN.YJ.ROBUST.NOTIFYACTIVITY");
        registerReceiver(myReceiver, intentFilter);
//		}
        transTitle();
    }

    @TargetApi(21)
    private void transTitle() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            vHead.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {
    }


    /**
     * 关闭Dialog
     */
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: This method is called when the BroadcastReceiver is receiving
            if (intent != null) {
                String shopInfo = intent.getStringExtra("shopInfo");
                if (shopInfo.equals("yes")) {
                    String serviceStartime = intent.getStringExtra("serviceStartime");
                    final String shopId = intent.getStringExtra("shopId");
                    String shopImg = intent.getStringExtra("shopImg");
                    String shopName = intent.getStringExtra("shopName");
                    String shopType = intent.getStringExtra("shopType");
                    String shopTypeName = intent.getStringExtra("shopTypeName");
                    String receipt = intent.getStringExtra("receipt");
                    String detailNumMonth = intent.getStringExtra("detailNumMonth");
                    String shopNumber = intent.getStringExtra("shopNumber");

                    Glide.with(HomeGoodsListActivity.this)
                            .load(URLBuilder.getUrl(shopImg))
                            .asBitmap()
                            .centerCrop()
                            .error(R.mipmap.default_goods)
                            .into(ivShopIcon);

                    tvTitle.setText(shopName);
                    tvExpenses.setText("起送价: ￥" + serviceStartime);
                    tvSales.setText("月售 " + detailNumMonth + " 单");
                    if (receipt.equals("1")) {
                        tvStoreStates.setVisibility(View.GONE);
                    } else {
                        tvStoreStates.setVisibility(View.VISIBLE);

                    }
                    if (shopType.equals("1")) {
                        tvTitle_.setVisibility(View.GONE);
                    } else {
                        tvTitle_.setVisibility(View.VISIBLE);
                        tvTitle_.setText(shopTypeName);
                    }
                    rlStoreInfo.setVisibility(View.VISIBLE);
                    rl_all_info.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(HomeGoodsListActivity.this, StoreDetailActivity.class);
                            intent.putExtra("shopId", shopId);
                            startActivity(intent);
                        }
                    });

                    if (Integer.parseInt(shopNumber) > 1) {
                        llButtomMore.setVisibility(View.VISIBLE);
                        llButtomMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(HomeGoodsListActivity.this, StoreListActivity.class);
                                intent.putExtra("name", name);
                                startActivity(intent);
                            }
                        });
                    } else {
                        llButtomMore.setVisibility(View.GONE);
                    }


                } else {
                    rlStoreInfo.setVisibility(View.GONE);

                }
            }
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        if (mUtils.isLogin()) {
            doAsyncGetInfo();
        } else {
            tvInfo.setVisibility(View.GONE);
        }
        super.onResume();
    }

    private boolean isShow = true;

    @OnClick({R.id.goods_list_ll_default, R.id.goods_list_ll_count, R.id.goods_list_ll_price, R.id.search_modify_tv, R.id.goods_list_info,
            R.id.goods_list_return, R.id.goods_list_ll_pr})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goods_list_ll_default:
                countTag = "default";
                priceTag = "default";
                tvCount.setSelected(false);
                tvPrice.setSelected(false);
                ivCount.setImageResource(R.mipmap.sort_by_default);
                ivPrice.setImageResource(R.mipmap.sort_by_default);
                tvDefault.setSelected(true);
                orderby = "recommend";
                instant.setOnclick(1);
                break;
            case R.id.goods_list_ll_count:
                priceTag = "default";
                tvDefault.setSelected(false);
                tvPrice.setSelected(false);
                ivPrice.setImageResource(R.mipmap.sort_by_default);
                tvCount.setSelected(true);
                switch (countTag) {
                    case "default":
                        countTag = "high";
                        ivCount.setImageResource(R.mipmap.high_to_low);
                        orderby = "salesVolumeHigh";
                        break;
                    case "high":
                        countTag = "low";
                        ivCount.setImageResource(R.mipmap.low_to_high);
                        orderby = "salesVolumeLow";
                        break;
                    case "low":
                        countTag = "high";
                        ivCount.setImageResource(R.mipmap.high_to_low);
                        orderby = "salesVolumeHigh";
                        break;
                }
                instant.setOnclick(2);

                break;
            case R.id.goods_list_ll_price:
                countTag = "default";
                tvDefault.setSelected(false);
                tvCount.setSelected(false);
                ivCount.setImageResource(R.mipmap.sort_by_default);
                tvPrice.setSelected(true);
                switch (priceTag) {
                    case "default":
                        priceTag = "high";
                        ivPrice.setImageResource(R.mipmap.high_to_low);
                        orderby = "priceHigh";
                        break;
                    case "high":
                        priceTag = "low";
                        ivPrice.setImageResource(R.mipmap.low_to_high);
                        orderby = "priceLow";
                        break;
                    case "low":
                        priceTag = "high";
                        ivPrice.setImageResource(R.mipmap.high_to_low);
                        orderby = "priceHigh";
                        break;
                }
                instant.setOnclick(3);
                break;
            case R.id.search_modify_tv:
                Intent intent = new Intent(this, SearchActivity.class);
                intent.putExtra("shopId", shopId);
                startActivity(intent);
                break;
            case R.id.goods_list_info:

                if (mUtils.isLogin()) {
                    IntentUtils.IntentToInfoCenter(this);
                } else {
                    IntentUtils.IntentToLogin(this);
                }
                break;
            case R.id.goods_list_return:
                onBackPressed();
                break;
            case R.id.goods_list_ll_pr:
                if (isShow) {
                    ivClassIcon.setBackgroundResource(R.mipmap.shangpinliebao_fenlei_1);
                    isShow = false;
                } else {
                    ivClassIcon.setBackgroundResource(R.mipmap.shangpinliebao_fenlei);
                    isShow = true;
                }
                instant.setOnclick(4);
                break;

        }
    }

    private void doAsyncGetInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", mUtils.getUid());
        LogUtils.i("传输的值" + URLBuilder.format(map));
        OkHttpUtils.post().url(URLBuilder.URLBaseHeader + "/phone/user/countMsg.act").tag(this)
                .addParams(Key.data, URLBuilder.format(map))
                .build().execute(new Utils.MyResultCallback<NormalEntity>() {

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                tvInfo.setVisibility(View.GONE);
                if (call.isCanceled()) {
                    call.cancel();
                } else {
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
                    if (response.getData() != null && Float.parseFloat(response.getData().toString()) > 0) {
                        String text = response.getData().toString();
                        tvInfo.setVisibility(View.VISIBLE);
                        int i = (int) Float.parseFloat(text);
                        if (i > 99) {
                            tvInfo.setText("99");
                        } else {
                            tvInfo.setText(i + "");
                        }
                    } else {
                        tvInfo.setVisibility(View.GONE);
                    }
                } else {
                    tvInfo.setVisibility(View.GONE);
                }
            }
        });
    }

    //
    @Override
    protected void onDestroy() {
        OkHttpUtils.getInstance().cancelTag(this);
//		if (shopId != null) {
        unregisterReceiver(myReceiver);
//		}
        super.onDestroy();
    }
}
