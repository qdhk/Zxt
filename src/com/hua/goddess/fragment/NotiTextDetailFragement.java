/**
 * 
 */
package com.hua.goddess.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.hua.goddess.R;
import com.hua.goddess.base.communicate.GetNewsDetailInterface;
import com.hua.goddess.base.communicate.GetNotiDataDetail;

import com.hua.goddess.entity.NotiDetail;
import com.hua.goddess.global.Globe;
import com.hua.goddess.utils.HtmlResolving;
import com.hua.goddess.utils.PreferencesUtils;
import com.hua.goddess.vo.NewsContentVo;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import android.R.color;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * @author Administrator
 * 
 */
public class NotiTextDetailFragement extends BaseFragment implements
		OnClickListener {

	private String URL;
	private View container_view;
	private LayoutInflater inflater;
	private LinearLayout container_lin;
	private ArrayList<NewsContentVo> content_list;//
	private Handler handler = new Handler();
	private ImageButton back, comment;
	private ImageButton read_mode;
	private boolean readerMode; // 阅读模式（夜间，白天）
	private ImageButton change_text_size;
	public static final int TEXT_TYPE = 0;
	public static final int IMG_TYPE = 1;
	private int fontsize = 17; // 字体大小
	private PreferencesUtils pu;
	private LinearLayout menu_layout, notiLayout;
	private PopupWindow popupWindow;
	private SeekBar fontseek;
	private TextView text1;
	private ListView listView;
	private int width;
	private RelativeLayout title_bar;
	private TextView time_smallfont;
	private TextView news_title_name;

	NotiDetail notiDetail;
	private TextView tv_title, tv_info, tv_content;
	private SlidingMenu sm;
	private FragmentActivity context;

	public NotiTextDetailFragement(String URL, SlidingMenu sm) {
		this.URL = URL;
		this.sm = sm;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		this.inflater = inflater;
		container_view = inflater.inflate(R.layout.fragment_container, null);
		container_lin = (LinearLayout) container_view
				.findViewById(R.id.container);
		// 取出屏幕的宽和高
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels;
		new getNotiDetailThread().start();
		pu = new PreferencesUtils(context);
		readerMode = pu.getBoolean(Globe.READERMODE, false);
		fontsize = pu.getInt(Globe.FONTSIZE, 14); // 初始化文字大小
		return container_view;
	}

	private void initView() {
		View view = inflater.inflate(R.layout.noti_text_detail, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		notiLayout = (LinearLayout) view.findViewById(R.id.noti);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_info = (TextView) view.findViewById(R.id.tv_info);
		tv_content = (TextView) view.findViewById(R.id.tv_content);
		tv_content.setText(notiDetail.getContent());
		tv_info.setText(notiDetail.getInfo());
		tv_title.setText(notiDetail.getTitle());
		// 初始化底部菜单
		menu_layout = (LinearLayout) view.findViewById(R.id.noti_layout);
		change_text_size = (ImageButton) view
				.findViewById(R.id.change_text_size);
		change_text_size.setOnClickListener(this);
		read_mode = (ImageButton) view.findViewById(R.id.read_mode);
		read_mode.setOnClickListener(this);
		back = (ImageButton) view.findViewById(R.id.back_img);
		back.setOnClickListener(this);
		comment = (ImageButton) view.findViewById(R.id.comment_img);
		comment.setOnClickListener(this);
		if (readerMode) {
			// 夜间模式
			readerModeNight();
		} else {
			readerMode();
		}
		container_lin.removeAllViews();
		container_lin.addView(view);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("NotiTextDetail"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("NotiTextDetail");
	}

	class getNotiDetailThread extends Thread {
		@Override
		public void run() {
			try {
				notiDetail = GetNotiDataDetail.getNotiDetail(URL);
				handler.post(new Runnable() {
					@Override
					public void run() {
						if (notiDetail != null) {
							initView();
						} else {
							Toast.makeText(context, R.string.no_net_data,
									Toast.LENGTH_SHORT).show();
						}
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.change_text_size: // 修改字体大小
			LinearLayout layout = (LinearLayout) inflater.inflate(
					R.layout.pop_text_size, null);
			popupWindow = new PopupWindow(context);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			popupWindow.setWidth(context.getWindowManager().getDefaultDisplay()
					.getWidth());
			popupWindow.setHeight(context.getWindowManager()
					.getDefaultDisplay().getHeight() / 6);
			popupWindow.setAnimationStyle(R.style.AnimationPreview2);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setFocusable(true);// 响应回退按钮事件
			popupWindow.setContentView(layout);

			int[] location = new int[2];
			v.getLocationOnScreen(location);
			popupWindow.showAtLocation(v.findViewById(R.id.change_text_size),
					Gravity.NO_GRAVITY, location[0],
					location[1] - popupWindow.getHeight());

			fontseek = (SeekBar) layout.findViewById(R.id.settings_font);
			fontseek.setMax(20);
			fontseek.setProgress(fontsize - 10);
			fontseek.setSecondaryProgress(0);
			text1 = (TextView) layout.findViewById(R.id.fontSub);
			text1.setText(fontsize + "");
			fontseek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					fontsize = progress + 10;
					text1.setText("" + fontsize);
					// notifyAdapter();
					tv_content.setTextSize(fontsize);
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {

				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					pu.putInt(Globe.FONTSIZE, fontsize);
				}
			});
			break;
		case R.id.read_mode:

			if (readerMode) {
				readerMode = false;
				readerMode();
				// 保存数据
				pu.putBoolean(Globe.READERMODE, false);
			} else {
				readerMode = true;
				readerModeNight();
				// 保存数据
				pu.putBoolean(Globe.READERMODE, true);
			}
			break;
		case R.id.back_img:
			context.finish();
			break;
		case R.id.comment_img:
			if (sm.isMenuShowing()) {
				sm.showMenu(false);
			} else {
				sm.showMenu(true);
			}
			break;
		}

	}

	// 白天模式修改界面
	public void readerMode() {
		// title_bar.setBackgroundColor(-1);
		// listView.setBackgroundColor(-1); // #000000
		// time_smallfont.setTextColor(-13421773);
		// news_title_name.setTextColor(-13421773);
		notiLayout.setBackgroundColor(-1);
		read_mode.setImageResource(R.drawable.bottom_menu_mode_light1);
		menu_layout.setBackgroundColor(context.getResources().getColor(
				R.color.menu_bottom_bg));
	}

	// 夜间模式修改界面
	public void readerModeNight() {
		// title_bar.setBackgroundColor(-13947856);
		// listView.setBackgroundColor(-13947856); // #2b2c30
		// time_smallfont.setTextColor(-7895161);
		// news_title_name.setTextColor(-7895161);
		notiLayout.setBackgroundColor(-13947856);
		read_mode.setImageResource(R.drawable.bottom_menu_mode_light2);
		menu_layout.setBackgroundColor(-13947856);
	}

}
