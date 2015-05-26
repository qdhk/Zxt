/**
 * 
 */
package com.hua.goddess.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hua.goddess.R;

/**
 * @author Administrator
 * 
 */
public class SetFragment extends Fragment implements OnClickListener {
	private View rootView;

	private LinearLayout sectionLayout;

	private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.set_layout, null);
		initview();
		return rootView;
	}

	public void initview() {
		sectionLayout = (LinearLayout) rootView
				.findViewById(R.id.set_section_layout);
		btn1 = (Button) rootView.findViewById(R.id.btn_01);
		btn2 = (Button) rootView.findViewById(R.id.btn_02);
		btn3 = (Button) rootView.findViewById(R.id.btn_03);
		btn4 = (Button) rootView.findViewById(R.id.btn_04);
		btn5 = (Button) rootView.findViewById(R.id.btn_05);
		btn6 = (Button) rootView.findViewById(R.id.btn_06);
		btn7 = (Button) rootView.findViewById(R.id.btn_07);

		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn6.setOnClickListener(this);
		btn7.setOnClickListener(this);

		sectionLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.btn_01:

			break;
		case R.id.btn_02:

			break;
		case R.id.btn_03:

			break;
		case R.id.btn_04:

			break;
		case R.id.btn_05:

			break;
		case R.id.btn_06:

			break;
		case R.id.btn_07:

			break;

		default:
			break;
		}
	}
}
