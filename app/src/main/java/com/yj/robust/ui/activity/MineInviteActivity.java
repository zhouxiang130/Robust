package com.yj.robust.ui.activity;

import android.annotation.TargetApi;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yj.robust.R;
import com.yj.robust.base.BaseActivity;
import com.yj.robust.base.URLBuilder;
import com.yj.robust.model.ShareEntity;
import com.yj.robust.model.UserInfoEntity;
import com.yj.robust.util.LogUtils;
import com.yj.robust.util.ToastUtils;
import com.yj.robust.util.Utils;
import com.yj.robust.util.ZXingUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
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
import okhttp3.Response;

/**
 * Created by Suo on 2018/3/15.
 *
 * @TODO 邀请好友界面
 */

public class MineInviteActivity extends BaseActivity {
    @BindView(R.id.mine_invite_tv_code)
    TextView tvCode;
    @BindView(R.id.mine_invite_copy_img)
    ImageView ivQRCode;
    @BindView(R.id.title_ll_iv)
    ImageView ivTitleIcon;
    @BindView(R.id.title_layout)
    LinearLayout lyTitle;
    @BindView(R.id.title_rl_next)
    RelativeLayout reLayout;
    private ShareEntity.ShareData data;
    private UserInfoEntity.UserInfoData userInfoData;

