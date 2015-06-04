/**
 * 
 */
package com.hua.goddess.fragment;

import com.hua.goddess.R;
import com.hua.goddess.activites.About_HdgbtActivity;
import com.hua.goddess.activites.ContentActivity;
import com.hua.goddess.activites.SongrequestActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @author Administrator
 * 
 */
public class BroadCastFragment extends Fragment implements OnClickListener {

	private Context context;
	private LayoutInflater inflater;
	private LinearLayout layout1, layout2, layout3, layout4, layout5;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = getActivity();
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
		View rootView = inflater.inflate(R.layout.hdbc_p1, null);

		layout1 = (LinearLayout) rootView.findViewById(R.id.linelay_fun1);
		layout2 = (LinearLayout) rootView.findViewById(R.id.linelay_fun2);
		layout3 = (LinearLayout) rootView.findViewById(R.id.linelay_fun3);
		layout4 = (LinearLayout) rootView.findViewById(R.id.linelay_fun4);
		layout5 = (LinearLayout) rootView.findViewById(R.id.linelay_fun5);

		layout1.setOnClickListener(this);
		layout2.setOnClickListener(this);
		layout3.setOnClickListener(this);
		layout4.setOnClickListener(this);
		layout5.setOnClickListener(this);
		return rootView;
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
		case R.id.linelay_fun1:
			Intent aboutIntent = new Intent(getActivity(),
					About_HdgbtActivity.class);
			startActivity(aboutIntent);
			break;
		case R.id.linelay_fun2:

			Toast.makeText(context, "2", 1).show();
			break;
		case R.id.linelay_fun3:
			Toast.makeText(context, "3", 1).show();
			break;
		case R.id.linelay_fun4:
			Toast.makeText(context, "4", 1).show();
			break;
		case R.id.linelay_fun5:
			// Fragment fragment = new SongRequestFragment();
			// FragmentManager fragmentManager = getFragmentManager();
			// fragmentManager.beginTransaction()
			// .replace(R.id.frame_container, fragment)
			// .commitAllowingStateLoss();
			Intent intent = new Intent(getActivity(), SongrequestActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("username", ContentActivity.username);
			intent.putExtras(bundle);
			startActivity(intent);
			break;

		default:
			break;
		}

	}

}
