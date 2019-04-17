package com.mitenotc.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.download.util.StringUtils;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.R;
import com.mitenotc.tc.MyApp;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;

/**
 * 订单javaBean
 * 
 * @author mitenotc
 */
public class OrderBean {
	private String orderId;
	/** 购彩时间戳(防止协议劫持) */
	private String orderedTime;
	/** 玩法代码(竞彩玩法见附录 为ticket 中的salesType) */
	private String lotteryId;
	/** 内部识别id **/
	private String appId;
	/** 期号 */
	private String issue;
	/** 购彩总金额 */
	private long amount;
	/** 中奖后是否停止追号(0不停止；1停止) */
	private String isStop;
	/** 追号期数 */
	private int chaseNum;
	/** 是否混关(竞彩独有)是否混关（1是0否） **/
	private String isMixture = "0";
	/** 附加参数(竞彩保留参数4001 J没有经过特殊定义可以不传递2014-11-19) **/
	private String additionParam;
	// 倍数
	private int fold = 1;

	private List<TicketBean> tickets = new ArrayList<TicketBean>();
	/***********************************************************************************************
	 * 以下属性只适用于竞彩 如有需要须增加特殊说明注释 /**整个方案的过关方式
	 */
	private String Obunch = "";
	/** 整个方案的过关方式List */
	private List<Integer> oPassTypeList = new LinkedList<Integer>();
	/** 整个方案 场次范围 */
	private List<MessageBean> ticketsMB = new LinkedList<MessageBean>();// 竞彩场次MessageBean

	/**
	 * 竞彩选票总场次数目
	 * 
	 * @return
	 */
	@SuppressLint("UseValueOf")
	public int getSceneNumber() {
		int s = 0;
		for (int i = 0; i < tickets.size(); i++) {
			s += tickets.get(i).getSession();
		}
		return s;
	}

	public void clearNullTicket() {
		ticketsMB.clear();
		for (int i = 0; i < tickets.size(); i++) {
			if (tickets.get(i).getSelectMessageBean().size() > 0) {
				for (int j = 0; j < tickets.get(i).getSelectMessageBean().size(); j++) {
					ticketsMB.add(tickets.get(i).getSelectMessageBean().get(j));
				}
			} else {
				tickets.remove(i);// 没有了选票 移除相应的ticket(废弃的空票)
			}
		}
		if (ticketsMB.size() > 0) {
			Collections.sort(ticketsMB, new Comparator<MessageBean>() {// 按筛选场次排序
						@Override
						public int compare(MessageBean mb0, MessageBean mb1) {
							Integer mInteger0 = null, mInteger1 = null;
							if (!StringUtils.isBlank(mb0.getG())) {
								mInteger0 = new Integer(mb0.getG());
							}
							if (!StringUtils.isBlank(mb1.getG())) {
								mInteger1 = new Integer(mb1.getG());
							}
							if (mInteger0 == null || mInteger1 == null)
								return 0;
							return mInteger0.compareTo(mInteger1);
						}
					});
		}
	}

	/**
	 * 串关方式
	 * 
	 * @param passTypeList
	 */
	public void setPassTypeList(List<Integer> plist) {
		if (plist == null)
			return;
		// System.out.println("125--->"+plist.toString());
		oPassTypeList = plist;
		// for (TicketBean tb : tickets) {
		// tb.setPassTypeList(oPassTypeList);
		// }
		for (TicketBean tb : tickets) {
			tb.setPassTypeList(oPassTypeList);
		}
	}

	public List<Integer> getPassTypeList() {
		if (oPassTypeList != null && oPassTypeList.size() > 0) {
			Collections.sort(oPassTypeList);// 排序
		}
		return oPassTypeList;
	}

	/**
	 * 竞彩 修改删除 指定场次选票
	 * 
	 * @param mb
	 */
	public void removeMessageBean(int position) {
		MessageBean mb = ticketsMB.get(position);
		for (int i = 0; i < tickets.size(); i++) {// 在具体的选票中移除
			if (tickets.get(i).getSelectMessageBean() != null && -1 != tickets.get(i).getSelectMessageBean().indexOf(mb)) {// -1
																															// 表示不存在
				String key = "";
				if (mb.getC().length() > 6) {// 为了字符串截取安全
					key = mb.getC().subSequence(mb.getC().length() - 6, mb.getC().length()) + "-" + mb.getG();
					if (tickets.get(i).getLotteryTag().containsKey(key)) {
						tickets.get(i).getLotteryTag().remove(key);// 移除选果
					}
				}
				tickets.get(i).getSelectMessageBean().remove(tickets.get(i).getSelectMessageBean().indexOf(mb));// 移除选场
			}
			ticketsMB.remove(position);
		}

	}

