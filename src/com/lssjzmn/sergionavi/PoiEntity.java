package com.lssjzmn.sergionavi;

//import android.support.design.widget.TabLayout;

public class PoiEntity {

	private String city;
	private String name;
	private String address;
	private String phoneNum;
	//private TabLayout mTabLayout;

	public PoiEntity() {

	}

	public PoiEntity(String city, String name, String address, String phoneNum) {
		super();
		this.city = city;
		this.name = name;
		this.address = address;
		this.phoneNum = phoneNum;
	}

	public String getCity() {
		return city;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getPhone() {
		return phoneNum;
	}

}
