package com.example.booksearch.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.booksearch.model.BookMessage;

public class HttpUtil
{
	public static void sendHttpRequest(final String address, final HttpCallbackListener listener)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				HttpURLConnection connection = null;
				try
				{
					URL url = new URL(address);
					connection = (HttpURLConnection)url.openConnection();
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					connection.setRequestMethod("GET");
					InputStream is = connection.getInputStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					StringBuilder sb = new StringBuilder();
					String line;
					while((line = br.readLine()) != null)
						sb.append(line);
					if(listener != null)
						listener.onFinish(sb.toString());
				}catch(Exception e)
				{
					if(listener != null)
						listener.onError(e);
					e.printStackTrace();
				}finally
				{
					if(connection != null)
						connection.disconnect();
				}
			}
		}).start();
	}
}
