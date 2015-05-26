/**
 * 
 */
package com.hua.goddess.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import com.hua.goddess.R;
import com.hua.goddess.activites.NewsTextDetail;
import com.hua.goddess.activites.NotiTextDetail;
import com.hua.goddess.base.communicate.GetNewsInterface;
import com.hua.goddess.base.communicate.GetNotiData;
import com.hua.goddess.entity.Noti;
import com.hua.goddess.fragment.NewsListFragment.GetNewsDataThread;
import com.hua.goddess.fragment.NewsListFragment.NewsAdapter;
import com.hua.goddess.fragment.NewsListFragment.NewsItemHolder;
import com.hua.goddess.utils.LocalDisplay;
import com.hua.goddess.vo.NewsListVo;
import com.hua.goddess.vo.NewsVo;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * @author Administrator
 * 
 */
public class NotiListFragment extends BaseFragment implements OnRefreshListener {

	private Handler handler = new Handler();
	private ListView news_list;
	private NotiItemHolder notiholder;
	private Context context;
	private NotiAdapter nAdapter;
	private int Id;
	private LinearLayout viewContainer;
	private View view;
	private PullToRefreshLayout mPullToRefreshLayout;

	List<Noti> notis = new ArrayList<Noti>();

	public HashMap<String, String> COOKIES = new HashMap<String, String>();

	public NotiListFragment() {
	}

	public NotiListFragment(int id) {
		this.Id = id;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_progress_container,
				null);
		viewContainer = (LinearLayout) rootView.findViewById(R.id.container);
		LocalDisplay.init(context);
		new GetNotisDataThread().start();
		return rootView;
	}

	private void initListView() {
		if (context == null)
			return;
		view = LayoutInflater.from(context).inflate(
				R.layout.fragment_classic_header_with_list_view, null);
		news_list = (ListView) view.findViewById(R.id.news_list);

		mPullToRefreshLayout = (PullToRefreshLayout) view
				.findViewById(R.id.ptr_layout);

		ActionBarPullToRefresh.from(getActivity()).allChildrenArePullable()
				.listener(this).setup(mPullToRefreshLayout);
		nAdapter = new NotiAdapter();
		news_list.setAdapter(nAdapter);

		viewContainer.removeAllViews();
		viewContainer.addView(view);
	}

	// 获取搜索数据
	class GetNotisDataThread extends Thread {
		@Override
		public void run() {
			try {
				notis = GetNotiData.getNotis(Id);
				handler.post(new Runnable() {
					@Override
					public void run() {
						if (notis != null && notis.size() > 0) {
							initListView(); // 初始化页面
						} else {
							initNetErro();
						}

					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onRefreshStarted(View view) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					notis = GetNotiData.getNotis(Id);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if (notis != null && notis.size() > 0) {
					nAdapter.notifyDataSetChanged();
				}
				mPullToRefreshLayout.setRefreshComplete();
			}
		}.execute();

	}

	private void initNetErro() {
		// TODO
		final View loadView = LayoutInflater.from(context).inflate(
				R.layout.loading_view, null);
		loadView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		View netErroView = LayoutInflater.from(context).inflate(
				R.layout.page_store_net_erro, null);
		Button reloadBtn = (Button) netErroView.findViewById(R.id.reload_btn);
		netErroView.findViewById(R.id.net_erro_img).setVisibility(View.VISIBLE);
		reloadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (viewContainer != null) {
					viewContainer.removeAllViews();
					viewContainer.addView(loadView);
					new GetNotisDataThread().start();
				}
			}
		});
		if (viewContainer != null) {
			viewContainer.removeAllViews();
			viewContainer.addView(netErroView);
		}

	}

	class NotiAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return notis.size();
		}

		@Override
		public Object getItem(int position) {

			return null;
		}

		@Override
		public long getItemId(int position) {

			// return 0;
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.noti_commom_item, null);
				notiholder = new NotiItemHolder();

				notiholder.title = (TextView) convertView
						.findViewById(R.id.noti_title);
				notiholder.date_text = (TextView) convertView
						.findViewById(R.id.noti_date);
				convertView.setTag(notiholder);
			} else {
				notiholder = (NotiItemHolder) convertView.getTag();
			}
			notiholder.title.setText(notis.get(position).getTitle());
			notiholder.date_text.setText(notis.get(position).getDate());
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),
							NotiTextDetail.class);// =================================
					Bundle bundle = new Bundle();
					bundle.putString("URL", notis.get(position).getURL());
					intent.putExtras(bundle);
					getActivity().startActivity(intent);
					getActivity().overridePendingTransition(R.anim.abc_fade_in,
							R.anim.abc_fade_out);
				}
			});
			return convertView;
		}
	}

	class NotiItemHolder {

		TextView title;
		TextView date_text;

	}
}
