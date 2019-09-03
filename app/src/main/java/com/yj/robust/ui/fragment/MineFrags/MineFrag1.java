package com.yj.robust.ui.fragment.MineFrags;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sobot.chat.SobotApi;
import com.sobot.chat.api.enumtype.SobotChatTitleDisplayMode;
import com.sobot.chat.api.model.Information;
import com.sobot.chat.utils.ZhiChiConstant;
import com.yj.robust.R;
import com.yj.robust.base.BaseFragment;
import com.yj.robust.base.URLBuilder;
import com.yj.robust.model.MineEntity;
import com.yj.robust.model.NormalEntity;
import com.yj.robust.ui.activity.MineAddressManageActivity;
import com.yj.robust.ui.activity.MineCollectionActivity;
import com.yj.robust.ui.activity.MineInviteActivity;
import com.yj.robust.ui.activity.MineOrderActivity;
import com.yj.robust.ui.activity.MinePersonalDataActivity;
import com.yj.robust.ui.activity.MineSettingActivity;
import com.yj.robust.ui.activity.mineAccount.MineAccount2Activity;
import com.yj.robust.ui.activity.mineCoupon.MineCouponActivity;
import com.yj.robust.ui.activity.mineRefundList.MineRefundListActivity;
import com.yj.robust.ui.activity.mineScoring.MineScoring1Activity;
import com.yj.robust.util.AuthorUtils;
import com.yj.robust.util.IntentUtils;
import com.yj.robust.util.ToastUtils;
import com.yj.robust.widget.Dialog.CustomNormalContentDialog;
import com.yj.robust.widget.RoundedImageView.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Suo on 2017/11/18.
 *
 * @TODO 个人中心 2.0版本升级页面
 */

public class MineFrag1 extends BaseFragment implements MineFrag_contract.View {
    @BindView(R.id.frag_mine_tv_info)
    TextView fragMineTvInfo;
    @BindView(R.id.frag_mine_login_iv)
    RoundedImageView fragMineLoginIv;
    @BindView(R.id.frag_mine_login_)
    LinearLayout fragMineLogin;
    @BindView(R.id.mine_new_tv_login)
    TextView mineNewTvLogin;
    @BindView(R.id.mine_new_tv_tel)
    TextView mineNewTvTel;
    @BindView(R.id.frag_mine_tv_pay_num)
    TextView fragMineTvPayNum;
    @BindView(R.id.frag_mine_tv_send_num)
    TextView fragMineTvSendNum;
    @BindView(R.id.frag_mine_tv_coustom_service_msg_num)
    TextView tvCoustomNum;
    @BindView(R.id.frag_mine_tv_get_num)
    TextView fragMineTvGetNum;
    @BindView(R.id.frag_mine_tv_judge_num)
    TextView fragMineTvJudgeNum;
    @BindView(R.id.frag_mine_login_ll)
    LinearLayout llLogin;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    Unbinder unbinder;
    CustomNormalContentDialog mDialog;
    private MineFrag_contract.Presenter mineFragView = new MineFrag_presenter(this);

    /**
     * 需要进行检测的权限数组
     */
    private static final int PERMISSON_REQUESTCODE = 0;
    private boolean isNeedCheck = true;
    protected String[] needPermissions = {
            Manifest.permission.CALL_PHONE
    };

