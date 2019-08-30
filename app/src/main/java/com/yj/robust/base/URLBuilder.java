package com.yj.robust.base;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Suo on 2017/4/12.
 */

public class URLBuilder {

//	public static final String URLBaseHeader = "http://www.wendiapp.com";//正式服务器/
//	public static final String URLBaseHeaders = "https://www.wendiapp.com";//部分使用 https:处理加密操作

	public static final String URLBaseHeader = "http://192.168.101.18:8085";//测试接口2
//	public static final String URLBaseHeader = "http://192.168.0.114:8088";//测试接口
//	public static final String URLBaseHeader = "http://192.168.0.108";//测试接口3

	public static final String Login = "/phone/user/login.shtm";
	public static final String ModifyPass = "/phone/user/findPassword.act";
	public static final String SendMsg = "/phone/user/verificationCodeSession.act";
	public static final String Regist = "/phone/user/register.shtm";//注册
	public static final String UpdateHeader = "/phone/user/updateUserHeadimg.act";
	public static final String OpenIdVerify = "/phone/user/quick.act";
	public static final String CompanyDes = "/page/desc.jsp";
	public static final String Agreement = "/page/serviceDesc.jsp";
	public static final String defaultAddressToIndex = "/phone/userUp/defaultAddressToIndex.act";//默认地址

	public static final String ScoreRule = "/phone/homePage/rule.do?type=1";
	public static final String InviteRule = "/phone/homePage/rule.do?type=2";

	public static String getUrl(String url) {
		if (url.startsWith("http:")) {
			return url;
		} else {
//			if (url.startsWith("/")) {
//				url = url.subSequence(1, url.length()).toString();
//			}
			return URLBaseHeader + url;
		}
	}


	public static String getURLs(String url) {
		if (url.startsWith("/")) {
			return url;
		} else {
			url = new StringBuffer(url).insert(0, "/") + "";
		}
		return url;
	}


	public static String format(Map<String, String> obj) {
		JSONObject object = new JSONObject(obj);
		return object.toString().trim();
	}

}
