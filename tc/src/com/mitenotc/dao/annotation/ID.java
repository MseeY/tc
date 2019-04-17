package com.mitenotc.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标示主键的信息
 * @author mitenotc
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ID {
	/**
	 * 主键是否自增
	 * @return
	 */
	boolean autoincrement();
	String value() default "";
}
