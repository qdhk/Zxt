/**
 * 
 */
package com.hua.goddess.activites;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

import com.hua.goddess.R;
import com.hua.goddess.adapter.MyConcernItemAdapter;
import com.hua.goddess.entity.MyConcern;
import com.umeng.analytics.MobclickAgent;

import android.app.ActionBar;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Administrator
 * 
 */
public class MyConcernActivity extends SwipeBackActivity {

	private ListView mListView = null;

	String usernameString = "未登录";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shetuan);
		Bundle bundle = getIntent().getExtras();
		usernameString = bundle.getString("username");
		mListView = (ListView) findViewById(R.id.lv_main);
		View view = LayoutInflater.from(MyConcernActivity.this).inflate(
				R.layout.shetuan_headview, null);
		TextView textView = (TextView) view.findViewById(R.id.username);
		textView.setText(usernameString);
		mListView.addHeaderView(view);

		setData();
		setActionBarStyle("设置");
	}

	private void setActionBarStyle(String title) {
		this.getActionBar().setTitle(title);
		getActionBar().setBackgroundDrawable(
				this.getBaseContext().getResources()
						.getDrawable(R.drawable.actionbar_back));
		getActionBar().setIcon(R.drawable.ic_action);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		int titleId = Resources.getSystem().getIdentifier("action_bar_title",
				"id", "android");
		TextView textView = (TextView) findViewById(titleId);
		textView.setTextColor(0xFFdfdfdf);
		textView.setTextSize(18);
		textView.setPadding(15, 0, 0, 0);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}

	private void setData() {
		List<MyConcern> mList = new ArrayList<MyConcern>();
		MyConcern concern1 = new MyConcern();
		MyConcern concern2 = new MyConcern();
		concern1.setContent("经核实，以下拟毕业同学未参加2014年11月16-18日学校组织的集体拍照，请各位同学务必于2015年1月31日之前到易拍数码影像中心补拍照片，否则会影响毕业证书发放。");
		concern1.setDate("昨天");
		concern1.setFenlei("研究生会");
		concern1.setTitle("【微通知】关于拟毕业同学未参加集体拍照需补拍照片的通知");

		concern2.setContent("为全面贯彻和落实《全民科学素质行动计划纲要》，促进高端科普专门人才成长，引导、支持高校科普相关专业方向在读研究生积极参与科普工作实践，增强其。。。，");
		concern2.setDate("前天");
		concern2.setFenlei("研究生会");
		concern2.setTitle("关于申报中国科协2015年度研究生科普能力提升项目的通知");
		mList.add(concern1);
		mList.add(concern2);
		MyConcernItemAdapter adapter = new MyConcernItemAdapter(
				MyConcernActivity.this);
		adapter.setdata(mList);
		mListView.setAdapter(adapter);
	}

	private View getheadView() {
		View view = LayoutInflater.from(MyConcernActivity.this).inflate(
				R.layout.shetuan_headview, null);

		return view;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
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
