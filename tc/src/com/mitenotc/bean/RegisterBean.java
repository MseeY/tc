package com.mitenotc.bean;
/**
 *  用户注册信息存储在sharedPerefences中
 *  此处存放的是对应的key
 * @author wanli
 *
 */
public interface RegisterBean {
	/**
	 * 用户注册对应的SharedPerferences name;
	 */
	String URM="U_MSG";
	/**
	 * 用户的手机号码 对应的存放 key 
	 */
	String UP="U_PHONE";
	/**
	 * 用户的手机号码 对应的存放 key 
	 */
	String UV="U_VERIFY";
	/**
	 * 用户的手机号码 对应的存放 key 
	 */
	String UPWD="U_PWD";
	
	

}
