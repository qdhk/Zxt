package com.hua.goddess.activites;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.R.integer;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hua.goddess.R;
import com.hua.goddess.adapter.NavDrawerListAdapter;
import com.hua.goddess.fragment.BeautyMainFragment;
import com.hua.goddess.fragment.BusAssistantFragment;
import com.hua.goddess.fragment.ChatSecretaryFragment;
import com.hua.goddess.fragment.JWXXFragment;
import com.hua.goddess.fragment.LibraryFragment;
import com.hua.goddess.fragment.NewsFragment;
import com.hua.goddess.fragment.NotiFragment;
import com.hua.goddess.fragment.WeatherFragment;
import com.hua.goddess.global.DeviceInfo;
import com.hua.goddess.vo.NavDrawerItem;
import com.hua.goddess.widget.ActionBarDrawerToggle;
import com.hua.goddess.widget.DrawerArrowDrawable;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class ContentActivity extends FragmentActivity {

	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;
	private NavDrawerListAdapter adapter;
	private ListView mDrawerList;
	private int curItem;
	private DrawerArrowDrawable drawerArrow;
	private String title = "校园资讯通";
	private ArrayList<CloseSoftKeyboardListener> myListeners = new ArrayList<CloseSoftKeyboardListener>();
	private String switcher;
	public int is_checked[] = { 1, 0, 0, 0, 0, 0, 0, 0 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
		setActionBarStyle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.content_drawer);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		drawerArrow = new DrawerArrowDrawable(this) {
			@Override
			public boolean isLayoutRtl() {
				return false;
			}
		};
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				drawerArrow, R.string.drawer_open, R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				refreshLiftView();
				invalidateOptionsMenu();
				closeSoftKeyboard();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		switcher = MobclickAgent.getConfigParams(this, "news_switcher");

		if ("".equals(switcher)) {
			switcher = "no";
		}
		initLiftView();

		if (savedInstanceState == null) {
			if ("no".equals(switcher)) {
				displayView(1);
			} else {
				displayView(0);
			}
		}
		UmengUpdateAgent.update(this); // 检测更新
	}

	private void initLiftView() {
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		adapter = new NavDrawerListAdapter(getApplicationContext(), getData(),
				switcher);
		// View headView = LayoutInflater.from(this).inflate(R.layout.head_view,
		// null);
		// mDrawerList.addHeaderView(headView);
		mDrawerList.setAdapter(adapter);
	}

	private void refreshLiftView() {
		adapter = new NavDrawerListAdapter(getApplicationContext(), getData(),
				switcher);
		// View headView = LayoutInflater.from(this).inflate(R.layout.head_view,
		// null);
		// mDrawerList.addHeaderView(headView);

		mDrawerList.setAdapter(adapter);

	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position == curItem)
				return;
			displayView(position);
		}
	}

	private int get_realpoi(int pos, int count) {

		int n = pos;
		if (pos == 0) {
			return 0;
		}
		for (int i = 1; i < count; i++) {
			if (is_checked[i] == 1) {
				n--;
				if (n <= 0)
					return i;
			}

		}
		return 0;
	}

	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		if (!DeviceInfo.isNetworkAvailable(this)) {
			Toast.makeText(this, R.string.error_msg, Toast.LENGTH_SHORT).show();
		}
		int realpoi = get_realpoi(position, 8);
		switch (realpoi) {
		case 0:
			curItem = 0;

			title = "教务信息";
			fragment = new JWXXFragment();
			MobclickAgent.onEvent(this, "jwxx");
			break;
		case 1:
			curItem = 1;
			title = "研究生院";
			fragment = new NotiFragment();
			MobclickAgent.onEvent(this, "noti");
			break;
		case 2:
			curItem = 2;
			title = "图书馆";
			// fragment = new ChatSecretaryFragment();
			fragment = new LibraryFragment();
			MobclickAgent.onEvent(this, "library");
			break;
		case 3:
			curItem = 3;
			title = "广播台";
			fragment = new BusAssistantFragment();
			MobclickAgent.onEvent(this, "busassistant");
			break;
		case 4:
			curItem = 4;
			title = "社团组织";
			// fragment = new BeautyMainFragment();
			// fragment = new LibraryFragment();
			MobclickAgent.onEvent(this, "beauty");
			break;
		case 5:
			curItem = 5;
			title = "保定天气";
			fragment = new WeatherFragment();
			MobclickAgent.onEvent(this, "weather");
			break;
		case 6:
			curItem = 6;
			title = "保定天气";
			fragment = new WeatherFragment();
			MobclickAgent.onEvent(this, "weather");
			break;
		case 7:
			curItem = 7;
			title = "保定天气";
			fragment = new WeatherFragment();
			MobclickAgent.onEvent(this, "weather");
			break;
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment)
					.commitAllowingStateLoss();
		}
		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		mDrawerLayout.closeDrawer(mDrawerList);
		this.getActionBar().setTitle(title);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean isDrawerOper = mDrawerLayout.isDrawerOpen(mDrawerList);
		if (isDrawerOper) {
			this.getActionBar().setTitle("校园资讯通");
		} else {
			this.getActionBar().setTitle(title);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {

				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_about:
			Intent intent = new Intent(this, AboutUsActivity.class);
			startActivity(intent);
			MobclickAgent.onEvent(this, "about");
			break;
		case R.id.action_updata:
			UmengUpdateAgent.setUpdateAutoPopup(false);
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
				@Override
				public void onUpdateReturned(int updateStatus,
						UpdateResponse updateInfo) {
					switch (updateStatus) {
					case UpdateStatus.Yes: // has update
						UmengUpdateAgent.showUpdateDialog(ContentActivity.this,
								updateInfo);
						break;
					case UpdateStatus.No: // has no update
						Toast.makeText(ContentActivity.this, "已是最新版本！",
								Toast.LENGTH_SHORT).show();
						break;
					case UpdateStatus.NoneWifi: // none wifi
						Toast.makeText(ContentActivity.this,
								"没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT)
								.show();
						break;
					case UpdateStatus.Timeout: // time out
						Toast.makeText(ContentActivity.this, "超时",
								Toast.LENGTH_SHORT).show();
						break;
					}
				}
			});
			UmengUpdateAgent.forceUpdate(this);
			MobclickAgent.onEvent(this, "update");
			break;
		case R.id.action_settings:
			Intent intent_set = new Intent(this, SettingActivity.class);
			startActivity(intent_set);
			MobclickAgent.onEvent(this, "about");
			break;
		default:
			break;
		}

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private ArrayList<NavDrawerItem> getData() {
		ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<NavDrawerItem>();
		TypedArray navMenuIcons = getResources().obtainTypedArray(
				R.array.nav_drawer_icons);
		String[] navMenuTitles = getResources().getStringArray(
				R.array.nav_drawer_items);

		SharedPreferences settings = getSharedPreferences("myset", MODE_PRIVATE);
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));

		if (settings.getBoolean("yjs", true)) {
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
					.getResourceId(1, -1)));
			is_checked[1] = 1;
		} else {
			is_checked[1] = 0;
		}
		if (settings.getBoolean("tsg", true)) {
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
					.getResourceId(2, -1)));
			is_checked[2] = 1;
		} else {
			is_checked[2] = 0;
		}
		if (settings.getBoolean("gbt", true)) {
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
					.getResourceId(3, -1)));
			is_checked[3] = 1;
		} else {
			is_checked[3] = 0;
		}
		if (settings.getBoolean("stzz", true)) {
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
					.getResourceId(4, -1)));
			is_checked[4] = 1;
		} else {
			is_checked[4] = 0;
		}
		if (settings.getBoolean("bdtq", true)) {
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
					.getResourceId(5, -1)));
			is_checked[5] = 1;
		} else {
			is_checked[5] = 0;
		}
		if (settings.getBoolean("sys", false)) {
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons
					.getResourceId(6, -1)));
			is_checked[6] = 1;
		} else {
			is_checked[6] = 0;
		}

		if (settings.getBoolean("fjdr", false)) {
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons
					.getResourceId(7, -1)));
			is_checked[7] = 1;
		} else {
			is_checked[7] = 0;
		}

		// Recycle the typed array
		navMenuIcons.recycle();

		return navDrawerItems;
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private void setActionBarStyle() {

		getActionBar().setBackgroundDrawable(
				this.getBaseContext().getResources()
						.getDrawable(R.drawable.actionbar_back));
		getActionBar().setIcon(R.color.transparent);
		// getActionBar().setIcon(R.drawable.ic_action);
		// getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		int titleId = Resources.getSystem().getIdentifier("action_bar_title",
				"id", "android");
		TextView textView = (TextView) findViewById(titleId);
		// textView.setTypeface(Typeface.createFromAsset(getAssets(),
		// "font/Wendy.ttf"));
		textView.setTextColor(0xFFdfdfdf);
		textView.setTextSize(17);
		// textView.setPadding(15, 0, 0, 0);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}

	/**
	 * 连续按两次返回键就退出
	 */
	private int keyBackClickCount = 0;

	@Override
	protected void onResume() {
		super.onResume();
		keyBackClickCount = 0;
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (keyBackClickCount++) {
			case 0:
				Toast.makeText(this,
						getResources().getString(R.string.press_again_exit),
						Toast.LENGTH_SHORT).show();
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						keyBackClickCount = 0;
					}
				}, 3000);
				break;
			case 1:
				this.finish();
				break;
			default:
				break;
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			// if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			// mDrawerLayout.closeDrawer(mDrawerList);
			// } else {
			// mDrawerLayout.openDrawer(mDrawerList);
			// }
		}
		return super.onKeyDown(keyCode, event);
	}

	/*------------  侧边栏打开后 关闭所有的软键盘  ------------*/

	public interface CloseSoftKeyboardListener {
		public void onCloseListener();
	}

	public void registerMyTouchListener(CloseSoftKeyboardListener listener) {
		myListeners.add(listener);
	}

	public void unRegisterMyTouchListener(CloseSoftKeyboardListener listener) {
		myListeners.remove(listener);
	}

	public void closeSoftKeyboard() {
		for (CloseSoftKeyboardListener listener : myListeners) {
			listener.onCloseListener();
		}
	}
}