	/**
	 * 清空竞彩选票
	 */
	public void clearALLJCTicket() {
		for (int i = 0; i < ticketsMB.size(); i++) {
			MessageBean mb = ticketsMB.get(i);
			for (int j = 0; j < tickets.size(); j++) {
				if (tickets.get(j).getSelectMessageBean() != null && -1 != tickets.get(j).getSelectMessageBean().indexOf(mb)) {// -1
																																// 表示不存在
					String key = "";
					if (mb.getC().length() > 6) {// 为了字符串截取安全
						key = mb.getC().subSequence(mb.getC().length() - 6, mb.getC().length()) + "-" + mb.getG();
						tickets.get(j).getLotteryTag().remove(key);// 移除选果
					}
					tickets.get(i).getSelectMessageBean().clear();// 移除选场
				}
			}
		}
		tickets.clear();
	}

	/**
	 * 取出所选position 位置对应的场次
	 * 
	 * @param position
	 * @return
	 */
	@SuppressLint("UseValueOf")
	public MessageBean getMessageBean(int position) {
		MessageBean temBean = null;
		if (ticketsMB.size() > position) {
			temBean = ticketsMB.get(position);
		}
		return temBean;
	}

	public List<MessageBean> getTicketsMB() {
		return ticketsMB;
	}

	/**
	 * * 竞彩设置胆拖 记录下标
	 * 
	 * @param tkMB
	 * @param pc_dz
	 * @param isChecked
	 *            为true 存储 false 移除
	 * @param position
	 *            指 MessageBean对应在ticketsMB 中的下标值
	 */
	public void setMessageBeanInvariant(int position, boolean isChecked) {
		MessageBean tkmb = ticketsMB.get(position);
		for (int i = 0; i < tickets.size(); i++) {
			// 重新设置胆拖
			if (-1 != tickets.get(i).getSelectMessageBean().indexOf(tkmb)) {
				String g = tickets.get(i).getSelectMessageBean().get(tickets.get(i).getSelectMessageBean().indexOf(tkmb)).getG();
				// System.out.println("321-------s---->c"+g+"|"+tkmb.getG());
				if ((!StringUtils.isBlank(g) && !StringUtils.isBlank(tkmb.getG())) && g.equals(tkmb.getG())) {// 为了安全期间
					tickets.get(i).getSelectMessageBean().get(tickets.get(i).getSelectMessageBean().indexOf(tkmb)).setISD(isChecked);// 设胆拖
				}
			}
		}
	}

	/**
	 * 只针对竞彩选票 清除胆拖设置
	 */
	public void clearTicketsDan() {
		for (int i = 0; i < tickets.size(); i++) {
			for (int j = 0; j < tickets.get(i).getSelectMessageBean().size(); j++) {
				tickets.get(i).getSelectMessageBean().get(j).setISD(false);
			}
		}
	}

	/**
	 * 获得竞彩选票tagList
	 * 
	 * @param pc_dz
	 * @return
	 */
	public List<String> getTagList(int position) {
		List<String> tagList = null;
		if (ticketsMB.size() > position) {
			MessageBean mB = ticketsMB.get(position);
			String key = "";
			if (Arrays.asList(MyApp.res.getStringArray(R.array.lottery_alllq)).contains(mB.getA())) {
				key = mB.getC() + "-" + mB.getG();
			} else if (Arrays.asList(MyApp.res.getStringArray(R.array.lottery_allzq)).contains(mB.getA())) {
				if (mB.getC().length() > 6) { // 为了字符串截取安全 key:
												// C批次时间-G对阵编号(每批次下的场次)
					key = mB.getC().subSequence(mB.getC().length() - 6, mB.getC().length()) + "-" + mB.getG();
				}
			}
			// System.out.println("321--key--->"+key);
			if (!StringUtils.isBlank(key)) {
				for (int i = 0; i < tickets.size(); i++) {
					if (tickets.get(i).getLotteryTag() != null && tickets.get(i).getLotteryTag().containsKey(key)) {
						tagList = tickets.get(i).getLotteryTag().get(key);
					}
				}
			}
		}
		return tagList;
	}

	/**
	 * 修改单场玩法
	 * 
	 * @param tag
	 *            (默认是: 篮球 )| 0 : 足球,
	 * @param position
	 * @param tagList
	 */

