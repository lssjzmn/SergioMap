package com.lssjzmn.sergionavi;

import android.app.Activity;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MineFragment extends Fragment implements OnClickListener {

	private Context context;
	private ImageView QRImage;
	private Boolean isOnTouch = false;
	private int templ = 0;
	private int tempt = 0;
	private int tempr = 0;
	private int tempb = 0;
	private int[] downDotInView = new int[] { 0, 0 };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.proposalfragmine, container,
				false);
		QRImage = (ImageView) view.findViewById(R.id.myqrcode);
		QRImage.setOnTouchListener(new ontouchlistener());

		return view;
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

	public class ontouchlistener implements OnTouchListener {

		private int originalX = 0;
		private int originalY = 0;
		private int padViewLeft = 0;
		private int padViewTop = 0;
		private int padViewRight = 0;
		private int padViewBottom = 0;

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			/*
			 * getRawX：触摸点相对于屏幕边界的坐标 getX： 触摸点相对于当前view边界的坐标 getTop：
			 * 当前view左上角相对于父view的y坐标 getLeft： 当前view左上角相对于父view的x坐标
			 */
			int id = v.getId();
			if (id != R.id.myqrcode)
				return true;
			else {
				// 获取控件的宽度高度
				int viewWidth = v.getWidth();
				int viewHetght = v.getHeight();
				// 触摸点屏幕坐标
				int rawx = (int) event.getRawX();
				int rawy = (int) event.getRawY();

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					originalX = (int) event.getRawX();
					originalY = (int) event.getRawY();

					downDotInView[0] = rawx - v.getLeft();
					downDotInView[1] = rawy - v.getTop();

					isOnTouch = true;
					break;
				case MotionEvent.ACTION_MOVE:
					// 当前view相对于父view边界
					padViewLeft = v.getLeft();
					padViewTop = v.getTop();
					padViewRight = v.getRight();
					padViewBottom = v.getBottom();

					// 刷新控件位置
					if ((rawx - downDotInView[0]) <= 10
							| (rawy - downDotInView[1]) <= 10) {
					} else {
						v.layout(rawx - downDotInView[0], rawy
								- downDotInView[1], rawx - downDotInView[0]
								+ viewWidth, rawy - downDotInView[1]
								+ viewHetght);
					}
					isOnTouch = true;
					break;
				case MotionEvent.ACTION_UP:
					if (Math.abs(originalX - rawx) <= 2
							&& Math.abs(originalY - rawy) <= 2) {
						System.out.println("lssjzmnQRcode image clicked......");
					}
					isOnTouch = false;
					break;
				}
				return true;
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.myqrcode) {
			System.out.println("lssjzmnQRcode image clicked......");
		}

	}

}
