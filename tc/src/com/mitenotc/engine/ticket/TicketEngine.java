package com.mitenotc.engine.ticket;

import com.mitenotc.bean.TicketBean;

public class TicketEngine {

//	private final static TicketEngine instance = new TicketEngine();
//	private TicketEngine(){}
//	public static TicketEngine getInstance() {
//		return instance;
//	}
	
	public static TicketBean createTicketByLotteryId(String id){
		try {
			Class<TicketBean> ticket = (Class<TicketBean>) Class.forName("com.mitenotc.bean.ticket.TicketBean_"+id);
			return ticket.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("该彩种的ticket 不存在");
		}
	}
}
