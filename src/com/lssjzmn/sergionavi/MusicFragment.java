package com.lssjzmn.sergionavi;

import android.app.Activity;
import android.content.Context;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class MusicFragment extends Fragment implements OnClickListener {

	private Context context;
	private ImageButton imageButton1;
	private ImageButton imageButton2;
	private ImageButton imageButton3;
	private ImageButton imageButton4;
	private ImageButton imageButton0;
	private ImageButton imageButton00;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.proposalfragmusic, container,
				false);
		initviews(container);
		return view;
	}

	private void initviews(ViewGroup container) {
		// È»²¢ÂÑ
		LayoutInflater inflater = LayoutInflater.from(context);
		imageButton0 = (ImageButton) inflater.inflate(R.layout.metrocardmiddle,
				container).findViewById(R.id.imgbtn0);
		imageButton00 = (ImageButton) inflater.inflate(R.layout.metrocardtop,
				container).findViewById(R.id.imgbtn00);
		imageButton1 = (ImageButton) inflater.inflate(R.layout.metrocardtop,
				container).findViewById(R.id.imgbtn1);
		imageButton2 = (ImageButton) inflater.inflate(R.layout.metrocardtop,
				container).findViewById(R.id.imgbtn2);
		imageButton3 = (ImageButton) inflater.inflate(R.layout.metrocardbottom,
				container).findViewById(R.id.imgbtn3);
		imageButton4 = (ImageButton) inflater.inflate(R.layout.metrocardbottom,
				container).findViewById(R.id.imgbtn4);
		System.out.println("initviews done...");
		initimgs();
	}

	private void initimgs() {
		imageButton0.setImageResource(R.drawable.music0);
		imageButton00.setImageResource(R.drawable.music00);
		imageButton1.setImageResource(R.drawable.music1);
		imageButton2.setImageResource(R.drawable.music2);
		imageButton3.setImageResource(R.drawable.music3);
		imageButton4.setImageResource(R.drawable.music4);
		System.out.println("initimgs done...");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.imgbtn0:
			break;
		case R.id.imgbtn00:
			break;
		case R.id.imgbtn1:
			break;
		case R.id.imgbtn2:
			break;
		case R.id.imgbtn3:
			break;
		case R.id.imgbtn4:
			break;
		}

	}

}
