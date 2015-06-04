/**
 * 
 */
package com.hua.goddess.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hua.goddess.R;
import com.hua.goddess.entity.MyConcern;

/**
 * @author Administrator
 * 
 */
public class MyConcernItemAdapter extends BaseAdapter {
	private List<MyConcern> concerns;
	private LayoutInflater layoutInflater;
	private Context mContext;

	public MyConcernItemAdapter(Context context, List<MyConcern> concerns) {
		this.mContext = context;
		this.concerns = concerns;
		this.layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public MyConcernItemAdapter(Context context) {
		this.mContext = context;
	}

	public void setdata(List<MyConcern> concerns) {
		this.concerns = concerns;
	}

	@Override
	public int getCount() {
		return concerns.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return concerns.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.shetuan_item, parent, false);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.fenlei = (TextView) convertView.findViewById(R.id.fenlei);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		MyConcern concern = (MyConcern) getItem(position);
		if (concerns != null && concerns.size() > 0) {
			holder.title.setText(concern.getTitle().toString());
			holder.content.setText(concern.getContent().toString());
			holder.time.setText(concern.getDate().toString());
			holder.fenlei.setText(concern.getFenlei().toString());

		}
		return convertView;
	}

	public class ViewHolder {
		TextView title;
		TextView content;
		TextView fenlei;
		TextView time;
	}
}
