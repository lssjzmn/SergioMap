package com.lssjzmn.sergionavi;

import com.lssjzmn.sergionavi.MyGridLayout.GridAdatper;

import android.app.Activity;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SiteFragment extends Fragment {

	private Context context;
	private HotCitys hotCity;
	private int[] cityLogos = { R.drawable.actions_booktag,
			R.drawable.actions_comment, R.drawable.actions_order,
			R.drawable.actions_account, R.drawable.actions_cent,
			R.drawable.actions_weibo, R.drawable.actions_feedback,
			R.drawable.actions_about, R.drawable.actions_booktag,
			R.drawable.actions_comment, R.drawable.actions_order,
			R.drawable.actions_account };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.proposalfragsite, container,
				false);
		MyGridLayout gridLayout = (MyGridLayout) view
				.findViewById(R.id.proposalfragsitecitygrid);
		gridLayout.setGridAdapter(new GridAdatper() {
			public View getView(int index) {
				LayoutInflater inflater = getActivity().getLayoutInflater();
				View cityItemView = inflater.inflate(R.layout.hotcityitemlay,
						null);
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

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		hotCity = new HotCitys();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

}
