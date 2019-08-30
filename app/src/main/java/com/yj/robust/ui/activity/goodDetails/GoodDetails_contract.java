package com.yj.robust.ui.activity.goodDetails;

import com.yj.robust.base.BasePresenter;
import com.yj.robust.base.BaseView;
import com.yj.robust.util.UserUtils;

/**
 * Created by Administrator on 2018/7/17 0017.
 */

public interface GoodDetails_contract {


	interface View extends BaseView {

		void isAsynGetDetailBefore();

		void setData();

		void dismissDialog();
	}

	interface Presenter extends BasePresenter {


		void doAsyncGetDetial(UserUtils mUtils, String productId, String sproductId);
	}
}
