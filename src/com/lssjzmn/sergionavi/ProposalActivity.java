package com.lssjzmn.sergionavi;

import java.util.ArrayList;
import java.util.List;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.viewpagerindicator.TabPageIndicator;

import android.R.color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
//import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class ProposalActivity extends FragmentActivity implements
		OnClickListener {

	private Context context;
	private final static int MSGSEND = 1;
	private final static int MSGBACK = 2;
	private final static int MSGDONE = 3;
	private static List<Fragment> fragmentList;
	private FragmentManager fragmentManager;
	private ViewPager viewPager;
	private ProposalFragmentPagerAdapter fragmentPagerAdapter;
	private TabPageIndicator tabPageIndicator;
	private ProgressDialog progressDialog;
	private RadioGroup naviRadioGroup;
	private RadioButton naviRadioButton1;
	private RadioButton naviRadioButton2;
	private RadioButton naviRadioButton3;
	private RadioButton naviRadioButton4;
	//private TabLayout mTabLayout;//实现可滑动的TabPageIndicator

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_proposal);
		this.context = getApplicationContext();
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbarproposal);
		actionBar.setHomeAction(new HomeAction());
		actionBar.addAction(new SendAction());
		// setDefaultFragment();
		initPager();
		naviRadioButton1.setOnClickListener(this);
		naviRadioButton2.setOnClickListener(this);
		naviRadioButton3.setOnClickListener(this);
		naviRadioButton4.setOnClickListener(this);

	}

	// FragmentTabHost例子：http://blog.csdn.net/zheng_sida/article/details/38279503
	static {
		fragmentList = new ArrayList<Fragment>();
		MusicFragment musicFragment = new MusicFragment();
		SiteFragment siteFragment = new SiteFragment();
		FindFragment findFragment = new FindFragment();
		MineFragment mineFragment = new MineFragment();
		Bundle bundle = new Bundle();
		bundle.putString("args", "This is a bundle from proposalActivity");
		findFragment.setArguments(bundle);
		fragmentList.add(musicFragment);
		fragmentList.add(siteFragment);
		fragmentList.add(findFragment);
		fragmentList.add(mineFragment);
		System.out.println("static block inited....");
	}

	private void initPager() {
		progressDialog = new ProgressDialog(context);
		tabPageIndicator = (TabPageIndicator) findViewById(R.id.viewPagerindicator);
		naviRadioButton1 = (RadioButton) findViewById(R.id.navibtn1);
		naviRadioButton2 = (RadioButton) findViewById(R.id.navibtn2);
		naviRadioButton3 = (RadioButton) findViewById(R.id.navibtn3);
		naviRadioButton4 = (RadioButton) findViewById(R.id.navibtn4);
		naviRadioButton1.setBackgroundColor(getResources().getColor(
				android.R.color.holo_red_dark));
		fragmentManager = this.getSupportFragmentManager();
		fragmentPagerAdapter = new ProposalFragmentPagerAdapter(
				fragmentManager, fragmentList);
		//mTabLayout = (TabLayout)findViewById(R.id.tablayout);
		//mTabLayout.setTabMode(TabLayout.MODE_FIXED);
		//mTabLayout.setTabTextColors(color.white, Color.BLUE);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(fragmentPagerAdapter);
		tabPageIndicator.setViewPager(viewPager, 0);
		//mTabLayout.setupWithViewPager(viewPager);
		//mTabLayout.setTabsFromPagerAdapter(fragmentPagerAdapter);
		viewPager.setCurrentItem(0, true);
		// viewPager的监听被tabPageIndicator所接管，使用tabPageIndicator需要在Mainfest文件中设置相应theme
		tabPageIndicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				setBtnNormal();
				switch (position) {
				case 0:
					naviRadioButton1.setBackgroundColor(getResources()
							.getColor(android.R.color.holo_red_dark));
					break;
				case 1:
					naviRadioButton2.setBackgroundColor(getResources()
							.getColor(android.R.color.holo_red_dark));
					break;
				case 2:
					naviRadioButton3.setBackgroundColor(getResources()
							.getColor(android.R.color.holo_red_dark));
					break;
				case 3:
					naviRadioButton4.setBackgroundColor(getResources()
							.getColor(android.R.color.holo_red_dark));
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// System.out.println("public void onPageScrolled...");
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	private void setBtnNormal() {
		naviRadioButton1.setBackgroundColor(getResources().getColor(
				android.R.color.holo_red_light));
		naviRadioButton2.setBackgroundColor(getResources().getColor(
				android.R.color.holo_red_light));
		naviRadioButton3.setBackgroundColor(getResources().getColor(
				android.R.color.holo_red_light));
		naviRadioButton4.setBackgroundColor(getResources().getColor(
				android.R.color.holo_red_light));
	}

	/*
	 * private void setDefaultFragment() { FragmentManager fragmentManager =
	 * this.getSupportFragmentManager(); FragmentTransaction fragmentTransaction
	 * = fragmentManager .beginTransaction();
	 * fragmentTransaction.replace(R.id.fragmentframe, new MusicFragment());
	 * fragmentTransaction.setCustomAnimations(android.R.animator.fade_in,
	 * android.R.animator.fade_out); fragmentTransaction.commit(); }
	 * 
	 * private void setSiteFragment() { FragmentManager fragmentManager =
	 * this.getSupportFragmentManager(); FragmentTransaction fragmentTransaction
	 * = fragmentManager .beginTransaction();
	 * fragmentTransaction.replace(R.id.fragmentframe, new SiteFragment());
	 * fragmentTransaction.setCustomAnimations(android.R.animator.fade_in,
	 * android.R.animator.fade_out); fragmentTransaction.commit(); }
	 * 
	 * private void setFindFragment() { FragmentManager fragmentManager =
	 * this.getSupportFragmentManager(); FragmentTransaction fragmentTransaction
	 * = fragmentManager .beginTransaction();
	 * 
	 * FindFragment findFragment = new FindFragment(); Bundle bundle = new
	 * Bundle(); bundle.putString("args",
	 * "This is a bundle from proposalActivity");
	 * findFragment.setArguments(bundle); // findFragment.setarguments(bundle);
	 * 
	 * fragmentTransaction.remove(fragmentManager
	 * .findFragmentById(R.id.fragmentframe));
	 * fragmentTransaction.add(R.id.fragmentframe, findFragment);
	 * 
	 * // fragmentTransaction.replace(R.id.fragmentframe, findFragment);// //
	 * replace=remove+add
	 * fragmentTransaction.setCustomAnimations(android.R.animator.fade_in,
	 * android.R.animator.fade_out); fragmentTransaction.commit(); }
	 * 
	 * private void setMineFragment() { FragmentManager fragmentManager =
	 * this.getSupportFragmentManager(); FragmentTransaction fragmentTransaction
	 * = fragmentManager .beginTransaction();
	 * fragmentTransaction.replace(R.id.fragmentframe, new MineFragment());
	 * fragmentTransaction.setCustomAnimations(android.R.animator.fade_in,
	 * android.R.animator.fade_out); fragmentTransaction.commit(); }
	 */

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSGSEND:
				progressDialog = ProgressDialog.show(ProposalActivity.this,
						"请稍后...", "发送中...", true, true);
				handler.postDelayed(sendRun, 1500);
				break;
			case MSGDONE:
				if (progressDialog != null)
					progressDialog.dismiss();
				Toast.makeText(ProposalActivity.this, "谢谢您的反馈意见！", 1).show();
				break;
			case MSGBACK:
				ProposalActivity.this.finish();
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);
		System.out.println("public void onAttachFragment..."
				+ fragment.getTag());
	}

	private Runnable sendRun = new Runnable() {

		@Override
		public void run() {
			try {
				Thread.sleep(500);// 模拟上传耗时操作
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			handler.sendEmptyMessage(MSGDONE);
		}
	};

	private class HomeAction implements Action {

		@Override
		public int getDrawable() {

			return android.R.drawable.ic_menu_close_clear_cancel;
		}

		@Override
		public void performAction(View view) {
			handler.sendEmptyMessage(MSGBACK);
		}

	}

	private class SendAction implements Action {

		@Override
		public int getDrawable() {
			return android.R.drawable.ic_menu_send;
		}

		@Override
		public void performAction(View view) {
			handler.sendEmptyMessage(MSGSEND);
		}

	}

	@Override
	public void onClick(View v) {
		setBtnNormal();
		int id = v.getId();
		switch (id) {
		case R.id.navibtn1:
			viewPager.setCurrentItem(0, true);
			naviRadioButton1.setBackgroundColor(getResources().getColor(
					android.R.color.holo_red_dark));
			break;
		case R.id.navibtn2:
			viewPager.setCurrentItem(1, true);
			naviRadioButton2.setBackgroundColor(getResources().getColor(
					android.R.color.holo_red_dark));
			break;
		case R.id.navibtn3:
			viewPager.setCurrentItem(2, true);
			naviRadioButton3.setBackgroundColor(getResources().getColor(
					android.R.color.holo_red_dark));
			break;
		case R.id.navibtn4:
			viewPager.setCurrentItem(3, true);
			naviRadioButton4.setBackgroundColor(getResources().getColor(
					android.R.color.holo_red_dark));
			break;
		}

	}

}
