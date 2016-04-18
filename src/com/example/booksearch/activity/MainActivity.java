package com.example.booksearch.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.example.booksearch.R;
import com.example.booksearch.model.BookMessage;
import com.example.booksearch.util.HttpCallbackListener;
import com.example.booksearch.util.HttpUtil;
import com.example.booksearch.util.MyBaseAdapter;
import com.example.booksearch.util.Utility;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity
{
	private EditText edit;
	private Button beginSearch;
	private Button scan;
	private ListView listView;
	private Button more;
	private ProgressDialog pdialog;
	private static final int WORD_SEARCH = 0;
	private static final int ISBN_SEARCH = 1;
	private List<BookMessage> list;
	private MyBaseAdapter myBaseAdapter;
	private String q;
	private int point = 0;
	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			List<BookMessage> l = (List<BookMessage>)msg.obj;
			list.addAll(l);
			myBaseAdapter = new MyBaseAdapter(MainActivity.this, list);
			listView.setAdapter(myBaseAdapter);
			closeProgressDialog();
			more.setVisibility(View.GONE);
			more.setText("加载更多...");
			listView.setVisibility(ListView.VISIBLE);
			listView.setSelection(point);
			listView.setSelection(point+1);
			listView.setSelection(point+2);
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		edit = (EditText)findViewById(R.id.edit);
		beginSearch = (Button)findViewById(R.id.search_button);
		more = (Button)findViewById(R.id.more);
		scan = (Button)findViewById(R.id.scan);
		scan.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startScan();
			}
		});
		more.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				more.setText("正在加载...");
				point = listView.getFirstVisiblePosition();
				searchBooks(q, list.size());
			}
		});
		listView = (ListView)findViewById(R.id.listView);
		listView.setOnScrollListener(new OnScrollListener()
		{
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				if(scrollState == OnScrollListener.SCROLL_STATE_IDLE && listView.getLastVisiblePosition() == list.size()-1)
					more.setVisibility(View.VISIBLE);
				else
					more.setVisibility(View.GONE);
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
			}
		});
		beginSearch.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				InputMethodManager imm = (InputMethodManager)getSystemService(MainActivity.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				showProgressDialog();
				list = new ArrayList<BookMessage>();
				q = edit.getText().toString();
				searchBooks(q, list.size());
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Intent intent = new Intent(MainActivity.this, MessageActivity.class);
				intent.putExtra("type", WORD_SEARCH);
				intent.putExtra("book", list.get(position));
				startActivity(intent);
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		String re = result.getContents();
		if(re != null)
		{
			Intent intent = new Intent(MainActivity.this, MessageActivity.class);
			intent.putExtra("type", ISBN_SEARCH);
			intent.putExtra("isbnCode", re);
			startActivity(intent);
		}
	}
	public void startScan()
	{
		IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
		intentIntegrator.initiateScan();
	}
	protected void searchBooks(String q, int index)
	{
		String address = "https://api.douban.com/v2/book/search?q=" + URLEncoder.encode(q) + "&start=" + index;
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener()
		{
			@Override
			public void onFinish(final String response)
			{
				Message message = new Message();
				message.obj = Utility.parseJsonMore(response);
				handler.sendMessage(message);
			}

			@Override
			public void onError(Exception e)
			{
			}
		});
	}
	private void showProgressDialog()
	{
		if(pdialog == null)
		{
			pdialog = new ProgressDialog(this);
			pdialog.setMessage("正在加载...");
			pdialog.setCanceledOnTouchOutside(false);
		}
		pdialog.show();
	}
	private void closeProgressDialog()
	{
		if(pdialog != null)
			pdialog.dismiss();
	}
}
