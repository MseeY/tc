package com.mitenotc.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mitenotc.bean.HallItem_Bean;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.ui_utils.AwardDetailCust;

public class FakeDatas {

	public static List<HallItem_Bean> getHallItemDatas() {
		List<HallItem_Bean> list = new ArrayList<HallItem_Bean>();
		/*int[] logoIds = new int[] { R.drawable.logo_kl8, R.drawable.logo_dlt,
				R.drawable.logo_hljd11, R.drawable.logo_jclq,
				R.drawable.logo_jczq, R.drawable.logo_jxd11,
				R.drawable.logo_jxssc, R.drawable.logo_k2, R.drawable.logo_k3 };
		String[] titles = new String[] { "快乐8", "大乐透", "11选5", "竞彩篮球", "竞彩足球",
				"jxdll", "时时彩", "快2", "快3" };
		int[] awardIconIds = new int[] { 1, 1, 1 };
		String[] descs = new String[] { "快乐8 彩票描述", "大乐透 彩票描述", "11选5 彩票描述",
				"竞彩篮球 彩票描述", "竞彩足球 彩票描述", "jxdll 彩票描述", "时时彩 彩票描述", "快2 彩票描述",
				"快3 彩票描述" };
		String[] issues = new String[] { "快乐8 期次信息", "大乐透 期次信息", "11选5 期次信息",
				"竞彩篮球 期次信息", "竞彩足球 期次信息", "jxdll 期次信息", "时时彩 期次信息", "快2 期次信息",
				"快3 期次信息" };
		String[] times = new String[] { "截止:7小时13分", "截止:7小时13分", "截止:7小时13分",
				"截止:7小时13分", "截止:7小时13分", "截止:7小时13分", "截止:7小时13分",
				"截止:7小时13分", "截止:7小时13分" };
		for (int i = 0; i < logoIds.length; i++) {
			list.add(new HallItem_Bean(logoIds[i], titles[i], awardIconIds,
					descs[i], issues[i], times[i]));
		}*/
		return list;
	}

	public static List<MessageBean> getAwardInfos() {
		List<MessageBean> list = new ArrayList<MessageBean>();
		for (int i = 0; i < 6; i++) {
			MessageBean bean = new MessageBean();
			bean.setA("FC633");
			bean.setB("双色球");
			bean.setC("123456");
			bean.setD("2013-12-25");
			bean.setE("12,33,16,29,28,33,15");
			bean.setF("9");
			list.add(bean);
		}
		for (int i = 0; i < 6; i++) {
			MessageBean bean = new MessageBean();
			bean.setA("D9");
			bean.setB("任选九场");
			bean.setC("999999");
			bean.setD("2013-12-25");
			bean.setE("3,3,3,3,3,1,1,1,1");
			list.add(bean);
		}
		for (int i = 0; i < 6; i++) {
			MessageBean bean = new MessageBean();
			bean.setA("K3");
			bean.setB("快3");
			bean.setC("333333");
			bean.setD("2013-12-25");
			bean.setE("3,5,6");
			list.add(bean);
		}
		return list;
	}

	public static List<MessageBean> getHallList() {
		List<MessageBean> list = new ArrayList<MessageBean>();
//		 <item>118</item><!-- ssq -->
//	        <item>100</item><!-- pl3 -->
//	        <item>102</item><!-- pl5 -->
//	        <item>106</item><!-- dlt,快3 未添加 -->
//	        <item>112</item><!-- 十一运,十一选5 -->
//	        <item>119</item><!-- SSC, 具体号码待确定-->
//	        <item>108</item><!-- SfC-->
//	        <item>109</item><!-- 任9场-->
//	        <item>117</item><!-- 七乐彩 -->
//	        <item>103</item><!-- 七星彩 -->
		int[] codes ={118,100,102,106,112,119,108,109,117,103}; //MyApp.res.getStringArray(R.array.codes_lottery);
		String[] names = new String[] { "ssq", "七乐彩", "七星彩 ", "xyc ", "dlt ",
				"22x5 ", "pl3", "pl5", "SSC", "快3", "胜负彩 ", "任选9场" };
		String[] issues = new String[] { "20131228001", "20131228002",
				"20131228003", "20131228004", "20131228005", "20131228006",
				"20131228007", "20131228008", "20131228009", "20131228010",
				"20131228011", "20131228012" };

		String[] endTime = new String[] { "2013-12-29", "2013-12-29",
				"2013-12-29", "2013-12-29", "2013-12-29", "2013-12-29",
				"2013-12-29", "2013-12-29", "2013-12-29", "2013-12-29",
				"2013-12-29", "2013-12-29", "2013-12-29" };
		int[] jiaJiang = new int[] { 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 0 };

		String[] descs = new String[] { "ssq 彩票描述", "七乐彩 彩票描述", "七星彩 彩票描述 ",
				"xyc 彩票描述 ", "dlt  彩票描述", "22x5  彩票描述", "pl3 彩票描述", "pl5 彩票描述",
				"SSC 彩票描述", "快3 彩票描述", "胜负彩  彩票描述", "任选9场 彩票描述" };

		String[] remainTime = new String[] { "1天3小时55分", "2天7小时55分",
				"3天3小时55分", "9小时55分", "55分30秒", "3小时55分", "6小时55分", "1天3小时20分",
				"1天9小时55分", "59分 : 55秒", "2天5小时22分", "1天3小时55分", "1天3小时55分" };
		for (int i = 0; i < codes.length; i++) {
			MessageBean bean = new MessageBean();
			bean.setA(codes[i]+"");
			bean.setB(names[i]);
			bean.setD(issues[i]);
			bean.setE(endTime[i]);
			bean.setH(descs[i]);
			bean.setG(remainTime[i]);
			bean.setK(jiaJiang[i] + "");

			list.add(bean);
		}

		MessageBean messageCenter = new MessageBean();
		messageCenter.setA("a");
		messageCenter.setB("消息中心");
		messageCenter.setD("2014-1-7");
		messageCenter.setE("2014-1-7");
		messageCenter.setH("消息中心描述");
		list.add(messageCenter);
		return list;
	}

