package com.yj.robust.ui.activity.mineAccount;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yj.robust.base.BasePresenter;
import com.yj.robust.base.BaseView;
import com.yj.robust.model.AccountEntity;
import com.yj.robust.model.AccountProfitEntity;
import com.yj.robust.util.UserUtils;
import com.yj.robust.widget.ProgressLayout;

import java.util.List;

/**
 * Created by Administrator on 2018/6/5 0005.
 */

public interface MineAccount_contract {

	interface View extends BaseView {

		void initDialog();

		void showToast(String o);

		void setDatas(AccountEntity.AccountData data);

		void dismissDialog();

		void notifyDataSetChanged();


		void pageNumReduce();
	}

	interface Presenter extends BasePresenter {


		void doAsyncGetData(UserUtils mUtils);

		void doRefreshData(int pageNum, ProgressLayout mProgressLayout, XRecyclerView mRecyclerView, List<AccountProfitEntity.AccountProfitData> mList);

		void doRequestData(int pageNum, ProgressLayout mProgressLayout, XRecyclerView mRecyclerView, List<AccountProfitEntity.AccountProfitData> mList);
	}
}
