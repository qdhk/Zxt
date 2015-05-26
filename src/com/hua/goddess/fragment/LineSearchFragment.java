package com.hua.goddess.fragment;

import java.util.ArrayList;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Type;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hua.goddess.R;
import com.hua.goddess.activites.BusLineDetailActivity;
import com.hua.goddess.activites.ContentActivity;
import com.hua.goddess.activites.ContentActivity.CloseSoftKeyboardListener;
import com.hua.goddess.base.communicate.GetBusLineInterface;
import com.hua.goddess.dao.BusCollectDao;
import com.hua.goddess.dao.DBHelper;
import com.hua.goddess.vo.BusLineDetailVo;
import com.hua.goddess.vo.BusLineListVo;
import com.hua.goddess.vo.BusLineVo;
import com.hua.goddess.widget.SlidingUpPanelLayout;
import com.hua.goddess.widget.SlidingUpPanelLayout.PanelSlideListener;

public class LineSearchFragment extends Fragment implements OnClickListener,
		CloseSoftKeyboardListener {

	private String searchWords;
	private EditText search_line;
	private ImageButton btn_search_delete;
	private Button icon_search;
	private BusLineListVo bls;
	private ArrayList<BusLineVo> list;
	private Handler handler = new Handler();
	private BusLineHolder busLineHolder;
	private BusLineAdapter bAdapter;
	private ListView listview;
	private ProgressDialog mpDialog;
	private Context context;
	private TextView bus_help;
	private InputMethodManager imm;// 软键盘相关
	private ImageView panel_img;
	private TextView panel_name;
	private DBHelper mDbHelper;
	private BusCollectDao collectDao;
	private LinearLayout collect_lin;
	private LayoutInflater inflater;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		mDbHelper = new DBHelper(context);
		collectDao = new BusCollectDao(mDbHelper);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		View rootView = inflater.inflate(R.layout.bus_search_common, null);
		search_line = (EditText) rootView.findViewById(R.id.search_text);
		search_line.setHint(R.string.line_hint);
		btn_search_delete = (ImageButton) rootView
				.findViewById(R.id.icon_clear);
		btn_search_delete.setOnClickListener(this);
		icon_search = (Button) rootView.findViewById(R.id.icon_search);
		icon_search.setOnClickListener(this);
		listview = (ListView) rootView.findViewById(R.id.listview);
		bus_help = (TextView) rootView.findViewById(R.id.bus_help_text);
		bus_help.setText(R.string.bus_text_line);
		// search_line.setKeyListener(new DigitsKeyListener(false,true));
		search_line.setRawInputType(InputType.TYPE_CLASS_NUMBER);
		search_line.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() > 0) {
					if (btn_search_delete.getVisibility() == View.GONE) {
						btn_search_delete.setVisibility(View.VISIBLE);
					}
				} else {
					if (btn_search_delete.getVisibility() == View.VISIBLE) {
						btn_search_delete.setVisibility(View.GONE);
					}
				}
			}
		});
		SlidingUpPanelLayout layout = (SlidingUpPanelLayout) rootView
				.findViewById(R.id.sliding_layout);
		panel_img = (ImageView) rootView.findViewById(R.id.panel_img);
		panel_name = (TextView) rootView.findViewById(R.id.panel_name);
		panel_name.setText(R.string.my_line);
		layout.setPanelSlideListener(new PanelSlideListener() {
			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				if (slideOffset < 0.5) {
					panel_img.setImageResource(R.drawable.ic_expand_down);
				} else {
					panel_img.setImageResource(R.drawable.ic_expand_up);
				}
			}

			@Override
			public void onPanelExpanded(View panel) {
				// 打开收藏
				new LoadBusLineList(0).start();
			}

			@Override
			public void onPanelCollapsed(View panel) {
			}

			@Override
			public void onPanelAnchored(View panel) {

			}
		});
		collect_lin = (LinearLayout) rootView.findViewById(R.id.collect_lin);
		((ContentActivity) context).registerMyTouchListener(this);
		return rootView;
	}

	private void initListView() {
		bus_help.setVisibility(View.GONE);
		bAdapter = new BusLineAdapter();
		listview.setAdapter(bAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.icon_search:
			imm.hideSoftInputFromWindow(search_line.getWindowToken(), 0);
			searchWords = search_line.getText().toString();
			if (searchWords == null || !(searchWords.length() > 0)) {
				Toast.makeText(getActivity(), "输入不能为空哦！", Toast.LENGTH_SHORT)
						.show();
			} else {
				Resources res = context.getResources();
				mpDialog = ProgressDialog.show(context,
						res.getString(R.string.dialog_search),
						res.getString(R.string.rd_cmlogic_dialog_waiting),
						true, false);// "请稍后..."
				mpDialog.setCancelable(true);
				new GetLineDataThread().start();
			}
			break;

		case R.id.icon_clear:
			search_line.setText("");
			break;
		}
	}

	private void dismissShow() {
		if (mpDialog != null) {
			mpDialog.dismiss();
		}
	}

	class GetLineDataThread extends Thread {
		@Override
		public void run() {
			try {
				bls = GetBusLineInterface.getNetData(searchWords);
				handler.post(new Runnable() {
					@Override
					public void run() {
						dismissShow();
						// 初始化页面
						if (bls != null) {
							list = bls.getList();
							if (list != null && list.size() > 0) {
								if (listview != null && bAdapter != null) {
									bAdapter.notifyDataSetChanged();
								} else {
									initListView();
								}
							} else {
								Toast.makeText(context, "暂无该线路信息，请稍后再试！",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(context, "暂无该线路信息，请稍后再试！",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class BusLineAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return list.size();
		}

		@Override
		public Object getItem(int position) {

			return null;
		}

		@Override
		public long getItemId(int position) {

			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.bus_search_line_result, null);
				busLineHolder = new BusLineHolder();
				busLineHolder.name = (TextView) convertView
						.findViewById(R.id.bus_line_result_name);
				busLineHolder.posotion_text = (TextView) convertView
						.findViewById(R.id.bus_line_posotion_text);
				convertView.setTag(busLineHolder);
			} else {
				busLineHolder = (BusLineHolder) convertView.getTag();
			}
			busLineHolder.name.setText(list.get(position).getLName());
			busLineHolder.posotion_text.setText(list.get(position)
					.getLDirection());
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					intentBusLineDetail(list.get(position).getGuid());
				}
			});
			return convertView;
		}
	}

	class BusLineHolder {
		TextView name;
		TextView posotion_text;
	}

	public void intentBusLineDetail(String guid) {
		Intent intent = new Intent(getActivity(), BusLineDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("Guid", guid);
		intent.putExtras(bundle);
		getActivity().startActivity(intent);
		getActivity().overridePendingTransition(R.anim.abc_fade_in,
				R.anim.abc_fade_out);
	}

	// -----------------------------------线路收藏------------------------------------

	private ArrayList<BusLineDetailVo> collectList;
	private ListView c_listview;
	private BusSiteCollectAdapter cAdapter;
	private TextView promptTextView;
	private View collectView;

	private class LoadBusLineList extends Thread {
		private int sleepTime;

		public LoadBusLineList(int sleepTime) {
			this.sleepTime = sleepTime;
		}

		@Override
		public void run() {
			if (sleepTime > 0) {// 第一次滑动过来延迟300ms 页面刷新过快导致卡顿
				try {
					Thread.sleep(sleepTime);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			collectList = collectDao.getAllLineData();
			handler.post(new Runnable() {
				@Override
				public void run() {
					if (collectList != null && collectList.size() > 0) {
						if (c_listview == null || cAdapter == null) {
							collect_lin.removeAllViews();
							initCollectList();
							collect_lin.addView(collectView);
						} else {
							collect_lin.removeAllViews();
							collect_lin.addView(collectView);
							cAdapter.notifyDataSetChanged();
						}
					} else {
						initNoCollectLayout();
					}
				}

			});
		}
	}

	private void initCollectList() {
		collectView = inflater.inflate(R.layout.listview_comm, null);
		c_listview = (ListView) collectView.findViewById(R.id.listview_comm);
		cAdapter = new BusSiteCollectAdapter();
		c_listview.setAdapter(cAdapter);
		c_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				intentBusLineDetail(collectList.get(position).getLGUID());
			}
		});
		c_listview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {

				final Dialog dialog = new Dialog(getActivity(),
						R.style.NoTitleDialog);
				View dialogView = inflater.inflate(R.layout.bus_dialog, null);
				TextView title = (TextView) dialogView
						.findViewById(R.id.dialog_title);
				TextView content = (TextView) dialogView
						.findViewById(R.id.dialog_content);
				title.setText(collectList.get(position).getLName() + "路");
				content.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						collectDao.deleteBusLine(collectList.get(position)
								.getLGUID());
						dialog.dismiss();
						collectList.remove(position);
						cAdapter.notifyDataSetChanged();
						Toast.makeText(context, R.string.del_collect,
								Toast.LENGTH_SHORT).show();
					}
				});
				dialog.setContentView(dialogView);
				/*
				 * 将对话框的大小按屏幕大小的百分比设置
				 */
				Window dialogWindow = dialog.getWindow();
				WindowManager m = getActivity().getWindowManager();
				Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
				WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
				// p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
				p.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的0.8
				dialogWindow.setAttributes(p);
				dialog.show();
				return false;
			}
		});
	}

	private void initNoCollectLayout() {
		if (promptTextView == null) {
			noSdCardTip(context.getResources().getString(R.string.no_collect));
		}
		collect_lin.removeAllViews();
		collect_lin.addView(promptTextView);
	}

	protected void noSdCardTip(String string) {
		promptTextView = new TextView(context);
		promptTextView.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		promptTextView.setGravity(Gravity.CENTER_VERTICAL
				| Gravity.CENTER_HORIZONTAL);
		promptTextView.setTextColor(Color.rgb(0x91, 0x91, 0x91));
		promptTextView.setText(string);
	}

	class BusSiteCollectAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return collectList.size();
		}

		@Override
		public Object getItem(int position) {

			return null;
		}

		@Override
		public long getItemId(int position) {

			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.bus_site_collect_item, null);
			}
			TextView title = (TextView) convertView
					.findViewById(R.id.bus_store_title);
			ImageView tag_img = (ImageView) convertView
					.findViewById(R.id.bus_store_list_image);
			title.setText(collectList.get(position).getLName() + "路  "
					+ collectList.get(position).getLDirection());
			tag_img.setImageDrawable(getResources().getDrawable(
					R.drawable.bus_ico_bus));
			return convertView;
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		((ContentActivity) context).unRegisterMyTouchListener(this);
	}

	@Override
	public void onCloseListener() {
		// TODO Auto-generated method stub
		imm.hideSoftInputFromWindow(search_line.getWindowToken(), 0);
	}

}
