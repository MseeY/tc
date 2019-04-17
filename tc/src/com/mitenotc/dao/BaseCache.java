package com.mitenotc.dao;

import com.mitenotc.net.MessageJson;

public interface BaseCache 
{
	public void setCache(int cmd,MessageJson quest ,MessageJson response);
	public MessageJson getCache(int cmd,MessageJson quest,int isforce);
}