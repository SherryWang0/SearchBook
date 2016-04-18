package com.example.booksearch.model;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BookMessage implements Serializable
{
	private String name;
	private String author;
	private String publish;
	private String publishTime;
	private String content;
	private String isbn;
	private byte[] pic;
	
	public Bitmap getPic()
	{
		return BitmapFactory.decodeByteArray(pic, 0, pic.length);
	}
	public void setPic(byte[] pic)
	{
		this.pic = pic;
	}
	public String getIsbn()
	{
		return isbn;
	}
	public void setIsbn(String isbn)
	{
		this.isbn = isbn;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getAuthor()
	{
		return author;
	}
	public void setAuthor(String author)
	{
		this.author = author;
	}
	public String getPublish()
	{
		return publish;
	}
	public void setPublish(String publish)
	{
		this.publish = publish;
	}
	public String getPublishTime()
	{
		return publishTime;
	}
	public void setPublishTime(String publishTime)
	{
		this.publishTime = publishTime;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	
}
