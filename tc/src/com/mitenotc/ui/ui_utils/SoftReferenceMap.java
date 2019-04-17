package com.mitenotc.ui.ui_utils;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * 朱万利
 * 
 * 软引用的map
 */
public class SoftReferenceMap<K, V> extends HashMap<K, V> {
	// private HashMap<K, SoftReference<V>> temp;
	private HashMap<K, SoftValue<K, V>> temp;
	private ReferenceQueue<V> q;

	// 改造SoftReference——功能——需要在操作之前——清除的工作——所有"空袋子"——temp循环处理
	// 增加功能：在”袋子“上贴一个标签（K），方便在map中删除工作

	public SoftReferenceMap() {
		// temp = new HashMap<K, SoftReference<V>>();
		temp = new HashMap<K, SoftValue<K, V>>();
		q = new ReferenceQueue<V>();
	}

	// @Override
	// public boolean containsKey(Object key) {
	// boolean containsKey = temp.containsKey(key);
	// if(containsKey)
	// {
	// SoftReference<V> softReference = temp.get(key);
	// return softReference.get()!=null;
	// }
	// return false;
	// }
	//	
	// @Override
	// public V get(Object key) {
	// SoftReference<V> softReference = temp.get(key);
	// return softReference.get();
	// }
	//	
	// @Override
	// public V put(K key, V value) {
	// temp.put(key, new SoftReference<V>(value));
	// return null;
	// }
	@Override
	public boolean containsKey(Object key) {
		clearMap();
		return temp.containsKey(key);
	}

	@Override
	public V get(Object key) {
		clearMap();
		SoftReference<V> softReference = temp.get(key);
		return softReference.get();
	}

	@Override
	public V put(K key, V value) {
		temp.put(key, new SoftValue<K, V>(key, value, q));
		return null;
	}
	/**
	 * 清空没有”手机“的”袋子“
	 */
	private void clearMap()
	{
		SoftValue<K,V> poll = (SoftValue<K, V>) q.poll();
		while(poll!=null)
		{
			temp.remove(poll.key);
			poll = (SoftValue<K, V>) q.poll();
		}
	}

	/**
	 * 对”袋子“的改造
	 * 
	 * @author Administrator
	 * 
	 * @param <K>
	 * @param <V>
	 */
	private class SoftValue<K, V> extends SoftReference<V> {
		private Object key;

		public SoftValue(K key, V r, ReferenceQueue<? super V> q) {
			super(r, q);
			this.key = key;
		}

	}

}
