package com.hua.goddess.activites;

import u.aly.ca;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import android.app.ActionBar;
import android.app.Fragment.SavedState;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.hua.goddess.R;
import com.hua.goddess.fragment.SetFragment;
import com.umeng.analytics.MobclickAgent;

public class SettingActivity extends SwipeBackActivity {

	private ToggleButton btn1, btn2, btn3, btn4, btn5, btn6, btn7;
	SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_shots_detail);
		setContentView(R.layout.set_layout);
		initview();
		setActionBarStyle("设置");
		// getSupportFragmentManager().beginTransaction()
		// .replace(R.id.container, new SetFragment()).commit();

	}

	public void initview() {
		settings = getSharedPreferences("myset", MODE_PRIVATE);

		btn1 = (ToggleButton) findViewById(R.id.btn_01);
		btn2 = (ToggleButton) findViewById(R.id.btn_02);
		btn3 = (ToggleButton) findViewById(R.id.btn_03);
		btn4 = (ToggleButton) findViewById(R.id.btn_04);
		btn5 = (ToggleButton) findViewById(R.id.btn_05);
		btn6 = (ToggleButton) findViewById(R.id.btn_06);
		btn7 = (ToggleButton) findViewById(R.id.btn_07);

		btn1.setChecked(settings.getBoolean("yjs", true));
		btn2.setChecked(settings.getBoolean("tsg", true));
		btn3.setChecked(settings.getBoolean("gbt", true));
		btn4.setChecked(settings.getBoolean("stzz", true));
		btn5.setChecked(settings.getBoolean("bdtq", true));
		btn6.setChecked(settings.getBoolean("sys", false));
		btn7.setChecked(settings.getBoolean("fjdr", false));

	}

	private void setActionBarStyle(String title) {
		this.getActionBar().setTitle(title);
		getActionBar().setBackgroundDrawable(
				this.getBaseContext().getResources()
						.getDrawable(R.drawable.actionbar_back));
		getActionBar().setIcon(R.drawable.actionbar_back);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		int titleId = Resources.getSystem().getIdentifier("action_bar_title",
				"id", "android");
		TextView textView = (TextView) findViewById(titleId);
		textView.setTextColor(0xFFdfdfdf);
		textView.setTextSize(12);
		textView.setPadding(10, 0, 0, 0);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.set_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.set_complete:
			save();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void save() {
		// Toast.makeText(SettingActivity.this,
		// btn1.isChecked() == true ? "true" : "false", 1).show();

		SharedPreferences.Editor editor = settings.edit();

		editor.putBoolean("yjs", btn1.isChecked());
		editor.putBoolean("tsg", btn2.isChecked());
		editor.putBoolean("gbt", btn3.isChecked());
		editor.putBoolean("stzz", btn4.isChecked());
		editor.putBoolean("bdtq", btn5.isChecked());
		editor.putBoolean("sys", btn6.isChecked());
		editor.putBoolean("fjdr", btn7.isChecked());

		editor.commit();
		Toast.makeText(SettingActivity.this, "保存成功", 1).show();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