	public void updatetkMessageBean(int tag, int position, List<String> tagList) {
		String key = "";
		if (ticketsMB.size() < position)
			return;
		MessageBean mb = ticketsMB.get(position);
		switch (tag) {
		case 0:
			if (mb.getC().length() > 6) {// 为了字符串截取安全
				key = mb.getC().subSequence(mb.getC().length() - 6, mb.getC().length()) + "-" + mb.getG();
			}
			break;
		default:
			key = mb.getC() + "-" + mb.getG();
			break;
		}
		for (int i = 0; i < tickets.size(); i++) {
			if (tickets.get(i).getLotteryTag() != null && tickets.get(i).getLotteryTag().containsKey(key)) {
				if (tagList.size() > 0) {
					tickets.get(i).getLotteryTag().put(key, tagList);
					if (-1 != tickets.get(i).getSelectMessageBean().indexOf(mb)) {// 重新加载
						tickets.get(i).getSelectMessageBean().get(tickets.get(i).getSelectMessageBean().indexOf(mb)).setCH_NUM(tagList.size());
					} else {
						mb.setCH_NUM(tagList.size());
						tickets.get(i).getSelectMessageBean().add(mb);
					}
				} else if (-1 != tickets.get(i).getSelectMessageBean().indexOf(mb)) {
					tickets.get(i).getSelectMessageBean().remove(tickets.get(i).getSelectMessageBean().indexOf(mb));
				}
			}
		}
	}

	/*********************************************************************************************/

	/** 大乐透追加模式 的标记 */
	private boolean is_dlt_pursue_mode_enabled;

	/**
	 * 普通彩种
	 * 
	 * @return
	 */
	public MessageJson getOrderJson() {
		MessageJson msg = new MessageJson();
		msg.put("A", FormatUtil.timeFormat(System.currentTimeMillis()));
		msg.put("B", lotteryId);
		msg.put("C", issue);
		msg.put("D", amount);
		msg.put("E", isStop);
		msg.put("F", chaseNum);
		msg.put("LIST", formatTicketList());
		return msg;
		// {"D":200,"LIST":[{"D":"1","E":"1","F":"200","A":"11","B":"0","C":"01"}],"E":"1","F":1,"A":"2014-05-05 15:59:23","B":"121","C":"14050544"}
	}

	/**
	 * 竞彩 4001 投注格式
	 * 
	 * @return
	 */
	// 标签 描述 长度 可能值
	// A 购彩时间戳(防止协议劫持) 19 时间型
	// B 玩法 * 玩法说明
	// C 期号 * 20000
	// D 购彩总金额（分） *
	// E 整个方案的过关方式 * "102^103^104"
	// F 整个方案的投注内容 * 141119-001(3_1.6);(141119-002(1_3.25));141119-003(0_3.3)
	// G 票信息(LIST)：标签 描述 长度 可能值
	// A 过关方式 * 串关格式说明
	// B 投注代码，注与注之间用^分开 *
	// 141119-001(3_1.6);(141119-002(1_3.25));141119-003(0_3.3)
	// C 倍数 *
	// D 注数 *
	// H 投注场次（最小场次^投注最大场次） *
	// I 是否混关（0否1是） 1
	// J 附加参数，存到方案宣言字段 *
	public MessageJson getJCOrderJson() {
		MessageJson msg = new MessageJson();
		msg.put("D", calcAmount());// 获得计算总金额
		msg.put("E", Obunch);// "102^103^104"
		msg.put("F", formatOrderInfo());// "141119-001(3_1.6);(141119-002(1_3.25));141119-003(0_3.3)"
		msg.put("G", formatTicketList());// ticket详情
		msg.put("H", getOPassType());// 投注场次（最小场次^投注最大场次） *
		msg.put("A", FormatUtil.timeFormat(System.currentTimeMillis()));
		msg.put("B", lotteryId);
		msg.put("C", issue);// 测试期号使用 20000
		// 协议修改 无需传递
		// msg.put("I", isMixture);//是否混关（0否1是） 1
		// msg.put("J", additionParam);//附加参数，存到方案宣言字段 *
		return msg;
	}

