package com.jiahe.px.common.core.utils.osinfo;

public class GetOSInfo {
	public static String getOSName() {
		return System.getProperty("os.name").toLowerCase();
	}
}
