package com.mitenotc.exception;

/**
 *发送协议出现异常,例如 Protocol 中 cmd 没有赋值 
 * @author mitenotc
 */
public class ProtocolException extends Exception{

	public ProtocolException() {
		super();
	}

	public ProtocolException(String detailMessage) {
		super(detailMessage);
	}
}
