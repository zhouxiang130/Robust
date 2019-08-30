package com.yj.robust.ui.activity.goodDetails;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sobot.chat.api.model.Information;
import com.sobot.chat.utils.ZhiChiConstant;
import com.yj.robust.R;
import com.yj.robust.base.BaseActivity;
import com.yj.robust.base.URLBuilder;
import com.yj.robust.function.CustomServices;
import com.yj.robust.model.GoodsCommentEntity;
import com.yj.robust.model.GoodsEntity;
import com.yj.robust.model.GoodsSaleEntity;
import com.yj.robust.model.NormalEntity;
import com.yj.robust.model.SaleStyleEntity;
import com.yj.robust.model.ShareEntity;
import com.yj.robust.model.ShopCartEntity;
import com.yj.robust.model.StyleDataEntity;
import com.yj.robust.ui.MainActivity;
import com.yj.robust.ui.activity.NormalWebViewActivity;
import com.yj.robust.ui.activity.SettlementGoodsActivity;
import com.yj.robust.ui.activity.goodDetail.GoodsDetailActivity;
import com.yj.robust.ui.activity.storeDetail.StoreDetailActivity;
import com.yj.robust.ui.adapter.GoodsDetailContentAdapter;
import com.yj.robust.ui.adapter.GoodsDetialTitleAdapter;
import com.yj.robust.util.IntentUtils;
import com.yj.robust.util.LogUtils;
import com.yj.robust.util.ToastUtils;
import com.yj.robust.util.Utils;
import com.yj.robust.widget.CustomBanner.CustomRollPagerView;
import com.yj.robust.widget.CustomViewGroup.CustomSizeDialogViewGroup;
import com.yj.robust.widget.Dialog.CustomProgressDialog;
import com.yj.robust.widget.Dialog.CustomShareDialog;
import com.yj.robust.widget.Dialog.CustomSizeDialog;
import com.yj.robust.widget.Dialog.GoodDetailTicketDialogs;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Suo on 2018/3/16.
 *
 * @TODO 商品详情界面(优化====)
 * @TODO 需要优化的为： 部分手机在查看商品详情的情况下 会出现卡顿现象
 */

public class GoodsDetailActivitys extends BaseActivity implements GoodDetails_contract.View {


	private Information userInfo;
	private ArrayList<Object> mTitleList;
	private String productId, sproductId;
	private CustomProgressDialog loadingDialog;

	private GoodDetails_contract.Presenter presenter = new GoodDetails_presenter(this);


	@Override
	protected int getContentView() {
		return R.layout.activity_goods_details;
	}

	@Override
	protected void initView() {
		userInfo = new Information();
		mTitleList = new ArrayList<>();
		productId = getIntent().getStringExtra("productId");
		sproductId = getIntent().getStringExtra("sproductId");
		transTitle();
	}

	@Override
	protected void initData() {
		presenter.doAsyncGetDetial(mUtils,productId,sproductId);
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
	public void isAsynGetDetailBefore() {
		if (loadingDialog == null) {
			loadingDialog = new CustomProgressDialog(GoodsDetailActivitys.this);
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
	public void setData() {

	}


	@Override
	public void dismissDialog() {
		if (loadingDialog != null) {
			loadingDialog.dismiss();
			loadingDialog = null;
		}
	}
}
