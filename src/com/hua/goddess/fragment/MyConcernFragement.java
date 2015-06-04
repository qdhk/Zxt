/**
 * 
 */

package com.hua.goddess.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.hua.goddess.R;
import com.hua.goddess.activites.MyConcernActivity;
import com.hua.goddess.adapter.MyConcernItemAdapter;
import com.hua.goddess.entity.MyConcern;

public class MyConcernFragement extends Fragment {
	private View rootView;
	private ListView mListView = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// mListView.setDividerHeight(0);

		rootView = inflater.inflate(R.layout.shetuan, null);
		mListView = (ListView) rootView.findViewById(R.id.lv_main);
		mListView.addHeaderView(getheadView());
		setData();
		return rootView;
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
		MyConcernItemAdapter adapter = new MyConcernItemAdapter(getActivity());
		adapter.setdata(mList);
		mListView.setAdapter(adapter);
	}

	private View getheadView() {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.shetuan_headview, null);
		return view;
	}

}
