/**
 * @author lssjzmn
 * SergioNavi Demo 
 */
package com.lssjzmn.sergionavi;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.bidaround.youtui_template.YouTuiViewType;
import cn.bidaround.youtui_template.YtTemplate;
import cn.bidaround.ytcore.YtShareListener;
import cn.bidaround.ytcore.data.ShareData;
import cn.bidaround.ytcore.data.YtPlatform;
import cn.bidaround.ytcore.util.HttpUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapDoubleClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.*;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.poi.IllegalPoiSearchArgumentException;
import com.jensdriller.libs.undobar.BottomBar;
import com.jensdriller.libs.undobar.BottomBar.UndoBarListener;
import com.lssjzmn.sergionavi.MyGridLayout;
import com.lssjzmn.sergionavi.MyGridLayout.GridAdatper;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnMapClickListener,
		OnMapDoubleClickListener, OnMapLoadedCallback {
	private MapView mapView = null;
	private BaiduMap baiduMap = null;
	private BitmapDescriptor bitmap = null;
	private Bitmap shareBitmap = null;
	private File mapImgFile = null;
	private UiSettings uiSettings = null;
	private InfoWindow siteInfoWindow;
	private LocationClient mLocationClient = null;
	private LatLng myPoint = null;
	private int choisenCityMsg = 0;
	private long exitTime = 0;
	private DrawerLayout drawerLayout = null;
	private MyGridLayout gridLayout;
	private View cityGridView;
	private Dialog cityDialog;
	private boolean isDrawerOpened = false;
	private boolean isTraficEnabled = false;
	private boolean isSateliteEnabled = false;
	private boolean isLocationSuccess = false;
	private boolean isLocChecked;
	private SharedPreferences preferLocCheck;
	private SharedPreferences.Editor preferLocCheckEdit;
	private EditText editText;
	private TextWatcher searchTextWatcher;
	private String searchContent = "";
	private String enter = "\n";
	private String shareContent = null;
	private Button searchBtn;
	private ListView listViewDrawer;
	private CheckedTextView checkLoc;
	private ImageView traficIndicator;
	private ImageView sateliteIndicator;
	/** 是否使用积分系统 */
	private boolean usePointSys = false;

	/** 点击社交平台后，是否隐藏分享界面, 默认不显示 */
	private boolean isShowSharePop = false;

	/** 分享的数据对象 */
	private ShareData shareData;

	/** 分享的界面平台 */
	private YtTemplate ytUITemplate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		startActivity(new Intent(getApplicationContext(), InitActivity.class));
		setContentView(R.layout.activity_main);
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHomeAction(new HomeAction());
		actionBar.addAction(new LocAction());
		actionBar.addAction(new baiduAction());
		editText = (EditText) findViewById(R.id.edittext);
		searchBtn = (Button) findViewById(R.id.searchBtn);
		listViewDrawer = (ListView) findViewById(R.id.listdrawer);
		checkLoc = (CheckedTextView) findViewById(R.id.checkloc);
		preferLocCheck = this.getSharedPreferences("locCheck", 0);
		traficIndicator = (ImageView) findViewById(R.id.traficindicator);
		sateliteIndicator = (ImageView) findViewById(R.id.sateliteindicator);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
		mapView = (MapView) findViewById(R.id.mapView);
		baiduMap = mapView.getMap();
		uiSettings = baiduMap.getUiSettings();
		uiSettings.setCompassEnabled(true);
		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);// 普通地图
		baiduMap.setOnMapClickListener(this);
		baiduMap.setOnMapDoubleClickListener(this);
		baiduMap.setOnMapLoadedCallback(this);
		// baiduMap.setBaiduHeatMapEnabled(true);//热力图
		bitmap = BitmapDescriptorFactory.fromResource(R.drawable.here);
		searchTextWatcher = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable txt) {
				searchContent = txt.toString();
			}
		};
		editText.addTextChangedListener(searchTextWatcher);
		checkLoc.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				checkLoc.toggle();
				isLocChecked = !isLocChecked;
			}
		});
		isLocChecked = preferLocCheck.getBoolean("locCheck", false);
		checkLoc.setChecked(isLocChecked);
		if (isLocChecked)
			RequestLocation(5000, true);
		YtTemplate.init(this);// 初始化友推分享功能
		final String[] txtdrawer = { "分享位置", "离线数据", "热门城市", "发现更多" };
		final int imgdrawer[] = { android.R.drawable.ic_menu_share,
				android.R.drawable.ic_menu_compass,
				android.R.drawable.ic_menu_add, android.R.drawable.ic_menu_sort_by_size };
		final int[] cityLogos = { R.drawable.actions_booktag,
				R.drawable.actions_comment, R.drawable.actions_order,
				R.drawable.actions_account, R.drawable.actions_cent,
				R.drawable.actions_weibo, R.drawable.actions_feedback,
				R.drawable.actions_about, R.drawable.actions_booktag,
				R.drawable.actions_comment, R.drawable.actions_order,
				R.drawable.actions_account };
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < txtdrawer.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("imgdrawer", imgdrawer[i]);
			map.put("txtdrawer", txtdrawer[i]);
			list.add(map);
		}
		SimpleAdapter simpleAdapterDrawer = new SimpleAdapter(
				getApplicationContext(), list, R.layout.simpleadapterdrawer,
				new String[] { "imgdrawer", "txtdrawer" }, new int[] { R.id.simpleimgdrawer,
						R.id.simpletxtdrawer });
		listViewDrawer.setAdapter(simpleAdapterDrawer);
		listViewDrawer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int item,
					long arg3) {
				if (siteInfoWindow != null)
					baiduMap.hideInfoWindow();
				if (mLocationClient != null) {
					mLocationClient.stop();
					mLocationClient = null;
				}
				switch (item) {
				case 0:
					drawerLayout.closeDrawer(Gravity.LEFT);
					isDrawerOpened = false;
					shareMyLoc();// 系统分享菜单
					/*baiduMap.snapshot(new SnapshotReadyCallback() {
						public void onSnapshotReady(Bitmap mapBitmap) {
							mapBitmap = Bitmap.createScaledBitmap(mapBitmap,
									480, 800, false);
							shareBitmap = mapBitmap;
							getMapImgFile();
							saveMapBitmap(mapImgFile, shareBitmap);
							handler.sendEmptyMessage(12);
						}
					});*/

					break;
				case 1:
					startActivity(new Intent(getApplicationContext(),
							DownloadActivity.class));
					break;
				case 2:
					final HotCitys hotCity = new HotCitys();
					LayoutInflater inflater = getLayoutInflater();
					cityGridView = inflater.inflate(R.layout.hotcitygridlay,
							null);
					gridLayout = (MyGridLayout) cityGridView
							.findViewById(R.id.citygrid);
					gridLayout.setGridAdapter(new GridAdatper() {
						public View getView(int index) {
							LayoutInflater inflater = getLayoutInflater();
							View cityItemView = inflater.inflate(
									R.layout.hotcityitemlay, null);
							ImageView cityImage = (ImageView) cityItemView
									.findViewById(R.id.cityImage);
							TextView cityText = (TextView) cityItemView
									.findViewById(R.id.cityText);
							cityImage.setImageResource(cityLogos[index]);
							cityText.setText((hotCity.getCityName())[index]);
							return cityItemView;
						}

						public int getCount() {
							return hotCity.getCityName().length;
						}
					});
					gridLayout
							.setOnItemClickListener(new com.lssjzmn.sergionavi.MyGridLayout.OnItemClickListener() {
								public void onItemClick(View v, int index) {
									switch (index) {
									case 0:
										choisenCityMsg = 0;
										break;
									case 1:
										choisenCityMsg = 1;
										break;
									case 2:
										choisenCityMsg = 2;
										break;
									case 3:
										choisenCityMsg = 3;
										break;
									case 4:
										choisenCityMsg = 4;
										break;
									case 5:
										choisenCityMsg = 5;
										break;
									case 6:
										choisenCityMsg = 6;
										break;
									case 7:
										choisenCityMsg = 7;
										break;
									case 8:
										choisenCityMsg = 8;
										break;
									case 9:
										choisenCityMsg = 9;
										break;
									case 10:
										choisenCityMsg = 10;
										break;
									case 11:
										choisenCityMsg = 11;
										break;
									default:
										choisenCityMsg = 4;
										break;
									}
									cityDialog.dismiss();
									if (mLocationClient != null
											&& mLocationClient.isStarted())
										mLocationClient.stop();
									handler.sendEmptyMessage(choisenCityMsg);
								}
							});
					Builder cityGridBuilder = new android.app.AlertDialog.Builder(
							MainActivity.this);
					cityGridBuilder
							.setCancelable(true)
							.setView(cityGridView)
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface arg0, int arg1) {
										}
									});
					cityDialog = cityGridBuilder.create();
					cityDialog.show();
					break;
				case 3:
					startActivity(new Intent(getApplicationContext(),
							ProposalActivity.class));
					break;
				default:
				}
				drawerLayout.closeDrawer(Gravity.LEFT);
				isDrawerOpened = false;
			}
		});

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				LatLng hotCityPoint0 = new LatLng(39.91667, 116.41667);
				getHotCityLocation(baiduMap, hotCityPoint0, null);
				break;
			case 1:
				LatLng hotCityPoint1 = new LatLng(31.15, 121.32);
				getHotCityLocation(baiduMap, hotCityPoint1, null);
				break;
			case 2:
				LatLng hotCityPoint2 = new LatLng(23.129163, 113.264435);
				getHotCityLocation(baiduMap, hotCityPoint2, null);
				break;
			case 3:
				LatLng hotCityPoint3 = new LatLng(22.61667, 114.06667);
				getHotCityLocation(baiduMap, hotCityPoint3, null);
				break;
			case 4:
				LatLng hotCityPoint4 = new LatLng(30.66667, 104.06667);
				getHotCityLocation(baiduMap, hotCityPoint4, null);
				break;
			case 5:
				LatLng hotCityPoint5 = new LatLng(39.80000, 117.28333);
				getHotCityLocation(baiduMap, hotCityPoint5, null);
				break;
			case 6:
				LatLng hotCityPoint6 = new LatLng(32.05000, 118.78333);
				getHotCityLocation(baiduMap, hotCityPoint6, null);
				break;
			case 7:
				LatLng hotCityPoint7 = new LatLng(30.26667, 120.20000);
				getHotCityLocation(baiduMap, hotCityPoint7, null);
				break;
			case 8:
				LatLng hotCityPoint8 = new LatLng(30.51667, 114.31667);
				getHotCityLocation(baiduMap, hotCityPoint8, null);
				break;
			case 9:
				LatLng hotCityPoint9 = new LatLng(29.56667, 106.45000);
				getHotCityLocation(baiduMap, hotCityPoint9, null);
				break;
			case 10:
				LatLng hotCityPoint10 = new LatLng(41.80000, 123.38333);
				getHotCityLocation(baiduMap, hotCityPoint10, null);
				break;
			case 11:
				LatLng hotCityPoint11 = new LatLng(34.26667, 108.95000);
				getHotCityLocation(baiduMap, hotCityPoint11, null);
				break;
			case 12:
				try {
					initYtShareData(mapImgFile.getAbsolutePath());// 初始化友推数据
					initYtUITemplate();// 初始化友推界面
					ytUITemplate.show();// 显示友推菜单
				} catch (NullPointerException error) {
					showMessage("暂时不能分享哦！");
				}
				break;
			}
			super.handleMessage(msg);
		}

	};

	/**
	 * 地图上标注记号
	 * 
	 * @author lssjzmn
	 * @return void
	 * @param baidumap
	 *            要标记的地图对象
	 * @param point
	 *            记号点(经纬度)
	 * @param bitmap
	 *            标记图标
	 */
	public void getHotCityLocation(BaiduMap baidumap, LatLng point,
			BitmapDescriptor bitmap) {
		baidumap.setMyLocationEnabled(true);// 开启定位图层
		MyLocationData locData = new MyLocationData.Builder().accuracy(10)
				.latitude(point.latitude).longitude(point.longitude).build();
		baidumap.setMyLocationData(locData);// 设置定位数据
		MyLocationConfiguration config = new MyLocationConfiguration(
				LocationMode.FOLLOWING, true, null);
		baidumap.setMyLocationConfigeration(config);
	}

	/**
	 * @author lssjzmn
	 * @return void
	 * @param baidumap
	 *            要标记的地图对象
	 * @param bitmap
	 *            标记图标
	 * @param point
	 *            要标记的坐标点
	 */
	public void getMyLocation(BaiduMap baidumap, BitmapDescriptor bitmap,
			LatLng point) throws NullPointerException {
		baidumap.setMyLocationEnabled(true);// 开启定位图层
		MyLocationData locData = new MyLocationData.Builder().accuracy(200)
				.latitude(point.latitude).longitude(point.longitude).build();
		baidumap.setMyLocationData(locData);// 设置定位数据
		MyLocationConfiguration config = new MyLocationConfiguration(
				LocationMode.FOLLOWING, true, null);
		baidumap.setMyLocationConfigeration(config);
		if (siteInfoWindow != null)
			baiduMap.hideInfoWindow();
		siteInfoWindow = new InfoWindow(bitmap, point, -4,
				new InfoWindow.OnInfoWindowClickListener() {
					public void onInfoWindowClick() {
						baiduMap.hideInfoWindow();
					}
				});
		baiduMap.showInfoWindow(siteInfoWindow);
	}

	private class HomeAction implements Action {

		public HomeAction() {

		}

		@Override
		public int getDrawable() {
			return R.drawable.drawer;
		}

		@Override
		public void performAction(View view) {
			if (siteInfoWindow != null)
				baiduMap.hideInfoWindow();
			if (!isDrawerOpened) {
				drawerLayout.openDrawer(Gravity.LEFT);
				isDrawerOpened = true;
			} else {
				drawerLayout.closeDrawer(Gravity.LEFT);
				isDrawerOpened = false;
			}
		}

	}

	private class LocAction implements Action {

		public LocAction() {

		}

		@Override
		public int getDrawable() {
			return android.R.drawable.ic_menu_mylocation;
		}

		@Override
		public void performAction(View view) {
			if (siteInfoWindow != null)
				baiduMap.hideInfoWindow();
			if (Utils.CheckNetwork(MainActivity.this))
				RequestLocation(5000, true);
			else {
				showMessage("网络不可用");
				return;
			}
		}

	}

	private class baiduAction implements Action {

		@Override
		public int getDrawable() {
			return R.drawable.baidulogo;
		}

		@Override
		public void performAction(View view) {
			if (siteInfoWindow != null)
				baiduMap.hideInfoWindow();
			if (mLocationClient != null) {
				mLocationClient.stop();
				mLocationClient = null;
			}
			NaviParaOption option = new NaviParaOption();// point数据？
			// option.startName("西南交通大学九里校区");
			// option.endName("西南交通大学犀浦校区");
			if (myPoint == null)
				myPoint = new LatLng(0.1f, 0.1f);
			option.startPoint(myPoint);
			option.endPoint(myPoint);
			try {
				BaiduMapNavigation.openBaiduMapNavi(option,
						getApplicationContext());
			} catch (BaiduMapAppNotSupportNaviException error) {
				showMessage("未安装百度地图APP或百度地图版本较旧");
			} catch (IllegalPoiSearchArgumentException error) {
				showMessage("非法的Poi搜索参数，不能为null");
			} catch (IllegalNaviArgumentException error) {
				showMessage("非法导航参数");
			}
			// baiduMap.clear();
			// BaiduMapRoutePlan.openBaiduMapTransitRoute(null, null);
		}
	}

	/**
	 * * 返回值： 61 ： GPS定位结果 62 ： 扫描整合定位依据失败。此时定位结果无效。 63 ：
	 * 网络异常，没有成功向服务器发起请求。此时定位结果无效。 65 ： 定位缓存的结果。 66 ：
	 * 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果 67 ：
	 * 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果 68 ： 网络连接失败时，查找本地离线定位时对应的返回结果
	 * 161： 表示网络定位结果 162~167： 服务端定位失败 502：key参数错误 505：key不存在或者非法
	 * 601：key服务被开发者自己禁用 602：key mcode不匹配 501～700：key验证失败
	 * 
	 * @param scanSpan
	 *            定位时间间隔/ms
	 * @param isOpenGPS
	 *            是否利用GPS定位
	 */
	public void RequestLocation(int scanSpan, Boolean isOpenGPS)
			throws NullPointerException {
		mLocationClient = new LocationClient(getApplicationContext());
		LocationClientOption locationClientOption = new LocationClientOption();
		locationClientOption.setOpenGps(isOpenGPS);// 室内无法用GPS定位
		locationClientOption.setScanSpan(scanSpan);
		locationClientOption.setIsNeedAddress(true);
		locationClientOption
				.setLocationMode(Utils.CheckNetwork(getApplicationContext()) ? LocationClientOption.LocationMode.Hight_Accuracy
						: LocationClientOption.LocationMode.Device_Sensors);
		mLocationClient.setLocOption(locationClientOption);
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				// 误差有点大，将近1km
				if (location == null) {
					showMessage("没有获取到定位信息！");
					isLocationSuccess = false;
					return;
				}
				myPoint = new LatLng(location.getLatitude(), location
						.getLongitude());
				getMyLocation(baiduMap, bitmap, myPoint);
				shareContent = "我的经纬度： " + enter + myPoint.latitude + " , "
						+ myPoint.longitude + enter + "地点："
						+ location.getAddrStr() + enter + "误差半径/m："
						+ location.getRadius() + enter + "返回码:"
						+ location.getLocType();
				showMessage(shareContent);

				isLocationSuccess = true;
				if (mLocationClient != null) {
					mLocationClient.stop();
					mLocationClient = null;
					if (mLocationClient != null) {
						mLocationClient.stop();
						mLocationClient = null;
					}
					if (mLocationClient != null) {
						mLocationClient.stop();
						mLocationClient = null;
					}
				}
			}
		});
		mLocationClient.start();
		mLocationClient.requestLocation();
	}

	public void dealPoiInfo(PoiResult result) {
		if (result.error != SearchResult.ERRORNO.NO_ERROR)
			return;
		// result每页有10个PoiInfo，默认第一页

		List<PoiInfo> poiInfo = new ArrayList<PoiInfo>();
		poiInfo = result.getAllPoi();// 获取当前页的Poi列表
		int poiNum = poiInfo.size();
		showMessage("poiInfo 大小：" + poiNum);
		HashMap<Integer, PoiEntity> poiMap = new HashMap<Integer, PoiEntity>();
		for (int i = 0; i < poiNum; i++) {
			poiMap.put(i, new PoiEntity(poiInfo.get(i).city,
					poiInfo.get(i).name, poiInfo.get(i).address,
					poiInfo.get(i).phoneNum));
		}
		visualPoiInfo(poiMap);
	}

	public void visualPoiInfo(HashMap<Integer, PoiEntity> poiMap) {
		List<String> poiInfos = new ArrayList<String>();
		for (int i = 0; i < poiMap.size(); i++) {
			poiInfos.add("城市：" + poiMap.get(i).getCity() + enter + "名称："
					+ poiMap.get(i).getName() + enter + "地址："
					+ poiMap.get(i).getAddress() + enter + "电话："
					+ poiMap.get(i).getPhone());
		}
		ArrayAdapter<String> poiArrayAdapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.poiitemview, poiInfos);
		LayoutInflater inflater = getLayoutInflater();
		ListView poiListView = (ListView) inflater.inflate(
				R.layout.poilistview, null).findViewById(R.id.poiListLay);
		poiListView.setAdapter(poiArrayAdapter);
		Builder builder = new Builder(this);
		builder.setTitle("附近搜索信息列表").setIcon(android.R.drawable.ic_dialog_map)
				.setCancelable(true).setView(poiListView)
				.setIcon(android.R.drawable.ic_menu_info_details)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
		Dialog dialog = builder.create();
		dialog.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			new BottomBar.Builder(this).setMessage("即将退出程序？")
					.setListener(new UndoBarListener() {
						public void onUndo(Parcelable token) {
							preferLocCheckEdit = preferLocCheck.edit();
							preferLocCheckEdit.putBoolean("locCheck",
									isLocChecked);
							preferLocCheckEdit.commit();
							finish();
							System.exit(0);
						}

						public void onHide() {

						}
					}).show();
			break;
		case KeyEvent.KEYCODE_BACK:
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				if ((System.currentTimeMillis() - exitTime) > 3000) {
					Toast.makeText(getApplicationContext(), "再按一次退出程序",
							Toast.LENGTH_SHORT).show();
					exitTime = System.currentTimeMillis();
				} else {
					preferLocCheckEdit = preferLocCheck.edit();
					preferLocCheckEdit.putBoolean("locCheck", isLocChecked);
					preferLocCheckEdit.commit();
					finish();
					System.exit(0);
				}
			}
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void showMessage(String message) {
		Toast.makeText(getApplicationContext(), message, 1).show();
	}

	@Override
	protected void onDestroy() {
		// 释放资源
		YtTemplate.release(this);
		super.onDestroy();
		/** 清理缓存 */
		try {
			HttpUtils.deleteImage(shareData.getImagePath());
		} catch (NullPointerException error) {
		}
		mapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
		isLocChecked = preferLocCheck.getBoolean("locCheck", false);
		checkLoc.setChecked(isLocChecked);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		preferLocCheckEdit = preferLocCheck.edit();
		preferLocCheckEdit.putBoolean("locCheck", isLocChecked);
		preferLocCheckEdit.commit();
	}

	/**
	 * 地图单击事件回调函数
	 * 
	 * @param point
	 *            点击的地理坐标
	 */
	@Override
	public void onMapClick(LatLng point) {
		if (siteInfoWindow != null)
			baiduMap.hideInfoWindow();
		showMessage("搜一搜这里吧：" + enter + point.toString());
		myPoint = point;
		searchContent = editText.getText().toString();
		if (searchContent == null) {
			showMessage("搜索啥？");
			return;
		}
		// 开始搜索
		final PoiSearch poiSearch = PoiSearch.newInstance();
		poiSearch
				.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {

					@Override
					public void onGetPoiResult(PoiResult result) {
						dealPoiInfo(result);
					}

					public void onGetPoiDetailResult(
							PoiDetailResult detailResult) {
					}
				});
		if (point != null) {
			poiSearch.searchNearby(new PoiNearbySearchOption()
					.keyword(searchContent).radius(300).location(point)
					.pageCapacity(40));
			if (siteInfoWindow != null)
				baiduMap.hideInfoWindow();
			final BitmapDescriptor locBitmap = BitmapDescriptorFactory
					.fromResource(R.drawable.clickindicator);
			siteInfoWindow = new InfoWindow(locBitmap, point, -4,
					new InfoWindow.OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							baiduMap.hideInfoWindow();
						}
					});
			baiduMap.showInfoWindow(siteInfoWindow);
		} else
			showMessage("手动定位一下吧");

	}

	/**
	 * 地图内 Poi 单击事件回调函数
	 * 
	 * @param poi
	 *            点击的 poi 信息
	 */
	@Override
	public boolean onMapPoiClick(MapPoi poi) {
		if (siteInfoWindow != null)
			baiduMap.hideInfoWindow();
		showMessage(poi.getName() + "\n" + poi.getPosition().toString());
		/*
		 * LayoutInflater layoutInflater = getLayoutInflater(); View view =
		 * layoutInflater.inflate(R.layout.poiitemview, null); TextView infoText
		 * = (TextView) view.findViewById(R.id.poiItemLay); if (siteInfoWindow
		 * != null) baiduMap.hideInfoWindow(); siteInfoWindow = new
		 * InfoWindow(infoText, poi.getPosition(), -2); try {
		 * baiduMap.showInfoWindow(siteInfoWindow); } catch
		 * (IllegalArgumentException error) { }
		 */
		return false;
	}

	/**
	 * 地图双击事件监听回调函数
	 * 
	 * @param point
	 *            双击的地理坐标
	 */
	@Override
	public void onMapDoubleClick(LatLng point) {
		if (siteInfoWindow != null)
			baiduMap.hideInfoWindow();
		// baiduMap.setMaxAndMinZoomLevel(arg0, arg1);
	}

	@Override
	public void onMapLoaded() {
		uiSettings.setCompassPosition(new Point(60, 130));
	}

	public void traficClick(View v) {
		if (isTraficEnabled) {
			traficIndicator.setImageResource(R.drawable.traficunabled);
			baiduMap.setTrafficEnabled(false);// 关闭交通图
			isTraficEnabled = false;
		} else {
			traficIndicator.setImageResource(R.drawable.traficenabled);
			baiduMap.setTrafficEnabled(true);// 启用交通图
			isTraficEnabled = true;
		}
	}

	public void sateliteClick(View v) {
		if (isSateliteEnabled) {
			sateliteIndicator.setImageResource(R.drawable.sateliteunabled);
			baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);// 切换普通图
			isSateliteEnabled = false;
		} else {
			sateliteIndicator.setImageResource(R.drawable.sateliteenabled);
			baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);// 切换卫星图
			isSateliteEnabled = true;
		}
	}

	public void search(View v) throws NullPointerException {
		searchContent = editText.getText().toString();
		if (searchContent == null) {
			showMessage("搜索啥？");
			return;
		}
		// 开始搜索
		final PoiSearch poiSearch = PoiSearch.newInstance();
		poiSearch
				.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {

					@Override
					public void onGetPoiResult(PoiResult result) {
						dealPoiInfo(result);
					}

					public void onGetPoiDetailResult(
							PoiDetailResult detailResult) {
					}
				});
		if (myPoint != null)
			poiSearch.searchNearby(new PoiNearbySearchOption()
					.keyword(searchContent).radius(300).location(myPoint)
					.pageCapacity(40));
		else
			showMessage("手动定位一下吧");
	}

	/**
	 * 分享位置功能
	 * 
	 * @param Title
	 *            分享对话框的名字
	 * @param msgTitle
	 *            消息标题
	 * @param msgText
	 *            消息内容
	 * @param mapImgFile
	 *            图片文件，不分享图片则传null
	 */
	public void shareMsg(String Title, String msgTitle, String msgText,
			File mapImgFile) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		if (mapImgFile == null || !mapImgFile.exists() || !mapImgFile.isFile()) {
			if (!mapImgFile.exists())
				showMessage("分享纯文本");
			if (msgText == null || msgText.equals("")) {
				showMessage("没有定位信息可分享");
				return;
			}
			intent.setType("text/plain"); // 纯文本
		} else {
			showMessage("分享图片");
			intent.setType("image/*");
			Uri uriMapImg = Uri.fromFile(mapImgFile);
			intent.putExtra(Intent.EXTRA_STREAM, uriMapImg);
		}
		intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
		intent.putExtra(Intent.EXTRA_TEXT, msgText);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(Intent.createChooser(intent, Title));// 只能分享文字或者图片

	}

	public void shareMyLoc() {
		baiduMap.snapshot(new SnapshotReadyCallback() {
			public void onSnapshotReady(Bitmap mapBitmap) {
				mapBitmap = Bitmap.createScaledBitmap(mapBitmap, 480, 800,
						false);
				shareBitmap = mapBitmap;
				getMapImgFile();
				saveMapBitmap(mapImgFile, shareBitmap);
				shareMsg("分享我的位置至", "来自于轻松地图App的位置分享_by Lssjzmn", shareContent,
						mapImgFile);
			}
		});
	}

	public void getMapImgFile() {
		File FileFolder = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);// 系统图库路径
		FileFolder = new File(FileFolder.getAbsolutePath() + "/" + "sergioMap");
		if (!FileFolder.exists()) {// 路径不存在则自动创建路径
			FileFolder.mkdir();
		}
		String imgTime = getDateTime();
		mapImgFile = new File(FileFolder.getAbsolutePath() + "/" + "map"
				+ imgTime + ".jpg");
	}

	public void saveMapBitmap(File fileDst, Bitmap bitmap) {
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(fileDst));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
		new Utils(MainActivity.this).updateGallery(fileDst.getAbsolutePath());
		try {
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		return;
	}

	private void initYtShareData(String imagePath) {
		shareData = new ShareData();
		shareData.setAppShare(false); // 是否为应用分享，如果为true，分享的数据需在友推后台设置
		shareData.setDescription("友推分享组件");// 待分享内容的描述
		shareData.setTitle("来自于轻松地图App的位置分享_by Lssjzmn"); // 待分享的标题
		shareData.setText(shareContent);// 待分享的文字
		shareData.setImage(ShareData.IMAGETYPE_SDCARD, imagePath);// 设置本地SD卡的图片
		// shareData.setPublishTime("2013-02-05");
		shareData.setTargetId(String.valueOf(100));
		// shareData.setTargetUrl("http://youtui.mobi/");// 待分享内容的跳转链接
	}

	@SuppressWarnings("static-access")
	private void initYtUITemplate() {
		ytUITemplate = new YtTemplate(this, YouTuiViewType.BLACK_POPUP,
				usePointSys);
		// ytUITemplate.setActionName("分享我的位置到");
		ytUITemplate.setDismissAfterShare(isShowSharePop);
		ytUITemplate.setScreencapVisible(false);
		ytUITemplate.setPopwindowHeight(630);
		ytUITemplate.setShareData(shareData);
		ytUITemplate.addListeners(new YtShareMapListener());
	}

	private class YtShareMapListener extends YtShareListener {

		public YtShareMapListener() {
			super();
		}

		@Override
		public void onCancel(YtPlatform arg0) {
			showMessage("取消分享");
			// HttpUtils.deleteImage(shareData.getImagePath());
		}

		@Override
		public void onError(YtPlatform arg0, String arg1) {
			showMessage("分享出错");
			// HttpUtils.deleteImage(shareData.getImagePath());
		}

		@Override
		public void onPreShare(YtPlatform arg0) {
		}

		@Override
		public void onSuccess(YtPlatform platform, String arg1) {
			showMessage("成功分享至：" + platform.getName() + "平台");
			HttpUtils.deleteImage(shareData.getImagePath());
		}

	}

	public String getDateTime() {
		new DateFormat();
		String time = DateFormat.format("yyyy_MM_dd_hhmmss",
				Calendar.getInstance(Locale.CHINA))
				+ "";
		return time;
	}

}