	/**
	 * 奖金优化后的订单 BonusOptimize
	 * 
	 * @return
	 */
	public MessageJson boJCOrderJson(JSONArray G, long calcAmount) {
		MessageJson msg = new MessageJson();
		msg.put("D", calcAmount);// 获得计算总金额
		msg.put("E", Obunch);// "102^103^104"
		msg.put("F", formatOrderInfo());// "141119-001(3_1.6);(141119-002(1_3.25));141119-003(0_3.3)"
		msg.put("G", G);// ticket详情
		msg.put("H", getOPassType());// 投注场次（最小场次^投注最大场次） *
		msg.put("A", FormatUtil.timeFormat(System.currentTimeMillis()));
		msg.put("B", lotteryId);
		msg.put("C", issue);// 测试期号使用 20000
		// 协议修改 无需传递
		// msg.put("I", isMixture);//是否混关（0否1是） 1
		// msg.put("J", additionParam);//附加参数，存到方案宣言字段 *
		return msg;
	}

	// {"D":4000,"E":"102^103^104^105",
	// "F":"20141120-001(3_0.00);20141120-002(1_0.00);20141120-003(3_0.00);20141120-004(3_0.00);20141120-005(1_0.00)",
	// "G":[{"D":"2","A":"102^103^104^105",
	// "B":"20141120-001(3_0.00);20141120-002(1_0.00);20141120-003(3_0.00);20141120-004(3_0.00);20141120-005(1_0.00)",
	// "C":"10"}],
	// "A":"2014-11-21 09:42:42","B":"218","C":"20141120","H":"20141120-001^20141120-005","I":"0"}
	/**
	 * 高频彩智能追号 金额和 高频智能追号的数据从 智能追号页传入
	 * 
	 * @param amount
	 * @param G
	 * @return
	 */
	public MessageJson getOrderJson(long amount, JSONArray G) {
		MessageJson msg = new MessageJson();
		msg.put("A", FormatUtil.timeFormat(System.currentTimeMillis()));
		msg.put("B", lotteryId);
		msg.put("C", issue);
		msg.put("D", amount);
		msg.put("E", isStop);
		msg.put("F", chaseNum + "");
		msg.put("G", G);// 高频彩才有的追号详情
		msg.put("LIST", formatTicketList());
		return msg;
	}

	private JSONArray formatTicketList() {
		
		JSONArray jsonArray = new JSONArray();
		for (TicketBean ticket : tickets) {
			if(isSplitTicket(ticket.getLotteryId())){
				List<MessageJson> messageJsons = ticket.getTicketJsonArray();
				for (MessageJson messageJson : messageJsons) {
					jsonArray.put(messageJson);//{"D":"1","E":"1","F":"200","A":"11","B":"0","C":"01"}
				}
				continue;
			}else{
				if("101".equals(Obunch)){//竞彩单关
					System.out.println("lotteryId--------->"+lotteryId);
					ticket.settBunch(Obunch);//竞彩所需要的过关方式 其它都不会影响
					jsonArray=ticket.getArrayTicket();
				}else{
					//jsonArray= new JSONArray();
					ticket.settBunch(Obunch);//竞彩所需要的过关方式 其它都不会影响
//					ticket.getTicketJson();
					jsonArray.put(ticket.getTicketJson());
				}
//				ticket.settBunch(Obunch);//竞彩所需要的过关方式 其它都不会影响
//				jsonArray.put(ticket.getTicketJson());
			}
		}
		return jsonArray;// [{"D":"1","E":"1","F":"200","A":"11","B":"0","C":"01"}]
	}

	/**
	 * 格式化竞彩所有ticket 选票的信息内容字符串
	 * 
	 * @return
	 */
	private String formatOrderInfo() {
		String fcos = "";
		for (int i = 0; i < tickets.size(); i++) {
			fcos += tickets.get(i).formatLotteryNums();
			if (i != tickets.size() - 1) {
				fcos += ";";
			}
		}
		return fcos;
	}

	/**
	 * 获得场次范围
	 * 
	 * @return
	 */
	private String getOPassType() {
		String OPassType = "";
		// ticketsMB 已经确认排过序,拿出串关方式
		if (ticketsMB.size() > 0) {
			MessageBean mB = ticketsMB.get(0);// MIN最小场
			if (Arrays.asList(MyApp.res.getStringArray(R.array.lottery_alllq)).contains(mB.getA())) {
				OPassType = mB.getC() + "-" + mB.getG();
			} else if (Arrays.asList(MyApp.res.getStringArray(R.array.lottery_allzq)).contains(mB.getA())) {
				if (mB.getC().length() > 6) {// 为了字符串截取安全
					OPassType = mB.getC().subSequence(mB.getC().length() - 6, mB.getC().length()) + "-" + mB.getG();
				}
			}
			if (0 != (ticketsMB.size() - 1)) {
				mB = ticketsMB.get(ticketsMB.size() - 1);// MAX最大场
			}
			if (mB != null) {
				if (Arrays.asList(MyApp.res.getStringArray(R.array.lottery_alllq)).contains(mB.getA())) {
					OPassType += "^" + mB.getC() + "-" + mB.getG();
				} else if (Arrays.asList(MyApp.res.getStringArray(R.array.lottery_allzq)).contains(mB.getA())) {
					if (mB.getC().length() > 6) {// 为了字符串截取安全
						OPassType += "^" + mB.getC().subSequence(mB.getC().length() - 6, mB.getC().length()) + "-" + mB.getG();
					}
				}
			}
		}
		return OPassType;
	}

