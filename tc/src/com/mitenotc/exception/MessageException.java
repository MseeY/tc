package com.mitenotc.exception;

/**
 * 返回的 Message 中 A 不为0,此时,B 中包含错误信息
 * @author mitenotc
 */
public class MessageException extends Exception{

	public MessageException() {
		super();
	}

	public MessageException(String detailMessage) {
		super(detailMessage);
	}
}
