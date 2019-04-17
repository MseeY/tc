package com.mitenotc.engine.ticket;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTicket {
	protected String lotteryId;
	//	子玩法代码
	protected String childId = "0";//默认为0
	//	销售方式
	protected String salesType = "1";//默认为复式
	//	投注号码
	protected List<List<Integer>> lotteryNums = new ArrayList<List<Integer>>();
	//	倍数
	protected int fold = 1;
	//	注数
	protected long lotteryCount = 1;
	//	金额
	protected long ticketAmount;
	
	protected int price = 200;//每注 的单价
	
	protected  boolean isTicketAvailable;
	
	public abstract long calcLotteryCount();
}
