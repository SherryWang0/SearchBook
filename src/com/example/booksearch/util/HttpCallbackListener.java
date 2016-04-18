package com.example.booksearch.util;

public interface HttpCallbackListener
{
	void onFinish(String address);
	void onError(Exception e);
}
