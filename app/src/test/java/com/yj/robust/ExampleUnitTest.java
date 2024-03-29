package com.yj.robust;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
	@Test
	public void addition_isCorrect() throws Exception {
		assertEquals(4, 2 + 2);
	}

	@Test
	public void text() {
//        <----------------------首先构造一个list<map> ----------------------->
		String  old_version = "2.1.0.123";
		String   new_version = "2.1.00.122";
		//结果说明：0代表相等，1代表version1大于version2，-1代表version1小于version2
//		System.out.println(compareVersion(new_version,old_version));
		System.out.println("TAG>>>>>>>>>-----     "+ compareVersions(old_version, new_version) );
	}

	/**
	 * 版本号对比
	 *
	 * @param oldVersion
	 * @param newVersion
	 * @return error : 返回-2 既传入版本号格式有误
	 * oldVersion > newVersion  return -1
	 * oldVersion = newVersion  return 0
	 * oldVersion < newVersion  return 1
	 */
	public static int compareVersions(String oldVersion, String newVersion) {
		//返回结果: -2 ,-1 ,0 ,1
		int result = 0;
		String matchStr = "[0-9]+(\\.[0-9]+)*";
		oldVersion = oldVersion.trim();
		newVersion = newVersion.trim();
		//非版本号格式,返回error
		if (!oldVersion.matches(matchStr) || !newVersion.matches(matchStr)) {
			return -2;
		}
		String[] oldVs = oldVersion.split("\\.");
		String[] newVs = newVersion.split("\\.");
		int oldLen = oldVs.length;
		int newLen = newVs.length;
		int len = oldLen > newLen ? oldLen : newLen;
		for (int i = 0; i < len; i++) {
			if (i < oldLen && i < newLen) {
				int o = Integer.parseInt(oldVs[i]);
				int n = Integer.parseInt(newVs[i]);
				if (o > n) {
					result = -1;
					break;
				} else if (o < n) {
					result = 1;
					break;
				}
			} else {
				if (oldLen > newLen) {
					for (int j = i; j < oldLen; j++) {
						if (Integer.parseInt(oldVs[j]) > 0) {
							result = -1;
						}
					}
					break;
				} else if (oldLen < newLen) {
					for (int j = i; j < newLen; j++) {
						if (Integer.parseInt(newVs[j]) > 0) {
							result = 1;
						}
					}
					break;
				}
			}
		}
		return result;
	}



}