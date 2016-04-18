package com.example.booksearch.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.booksearch.model.BookMessage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Utility
{
	public static List<BookMessage> parseJsonMore(String response)
	{
		List<BookMessage> list = new ArrayList<BookMessage>();
		try
		{
			JSONObject jsonObject = new JSONObject(response);
			int count = jsonObject.getInt("count");
			int start = jsonObject.getInt("start");
			String book = jsonObject.getString("books");
			JSONArray jsonArray = new JSONArray(book);
			for(int i = 0; i < count; i++)
			{
				JSONObject jO = jsonArray.getJSONObject(i);
				BookMessage bookMessage = parse(jO);
				list.add(bookMessage);
				start++;
			}
		}catch(JSONException e)
		{
			e.printStackTrace();
		}
		return list;
	}
	public static BookMessage parseJSON(String response)
	{
		BookMessage book = new BookMessage();
		JSONObject jsonObject = null;
		try
		{
			jsonObject = new JSONObject(response);
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		return parse(jsonObject);
	}

	private static BookMessage parse(JSONObject jsonObject)
	{
		BookMessage book = new BookMessage();
		try
		{
			String url = jsonObject.getString("image");
			String name = jsonObject.getString("title");
			String author = jsonObject.getString("author");
			String publish = jsonObject.getString("publisher");
			String publishTime = jsonObject.getString("pubdate");
			String content = jsonObject.getString("summary");
			String isbn = jsonObject.getString("isbn13");
			byte[] image = parseImage(url);
			book.setAuthor(author);
			book.setContent(content);
			book.setPic(image);
			book.setName(name);
			book.setPublish(publish);
			book.setPublishTime(publishTime);
			book.setIsbn(isbn);
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		return book;
	}
	private static byte[] parseImage(String url)
	{
		Bitmap b = null;
		byte[] by = null;
		try
		{
			URL u = new URL(url);
			HttpURLConnection connection = (HttpURLConnection)u.openConnection();
			InputStream is = connection.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			b = BitmapFactory.decodeStream(bis);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			b.compress(Bitmap.CompressFormat.PNG, 0, baos);
			by = baos.toByteArray();
		}catch(MalformedURLException e)
		{
			e.printStackTrace();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		return by;
	}

}
