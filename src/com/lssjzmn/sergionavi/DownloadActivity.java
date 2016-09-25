package com.lssjzmn.sergionavi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

public class DownloadActivity extends Activity implements OnClickListener {

	private ProgressBar progressBar;
	private EditText editTextCityName;
	private TextView textViewDataSize;
	private TextView textViewLocalSize;
	private TextView textViewSpeed;
	private Button buttonDownload;
	private Button buttonCancel;
	private ListView listViewCity;
	private ArrayAdapter<String> cityListAdapter;
	private List<String> cityList;
	private int rate = 0;
	private float serverSize = 0;
	private float localSize = 0;
	private float localSizePre = 0;
	private int speed = 0;
	private MKOfflineMap offlineMap;
	private MKOLUpdateElement updateElement;
	private String cityName = null;
	private int cityId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
		progressBar = (ProgressBar) findViewById(R.id.progressbar);
		textViewDataSize = (TextView) findViewById(R.id.downloadsizetxt);
		textViewLocalSize = (TextView) findViewById(R.id.downloadsizetxtlocal);
		textViewSpeed = (TextView) findViewById(R.id.downloadspeed);
		editTextCityName = (EditText) findViewById(R.id.editcityname);
		editTextCityName.addTextChangedListener(new TextWatcher() {

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
				cityName = txt.toString();
				if (Utils.CheckNetwork(getApplicationContext())) {
					try {
						cityId = offlineMap.searchCity(cityName).get(0).cityID;
					} catch (NullPointerException erroe) {
						cityName = null;
						Toast.makeText(DownloadActivity.this, "��ȡ��������ʧ�ܣ�������", 1)
								.show();
					}
				} else {
					cityName = null;
					Toast.makeText(DownloadActivity.this, "�������Ӳ����ã��޷�����", 1)
							.show();
				}
			}
		});
		buttonDownload = (Button) findViewById(R.id.btndownload);
		buttonDownload.setOnClickListener(this);
		buttonCancel = (Button) findViewById(R.id.btncanceldownload);
		buttonCancel.setOnClickListener(this);
		listViewCity = (ListView) findViewById(R.id.listcitydownloaded);
		try {
			getCityInfos();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!cityList.isEmpty()) {
			cityListAdapter = new ArrayAdapter<String>(DownloadActivity.this,
					R.layout.cityitemview, cityList);
			listViewCity.setAdapter(cityListAdapter);
		}
		listViewCity.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int id,
					long arg3) {
				switch (id) {
				case 0:
					break;
				default:
					break;
				}
			}
		});
		offlineMap = new MKOfflineMap();
		offlineMap.init(new MKOfflineMapListener() {
			public void onGetOfflineMapState(int type, int state) {
				switch (type) {
				case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:// ���ߵ�ͼ���ؽ��ȸ���
					// localSize = updateElement.size / 1024 / 1024;//
					// һֱ��0��û���������
					// rate = (int) (localSize / serverSize);
					break;
				case MKOfflineMap.TYPE_NEW_OFFLINE:
					break;
				case MKOfflineMap.TYPE_VER_UPDATE:
					break;
				}
			}
		});
	}

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			int msgValue = msg.what;
			progressBar.setProgress(msgValue);
			if (rate >= 100) {
				handler.removeCallbacks(runnable);
				rate = 0;
				buttonCancel.setText("�������");
				buttonDownload.setEnabled(true);
				buttonCancel.setEnabled(true);
				editTextCityName.setEnabled(true);
			} else {
				handler.postDelayed(runnable, 400);// ÿ�μ��400msȥ���½���
			}
			super.handleMessage(msg);
		}

	};

	private Runnable runnable = new Runnable() {

		public void run() {
			offlineMap.pause(cityId);
			offlineMap.start(cityId);// ����start֮��ratioֵ�Ż���£�����ѧ��
			updateElement = offlineMap.getUpdateInfo(cityId);
			rate = updateElement.ratio;
			localSize = rate * serverSize / 100;
			double random = 0.1 * Math.random();
			speed = (int) ((localSize - localSizePre) * 1024 / (0.4 + random));
			localSizePre = localSize;
			textViewLocalSize.setText("�������ݣ�" + localSize + "MB");
			buttonCancel.setText("ȡ������" + " " + "(" + rate + "%" + ")");
			textViewSpeed.setText("�����ٶȣ�" + speed + "KB/s");
			Message message = handler.obtainMessage(rate);
			handler.sendMessage(message);
		}
	};

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btndownload:
			if (cityName == null)
				return;
			rate = 0;
			offlineMap.start(cityId);
			buttonCancel.setText("ȡ������");
			updateElement = offlineMap.getUpdateInfo(cityId);
			serverSize = updateElement.serversize;
			localSize = updateElement.size;
			rate = updateElement.ratio;
			if (rate >= 100) {
				handler.removeCallbacks(runnable);
				buttonCancel.setText("�������");
				buttonDownload.setEnabled(true);
				buttonCancel.setEnabled(true);
				editTextCityName.setEnabled(true);
				return;
			}
			serverSize = serverSize / 1024f / 1024f;
			localSize = localSize / 1024f / 1024f;
			textViewDataSize.setText("�������ݣ�" + serverSize + "MB");
			textViewLocalSize.setText("�������ݣ�" + localSize + "MB");
			handler.post(runnable);
			buttonDownload.setEnabled(false);
			buttonCancel.setEnabled(true);
			editTextCityName.setEnabled(false);
			break;
		case R.id.btncanceldownload:
			if (cityName == null)
				this.finish();
			offlineMap.pause(cityId);
			this.finish();
			break;
		}

	}

	/**
	 * �ò���ϵͳ������ڲ��洢Ŀ¼
	 * 
	 * @throws IOException
	 */
	public void getCityInfos() throws IOException {
		cityList = new ArrayList<String>();
		// File rootFile = Environment.getRootDirectory();
		File rootFile = Environment.getExternalStorageDirectory();
		// rootFile = new File(rootFile, "BaiduMapSDK" + File.separator + "vmp"
		// + File.separator + "h");
		rootFile = new File(rootFile, "DCIM");
		if (!rootFile.exists()) {
			System.out.println("��Ŀ¼�����ڣ�����");
			return;
		}
		String[] fileList = rootFile.list();
		for (String name : fileList) {
			name = getNameBody(name);
			cityList.add(name);
		}
	}

	public String getNameBody(String name) {
		StringBuffer stringBuffer = new StringBuffer();
		int len = name.length();
		for (int i = 0; i < len; i++) {
			char temp = name.charAt(i);
			if (temp != ".".charAt(0))
				stringBuffer.append(temp);
			else {
				return stringBuffer.toString();
			}
		}
		return "";
	}
}
