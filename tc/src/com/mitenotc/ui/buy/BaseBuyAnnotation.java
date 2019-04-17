package com.mitenotc.ui.buy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BaseBuyAnnotation {
	String lotteryId() default "118";
	String salesType() default "1";
	String childId() default "0";
}
