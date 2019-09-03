package com.yj.robust.ui.activity.mineSettingHelp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sobot.chat.SobotApi;
import com.sobot.chat.api.enumtype.SobotChatTitleDisplayMode;
import com.sobot.chat.api.model.Information;
import com.yj.robust.R;
import com.yj.robust.base.BaseActivity;
import com.yj.robust.base.Constant;
import com.yj.robust.base.URLBuilder;
import com.yj.robust.ui.activity.mineHelpSug.MineHelpSugActivity;

import java.lang.ref.SoftReference;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/11 0011.
 *
 * @TODO 求助反馈界面 2.0版本添加页面
 */

public class MineSettingHelpActivity extends BaseActivity {
    private Information userInfo;
    @BindView(R.id.title_ll_iv)
    ImageView ivTitleIcon;
    @BindView(R.id.title_layout)
    LinearLayout lyTitle;
    @BindView(R.id.title_rl_next)
    RelativeLayout reLayout;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_setting_helps;
    }

    @Override
    protected void initView() {
        setTitleInfo();
        transTitle();
        userInfo = new Information();
    }

    private void setTitleInfo() {
        setTitleText("求助反馈");
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


    @OnClick({R.id.mine_setting_helps_help, R.id.mine_setting_helps_suggestion})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_setting_helps_help:

                doCustomServices();
                break;
            case R.id.mine_setting_helps_suggestion:
                Intent intent = new Intent(this, MineHelpSugActivity.class);
                startActivity(intent);

                break;
        }
    }

    public void doCustomServices() {
        //用户信息设置
        //设置用户自定义字段
        userInfo.setUseRobotVoice(false);//这个属性默认都是false。想使用需要付费。付费才可以设置为true。
        userInfo.setUid(mUtils.getUid());
        userInfo.setTel(mUtils.getTel());
        userInfo.setUname(mUtils.getUserName());
        userInfo.setFace(URLBuilder.getUrl(mUtils.getAvatar()));//头像
        SoftReference<String> appkeySR = new SoftReference<>(Constant.ZC_appkey);
        String appkey = appkeySR.get();
        if (!TextUtils.isEmpty(appkey)) {
            userInfo.setAppkey(appkey);
            //设置标题显示模式
            SobotApi.setChatTitleDisplayMode(getApplicationContext(), SobotChatTitleDisplayMode.values()[0], "");
            //设置是否开启消息提醒
            SobotApi.setNotificationFlag(getApplicationContext(), true, R.mipmap.logo, R.mipmap.logo);
            SobotApi.hideHistoryMsg(getApplicationContext(), 0);
            SobotApi.setEvaluationCompletedExit(getApplicationContext(), false);
            SobotApi.startSobotChat(this, userInfo);
        } else {
        }
    }
}
