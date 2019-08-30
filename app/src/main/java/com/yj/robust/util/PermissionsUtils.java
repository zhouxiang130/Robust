package com.yj.robust.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/29 0029.
 */

public class PermissionsUtils {


	/**
	 * @since 2.5.0
	 */
//	private boolean checkPermissions(Context context,int PERMISSON_REQUESTCODE, String... permissions) {
//		List<String> needRequestPermissonList = findDeniedPermissions(permissions,context);
//		if (null != needRequestPermissonList && needRequestPermissonList.size() > 0) {
////			ActivityCompat.requestPermissions(this, needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]), PERMISSON_REQUESTCODE);
//		}
//		if (needRequestPermissonList.size() == PERMISSON_REQUESTCODE) {
//			//版本有更新
//			return true;
//		} else {
//
//			return false;
//		}
//	}

	/**
	 * 获取权限集中需要申请权限的列表
	 *
	 * @param permissions
	 * @return
	 * @since 2.5.0
	 */
//	private List<String> findDeniedPermissions(String[] permissions,Context context) {
//		List<String> needRequestPermissonList = new ArrayList<>();
//		for (String perm : permissions) {
//			if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(context, perm)) {
//				needRequestPermissonList.add(perm);
//			}
//		}
//		return needRequestPermissonList;
//	}

}
