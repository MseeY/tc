package com.mitenotc.net;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.mitenotc.utils.AppUtil;


public class MessageJson extends JSONObject{

	public MessageJson() {
		super();
	}
	public MessageJson(JSONObject copyFrom, String[] names)
			throws JSONException {
		super(copyFrom, names);
	}
	public MessageJson(JSONTokener readFrom) throws JSONException {
		super(readFrom);
	}
	public MessageJson(Map copyFrom) {
		super(copyFrom);
	}
	public MessageJson(String json) throws JSONException {
		super(json);
	}
	@Override
	public JSONObject put(String name, double value) {
		try {
			return super.put(name, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public JSONObject put(String name, long value){
		try {
			return super.put(name, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public JSONObject put(String name, int value){
		try {
			return super.put(name, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public JSONObject put(String name,boolean value){
		try {
			return super.put(name, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public JSONObject put(String name, Object value){
		if(value instanceof String){
			if(AppUtil.isEmpty((String)value)){
				return this;
			}
		}
		try {
			return super.put(name, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public  Object get(String name){
		try {
			return super.get(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public String getString(String name){
		try {
			return super.getString(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public JSONObject getJSONObject(String name){
		try {
			return super.getJSONObject(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