	public static List<String> getCodeList() {
		String[] codes = MyApp.res.getStringArray(R.array.codes_lottery);
		return Arrays.asList(codes);
	}

	public static List<MessageBean> getKjInfoList() {

		List<MessageBean> list = new ArrayList<MessageBean>();

		String[] jx = new String[] { "一等奖", "二等奖", "三等奖", "四等奖", "五等奖", "六等奖",
				"七等奖", "八等奖", "九等奖", "十等奖", };
		String[] zjzs = new String[] { "112", "24", "123", "27", "2", "92",
				"719", "2", "5", "13", };
		String[] jj = new String[] { "134", "7868", "678832", "1235445",
				"345345", "345762", "457564", "8678", "123123", "7456",
				"123123", };

		for (int i = 0; i < 10; i++) {
			MessageBean bean = new MessageBean();
			bean.setA(jx[i]);
			bean.setB(zjzs[i]);
			bean.setC(jj[i]);
			list.add(bean);
		}
		return list;
	}

	public static List<MessageBean> getBettingDetailList() {
		List<MessageBean> list = new ArrayList<MessageBean>();
		for (int i = 0; i < 5; i++) {
			MessageBean bean = new MessageBean();
			bean.setC("单式 1 倍");
			bean.setD("01  02  03  04  05  06  07");
			list.add(bean);
		}
		return list;
	}

	public static List<MessageBean> getChaseDetail() {
		List<MessageBean> list = new ArrayList<MessageBean>();
		for (int i = 0; i < 3; i++) {
			MessageBean bean = new MessageBean();
			bean.setA("111111111111111");
			bean.setB("101");
			bean.setB1("双色球");
			bean.setC("20140103");
			bean.setD("2014-1-2");
			bean.setE("2.00");
			bean.setF("1");
			bean.setG("1");
			bean.setH("1");
			list.add(bean);
		}
		return list;
	}

	public static List<MessageBean> getChaseDetail_unfinished() {
		List<MessageBean> list = new ArrayList<MessageBean>();
		for (int i = 0; i < 5; i++) {
			MessageBean bean = new MessageBean();
			bean.setA("3333333");
			bean.setB("101");
			bean.setB1("双色球");
			bean.setC("20140105");
			bean.setD("2014-1-2");
			bean.setE("2.00");
			bean.setF("1");
			bean.setG("1");
			bean.setH("1");
			list.add(bean);
		}
		return list;
	}

	public static List<MessageBean> getAwardDetail() {
		List<MessageBean> mList = new ArrayList<MessageBean>();
		for (int i = 0; i < 9; i++) {

			MessageBean awardResult = new MessageBean();
			awardResult.setA("FC633");
			awardResult.setB("双色球");
			awardResult.setC("123456");
			awardResult.setD("2013-12-25");
			awardResult.setE("12,33,16,29,28,33,15");
			awardResult.setF("9");
			awardResult.setG("897");
			awardResult.setH("9999");
			
			List<MessageBean> list = new ArrayList<MessageBean>();
			for (int j = 0; j < 3; j++) {
				MessageBean bean = new MessageBean();
				bean.setA("一等奖");
				bean.setB("333");
				bean.setC("88888");

				list.add(bean);
			}
			awardResult.setLIST(list);
			
			mList.add(awardResult);
		}

		return mList;
	}

	public static List<MessageBean> getCustomDatas(String custCodes) {
		List<MessageBean> list = new ArrayList<MessageBean>();

		int[] codes ={118,1001,1002,1003,1004};
		String[] names = new String[] { "双色球","购彩中心", "开奖信息","用户中心","消息中心" };
		String[] issues = new String[] { null,null, null, null, null};

//		String[] endTime = new String[] { "2013-12-29", null, null, null };
		String[] endTime = new String[] { null,null, null, null, null };
		int[] jiaJiang = new int[] { 1,0,0,0,0 };

		String[] descs = new String[] { "双色球 描述信息","购彩中心 彩票描述", "开奖信息 描述信息","用户中心 描述信息","消息中心 描述信息"};

//		String[] remainTime = new String[] { "1天3小时55分", null, null, null};
		String[] remainTime = new String[] { null,null, null, null, null};
		for (int i = 0; i < codes.length; i++) {
			MessageBean bean = new MessageBean();
			bean.setA(codes[i]+"");
			bean.setB(names[i]);
			bean.setD(issues[i]);
			bean.setE(endTime[i]);
			bean.setH(descs[i]);
			bean.setG(remainTime[i]);
			bean.setK(jiaJiang[i] + "");

			list.add(bean);
		}
		list.addAll(getHallList());
		String[] split = custCodes.split(",");
		List<MessageBean> result_list = new ArrayList<MessageBean>();
		for (int i = 0; i < split.length; i++) {
			MessageBean result_bean = getMessageBeanByCode(split[i],list);
			if(result_bean != null){
				result_list.add(result_bean);
			}
		}
		return result_list;
	}
	
	public static MessageBean getMessageBeanByCode(String code,List<MessageBean> beans){
		for (MessageBean messageBean : beans) {
			if(code.equals(messageBean.getA()))
				return messageBean;
		}
		return null;
	}
}
