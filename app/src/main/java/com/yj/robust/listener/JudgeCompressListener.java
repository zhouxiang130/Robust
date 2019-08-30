package com.yj.robust.listener;


import com.yj.robust.model.JudgeGoodsData;
import com.yj.robust.util.luban.OnCompressListener;

/**
 * Created by Suo on 2017/9/21.
 */

public interface JudgeCompressListener extends OnCompressListener {

    void onJudgeSuccess(JudgeGoodsData data);
}
