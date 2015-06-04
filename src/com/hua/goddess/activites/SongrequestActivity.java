/**
 * 
 */
package com.hua.goddess.activites;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import u.aly.bu;
import u.aly.ca;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Fragment.SavedState;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.hua.goddess.R;
import com.hua.goddess.dao.WsRequestHelper;
import com.hua.goddess.fragment.SetFragment;
import com.hua.goddess.utils.EmailSender;
import com.hua.goddess.utils.IsFastClick;
import com.hua.goddess.utils.NetUtil;
import com.hua.goddess.widget.SlidingUpPanelLayout;
import com.hua.goddess.widget.SlidingUpPanelLayout.PanelSlideListener;
import com.umeng.analytics.MobclickAgent;

public class SongrequestActivity extends SwipeBackActivity implements
		OnClickListener {

	// WsRequestHelper wh1;
	// private Context context;
	private LayoutInflater inflater;
	private ImageView panel_img;
	private TextView panel_name;
	private EditText editText1, editText2, editText3, editText5;
	private TextView tv4;
	private LinearLayout collect_lin;

	public String contentString;
	Boolean flag;
	private Button btn_song, btn_register;
	private TimeCount time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.songrequest2);

		time = new TimeCount(60000, 1000);
		setActionBarStyle("点歌互动");
		// SlidingUpPanelLayout layout = (SlidingUpPanelLayout)
		// findViewById(R.id.sliding_layout);
		// panel_img = (ImageView) findViewById(R.id.panel_img);
		// panel_name = (TextView) findViewById(R.id.panel_name);
		// panel_name.setText(R.string.my_song);
		editText1 = (EditText) findViewById(R.id.edit_1);
		editText2 = (EditText) findViewById(R.id.edit_2);
		editText3 = (EditText) findViewById(R.id.edit_3);
		tv4 = (TextView) findViewById(R.id.edit_4);
		editText5 = (EditText) findViewById(R.id.edit_5);
		btn_song = (Button) findViewById(R.id.btn_song);
		// btn_register = (Button) findViewById(R.id.btn_register);
		Calendar cd = Calendar.getInstance();
		Date date = new Date();
		cd.setTime(date);
		int mYear = cd.get(Calendar.YEAR);
		int mMonth = cd.get(Calendar.MONTH);
		int mDay = cd.get(Calendar.DAY_OF_MONTH);
		mMonth++;
		tv4.setText(mYear + "-" + mMonth + "-" + mDay);

		btn_song.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Bundle bundle = getIntent().getExtras();
				if (bundle.getString("username").equals("未登录")
						|| bundle == null) {
					Toast.makeText(SongrequestActivity.this, "请先登录", 1).show();
					return;
				}

				// TODO Auto-generated method stub
				if (IsFastClick.isFastDoubleClick()) {
					Toast.makeText(SongrequestActivity.this, "您点击频率过快", 1)
							.show();
					return;
				}
				if (editText1.getText().toString().equals("")) {
					Toast.makeText(SongrequestActivity.this, "歌曲名不能为空", 1)
							.show();
					return;
				}
				if (editText2.getText().toString().equals("")) {
					Toast.makeText(SongrequestActivity.this, "点歌对象不能为空", 1)
							.show();
					return;
				}
				if (NetUtil.getNetworkState(SongrequestActivity.this) == 0) {
					Toast.makeText(SongrequestActivity.this, "无网络连接", 1).show();
					return;
				}
				contentString = "点歌名称：" + editText1.getText().toString();
				contentString += "\n" + "点歌对象："
						+ editText2.getText().toString();
				contentString += "\n" + "点歌者：" + editText3.getText().toString()
						+ "--用户名:" + bundle.getString("username");
				contentString += "\n" + "歌曲播放时间：" + tv4.getText().toString();
				contentString += "\n" + "其余备注："
						+ editText5.getText().toString();

				new Thread() {
					public void run() {

						try {
							EmailSender sender = new EmailSender();
							// 设置服务器地址和端口，网上搜的到
							sender.setProperties("smtp.163.com", "25");
							// 分别设置发件人，邮件标题和文本内容
							sender.setMessage("15230277836@163.com", "点歌",
									contentString);
							// 设置收件人
							// sender.setReceiver(new String[] {
							// "1259039225@qq.com" });
							sender.setReceiver(new String[] { "huadian_dgt@163.com" });
							// 添加附件，我这里注释掉，因为有人跟我说这行报错...
							// 这个附件的路径是我手机里的啊，要发你得换成你手机里正确的路径
							// sender.addAttachment("/default.prop");
							// 发送邮件
							sender.sendEmail("smtp.163.com",
									"15230277836@163.com", "jike1002niurui");

							handler.sendEmptyMessage(0);
						} catch (AddressException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (MessagingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					};
				}.start();

			}
		});

		tv4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new DatePickerDialog(SongrequestActivity.this,
						new DatePickerDialog.OnDateSetListener() {
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								String formatString = String.valueOf(year)
										+ "-";
								monthOfYear++;
								if (monthOfYear < 10) {
									formatString += "0";
								}
								formatString += String.valueOf(monthOfYear)
										+ "-";
								if (dayOfMonth < 10) {
									formatString += "0";
								}
								formatString += String.valueOf(dayOfMonth);
								tv4.setText(formatString);
							}
						},
						Integer.parseInt(tv4.getText().toString().split("-")[0]
								.toString()), Integer.parseInt(tv4.getText()
								.toString().split("-")[1].toString()) - 1,
						Integer.parseInt(tv4.getText().toString().split("-")[2]
								.toString())).show();

			}

		});

		// layout.setPanelSlideListener(new PanelSlideListener() {
		// @Override
		// public void onPanelSlide(View panel, float slideOffset) {
		// if (slideOffset < 0.5) {
		// // panel_img.setBackgroundResource(R.drawable.ic_expand_down);
		// panel_img.setImageResource(R.drawable.ic_expand_down);
		// } else {
		// // panel_img.setBackgroundResource(R.drawable.ic_expand_up);
		// panel_img.setImageResource(R.drawable.ic_expand_up);
		// }
		// }
		//
		// @Override
		// public void onPanelExpanded(View panel) {
		// // 打开收藏
		// //
		// }
		//
		// @Override
		// public void onPanelCollapsed(View panel) {
		//
		// }
		//
		// @Override
		// public void onPanelAnchored(View panel) {
		// }
		// });
		// collect_lin = (LinearLayout) findViewById(R.id.collect_lin);
		// ((ContentActivity) context).registerMyTouchListener(this);

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// btn_song.setEnabled(false);
			Toast.makeText(SongrequestActivity.this, "发送成功！1分钟后再次发送！", 1)
					.show();
			time.start();
			super.handleMessage(msg);
		};
	};

	class TimeCount extends CountDownTimer {

		/**
		 * @param millisInFuture
		 * @param countDownInterval
		 */
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.CountDownTimer#onTick(long)
		 */
		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			btn_song.setClickable(false);
			btn_song.setText("稍等" + millisUntilFinished / 1000 + "秒");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.CountDownTimer#onFinish()
		 */
		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			btn_song.setText("立即点歌");
			btn_song.setClickable(true);
		}

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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
