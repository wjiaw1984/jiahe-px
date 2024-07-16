package com.jiahe.px.common.core.utils.osinfo;

import com.efuture.myshop.core.utils.Convert;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dengyongdong on 2017/6/8.
 * modify by wujiawen on 2019/11/12
 */
@Slf4j
public class GetMacUtil {
    public static void main(String[] args) {
        getLocalMac();
    }

    private static Map<String, String> macMap = null;

    public static String getLocalMac() {
        String mac = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(Runtime.getRuntime().exec("ipconfig /all").getInputStream()));
            String line = null;
            int index = -1;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                index = line.toLowerCase().indexOf("physical address");
                if (index >= 0) {
                    index = line.indexOf(":");
                    String macOut = line.substring(index + 1).trim();
                    if (macOut.length() == 17) {
                        mac = macOut;
                        break;
                    }
                }
            }

            if (mac == null) {
                bufferedReader = new BufferedReader(
                        new InputStreamReader(Runtime.getRuntime().exec("ipconfig /all").getInputStream(), "GBK"));
                while ((line = bufferedReader.readLine()) != null) {
                    index = line.toLowerCase().indexOf("物理地址");
                    if (index >= 0) {
                        index = line.indexOf(":");
                        String macOut = line.substring(index + 1).trim();
                        if (macOut.length() == 17) {
                            mac = macOut;
                            break;
                        }
                    }
                }
            }
            log.info("读取到的MAC地址为:" + mac);
            System.out.println("读取到的MAC地址为:" + mac);
            return mac;
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static Map<String, String> getLocalMacWinToMap() {
        Map<String, String> map = new HashMap<String, String>();
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(Runtime.getRuntime().exec("ipconfig /all").getInputStream()));
            String line = null;
            int index = -1;
            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);
                index = line.toLowerCase().indexOf("physical address");
                if (index >= 0) {
                    index = line.indexOf(":");
                    String macOut = line.substring(index + 1).trim();
                    if (macOut.length() == 17) {
                        map.put(Convert.ToString(i), macOut);
                        i++;
                    }
                }
            }

            if (map.size() == 0) {
                bufferedReader = new BufferedReader(
                        new InputStreamReader(Runtime.getRuntime().exec("ipconfig /all").getInputStream(), "GBK"));
                while ((line = bufferedReader.readLine()) != null) {
                    index = line.toLowerCase().indexOf("物理地址");
                    if (index >= 0) {
                        index = line.indexOf(":");
                        String macOut = line.substring(index + 1).trim();
                        if (macOut.length() == 17) {
                            map.put(Convert.ToString(i), macOut);
                            i++;
                        }
                    }
                }
            }
            log.info("读取到的MAC地址为:" + map.toString());
            System.out.println("读取到的MAC地址列表 :" + map.toString());
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, String> getLocalMacLinuxToMap() {
        Map<String, String> map = new HashMap<String, String>();
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(Runtime.getRuntime().exec("ifconfig").getInputStream()));
            String line = null;
            int index = -1;
            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                index = line.indexOf("HWaddr");
                if (index >= 0) {
                    index = line.indexOf("HWaddr");
                    String macOut = line.substring(index + 6).trim();
                    macOut = macOut.replace(":", "-");
                    map.put(Convert.ToString(i), macOut);
                    i++;
                }
            }
            log.info("读取到的MAC地址为:" + map.toString());
            System.out.println("读取到的MAC地址列表 :" + map.toString());
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, String> getLocalMacMap() {
        try {
            if (macMap == null) {
                String os = GetOSInfo.getOSName();
                System.out.println(os);
                if (os.startsWith("windows")) {
                    // 本地是windows
                    macMap = getMac();
                } else {
                    // 本地是非windows系统 一般就是unix
                    macMap = getMac();
                }
            }
            return macMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, String> getMac() {
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			byte[] mac = null;
			Map<String,String> result = new HashMap<>();
			int t = 0;
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				/*if (netInterface.isLoopback()
						//虚拟网卡
						*//*|| netInterface.isVirtual()*//*
						|| !netInterface.isUp()) {
					continue;
				} else {*/
					mac = netInterface.getHardwareAddress();
					if (mac != null) {
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < mac.length; i++) {
							sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                            //result.put(i,)
						}
						result.put(Convert.ToString(t),sb.toString());
						t++;
					}
//				}
			}
			return result;
		} catch (Exception e) {
			log.error("MAC地址获取失败", e);
		}
		return null;
    }

}
