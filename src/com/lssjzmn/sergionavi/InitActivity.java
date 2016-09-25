package com.lssjzmn.sergionavi;

import com.jensdriller.libs.undobar.BottomBar;
import com.jensdriller.libs.undobar.BottomBar.UndoBarListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.app.Activity;
import android.view.View;

public class InitActivity extends Activity {

	private Exitable exitrun;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		if (!Utils.CheckNetwork(this)) {
			new BottomBar.Builder(this).setMessage("网络连接不可用，去设置一下？")
					.setListener(new UndoBarListener() {
						public void onUndo(Parcelable token) {
							Utils.openSetting(InitActivity.this);
						}

						public void onHide() {

						}
					}).show();
		}
		exitrun = new Exitable();
		new Handler().postDelayed(exitrun, 5000);

	}

	private class Exitable implements Runnable {

		@Override
		public void run() {
			handler.sendEmptyMessage(1);
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			handler.removeCallbacks(exitrun);
			InitActivity.this.finish();
			super.handleMessage(msg);
		}

	};

	public void tap2exitinit(View v) {
		handler.sendEmptyMessage(1);
	}

}
