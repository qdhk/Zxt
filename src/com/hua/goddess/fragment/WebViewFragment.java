package com.hua.goddess.fragment;

import java.util.zip.Inflater;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hua.goddess.R;
import com.hua.goddess.widget.ProgressWebView;
import com.umeng.analytics.MobclickAgent;

public class WebViewFragment extends Fragment {
	private View rootView;
	private ProgressWebView webView;
	private String url;
	private View mErrorView;

	public WebViewFragment() {
	}

	public WebViewFragment(String url) {
		this.url = url;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.webview, null);
		initView();
		return rootView;
	}

	private void initView() {
		webView = (ProgressWebView) rootView.findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				showErrorPage();// 显示错误页面
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
			}

		});
		webView.loadUrl(url);
	}

	/**
	 * 显示自定义错误提示页面，用一个View覆盖在WebView
	 */
	boolean mIsErrorPage;

	protected void showErrorPage() {
		LinearLayout webParentView = (LinearLayout) webView.getParent();
		initErrorPage();// 初始化自定义页面
		while (webParentView.getChildCount() > 1) {
			webParentView.removeViewAt(0);
		}
		@SuppressWarnings("deprecation")
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		webParentView.addView(mErrorView, 0, lp);
		mIsErrorPage = true;
	}

	/****
	 * 把系统自身请求失败时的网页隐藏
	 */
	protected void hideErrorPage() {
		LinearLayout webParentView = (LinearLayout) webView.getParent();

		mIsErrorPage = false;
		while (webParentView.getChildCount() > 1) {
			webParentView.removeViewAt(0);
		}
	}

	/***
	 * 显示加载失败时自定义的网页
	 */
	protected void initErrorPage() {
		if (mErrorView == null) {
			mErrorView = getActivity().getLayoutInflater().inflate(
					R.layout.activity_url_error, null);
			RelativeLayout button = (RelativeLayout) mErrorView
					.findViewById(R.id.online_error_btn_retry);
			button.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					webView.reload();
				}
			});
			mErrorView.setOnClickListener(null);
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("WebViewFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("WebViewFragment");
	}

	public boolean onKeyDown() {
		if (webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return false;
	}
}
