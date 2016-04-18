package com.example.booksearch.activity;

import java.util.List;

import com.example.booksearch.R;
import com.example.booksearch.model.BookMessage;
import com.example.booksearch.util.HttpCallbackListener;
import com.example.booksearch.util.HttpUtil;
import com.example.booksearch.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageActivity extends Activity
{
	private TextView name;
	private ImageView image;
	private TextView author;
	private TextView publish;
	private TextView publishTime;
	private TextView content;
	private ProgressDialog pdialog;
	private TextView isbn;
	private BookMessage book;
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			BookMessage book = (BookMessage)msg.obj;
			name.setText(book.getName());
			image.setImageBitmap(book.getPic());
			author.setText(book.getAuthor());
			publish.setText(book.getPublish());
			isbn.setText(book.getIsbn());
			publishTime.setText(book.getPublishTime());
			content.setText(book.getContent());
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.book_layout);
		name = (TextView)findViewById(R.id.book_name);
		image = (ImageView)findViewById(R.id.image);
		author = (TextView)findViewById(R.id.author);
		publish = (TextView)findViewById(R.id.publish);
		isbn = (TextView)findViewById(R.id.isbn);
		publishTime = (TextView)findViewById(R.id.publish_time);
		content = (TextView)findViewById(R.id.content);
		Intent intent = getIntent();
		int type = intent.getIntExtra("type", -1);
		if(type == 0)
		{
			book = (BookMessage)intent.getSerializableExtra("book");
			name.setText(book.getName());
			image.setImageBitmap(book.getPic());
			author.setText(book.getAuthor());
			publish.setText(book.getPublish());
			isbn.setText(book.getIsbn());
			publishTime.setText(book.getPublishTime());
			content.setText(book.getContent());
		}
		else if(type == 1)
		{
			String isbnCode = intent.getStringExtra("isbnCode");
			parseISBN(isbnCode);
		}
	}
//	private void search(String word)
//	{
//		showProgressDialog();
//		String address = "https://api.douban.com/v2/book/search?q=" + word;
//		HttpUtil.sendHttpRequest(address, new HttpCallbackListener()
//		{
//			@Override
//			public void onFinish(final String response)
//			{
//				List<BookMessage> l = Utility.parseJsonMore(response);
//				Message message = new Message();
//				message.obj = list;
//				message.what = 0;
//				handler.sendMessage(message);
//			}
//			@Override
//			public void onError(Exception e)
//			{
//			}
//		});
//		closeProgressDialog();
//	}
	private void parseISBN(String isbnCode)
	{
		showProgressDialog();
		String address = "http://api.douban.com/v2/book/isbn/" + isbnCode;
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener()
		{
			@Override
			public void onFinish(final String response)
			{
				BookMessage book = Utility.parseJSON(response);
				Message message = new Message();
				message.obj = book;
				handler.sendMessage(message);
			}
			@Override
			public void onError(Exception e)
			{
			}
		});
		closeProgressDialog();
	}
	private void showProgressDialog()
	{
		if(pdialog == null)
		{
			pdialog = new ProgressDialog(this);
			pdialog.setMessage("ÕýÔÚ¼ÓÔØ...");
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
