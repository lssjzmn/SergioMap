package com.lssjzmn.sergionavi;

import java.util.HashMap;

public class HotCitys {

	private HashMap<Object, String> cityMap;
	private String[] cityKey = { "beijing", "shanghai", "guangzhou",
			"shenzheng", "chengdu", "tianjing", "hangzhou", "nanjing", "wuhan",
			"chongqing", "xianggang", "taibei" };
	public String[] cityName = { "北京", "上海", "广州", "深圳", "成都 ", "天津", "南京",
			"杭州", "武汉", "重庆", "沈阳", "西安" };
	private int cityLengh = 0;

	public HotCitys() {
		cityMap = new HashMap<Object, String>();
		for (int i = 0; i < 10; i++) {
			cityMap.put(cityKey[i], cityName[i]);
		}

	}

	public HashMap<Object, String> getCityMap() {
		return cityMap;
	}

	public int getCityLengh() {
		cityLengh = cityMap.size();
		return cityLengh;
	}

	public String[] getCityName() {
		return cityName;
	}

}
