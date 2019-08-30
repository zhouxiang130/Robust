package com.yj.robust.ui.activity.mineSignIn;

import com.yj.robust.base.BasePresenter;
import com.yj.robust.base.BaseView;
import com.yj.robust.model.SignInEntity;

/**
 * Created by Administrator on 2018/6/6 0006.
 */

public interface MineSign_contract {

	interface View extends BaseView {

		void setSignInData(SignInEntity.DataBean data);
	}

	interface Presenter extends BasePresenter {

		void doAsyncGetSignIn(String uid);
	}

}
