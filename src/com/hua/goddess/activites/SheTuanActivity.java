/**
 * 
 */
package com.hua.goddess.activites;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import com.hua.goddess.R;

public class SheTuanActivity extends Activity {

	private ListView mListView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shetuan);
		mListView = (ListView) findViewById(R.id.lv_main);
		mListView.addHeaderView(getheadView());
		// mListView.setDividerHeight(0);
		setData();
	}

	private void setData() {

	}

	private View getheadView() {
		View view = LayoutInflater.from(SheTuanActivity.this).inflate(
				R.layout.shetuan_headview, null);
		return view;
	}

}
