package com.yj.robust.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.yj.robust.MyApplication;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


/**
 * Created by Administrator on 2018/7/24 0024.
 */

public class LocationUtil {

	private static final String TAG = "LocationUtil";

	private static LocationUtil instance;
	private static Activity mActivity;
	private static LocationManager locationManager;
	private static AMapLocationClient locationClient = null;
	private static AMapLocationClientOption locationOption = null;

	public static LocationUtil getInstance(Activity activity) {
		mActivity = activity;
		if (instance == null) {
			instance = new LocationUtil();
		}
		locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
		return instance;
	}


	public static AMapLocation getAMapLocation(Context context) {
		if (locationClient == null) {
			locationClient = new AMapLocationClient(context);
		}
		locationOption = new AMapLocationClientOption();

		// 设置定位模式为高精度模式
		locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
		//设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		//设置是否返回地址信息（默认返回地址信息）
		locationOption.setNeedAddress(true);
		//设置是否只定位一次,默认为false
		locationOption.setOnceLocation(false);
		//设置是否强制刷新WIFI，默认为强制刷新
		locationOption.setWifiActiveScan(false);
		//设置是否允许模拟位置,默认为false，不允许模拟位置
		locationOption.setMockEnable(false);
		//设置定位间隔,单位毫秒,默认为2000ms
		locationOption.setInterval(2000);
		//关闭缓存机制 默认开启 ，在高精度模式和低功耗模式下进行的网络定位结果均会生成本地缓存,不区分单次定位还是连续定位。GPS定位结果不会被缓存。
		locationOption.setLocationCacheEnable(true);
		//启动定位
		locationClient.startLocation();
		AMapLocation location = locationClient.getLastKnownLocation();
		String locationStr = LocUtils.getLocationStr(location);
		Log.i(TAG, "getAMapLocation: " + locationStr);
		return location;
	}


	private static SimpleDateFormat sdf = null;

	public static String formatUTC(long l, String strPattern) {
		if (TextUtils.isEmpty(strPattern)) {
			strPattern = "yyyy-MM-dd HH:mm:ss";
		}
		if (sdf == null) {
			try {
				sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
			} catch (Throwable e) {
			}
		} else {
			sdf.applyPattern(strPattern);
		}
		return sdf == null ? "NULL" : sdf.format(l);
	}


	public static void onDestroy() {
		if (null != locationClient) {
			Log.e(TAG, " locationClient onDestroy: ");
			locationClient.onDestroy();
			locationClient.stopLocation();
			locationClient = null;
		}
	}


