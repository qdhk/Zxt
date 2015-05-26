/**
 * 
 */
package com.hua.goddess.fragment;

import android.R.color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hua.goddess.R;
import com.hua.goddess.animation.ZoomOutPageTransformer;
import com.hua.goddess.widget.PagerSlidingTabStrip;
import com.umeng.analytics.MobclickAgent;

/**
 * @author Administrator
 * 
 */
public class NotiFragment extends Fragment {
	private ViewPager contentPager;
	private mPagerAdapter adapter;
	private PagerSlidingTabStrip tabs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.news_fragment, null);
		setPager(rootView);
		return rootView;
	}

	private void setPager(View rootView) {
		contentPager = (ViewPager) rootView.findViewById(R.id.content_pager);
		adapter = new mPagerAdapter(getActivity().getSupportFragmentManager());
		contentPager.setAdapter(adapter);
		contentPager.setOffscreenPageLimit(2);
		contentPager.setBackgroundColor(color.holo_purple);
		contentPager.setPageTransformer(true, new ZoomOutPageTransformer());
		tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
		tabs.setTextColorResource(R.color.light_gray_text);
		tabs.setDividerColorResource(R.color.common_list_divider);
		// tabs.setUnderlineColorResource(R.color.common_list_divider);
		tabs.setIndicatorColorResource(R.color.red);
		tabs.setSelectedTextColorResource(R.color.red);
		// tabs.setIndicatorHeight(5);
		tabs.setViewPager(contentPager);
	}

	private class mPagerAdapter extends FragmentStatePagerAdapter {

		private String Title[] = { "通知公告", "学生活动", "新闻动态" };

		public mPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {

			return new NotiListFragment(arg0);
		}

		@Override
		public int getCount() {
			return Title.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return Title[position];
		}

	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("NotiFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("NotiFragment");
	}
}
