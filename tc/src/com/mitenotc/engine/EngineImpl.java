package com.mitenotc.engine;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.exception.MessageException;
import com.mitenotc.net.Protocol;
/**
 * @author mitenotc
 *该类目前没用
 */
public class EngineImpl implements EngineInterface1{

	/**
	 * 继承 是 给protocol 完善(设置)信息的方法之一,也可以用 注解. 在代理类中调用注解的相关信息即可.
	 * 相比而言, 继承多出了一个类要继承,而且该类中的每个方法做的事情基本相同,这样就应该用 代理 来抽取 共同的部分.
	 * 以减少 冗余代码.
	 */
	@Override
	public MessageBean getRegistData(String intro, String psd,
			String lotteryName) {
		Protocol.getInstance().setCMD(""+1000);
		return null;
	}

	@Override
	public MessageBean getCMD_1000(String intro, String psd, String lotteryName)
			throws MessageException {
		return null;
	}

}
