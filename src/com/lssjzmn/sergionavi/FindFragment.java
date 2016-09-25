package com.lssjzmn.sergionavi;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FindFragment extends Fragment {

	private Context context;
	private ListView listview0;
	private ListView listview;
	private ListView listview2;
	private BaseAdapter baseAdapter;
	private SimpleAdapter simpleAdapter;
	private SimpleAdapter simpleAdapter2;
	private Bundle bundle;
	private String argument;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// ����UI
		System.out.println("public View onCreateView...");
		View findview = inflater.inflate(R.layout.proposalfragfind, container,
				false);
		listview0 = (ListView) findview.findViewById(R.id.listview0);
		listview = (ListView) findview.findViewById(R.id.listview);
		listview2 = (ListView) findview.findViewById(R.id.listview2);
		listview0.setAdapter(baseAdapter);
		listview.setAdapter(simpleAdapter);
		listview2.setAdapter(simpleAdapter2);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int id,
					long arg3) {
				switch (id) {
				case 0:
					System.out.println("you have just clicked on item ����Ȧ");
					break;
				case 1:
					System.out.println("you have just clicked on item ��Ϸ");
					break;
				case 2:
					System.out.println("you have just clicked on item ����");
					break;
				case 3:
					System.out.println("you have just clicked on item ����");
					break;
				}
			}
		});
		return findview;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// ����UI����
		super.onCreate(savedInstanceState);
		//context = getActivity();
		bundle = this.getArguments();
		if (bundle != null) {
			argument = bundle.getString("args");
			System.out.println(argument);
		} else
			System.out.println("null bundle found..");

		String[] txt = { "����Ȧ", "��Ϸ", "����", "����" };
		String[] txt2 = { "ɨһɨ", "ҡһҡ", "ͨѶ¼" };
		int img[] = { android.R.drawable.ic_menu_share,
				android.R.drawable.ic_menu_compass,
				android.R.drawable.ic_input_add,
				android.R.drawable.ic_menu_camera };
		int img2[] = { android.R.drawable.ic_menu_call,
				android.R.drawable.ic_menu_rotate,
				android.R.drawable.ic_menu_today };

		baseAdapter = new PersonBaseAdapter(context);
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 4; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("img", img[i]);
			map.put("txt", txt[i]);
			map.put("img_to", R.drawable.go);
			list.add(map);
		}
		simpleAdapter = new SimpleAdapter(getActivity().getApplication(), list,
				R.layout.simpleadapter,
				new String[] { "img", "txt", "img_to" }, new int[] {
						R.id.simpleimg, R.id.simpletxt, R.id.simpleimg_to });

		ArrayList<HashMap<String, Object>> list2 = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 3; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("img", img2[i]);
			map.put("txt", txt2[i]);
			map.put("img_to", R.drawable.go);
			list2.add(map);
		}
		simpleAdapter2 = new SimpleAdapter(getActivity().getApplication(),
				list2, R.layout.simpleadapter, new String[] { "img", "txt",
						"img_to" }, new int[] { R.id.simpleimg, R.id.simpletxt,
						R.id.simpleimg_to });
	}

	/**
	 * @���� ΪFindFragment��������Activity�õ���Intent�е�����bundle
	 * @param args
	 *            ��Activity�������ݵ�����
	 * @return void
	 */
	public void setarguments(Bundle args) {
		this.bundle = args;
	}

	@Override
	public void setArguments(Bundle args) {
		super.setArguments(args);
		this.bundle = args;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = activity;
		System.out.println("public void onAttach...");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("public void onDestroy...");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		System.out.println("public void onDestroyview...");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		System.out.println("public void onDetach...");
	}

	@Override
	public void onPause() {
		super.onPause();
		System.out.println("public void onpause...");
	}

	@Override
	public void onResume() {
		super.onResume();
		System.out.println("public void onresume...");
	}

	@Override
	public void onStart() {
		super.onStart();
		System.out.println("public void onstart...");
	}

	@Override
	public void onStop() {
		super.onStop();
		System.out.println("public void onstop...");
	}

}
