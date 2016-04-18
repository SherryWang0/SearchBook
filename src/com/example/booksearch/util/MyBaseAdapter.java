package com.example.booksearch.util;

import java.util.List;

import com.example.booksearch.R;
import com.example.booksearch.model.BookMessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyBaseAdapter extends BaseAdapter
{
	private Context context;
	private List<BookMessage> list;
	public MyBaseAdapter(Context context, List<BookMessage> list)
	{
		this.context = context;
		this.list = list;
	}
	@Override
	public int getCount()
	{
		return list.size();
	}
	@Override
	public Object getItem(int position)
	{
		return list.get(position);
	}
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view;
		if(convertView == null)
			view = LayoutInflater.from(context).inflate(R.layout.list_layout, null);
		else
			view = convertView;
		TextView bookName = (TextView)view.findViewById(R.id.book_name);
		TextView author = (TextView)view.findViewById(R.id.author);
		bookName.setText(list.get(position).getName());
		author.setText(list.get(position).getAuthor());
		return view;
	}
}
