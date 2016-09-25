package com.lssjzmn.sergionavi;

import java.io.File;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.widget.Toast;

//跟网络相关的工具类
public class Utils {

	public static boolean isNetAvaliable;
	public static boolean isNetWifi;
	public Context context;

	public Utils(Context context) {
		super();
		this.context = context;
	}

	/**
	 * 判断网络连接是否打开
	 */
	public static boolean CheckNetwork(Context ctx) {
		boolean flag = false;
		ConnectivityManager netManager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (netManager.getActiveNetworkInfo() != null) {
			flag = netManager.getActiveNetworkInfo().isAvailable();
			isNetAvaliable = flag;
		}
		return flag;
	}

	/**
	 * 判断是否是wifi连接
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (cm == null)
			return false;
		return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

	}

	/**
	 * 打开wifi设置界面
	 */
	public static void openSetting(Activity activity) {
		Intent intent = new Intent(
				android.provider.Settings.ACTION_WIFI_SETTINGS);
		activity.startActivityForResult(intent, 0);
	}
	
	public void updateGallery(String filename) {
		MediaScannerConnection.scanFile(context, new String[] { filename },
				null, new MediaScannerConnection.OnScanCompletedListener() {
					public void onScanCompleted(String path, Uri uri) {
					}
				});
	}

}
