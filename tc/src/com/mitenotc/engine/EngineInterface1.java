package com.mitenotc.engine;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.exception.MessageException;

/**
 * 该接口是对 EngineInterface 的一个保留
 * @author mitenotc
 */
public interface EngineInterface1 {
	/**
	 * 用户注册: CMD 1000
	 * @param A: 推荐人
	 * @param B: 密码
	 * @param C: 彩名
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1000(String intro, String psd, String lotteryName)
			throws MessageException;

	MessageBean getRegistData(String intro, String psd, String lotteryName);

}