    private String serviceTel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = createView(inflater.inflate(R.layout.fragment_mine1, container, false));
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        mineFragView.subscribe();
        regReceiver();
    }


    @Override
    public void onResume() {
        super.onResume();
        mineFragView.setMineHeadInfo(mUtils);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //    iv_qr
    @OnClick({R.id.frag_mine_info, /*R.id.frag_mine_login_iv,*/ R.id.frag_mine_login_,/* R.id.frag_mine_head_img,*/ R.id.frag_mine_login_ll, R.id.rl_wallet, R.id.rl_integration,
            R.id.frag_mine_order_all, R.id.frag_mine_order_pay, R.id.frag_mine_order_send, R.id.frag_mine_order_get, R.id.frag_mine_order_judge, R.id.frag_mine_order_drawback,
            R.id.iv_qr, R.id.frag_mine_collection, R.id.frag_mine_address, R.id.frag_mine_coustom_service,  /* R.id.frag_mine_ticket, R.id.frag_mine_contact,*/
            R.id.frag_mine_rl_setting, R.id.rl_coupon, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.frag_mine_info:
                if (mUtils.isLogin()) {
                    IntentUtils.IntentToInfoCenter(getActivity());
                } else {
                    IntentUtils.IntentToLogin(getActivity());
                }
                break;
            case R.id.frag_mine_login_:

                if (mUtils.isLogin()) {
                    Intent intentPersonal = new Intent(getActivity(), MinePersonalDataActivity.class);
                    startActivity(intentPersonal);
                } else {
                    IntentUtils.IntentToLogin(getActivity());
                }
                break;
//			case R.id.frag_mine_head_img:
//
//				break;
            case R.id.frag_mine_login_ll:
                if (mUtils.isLogin()) {
                    Intent intentPersonal = new Intent(getActivity(), MinePersonalDataActivity.class);
                    startActivity(intentPersonal);
                } else {
                    IntentUtils.IntentToLogin(getActivity());
                }
                break;
            case R.id.rl_wallet://钱包
                if (mUtils.isLogin()) {
                    Intent intentAccount = new Intent(getActivity(), MineAccount2Activity.class);
                    startActivity(intentAccount);
                } else {
                    IntentUtils.IntentToLogin(getActivity());
                }
                break;
            case R.id.rl_integration://积分
                if (mUtils.isLogin()) {
                    Intent intentScoring = new Intent(getActivity(), MineScoring1Activity.class);
                    startActivity(intentScoring);
                } else {
                    IntentUtils.IntentToLogin(getActivity());
                }
                break;
            case R.id.frag_mine_order_all:

                if (mUtils.isLogin()) {
                    Intent intentAll = new Intent(getActivity(), MineOrderActivity.class);
                    intentAll.putExtra("page", 0);
                    startActivity(intentAll);
                } else {
                    IntentUtils.IntentToLogin(getActivity());
                }
                break;
            case R.id.frag_mine_order_pay:

                if (mUtils.isLogin()) {
                    Intent intentPay = new Intent(getActivity(), MineOrderActivity.class);
                    intentPay.putExtra("page", 1);
                    startActivity(intentPay);
                } else {
                    IntentUtils.IntentToLogin(getActivity());
                }
                break;
            case R.id.frag_mine_order_send:
                if (mUtils.isLogin()) {
                    Intent intentSend = new Intent(getActivity(), MineOrderActivity.class);
                    intentSend.putExtra("page", 2);
                    startActivity(intentSend);
                } else {
                    IntentUtils.IntentToLogin(getActivity());
                }
                break;
            case R.id.frag_mine_order_get:
                if (mUtils.isLogin()) {
                    Intent intentGet = new Intent(getActivity(), MineOrderActivity.class);
                    intentGet.putExtra("page", 3);
                    startActivity(intentGet);
                } else {
                    IntentUtils.IntentToLogin(getActivity());
                }
                break;
            case R.id.frag_mine_order_judge:
                if (mUtils.isLogin()) {
                    Intent intentJudge = new Intent(getActivity(), MineOrderActivity.class);
                    intentJudge.putExtra("page", 4);
                    startActivity(intentJudge);
                } else {
                    IntentUtils.IntentToLogin(getActivity());
                }
                break;
            case R.id.frag_mine_order_drawback://退款
                if (mUtils.isLogin()) {
                    Intent intentJudge = new Intent(getActivity(), MineRefundListActivity.class);
                    startActivity(intentJudge);
                } else {
                    IntentUtils.IntentToLogin(getActivity());
                }
                break;
            case R.id.iv_qr:
                if (mUtils.isLogin()) {
                    Intent intentInvite = new Intent(getActivity(), MineInviteActivity.class);
                    startActivity(intentInvite);
                } else {
                    IntentUtils.IntentToLogin(getActivity());
                }
                break;
            case R.id.frag_mine_collection:
                if (mUtils.isLogin()) {
                    Intent intentCollection = new Intent(getActivity(), MineCollectionActivity.class);
                    startActivity(intentCollection);
                } else {
                    IntentUtils.IntentToLogin(getActivity());
                }
                break;
            case R.id.frag_mine_address://地址
                if (mUtils.isLogin()) {
                    Intent intentAddress = new Intent(getActivity(), MineAddressManageActivity.class);
                    startActivity(intentAddress);
                } else {
                    IntentUtils.IntentToLogin(getActivity());
                }
                break;
//            case R.id.frag_mine_signIn://签到中心
//                if (mUtils.isLogin()) {
//                    Intent intentSetting = new Intent(getActivity(), MineSignInActivity.class);
//                    startActivity(intentSetting);
//                } else {
//                    IntentUtils.IntentToLogin(getActivity());
//                }
//                break;
            case R.id.rl_coupon://我的优惠券
                if (mUtils.isLogin()) {
                    Intent intentCoupon = new Intent(getActivity(), MineCouponActivity.class);
                    startActivity(intentCoupon);
                } else {
                    IntentUtils.IntentToLogin(getActivity());
                }
                break;
            case R.id.frag_mine_coustom_service:
                if (mUtils.isLogin()) {
                    mineFragView.doCustomServices();
                } else {
                    IntentUtils.IntentToLogin(getActivity());
                }
                break;
//            case R.id.frag_mine_contact:
//                if (mUtils.isLogin()) {
//                    setCallDialog();
//                    mineFragView.showCallDialog(mDialog);
//                } else {
//                    IntentUtils.IntentToLogin(getActivity());
//                }
//                break;
            case R.id.frag_mine_rl_setting:

//				Intent intent = new Intent(getActivity(),MineRefundClassActivity.class);
//				intent.putExtra("mList", mList.get(position));
//				intent.putExtra("Money", mList.get(position).getMoney());
//				startActivity(intent);

                if (mUtils.isLogin()) {
                    Intent intentSetting = new Intent(getActivity(), MineSettingActivity.class);
                    startActivity(intentSetting);
                } else {
                    IntentUtils.IntentToLogin(getActivity());
                }
                break;
            case R.id.tv_login:
                IntentUtils.IntentToLogin(getActivity());
                break;
        }
    }

    private void setCallDialog() {
        if (mDialog == null) {
            mDialog = new CustomNormalContentDialog(getActivity());
        }
    }


    private MyReceiver receiver;//广播

    private void regReceiver() {
        //注册广播获取新收到的信息和未读消息数
        if (receiver == null) {
            receiver = new MyReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ZhiChiConstant.sobot_unreadCountBrocast);
        getActivity().registerReceiver(receiver, filter);
    }

    //设置广播获取新收到的信息和未读消息数
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int noReadNum = intent.getIntExtra("noReadCount", 0);
            String content = intent.getStringExtra("content");
            //未读消息数
            if (noReadNum != 0) {
//				tvCoustomNum.setVisibility(View.VISIBLE);
//				tvCoustomNum.setText(noReadNum + "");
            }
            //新消息内容
            com.sobot.chat.utils.LogUtils.i("新消息内容:" + content);
        }
    }


    @Override
    public void isNoLogin() {
        fragMineTvPayNum.setVisibility(View.GONE);
        fragMineTvSendNum.setVisibility(View.GONE);
        fragMineTvGetNum.setVisibility(View.GONE);
        fragMineTvJudgeNum.setVisibility(View.GONE);
        fragMineTvInfo.setVisibility(View.GONE);

    }

    @Override
    public void setLoginName(boolean isLoginName) {
        if (isLoginName) {
            llLogin.setVisibility(View.VISIBLE);
            mineNewTvLogin.setText(mUtils.getUserName());
            tvLogin.setVisibility(View.GONE);
        } else {
            llLogin.setVisibility(View.GONE);
            tvLogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void isLoginHeadImg(boolean isLoginHeadImg) {
        if (isLoginHeadImg) {
            Glide.with(getActivity().getApplicationContext())
                    .load(URLBuilder.getUrl(mUtils.getAvatar()))
                    .asBitmap()
                    .fitCenter()
                    .error(R.mipmap.default_avatar)
                    .into(fragMineLoginIv);
        } else {
            Glide.with(getActivity().getApplicationContext())
                    .load(R.mipmap.default_avatar)
                    .asBitmap()
                    .fitCenter()
                    .error(R.mipmap.default_avatar)
                    .into(fragMineLoginIv);

        }
    }


    @Override
    public void setCallPhone(String serviceTel) {
        this.serviceTel = serviceTel;
        if (new AuthorUtils(getActivity()).checkPermissions(needPermissions)) {
            setActionCall(serviceTel);
        }

    }

    private void setActionCall(String serviceTel) {
        //拨打电话
        if (serviceTel != null) {
            Intent callIntent = new Intent();
            callIntent.setAction(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + serviceTel));
            startActivity(callIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSON_REQUESTCODE:
                if (!AuthorUtils.verifyPermissions(grantResults)) {
                    isNeedCheck = false;
                } else {
                    isNeedCheck = true;
                }
                if (isNeedCheck) {
                    setActionCall(serviceTel);
                }
                break;
        }
    }


    @Override
    public void setSobotApi(Information userInfo) {
        //设置标题显示模式
        SobotApi.setChatTitleDisplayMode(getActivity().getApplicationContext(), SobotChatTitleDisplayMode.values()[0], "");
        //设置是否开启消息提醒
        SobotApi.setNotificationFlag(getActivity().getApplicationContext(), true, R.mipmap.logo, R.mipmap.logo);
        SobotApi.hideHistoryMsg(getActivity().getApplicationContext(), 0);
        SobotApi.setEvaluationCompletedExit(getActivity().getApplicationContext(), false);
        SobotApi.startSobotChat(getActivity(), userInfo);
    }

    @Override
    public void setDatas(MineEntity.MineData data) {
        preferencesUtil.setValue("userType", data.getUserType());
        mineFragView.setServiceTel(data.getServiceTel());
        if (!TextUtils.isEmpty(data.getServiceTel())) {
            mUtils.saveServiceTel(data.getServiceTel());
        }
        if (!TextUtils.isEmpty(data.getOrderpay())) {
            if (Integer.parseInt(data.getOrderpay()) > 99) {
                fragMineTvPayNum.setText("99+");
            } else {
                fragMineTvPayNum.setText(data.getOrderpay());
            }
            fragMineTvPayNum.setVisibility(View.VISIBLE);
        } else {
            fragMineTvPayNum.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(data.getOrdersend())) {
            if (Integer.parseInt(data.getOrdersend()) > 99) {
                fragMineTvSendNum.setText("99+");
            } else {
                fragMineTvSendNum.setText(data.getOrdersend());
            }
            fragMineTvSendNum.setVisibility(View.VISIBLE);

        } else {
            fragMineTvSendNum.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(data.getOrderget())) {
            if (Integer.parseInt(data.getOrderget()) > 99) {
                fragMineTvGetNum.setText("99+");
            } else {
                fragMineTvGetNum.setText(data.getOrderget());
            }
            fragMineTvGetNum.setVisibility(View.VISIBLE);
        } else {
            fragMineTvGetNum.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(data.getOrderjudge())) {
            if (Integer.parseInt(data.getOrderjudge()) > 99) {
                fragMineTvJudgeNum.setText("99+");
            } else {
                fragMineTvJudgeNum.setText(data.getOrderjudge());
            }
            fragMineTvJudgeNum.setVisibility(View.VISIBLE);
        } else {
            fragMineTvJudgeNum.setVisibility(View.GONE);
        }
    }

    @Override
    public void setTel(boolean b, String replace) {
        if (b) {
            mineNewTvTel.setVisibility(View.VISIBLE);
        }
        if (b) {
            mineNewTvTel.setText(replace);
        } else {
            mineNewTvTel.setText(mUtils.getTel());
        }
    }

    @Override
    public void setMineNewNum(boolean b, NormalEntity response) {
        if (b) {
            String text = response.getData().toString();
            fragMineTvInfo.setVisibility(View.VISIBLE);
            int i = (int) Float.parseFloat(text);
            if (i > 99) {
                fragMineTvInfo.setText("99");
            } else {
                fragMineTvInfo.setText(i + "");
            }

        } else {
            fragMineTvInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public void showToast() {
        ToastUtils.showToast(getActivity(), "无法获取联系电话，请检查网络稍后再试");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mineFragView.dismissDialog(mDialog);
        getActivity().unregisterReceiver(receiver);
    }
}
