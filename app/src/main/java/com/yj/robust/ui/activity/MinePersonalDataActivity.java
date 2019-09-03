package com.yj.robust.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
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
import com.yj.robust.util.IntentUtils;
import com.yj.robust.util.LogUtils;
import com.yj.robust.util.PicUtils;
import com.yj.robust.util.ToastUtils;
import com.yj.robust.util.Utils;
import com.yj.robust.util.luban.Luban;
import com.yj.robust.util.luban.OnCompressListener;
import com.yj.robust.widget.Dialog.CustomNormalDialog;
import com.yj.robust.widget.Dialog.CustomProgressDialog;
import com.yj.robust.widget.RoundedImageView.RoundedImageView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.GlideEngine;
import io.valuesfeng.picker.utils.PicturePickerUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Suo on 2017/4/28.
 */

public class MinePersonalDataActivity extends BaseActivity {

	private static final String TAG = "MinePersonalDataActivity";
	@BindView(R.id.mine_personal_iv_header)
	RoundedImageView ivHeader;
	@BindView(R.id.mine_personal_tv_name)
	TextView tvName;
	@BindView(R.id.mine_personal_tv_tel)
	TextView tvTel;
	@BindView(R.id.mine_personal_tv_alipay)
	TextView tvAlipay;
	@BindView(R.id.mine_personal_tv_leaguer)
	TextView tvLeaguer;
	@BindView(R.id.title_ll_iv)
	ImageView ivTitleIcon;
	@BindView(R.id.title_layout)
	LinearLayout lyTitle;
	@BindView(R.id.title_rl_next)
	RelativeLayout reLayout;
	CustomProgressDialog loadingDialog;
	private CustomNormalDialog infoDialog;

	/**
	 * 判断是否需要检测，防止不停的弹框
	 */
	private boolean isNeedCheck = true;
	private static final int PERMISSON_REQUESTCODE = 0;
	/**
	 * 需要进行检测的权限数组
	 */
	protected String[] needPermissions = {
			Manifest.permission.CAMERA,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.READ_EXTERNAL_STORAGE,
	};


	@Override
	protected int getContentView() {
		return R.layout.activity_mine_personal;
	}

	@Override
	protected void initView() {
		setTitleInfo();
		transTitle();
	}

	@Override
	protected void initData() {
		if (!TextUtils.isEmpty(mUtils.getAvatar())) {
			String url;
			if (mUtils.getAvatar().startsWith("http")) {
				url = mUtils.getAvatar();
			} else {
				url = URLBuilder.URLBaseHeader + mUtils.getAvatar();
			}
			Glide.with(getApplicationContext())
					.load(url)
					.asBitmap()
					.fitCenter()
					.error(R.mipmap.default_avatar)
					.into(ivHeader);

		}
		loadingDialog = new CustomProgressDialog(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!TextUtils.isEmpty(mUtils.getUserName())) {
			tvName.setText(mUtils.getUserName());
		}
		if (!TextUtils.isEmpty(mUtils.getTel())) {
			if (mUtils.getTel().length() > 8) {
				tvTel.setText(mUtils.getTel().replace(mUtils.getTel().substring(4, 8), "****"));
			} else {
				tvTel.setText(mUtils.getTel());
			}
		}
		if (!TextUtils.isEmpty(mUtils.getAlipay())) {
			tvAlipay.setText(mUtils.getAlipay());
		}
		if (!TextUtils.isEmpty(preferencesUtil.getValue("userType", ""))) {
			tvLeaguer.setText(preferencesUtil.getValue("userType", ""));
		}
	}

