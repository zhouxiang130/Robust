package com.yj.robust.ui.fragment.MineCouponDetail;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yj.robust.base.BasePresenter;
import com.yj.robust.base.BaseView;
import com.yj.robust.model.CouponListDataEntity;
import com.yj.robust.util.UserUtils;
import com.yj.robust.widget.ProgressLayout;

import java.util.List;

/**
 * Created by Administrator on 2018/6/7 0007.
 */

public interface MineCouponDetail_contract {


	interface View extends BaseView {
		void adapterNotifyDataChanged();

		void showToast(String s);

		void initDatas(int UserPossession);
	}

	interface Presenter extends BasePresenter {
		void doRefreshData(ProgressLayout mProgressLayout, UserUtils mUtils, XRecyclerView mRecyclerView, List<CouponListDataEntity.DataBean.ListBean> mList, String flag);

		void doRequestData(ProgressLayout mProgressLayout, UserUtils mUtils, XRecyclerView mRecyclerView, List<CouponListDataEntity.DataBean.ListBean> mList, String flag);
	}

}