	/**
	 * 判断GPS导航是否打开.
	 * false：弹窗提示打开,不建议采用在后台强行开启的方式。
	 * true:不做任何处理
	 *
	 * @return
	 */
	public static void isOpenGPS() {

		if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
			dialog.setMessage("GPS未打开，是否打开?");
			dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					// 设置完成后返回到原来的界面
					mActivity.startActivityForResult(intent, 0);
				}
			});
			dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			dialog.show();
		}
	}

	/**
	 * 通过LocationListener来获取Location信息
	 */
	public static void formListenerGetLocation() {

		LocationListener locationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				//位置信息变化时触发
				Log.i(TAG, "formListenerGetLocation 纬度：" + location.getLatitude());
				Log.i(TAG, "formListenerGetLocation 经度：" + location.getLongitude());
				Log.i(TAG, "formListenerGetLocation 海拔：" + location.getAltitude());
				Log.i(TAG, "formListenerGetLocation 时间：" + location.getTime());
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				//GPS状态变化时触发
			}

			@Override
			public void onProviderEnabled(String provider) {
				//GPS禁用时触发
			}

			@Override
			public void onProviderDisabled(String provider) {
				//GPS开启时触发
			}
		};
		/**
		 * 绑定监听
		 * 参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种，前者是GPS,后者是GPRS以及WIFI定位
		 * 参数2，位置信息更新周期.单位是毫秒
		 * 参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
		 * 参数4，监听
		 * 备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新
		 */
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	}

	/**
	 * 主动获取Location，通过以下方法获取到的是最后一次定位信息。
	 * 注意：Location location=new Location(LocationManager.GPS_PROVIDER)方式获取的location的各个参数值都是为0。
	 */
	public static Location getLocation() {

		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null) {
			Log.i(TAG, "getLocation 纬度：" + location.getLatitude());
			Log.i(TAG, "getLocation 经度：" + location.getLongitude());
			Log.i(TAG, "getLocation 海拔：" + location.getAltitude());
			Log.i(TAG, "getLocation 时间：" + location.getTime());
			Log.i(TAG, "getLocation toString：" + location.toString());

		}


		return location;
	}

	// 获取地址信息
	public List<Address> getAddress(Location location) {
		List<Address> result = null;
		try {
			if (location != null) {
				Geocoder gc = new Geocoder(mActivity, Locale.getDefault());
				result = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取GPS状态监听，包括GPS启动、停止、第一次定位、卫星变化等事件。
	 */
	public static void getStatusListener() {

		GpsStatus.Listener listener = new GpsStatus.Listener() {

			@Override
			public void onGpsStatusChanged(int event) {
				if (event == GpsStatus.GPS_EVENT_FIRST_FIX) {
					//第一次定位
				} else if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
					//卫星状态改变
					GpsStatus gpsStauts = locationManager.getGpsStatus(null); // 取当前状态
					int maxSatellites = gpsStauts.getMaxSatellites(); //获取卫星颗数的默认最大值
					Iterator<GpsSatellite> it = gpsStauts.getSatellites().iterator();//创建一个迭代器保存所有卫星
					int count = 0;
					while (it.hasNext() && count <= maxSatellites) {
						count++;
						GpsSatellite s = it.next();
					}
					Log.i(TAG, "搜索到：" + count + "颗卫星");
				} else if (event == GpsStatus.GPS_EVENT_STARTED) {
					//定位启动
				} else if (event == GpsStatus.GPS_EVENT_STOPPED) {
					//定位结束
				}
			}
		};
		//绑定
		locationManager.addGpsStatusListener(listener);
	}

	/**
	 * 获取所有卫星状态
	 *
	 * @return
	 */
	public static List<GpsSatellite> getGpsStatus() {
		List<GpsSatellite> result = new ArrayList<GpsSatellite>();
		GpsStatus gpsStatus = locationManager.getGpsStatus(null); // 取当前状态
		//获取默认最大卫星数
		int maxSatellites = gpsStatus.getMaxSatellites();
		//获取第一次定位时间（启动到第一次定位）
		int costTime = gpsStatus.getTimeToFirstFix();
		Log.i(TAG, "第一次定位时间:" + costTime);
		//获取卫星
		Iterable<GpsSatellite> iterable = gpsStatus.getSatellites();
		//一般再次转换成Iterator
		Iterator<GpsSatellite> itrator = iterable.iterator();
		int count = 0;
		while (itrator.hasNext() && count <= maxSatellites) {
			count++;
			GpsSatellite s = itrator.next();
			result.add(s);
		}
		return result;
	}

	/**
	 * 某一个卫星的信息.
	 *
	 * @param gpssatellite
	 */
	public static void getGpsStatelliteInfo(GpsSatellite gpssatellite) {

		//卫星的方位角，浮点型数据
		Log.i(TAG, "卫星的方位角：" + gpssatellite.getAzimuth());
		//卫星的高度，浮点型数据
		Log.i(TAG, "卫星的高度：" + gpssatellite.getElevation());
		//卫星的伪随机噪声码，整形数据
		Log.i(TAG, "卫星的伪随机噪声码：" + gpssatellite.getPrn());
		//卫星的信噪比，浮点型数据
		Log.i(TAG, "卫星的信噪比：" + gpssatellite.getSnr());
		//卫星是否有年历表，布尔型数据
		Log.i(TAG, "卫星是否有年历表：" + gpssatellite.hasAlmanac());
		//卫星是否有星历表，布尔型数据
		Log.i(TAG, "卫星是否有星历表：" + gpssatellite.hasEphemeris());
		//卫星是否被用于近期的GPS修正计算
		Log.i(TAG, "卫星是否被用于近期的GPS修正计算：" + gpssatellite.hasAlmanac());
	}


}
