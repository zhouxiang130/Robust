package com.yj.robust.ui.adapter;

import com.yj.robust.model.CartsEntity;

import java.util.List;

/**
 * Created by Horrarndoo on 2017/11/24.
 * <p>
 */

public interface OnCheckChangeListener {
    void onCheckedChanged(List<CartsEntity.DataBean.ProCartsBean> beans, int position, boolean isChecked, int itemType);
}
