package com.hua.goddess.adapter;

import java.util.List;

import com.hua.goddess.R;
import com.hua.goddess.entity.Book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BookListAdapter extends BaseAdapter {

	private List<Book> books;
	private LayoutInflater layoutInflater;

	public BookListAdapter(Context context, List<Book> books) {
		this.books = books;
		this.layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	@Override
	public int getCount() {
		return books.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return books.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = (View) layoutInflater.inflate(
					R.layout.listview_book_list, null);
		}
		TextView name = (TextView) convertView
				.findViewById(R.id.listview_book_name);
		TextView id = (TextView) convertView
				.findViewById(R.id.listview_book_id);
		TextView author = (TextView) convertView
				.findViewById(R.id.listview_book_author);
		TextView publish = (TextView) convertView
				.findViewById(R.id.listview_book_publish);
		TextView amount = (TextView) convertView
				.findViewById(R.id.listview_book_amount);
		name.setText(books.get(position).getBookName());
		id.setText(books.get(position).getBookId());
		author.setText(books.get(position).getAuthor());
		publish.setText(books.get(position).getBookPublish());
		amount.setText(books.get(position).getBookAmount());
		return convertView;
	}

}