    final int Finish = 0x11;
    final int Cancel = 0x88;
    final int Failed = 0x99;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            switch (msg.what) {
                case Finish:
                    ToastUtils.showToast(MineInviteActivity.this, "分享成功了");
//                    disMissDialog();
                    break;
                case Cancel:
                    ToastUtils.showToast(MineInviteActivity.this, "取消分享");
                    break;
                case Failed:
                    if (!msg.getData().isEmpty()) {
                        String error = msg.getData().getString("error");
                        LogUtils.i("error" + error);
                        ToastUtils.showToast(MineInviteActivity.this, "分享失败");
                    }
                    break;
            }
        }
    };


    @Override
    protected int getContentView() {
        return R.layout.activity_mine_invite;
    }

    @Override
    protected void initView() {
        setTitleInfo();
        transTitle();
    }

    @Override
    protected void initData() {
        doasynGetInviteCode();
    }

    private void setTitleInfo() {
        setTitleText("邀请好友");
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


    @OnClick({R.id.mine_invite_ll_qq, R.id.mine_invite_ll_wechat, R.id.mine_invite_ll_sina, R.id.mine_invite_ll_circle,
            R.id.mine_invite_btn_copy, R.id.mine_invite_tv_rule})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_invite_ll_qq:
                showShare(QQ.NAME);
                break;
            case R.id.mine_invite_ll_wechat:
                showShare(Wechat.NAME);
                break;
            case R.id.mine_invite_ll_sina:
                showShare(SinaWeibo.NAME);
                break;
            case R.id.mine_invite_ll_circle:
                showShare(WechatMoments.NAME);
                break;
            case R.id.mine_invite_btn_copy:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(mUtils.getInviteCode());
                //+"\n这是我的邀请码,注册时一定要填写哦^_^"
                ToastUtils.showToast(MineInviteActivity.this, "复制成功,可以发给朋友们了^_^");
                break;
            case R.id.mine_invite_tv_rule:
                Intent intentRule = new Intent(this, NormalWebViewActivity.class);
                intentRule.putExtra("url", URLBuilder.URLBaseHeader + URLBuilder.InviteRule);
                intentRule.putExtra("title", "活动规则");
                startActivity(intentRule);
                break;
        }
    }


    private void doasynGetInviteCode() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", mUtils.getUid());
        LogUtils.i("传输的网络数据" + URLBuilder.format(map));
        OkHttpUtils.post().url(URLBuilder.URLBaseHeader + "/phone/user/searchUser.act")
                .addParams("data", URLBuilder.format(map))
                .tag(this).build().execute(new Utils.MyResultCallback<UserInfoEntity>() {

            @Override
            public UserInfoEntity parseNetworkResponse(Response response) throws Exception {
                String json = response.body().string().trim();
                LogUtils.i("doasynGetInviteCode --- json的值" + json);
                return new Gson().fromJson(json, UserInfoEntity.class);
            }

            @Override
            public void onResponse(UserInfoEntity response) {
                if (response != null && response.getCode().equals(response.HTTP_OK)) {
                    //返回值为200 说明请求成功
                    if (response.getData() != null) {
                        userInfoData = response.getData();
                        tvCode.setText(userInfoData.getAgentNumber());
                        mUtils.saveInviteCode(userInfoData.getAgentNumber());
                        doAsyncGetShare();
                    }
                } else {
//                    ToastUtils.showToast(MineInviteActivity.this, "无法获取邀请信息 :)" + response.getMsg());
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                LogUtils.i("网络请求失败 " + e);
//                ToastUtils.showToast(MineInviteActivity.this, "网络故障,请稍后再试 ");
            }
        });
    }


    public void doAsyncGetShare() {
        Map<String, String> map = new HashMap<>();
        map.put("codeOrId", userInfoData.getAgentNumber());
        map.put("type", "2");
        LogUtils.i("传输的网络数据" + URLBuilder.format(map));
        OkHttpUtils.post().url(URLBuilder.URLBaseHeader + "/phone/user/shareUrl.act")
                .addParams("data", URLBuilder.format(map))
                .tag(this).build().execute(new Utils.MyResultCallback<ShareEntity>() {

            @Override
            public ShareEntity parseNetworkResponse(Response response) throws Exception {
                String json = response.body().string().trim();
                LogUtils.i("json的值" + json);
                return new Gson().fromJson(json, ShareEntity.class);
            }

            @Override
            public void onResponse(ShareEntity response) {
                if (response != null && response.getCode().equals(response.HTTP_OK)) {
                    //返回值为200 说明请求成功
                    if (response.getData() != null) {
                        data = response.getData();
                        setQRCode();

                    }
                } else {
//                    ToastUtils.showToast(MineInviteActivity.this, "无法获取邀请信息 :)" + response.getMsg());
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                LogUtils.i("网络请求失败 " + e);
//                ToastUtils.showToast(MineInviteActivity.this, "网络故障,请稍后再试 ");
            }
        });
    }

    private void setQRCode() {
        if (data.getUrl() != null) {
//			Bitmap bitmap = ZXingUtils.createQRImage(URLBuilder.URLBaseHeader + data.getUrl(), 500, 500);
            Bitmap bitmap = ZXingUtils.createQRImage(URLBuilder.getUrl(data.getUrl()), ivQRCode.getWidth(), ivQRCode.getHeight());
//			SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bitmap);
            ivQRCode.setImageBitmap(bitmap);
        }
    }


    private void showShare(String platform) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        if (data != null && data.getUrl() != null) {
            LogUtils.i(data.getImage() + "....img的值");
            LogUtils.i(data.getUrl() + ".....link的值");
            LogUtils.i(data.getTitle() + ",,,title的值");
            LogUtils.i("我在data部位空里" + platform);
            if (data.getTitle() != null) {
                if (platform.equals(SinaWeibo.NAME)) {
                    oks.setText(data.getTitle() + URLBuilder.getUrl(data.getUrl()));
                } else {
                    oks.setText(data.getTitle());
                    oks.setUrl(URLBuilder.getUrl(data.getUrl()));
                    oks.setTitle(data.getTitle());
                    // titleUrl是标题的网络链接，QQ和QQ空间等使用
                    oks.setTitleUrl(URLBuilder.getUrl(data.getUrl()));
                    // text是分享文本，所有平台都需要这个字段
                }
            }
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//			oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
            if (!TextUtils.isEmpty(data.getImage())) {
                LogUtils.i("我进入到图片这了" + data.getImage());
                if (data.getImage().contains(",")) {
                    //获取第一个,的位置,我们需要获取从开始到,之前的所有字符
                    String imgUrl = data.getImage().substring(0, data.getImage().indexOf(","));
                    LogUtils.i("获取,的值......" + data.getImage().indexOf(","));
                    LogUtils.i("多张图片分割后取路径......" + imgUrl);
                    oks.setImageUrl(URLBuilder.getUrl(imgUrl));
                } else {
                    LogUtils.i("我进入到图片else了" + data.getImage());
                    oks.setImageUrl(URLBuilder.getUrl(data.getImage()));
                }
            }
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
            oks.setComment("请输入您的评论");
            // site是分享此内容的网站名称，仅在QQ空间使用
            oks.setSite(getString(R.string.app_name));
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//            oks.setSiteUrl("http://www.51xianchang.com");
            LogUtils.i("分享信息设置结束了");
        } else {
            ToastUtils.showToast(this, "无法获取分享信息,请检查网络");
        }
        if (platform != null) {
            LogUtils.i("我在设置分享平台");
            oks.setPlatform(platform);
        }
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                doShareConfirm(data.getLink());
                Message msg = mHandler.obtainMessage();
                msg.what = Finish;
                mHandler.sendMessage(msg);
                LogUtils.i("我完成分享了");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
//                cancel = true;
                Bundle bundle = new Bundle();
                bundle.putString("error", throwable + "");
                Message msg = mHandler.obtainMessage();
                msg.what = Failed;
                msg.setData(bundle);
                mHandler.sendMessage(msg);
                LogUtils.i("我分享失败了" + throwable);
            }

            @Override
            public void onCancel(Platform platform, int i) {
//                cancel = true;
                Message msg = mHandler.obtainMessage();
                msg.what = Cancel;
                mHandler.sendMessage(msg);
                LogUtils.i("我分享退出了");
            }
        });
// 启动分享GUI
        LogUtils.i("我要启动分享的UI了");
        oks.show(this);
    }

    @Override
    protected void onDestroy() {
        OkHttpUtils.getInstance().cancelTag(this);
        super.onDestroy();
    }
}