	private void setTitleInfo() {
		setTitleText("编辑个人资料");
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

	@OnClick({R.id.mine_personal_header, R.id.mine_personal_tel, R.id.mine_personal_pwd, R.id.mine_personal_alipay, R.id.mine_personal_name})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.mine_personal_header:
				if (mUtils.isLogin()) {
					checkPermissions(needPermissions);
				} else {
					IntentUtils.IntentToLogin(this);
				}
				break;
			case R.id.mine_personal_tel:
				if (mUtils.isLogin()) {
					Intent intentTel = new Intent(this, MinePersonalConfirmPwdActivity.class);
					startActivity(intentTel);
				} else {
					IntentUtils.IntentToLogin(this);
				}
				break;
			case R.id.mine_personal_pwd:
				if (mUtils.isLogin()) {
					Intent intentPwd = new Intent(this, MinePersonalPwdActivity.class);
					startActivity(intentPwd);
				} else {
					IntentUtils.IntentToLogin(this);
				}
				break;
			case R.id.mine_personal_alipay:
				if (mUtils.isLogin()) {
					Intent intentAlipay = new Intent(this, MinePersonalConfirmTelActivity.class);
					startActivity(intentAlipay);
				} else {
					IntentUtils.IntentToLogin(this);
				}
				break;
			case R.id.mine_personal_name:
				if (mUtils.isLogin()) {
					Intent intentName = new Intent(this, MinePersonalNameActivity.class);
					startActivity(intentName);
				} else {
					IntentUtils.IntentToLogin(this);
				}
				break;
		}
	}

	/**
	 * @since 2.5.0
	 */
	private void checkPermissions(String... permissions) {
		List<String> needRequestPermissonList = findDeniedPermissions(permissions);
		if (null != needRequestPermissonList && needRequestPermissonList.size() > 0) {
			ActivityCompat.requestPermissions(this, needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]), PERMISSON_REQUESTCODE);
		}
		if (needRequestPermissonList.size() == PERMISSON_REQUESTCODE) {
			//版本有更新
			doUpdateAvatar();
		}
	}

	/**
	 * 获取权限集中需要申请权限的列表
	 *
	 * @param permissions
	 * @return
	 * @since 2.5.0
	 */
	private List<String> findDeniedPermissions(String[] permissions) {
		List<String> needRequestPermissonList = new ArrayList<>();
		for (String perm : permissions) {
			if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED
					|| ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
				needRequestPermissonList.add(perm);
			}
		}
		return needRequestPermissonList;
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case 100:
				LogUtils.i("我在ActivityResult中");
				if (resultCode == Activity.RESULT_OK) {
					LogUtils.i("我在resultOk中");
					LogUtils.i(data.getExtras().toString());
					List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
					LogUtils.i(mSelected.get(0).getPath());
					LogUtils.i(mSelected.get(0) + "");
					switch (requestCode) {
						case 100:
							String path = PicUtils.getPicPath(this, mSelected.get(0));
							LogUtils.i(path);
							showImg(path);
							break;
					}
				}
				break;
		}
	}

	private void showImg(final String path) {
		Luban.get(this)
				.load(new File(path))
				.putGear(Luban.THIRD_GEAR)
				.setCompressListener(new OnCompressListener() {
					@Override
					public void onStart() {
					}

					@Override
					public void onSuccess(File file) {
						LogUtils.i("path的值" + path);
						LogUtils.i("file的路径" + file.getAbsolutePath());
						Glide.with(getApplicationContext()).load(file).fitCenter().into(ivHeader);
						if (!TextUtils.isEmpty(mUtils.getUid())) {
							doPostImage(file);
						}
					}

					@Override
					public void onError(Throwable e) {
						LogUtils.i("我鲁班onError了" + e);
						ToastUtils.showToast(MinePersonalDataActivity.this, "图片已损坏,请重新选取");
					}
				}).launch();
	}

	private void doPostImage(File file) {

		Map<String, String> map = new HashMap<>();
		map.put(Key.userId, mUtils.getUid());
		LogUtils.i("image的传输值" + URLBuilder.format(map));
		LogUtils.i("file的值" + file);
		OkHttpUtils.post().url(URLBuilder.URLBaseHeader + URLBuilder.UpdateHeader).tag(this)
				.addFile("userHeadimg", mUtils.getTel() + ".jpg", file)
				.addParams(Key.data, URLBuilder.format(map))
				.build().execute(new Utils.MyResultCallback<NormalEntity>() {

			@Override
			public void inProgress(float progress) {
				super.inProgress(progress);
				if (loadingDialog == null) {
					loadingDialog = new CustomProgressDialog(MinePersonalDataActivity.this);
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
					LogUtils.i("我onResponse了");
					if (response.getCode().equals(response.HTTP_OK)) {
						//返回值为200 说明请求成功
						LogUtils.i("请求成功" + response.getCode());
						mUtils.saveAvatar(response.getData().toString());
						ToastUtils.showToast(MinePersonalDataActivity.this, "头像上传成功");
					} else {
						LogUtils.i("请求失败" + response.getCode());
						ToastUtils.showToast(MinePersonalDataActivity.this, "上传失败" + response.getMsg() + " :)" + response.getCode());
					}
				}
			}

			@Override
			public void onError(Call call, Exception e) {
				super.onError(call, e);
				LogUtils.i("网络请求失败" + e);
				dismissDialog2();
				if (call.isCanceled()) {
					call.cancel();
				} else {
					ToastUtils.showToast(MinePersonalDataActivity.this, "网络故障,请稍后再试");
				}

			}
		});
	}


	private void doUpdateAvatar() {
		Picker.from(this)
				.count(1)
				.enableCamera(true)
				.setEngine(new GlideEngine())
				.forResult(100);
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {

			case PERMISSON_REQUESTCODE:
				if (!verifyPermissions(grantResults)) {
//					showMissingPermissionDialog();
					isNeedCheck = false;
				} else {
					isNeedCheck = true;
				}
				if (isNeedCheck) {
					doUpdateAvatar();
				}
				Log.e(TAG, "onRequestPermissionsResult: " + isNeedCheck);
				break;
		}
	}

	/**
	 * 检测是否说有的权限都已经授权
	 *
	 * @param grantResults
	 * @return
	 * @since 2.5.0
	 */
	private boolean verifyPermissions(int[] grantResults) {
		for (int result : grantResults) {
			if (result != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	private void showDialog(String text) {
		if (infoDialog == null) {
			infoDialog = new CustomNormalDialog(this);
		}
		if (!infoDialog.isShowing()) {
			infoDialog.show();
		}
		infoDialog.getTvTitle().setText(text);
		infoDialog.getTvConfirm().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//				if (isNeedCheck) {
				checkPermissions(needPermissions);
//				}
				infoDialog.dismiss();
			}
		});
		infoDialog.getTvCancel().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				LogUtils.i("我cancel了");
				infoDialog.dismiss();
			}
		});
	}

	private void dismissDialog() {
		if (infoDialog != null) {
			infoDialog.dismiss();
			infoDialog = null;
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
		super.onDestroy();
		dismissDialog2();
		dismissDialog();
		OkHttpUtils.getInstance().cancelTag(this);
	}
}