	/**
	 * 不包含追期和倍数
	 * 
	 * @return
	 */
	public long calcAmount() {
		if (chaseNum <= 1)
			chaseNum = 1;
		// ////System.out.println("chasenum = "+chaseNum);
		// ////System.out.println("folder = "+fold);
		amount = 0;
		for (TicketBean bean : tickets) {
			amount += bean.getTicketAmount();
		}
		amount *= chaseNum;
		// ////System.out.println("amount ====== "+amount);
		return amount;
	}

	/**
	 * 是否对下单选票进行拆分
	 * 
	 * @param lotteryId2
	 * @return
	 */
	private boolean isSplitTicket(String lotteryId2) {
		boolean tag = false;
		String[] temLottery = MyApp.res.getStringArray(R.array.split_lottery_id);
		if (AppUtil.isEmpty(lotteryId2)) {
			tag = false;
		}
		if (Arrays.asList(temLottery).contains(lotteryId2)) {
			tag = true;
		}
		return tag;
	}

	public int getLotterysCount() {
		int count = 0;
		for (TicketBean ticket : tickets) {
			count += ticket.getLotteryCount();
		}
		return count;
	}

	public void setObunch(String obunch) {
		this.Obunch = obunch;
		for (TicketBean ticket : tickets) {
			ticket.settBunch(this.Obunch);// 每一票的过关方式默认和订单里的过关方式一样
		}
	}

	public String getObunch() {
		return Obunch;
	}

	public String getOrderedTime() {
		return orderedTime;
	}

	public void setOrderedTime(String orderedTime) {
		this.orderedTime = orderedTime;
	}

	public String getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getIssue() {
		return issue;
	}

	public void setIsMixture(String isMixture) {
		this.isMixture = isMixture;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	/**
	 * 
	 * @return
	 */
	public long getAmount() {
		return calcAmount();
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public int getFold() {
		return fold;
	}

	public void setFold(int fold) {
		this.fold = fold;
		for (TicketBean ticket : tickets) {
			ticket.setFold(this.fold);
		}
	}

	public String getIsStop() {
		return isStop;
	}

	public void setIsStop(String isStop) {
		this.isStop = isStop;
	}

	public int getChaseNum() {
		return chaseNum;
	}

	public void setChaseNum(int chaseNum) {
		this.chaseNum = chaseNum;
	}

	public List<TicketBean> getTickets() {
		return tickets;
	}

	public void setTickets(List<TicketBean> tickets) {
		this.tickets = tickets;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * 判断是否只有一种销售方式. 只有当orderbean中只有一种销售方式是才能进行智能追号
	 * 
	 * @return
	 */
	public boolean isChildIdUnique() {
		if (tickets.size() == 0) {
			Toast.makeText(MyApp.context, "请至少选择一注", 0).show();
			return false;
		}
		String childId = tickets.get(0).getChildId();
		for (TicketBean ticket : tickets) {
			if (!childId.equals(ticket.getChildId())) {
				return false;
			}
		}
		return true;
	}

	public boolean isIs_dlt_pursue_mode_enabled() {
		return is_dlt_pursue_mode_enabled;
	}

	/**
	 * 设置大乐透是否为追加模式,并且改变 tickets 中所有ticket 的 price
	 * 
	 * @param is_dlt_pursue_mode_enabled
	 */
	public void setIs_dlt_pursue_mode_enabled(boolean is_dlt_pursue_mode_enabled) {
		if (!"106".equals(lotteryId)) {// 必须保证彩种为大乐透
			return;
		}

		this.is_dlt_pursue_mode_enabled = is_dlt_pursue_mode_enabled;
		for (TicketBean ticket : tickets) {
			if (is_dlt_pursue_mode_enabled) {// 追加模式时
				ticket.setChildId("1");
				ticket.setPrice(200);
			} else {// 不是 追加模式时
				ticket.setChildId("0");
				ticket.setPrice(300);
			}
		}
	}

}
