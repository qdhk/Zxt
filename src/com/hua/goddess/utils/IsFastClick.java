/**
 * 
 */
package com.hua.goddess.utils;

/**
 * @author Administrator
 * 
 */
public class IsFastClick {

	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 3000) { // 内按钮无效，这样可以控制快速点击，自己调整频率
			return true;
		}
		lastClickTime = time;
		return false;
	}
}
