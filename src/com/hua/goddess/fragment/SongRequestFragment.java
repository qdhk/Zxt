/**
 * 
 */
package com.hua.goddess.fragment;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hua.goddess.R;
import com.hua.goddess.activites.ContentActivity;
import com.hua.goddess.activites.ContentActivity.CloseSoftKeyboardListener;
import com.hua.goddess.dao.WsHelper;
import com.hua.goddess.dao.WsHelper.InterfaceCallBack;
import com.hua.goddess.dao.WsRequestHelper;
import com.hua.goddess.utils.EmailSender;
import com.hua.goddess.utils.IsFastClick;
import com.hua.goddess.utils.NetUtil;
import com.hua.goddess.widget.SlidingUpPanelLayout;
import com.hua.goddess.widget.SlidingUpPanelLayout.PanelSlideListener;

/**
 * @author Administrator
 * 
 */
public class SongRequestFragment extends Fragment implements OnClickListener,
		CloseSoftKeyboardListener {

	final String REGISTER = "user_register";
	// WsHelper wh = new WsHelper();
	WsRequestHelper wh1;
	private InputMethodManager imm;// 软键盘相关
	private Context context;
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
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = getActivity();
		time = new TimeCount(60000, 1000);
		imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		this.inflater = inflater;
		View rootView = inflater.inflate(R.layout.song_request, null);
		SlidingUpPanelLayout layout = (SlidingUpPanelLayout) rootView
				.findViewById(R.id.sliding_layout);
		panel_img = (ImageView) rootView.findViewById(R.id.panel_img);
		panel_name = (TextView) rootView.findViewById(R.id.panel_name);
		panel_name.setText(R.string.my_song);
		editText1 = (EditText) rootView.findViewById(R.id.edit_1);
		editText2 = (EditText) rootView.findViewById(R.id.edit_2);
		editText3 = (EditText) rootView.findViewById(R.id.edit_3);
		tv4 = (TextView) rootView.findViewById(R.id.edit_4);
		editText5 = (EditText) rootView.findViewById(R.id.edit_5);
		btn_song = (Button) rootView.findViewById(R.id.btn_song);
		btn_register = (Button) rootView.findViewById(R.id.btn_register);
		Calendar cd = Calendar.getInstance();
		Date date = new Date();
		cd.setTime(date);
		int mYear = cd.get(Calendar.YEAR);
		int mMonth = cd.get(Calendar.MONTH);
		int mDay = cd.get(Calendar.DAY_OF_MONTH);
		mMonth++;
		tv4.setText(mYear + "-" + mMonth + "-" + mDay);

		// wh.WsRequestCallBack = new InterfaceCallBack() {
		//
		// @Override
		// public void RequestCallBack(Object result) {
		// // TODO Auto-generated method stub
		// Toast.makeText(context, result.toString(), 1).show();
		// }
		// };
		wh1 = new WsRequestHelper(new WsRequestHelper.InterfaceCallBack() {
			@Override
			public void RequestCallBack(Object result) {
				// TODO Auto-generated method stub
				Toast.makeText(context, result.toString(), Toast.LENGTH_SHORT)
						.show();

			}
		});
		btn_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Map<String, Object> values = new HashMap<String, Object>();
				values.put("name", editText1.getText().toString());
				values.put("pwd", editText2.getText().toString());
				// wh.Request(REGISTER, values);
				wh1.Request(REGISTER, values);
			}
		});

		btn_song.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (IsFastClick.isFastDoubleClick()) {
					Toast.makeText(context, "您点击频率过快", 1).show();
					return;
				}
				if (editText1.getText().toString().equals("")) {
					Toast.makeText(context, "歌曲名不能为空", 1).show();
					return;
				}
				if (editText2.getText().toString().equals("")) {
					Toast.makeText(context, "点歌对象不能为空", 1).show();
					return;
				}
				if (NetUtil.getNetworkState(context) == 0) {
					Toast.makeText(context, "无网络连接", 1).show();
					return;
				}
				contentString = "点歌名称：" + editText1.getText().toString();
				contentString += "\n" + "点歌对象："
						+ editText2.getText().toString();
				contentString += "\n" + "点歌者：" + editText3.getText().toString();
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
				new DatePickerDialog(context,
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

		layout.setPanelSlideListener(new PanelSlideListener() {
			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				if (slideOffset < 0.5) {
					// panel_img.setBackgroundResource(R.drawable.ic_expand_down);
					panel_img.setImageResource(R.drawable.ic_expand_down);
				} else {
					// panel_img.setBackgroundResource(R.drawable.ic_expand_up);
					panel_img.setImageResource(R.drawable.ic_expand_up);
				}
			}

			@Override
			public void onPanelExpanded(View panel) {
				// 打开收藏
				//
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

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// btn_song.setEnabled(false);
			Toast.makeText(context, "发送成功！1分钟后再次发送！", 1).show();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hua.goddess.activites.ContentActivity.CloseSoftKeyboardListener#
	 * onCloseListener()
	 */
	@Override
	public void onCloseListener() {
		// TODO Auto-generated method stub

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
