package com.mitenotc.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * 工厂
 * 
 * @author Administrator
 * 
 */
public class BeanFactory {
	private static Properties properties;
	static {
		properties = new Properties();
		try {
			properties.load(BeanFactory.class.getClassLoader().getResourceAsStream("bean.properties"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 获取到对应实例
	 * 
	 * @param engine
	 * @return
	 */
	public static <T> T getImpl(Class<T> clazz) {
		String name = clazz.getSimpleName();
		String className = properties.getProperty(name);// name:简单名称
		try {
			return (T) Class.forName(className).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
