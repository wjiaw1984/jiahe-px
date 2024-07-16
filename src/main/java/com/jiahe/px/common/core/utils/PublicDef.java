package com.jiahe.px.common.core.utils;


public class PublicDef {
	/**
	 * 字符串拼接 性能最高的方法
	 */
	public static String strBuilder(String str1, String str2) {
		StringBuilder sb = new StringBuilder(str1);
		sb.append(str2);
		return sb.toString();
	}

}
