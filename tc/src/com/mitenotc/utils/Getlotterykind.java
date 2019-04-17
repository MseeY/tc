package com.mitenotc.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mitenotc.bean.RATEBean;

/**
 * 用于智能追号 时候 获取单注奖金（便于后期智能追号的计算）
 * 
 * @author admin
 */
public class Getlotterykind {
	private static Getlotterykind instance;

	public static Getlotterykind getInstance() {
		if (instance == null) {
			instance = new Getlotterykind();
		}
		return instance;
	}

	private static Map<String, String> mMap = null;// 存放各个彩种的销售方式名称
	private List<Integer> saveBonusList = null;// 用来暂时存储 Bonus 等待 Ticeket 生成之后
												// 要存放在 RATEBean Bean 中

	private static List<Integer> pl120Bonu_hz;
	private static Map<String, Integer> pl120_hz_chidBonus;
	private static Map<String, Integer> pl112BonusMap = new HashMap<String, Integer>();
	private static Map<String, Integer> pl119BonusMap;
	private static Map<String, Integer> pl120BonusMap;

	/**
	 * 串关 竞彩根据所选串关获得获得对应的串关方式
	 * 
	 * @param key
	 * @return
	 */
	public static int[] getCG(int key) {
		int[] list = null;
		switch (key) {
		case 31:
		case 603:
		case 33:
		case 605:
		case 35:
		case 607:
		case 38:
		case 610:
			// jcMap.put("603", new int[]{102});//"3串3"
			// jcMap.put("605", new int[]{102});//"4串6"
			// jcMap.put("607", new int[]{102});//5串10
			// jcMap.put("610", new int[]{102});//6串15
			list = new int[] { 102 };
			break;
		case 32:
		case 604:
		case 39:
		case 611:
			// jcMap.put("604", new int[]{103});//"4串4"
			// jcMap.put("611",new int[]{103});//6串20
			list = new int[] { 103 };
			break;

		case 37:
		case 609:
		case 50:
		case 805:
		case 44:
		case 704:
			// jcMap.put("609",new int[]{105});//6串6
			// jcMap.put("805",new int[]{105});//"8串56"
			// jcMap.put("704",new int[]{105});//7串21
			list = new int[] { 105 };
			break;
		case 45:
		case 705:
		case 51:
		case 806:
		case 34:
		case 606:
			// jcMap.put("705", new int[]{104});//7串35
			// jcMap.put("806", new int[]{104});//"8串70"
			// jcMap.put("606", new int[]{104});//5串5
			list = new int[] { 104 };
			break;
		case 17:
		case 118:
		case 36:
		case 608:
			// jcMap.put("118", new int[]{102,103});//"3串4"
			// jcMap.put("608", new int[]{102,103});//5串20
			list = new int[] { 102, 103 };
			break;
		case 19:
		case 120:
			// jcMap.put("120", new int[]{103,104});//"4串5"
			list = new int[] { 103, 104 };
			break;
		case 20:
		case 121:
			// jcMap.put("121", new int[]{102,103,104});//"4串11"
			list = new int[] { 102, 103, 104 };
			break;
		case 22:
		case 123:
			// jcMap.put("123",new int[]{104,105});//5串6
			list = new int[] { 104, 105 };
			break;
		case 23:
		case 124:
			// jcMap.put("124",new int[]{103,104,105});//5串16
			list = new int[] { 103, 104, 105 };
			break;
		case 24:
		case 125:
			// jcMap.put("125",new int[]{102,103,104,105});//5串26
			list = new int[] { 102, 103, 104, 105 };
			break;
		case 26:
		case 127:
			// jcMap.put("127",new int[]{105,106});//6串7
			list = new int[] { 105, 106 };
			break;
		case 27:
		case 128:
			// jcMap.put("128",new int[]{104,105,106});//6串22
			list = new int[] { 104, 105, 106 };
			break;
		case 40:
		case 612:
			// jcMap.put("612",new int[]{102,103});//6串35
			list = new int[] { 102, 103 };
			break;
		case 28:
		case 129:
			// jcMap.put("129",new int[]{103,104,105,106});//6串42
			list = new int[] { 103, 104, 105, 106 };
			break;
		case 41:
		case 613:
			// jcMap.put("613",new int[]{102,103,104});//6串50
			list = new int[] { 102, 103, 104 };
			break;
		case 30:
		case 602:
			// jcMap.put("602",new int[]{102,103,104,105,106});//6串57
			list = new int[] { 102, 103, 104, 105, 106 };
			break;
		case 42:
		case 702:
			// jcMap.put("702",new int[]{106});//7串7
			list = new int[] { 106 };
			break;
		case 43:
		case 703:
			// jcMap.put("703",new int[]{106,107});//7串8
			list = new int[] { 106, 107 };
			break;
		case 46:
		case 706:
			// jcMap.put("706",new int[]{102,103,104,105,106,107});//7串120
			list = new int[] { 102, 103, 104, 105, 106, 107 };
			break;
		case 47:
		case 802:
			// jcMap.put("802",new int[]{107});//"8串8"
			list = new int[] { 107 };
			break;
		case 48:
		case 803:
			// jcMap.put("803",new int[]{107,108});//"8串9"
			list = new int[] { 107, 108 };
			break;
		case 49:
		case 804:
			// jcMap.put("804",new int[]{106});//"8串28"
			list = new int[] { 106 };
			break;
		case 52:
		case 807:
			// jcMap.put("807",new int[]{102,103,104,105,106,107,108});//"8串247"
			list = new int[] { 102, 103, 104, 105, 106, 107, 108 };
			break;
		default:
			list = new int[] {101};//默认返回单关
			break;
		}
		// // $this->passTypeNameDecoder['3_3']=array('2_1');
		// jcMap.put("603", new int[]{102});//"3串3"
		// // $this->passTypeNameDecoder['3_4']=array('2_1','3_1');
		// jcMap.put("118", new int[]{102,103});//"3串4"
		//
		// // $this->passTypeNameDecoder['4_4']=array('3_1');
		// jcMap.put("604", new int[]{103});//"4串4"
		// // $this->passTypeNameDecoder['4_5']=array('3_1','4_1');
		// jcMap.put("120", new int[]{103,104});//"4串5"
		// // $this->passTypeNameDecoder['4_6']=array('2_1');
		// jcMap.put("605", new int[]{102});//"4串6"
		// // $this->passTypeNameDecoder['4_11']=array('2_1','3_1','4_1');
		// jcMap.put("121", new int[]{102,103,104});//"4串11"
		//
		// // $this->passTypeNameDecoder['5_5']=array('4_1');
		// jcMap.put("606",new int[]{104});//5串5
		// // $this->passTypeNameDecoder['5_6']=array('4_1','5_1');
		// jcMap.put("123",new int[]{104,105});//5串6
		// // $this->passTypeNameDecoder['5_10']=array('2_1');
		// jcMap.put("607",new int[]{102});//5串10
		// // $this->passTypeNameDecoder['5_16']=array('3_1','4_1','5_1');
		// jcMap.put("124",new int[]{103,104,105});//5串16
		// // $this->passTypeNameDecoder['5_20']=array('2_1','3_1');
		// jcMap.put("608",new int[]{102,103});//5串20
		// // $this->passTypeNameDecoder['5_26']=array('2_1','3_1','4_1','5_1');
		// jcMap.put("125",new int[]{102,103,104,105});//5串26
		//
		// // $this->passTypeNameDecoder['6_6']=array('5_1');
		// jcMap.put("609",new int[]{105});//6串6
		// // $this->passTypeNameDecoder['6_7']=array('5_1','6_1');
		// jcMap.put("127",new int[]{105,106});//6串7
		// // $this->passTypeNameDecoder['6_15']=array('2_1');
		// jcMap.put("610",new int[]{102});//6串15
		// // $this->passTypeNameDecoder['6_20']=array('3_1');
		// jcMap.put("611",new int[]{103});//6串20
		// // $this->passTypeNameDecoder['6_22']=array('4_1','5_1','6_1');
		// jcMap.put("128",new int[]{104,105,106});//6串22
		// // $this->passTypeNameDecoder['6_35']=array('2_1','3_1');
		// jcMap.put("612",new int[]{102,103});//6串35
		// // $this->passTypeNameDecoder['6_42']=array('3_1','4_1','5_1','6_1');
		// jcMap.put("129",new int[]{103,104,105,106});//6串42
		// // $this->passTypeNameDecoder['6_50']=array('2_1','3_1','4_1');
		// jcMap.put("613",new int[]{102,103,104});//6串50
		// // $this->passTypeNameDecoder['6_57']=array('2_1','3_1','4_1', "5_1",
		// "6_1");
		// jcMap.put("602",new int[]{102,103,104,105,106});//6串57

		// // $this->passTypeNameDecoder['7_7']=array('6_1');
		// jcMap.put("702",new int[]{106});//7串7
		// // $this->passTypeNameDecoder['7_8']=array('6_1','7_1');
		// jcMap.put("703",new int[]{106,107});//7串8
		// // $this->passTypeNameDecoder['7_21']=array('5_1');
		// jcMap.put("704",new int[]{105});//7串21
		// // $this->passTypeNameDecoder['7_35']=array('4_1');
		// jcMap.put("705",new int[]{104});//7串35
		// //
		// $this->passTypeNameDecoder['7_120']=array('2_1','3_1','4_1','5_1','6_1','7_1');
		// jcMap.put("706",new int[]{102,103,104,105,106,107});//7串120
		//
		// // $this->passTypeNameDecoder['8_8']=array('7_1');
		// jcMap.put("802",new int[]{107});//"8串8"
		// // $this->passTypeNameDecoder['8_9']=array('7_1','8_1');
		// jcMap.put("803",new int[]{107,108});//"8串9"
		// // $this->passTypeNameDecoder['8_28']=array('6_1');
		// jcMap.put("804",new int[]{106});//"8串28"
		// // $this->passTypeNameDecoder['8_56']=array('5_1');
		// jcMap.put("805",new int[]{105});//"8串56"
		// // $this->passTypeNameDecoder['8_70']=array('4_1');
		// jcMap.put("806",new int[]{104});//"8串70"
		// //
		// $this->passTypeNameDecoder['8_247']=array('2_1','3_1','4_1','5_1','6_1','7_1','8_1');
		// jcMap.put("807",new int[]{102,103,104,105,106,107,108});//"8串247"

		return list;
	}

	/**
	 * 获得串关对应的text描述
	 * 
	 * @param key
	 * @return
	 */
	public static String getCGString(int key) {
		String s = "";
		switch (key) {
		case 1:
			s = "单关";
			break;
		case 2:
			s = "2串1";
			break;
		case 3:
			s = "3串1";
			break;
		case 4:
			s = "4串1";
			break;
		case 5:
			s = "5串1";
			break;
		case 6:
			s = "6串1";
			break;
		case 7:
			s = "7串1";
			break;
		case 8:
			s = "8串1";
			break;
		default:
			break;
		}
		return s;
	}
/**
 * 以下为篮彩,其它则为足彩的串关描述
 *  02	1串1
	03	2串1
	04	3串1
	05	4串1
	06	5串1
	07	6串1
	08	7串1
	09	8串1
 * @param key
 * @return
 */
	public static String getcgText(int key) {
		String s = "";
		switch (key) {
		case 2:	
		case 101:
			s = "1串1";
			break;
		case 102:
		case 3:
			s = "2串1";
			break;
		case 103:
		case 4:
			s = "3串1";
			break;
		case 31:
		case 603:
			s = "3串3";
			break;
		case 17:
		case 118:
			s = "3串4";
			break;
		case 104:
		case 5:
			s = "4串1";
			break;
		case 32:
		case 604:
			s = "4串4";
			break;
		case 19:
		case 120:
			s = "4串5";
			break;
		case 33:
		case 605:
			s = "4串6";
			break;
		case 20:
		case 121:
			s = "4串11";
			break;
		case 105:
		case 6:
			s = "5串1";
			break;
		case 34:
		case 606:
			s = "5串5";
			break;
		case 22:
		case 123:
			s = "5串6";
			break;
		case 35:
		case 607:
			s = "5串10";
			break;
		case 23:
		case 124:
			s = "5串16";
			break;
		case 36:
		case 608:
			s = "5串20";
			break;
		case 24:
		case 125:
			s = "5串26";
			break;
		case 106:
		case 7:
			s = "6串1";
			break;
		case 37:
		case 609:
			s = "6串6";
			break;
		case 26:
		case 127:
			s = "6串7";
			break;
		case 38:
		case 610:
			s = "6串15";
			break;
		case 39:
		case 611:
			s = "6串20";
			break;
		case 27:
		case 128:
			s = "6串22";
			break;
		case 40:
		case 612:
			s = "6串35";
			break;
		case 28:
		case 129:
			s = "6串42";
			break;
		case 41:
		case 613:
			s = "6串50";
			break;
		case 30:
		case 602:
			s = "6串57";
			break;
		case 107:
		case 8:
			s = "7串1";
			break;
		case 42:
		case 702:
			s = "7串7";
			break;
		case 43:
		case 703:
			s = "7串8";
			break;
		case 44:
		case 704:
			s = "7串21";
			break;
		case 45:
		case 705:
			s = "7串35";
			break;
		case 46:
		case 706:
			s = "7串120";
			break;
		case 108:
		case 9:
			s = "8串1";
			break;
		case 47:
		case 802:
			s = "8串8";
			break;
		case 48:
		case 803:
			s = "8串9";
			break;
		case 49:
		case 804:
			s = "8串28";
			break;
		case 50:
		case 805:
			s = "8串56";
			break;
		case 51:
		case 806:
			s = "8串70";
			break;
		case 52:
		case 807:
			s = "8串247";
			break;
		}
		return s;
	}

	/**
	 * 快三 和值子玩法对应的单注奖金
	 * 
	 * @param tag
	 * @return
	 */
	private static int getpl120_hz_chidBonus(String key) {
		if (pl120_hz_chidBonus == null || pl120_hz_chidBonus.size() < 16) {
			pl120_hz_chidBonus = new HashMap<String, Integer>();
			pl120_hz_chidBonus.put("3", 240);
			pl120_hz_chidBonus.put("4", 80);
			pl120_hz_chidBonus.put("5", 40);
			pl120_hz_chidBonus.put("6", 25);
			pl120_hz_chidBonus.put("7", 16);
			pl120_hz_chidBonus.put("8", 12);
			pl120_hz_chidBonus.put("9", 10);
			pl120_hz_chidBonus.put("10", 9);
			pl120_hz_chidBonus.put("11", 9);
			pl120_hz_chidBonus.put("12", 10);
			pl120_hz_chidBonus.put("13", 12);
			pl120_hz_chidBonus.put("14", 16);
			pl120_hz_chidBonus.put("15", 25);
			pl120_hz_chidBonus.put("16", 40);
			pl120_hz_chidBonus.put("17", 80);
			pl120_hz_chidBonus.put("18", 240);

		}
		return pl120_hz_chidBonus.get(key);
	}

	/**
	 * 获得十一选五的子玩法对应的单注奖金
	 * 
	 * @param tag
	 * @return
	 */
	public static int getPl112Bonus(String tag) {
		if (pl112BonusMap == null || pl112BonusMap.size() != 20) {

			pl112BonusMap.put("1", 13);
			pl112BonusMap.put("2", 6);
			pl112BonusMap.put("3", 19);
			pl112BonusMap.put("4", 78);
			pl112BonusMap.put("5", 540);
			pl112BonusMap.put("6", 90);
			pl112BonusMap.put("7", 26);
			pl112BonusMap.put("8", 9);
			pl112BonusMap.put("9", 130);
			pl112BonusMap.put("10", 1170);
			pl112BonusMap.put("11", 65);
			pl112BonusMap.put("12", 195);
		}

		return pl112BonusMap.get(tag);

	}

	/**
	 * 获得江西时时彩 的子玩法对应的单注奖金
	 * 
	 * @param tag
	 * @return
	 */
	public static int getPl119Bonus(String tag) {
		if (pl119BonusMap == null || pl119BonusMap.size() != 13) {
			pl119BonusMap = new HashMap<String, Integer>();
			pl119BonusMap.put("01", 11);
			pl119BonusMap.put("11", 11);
			pl119BonusMap.put("02", 116);
			pl119BonusMap.put("12", 116);
			pl119BonusMap.put("52", 58);
			pl119BonusMap.put("62", 58);
			pl119BonusMap.put("03", 1160);
			pl119BonusMap.put("13", 1160);
			pl119BonusMap.put("23", 385);
			pl119BonusMap.put("33", 385);
			pl119BonusMap.put("43", 190);
			pl119BonusMap.put("53", 190);
			pl119BonusMap.put("04", 10000);
			pl119BonusMap.put("14", 10000);
			pl119BonusMap.put("34", 10000);
			pl119BonusMap.put("05", 116000);
			pl119BonusMap.put("15", 116000);
			pl119BonusMap.put("25", 116000);
			pl119BonusMap.put("35", 20460);
			pl119BonusMap.put("22", 4);
			pl119BonusMap.put("06", 11);
			pl119BonusMap.put("07", 116);
		}

		return pl119BonusMap.get(tag);

	}

	/**
	 * 获得选号对应的最大 奖金 和最小奖金
	 * 
	 * @param tag
	 *            true 返回选号的最小奖金对应的选号 false 返回选号的最大奖金对应的选号
	 * @param tag1
	 * @return
	 */
	private Integer getHZMin_or_Max_IntegerBonus(boolean tag, List<Integer> tag1) {
		if (tag1.size() <= 1) {
			return tag1.get(0);
		} else {
			Object[] objs = tag1.toArray();
			Arrays.sort(objs);
			int n = Integer.valueOf(String.valueOf(objs[0]));
			if (tag) {
				if (n <= 10) {// 3--10 奖金是降序 11--18 奖金是降序
					return Integer.valueOf(String
							.valueOf(objs[(objs.length - 1)]));
				} else {
					return n;
				}

			} else {
				if (n <= 10) {// 3--10 奖金是降序 11--18 奖金是降序
					return n;
				} else {
					return Integer.valueOf(String
							.valueOf(objs[(objs.length - 1)]));
				}

			}

		}
	}

	public List<Integer> getK3_hz_List_Bonus(List<Integer> seletorBallList) {
		List<Integer> bonusList = new ArrayList<Integer>();
		int minKey = getHZMin_or_Max_IntegerBonus(false, seletorBallList);// 最小奖金
		int MaxKey = getHZMin_or_Max_IntegerBonus(true, seletorBallList);// 累计最大奖金

		bonusList.add(getpl120_hz_chidBonus(minKey + ""));
		bonusList.add(getpl120_hz_chidBonus(MaxKey + ""));

		return bonusList;// 和值奖金必须根据选号决定最低奖金
	}

	/**
	 * * 普通彩种 追号方案时候查询的 对应单注奖金 返回的是 int 型的 bonus 对于特殊彩种 快三 则需要返回 int[];
	 * 
	 * * 120 快三（特殊）
	 * 
	 * 112 十一选五 119 江西 时时彩 109 胜负彩任9场（版本一不涉及上线）
	 * 
	 * @param lotteryId
	 *            彩票种类
	 * @param tag
	 * @return
	 */
	public int commonLottery_Singlepour_Bonus(String lotteryId, String tag) {
		int SinglepourBonus = 0;
		switch (Integer.parseInt(lotteryId)) {
		// 十一选五
		case 112:

			SinglepourBonus = getPl112Bonus(tag);
			break;
		// 江西时时彩
		case 119:

			SinglepourBonus = getPl119Bonus(tag);

			break;
		// 120 快三
		case 120:

			// SinglepourBonus=getPl120Bonus(tag);

			break;

		// 胜负彩票 版本一不涉及上线
		case 109:

			// SinglepourBonus=getPl109Bonus(tag);
			break;

		default:
			break;
		}

		return SinglepourBonus;

	}

	// ****************************************************十一选五*******************************************

	/**
	 * 
	 * 十一选五 ticket 对应的奖金 智能追号 所需要的奖金 存储在 RATEBean中
	 * 
	 * @param lotteryCount
	 * @param lotteryNums
	 * @param saleType
	 *            +childId
	 */
	public void znzh_11x5_tosaveBonus(int lotteryCount,
			List<List<Integer>> lotteryNums, String tag) {
		// System.out.println("----------lotteryCount ="+lotteryCount);
		// System.out.println("----------lotteryNums  ="+lotteryNums.toString());
		// System.out.println("----------tag ="+tag);
		if (lotteryCount < 1) {// 不足一注
			return;
		}
		if ("".equals(tag) || tag == null) {
			return;
		}
		if (0 == lotteryNums.size()) {
			return;
		}
		if ("0-1".equals(tag) || "1-1".equals(tag)) {// 前一
			saveBonusList = new ArrayList<Integer>();
			saveBonusList.add(13);
			RATEBean.getInstance().setBonusScopeList(saveBonusList);
		} else if ("0-2".equals(tag) || "1-2".equals(tag)) {// 任选二-单复式奖金
			Getlotterykind.getInstance().tosaveBonus_11x5_tag2(lotteryCount);
		} else if ("2-2".equals(tag)) {// 任选二-胆拖 奖金
			Getlotterykind.getInstance().tosaveBonus_11x5_tag22(lotteryCount);
		} else if ("0-3".equals(tag) || "1-3".equals(tag)) {// 任选三-单复式奖金
			Getlotterykind.getInstance().tosaveBonus_11x5_tag3(lotteryNums);
		} else if ("2-3".equals(tag)) {// 任选三-胆拖 奖金
			Getlotterykind.getInstance().tosaveBonus_11x5_tag23(lotteryCount);
		} else if ("0-4".equals(tag) || "1-4".equals(tag)) {// 任选四-单复式奖金
			Getlotterykind.getInstance().tosaveBonus_11x5_tag4(lotteryNums);
		} else if ("2-4".equals(tag)) {// 任选四-胆拖 奖金
			Getlotterykind.getInstance().tosaveBonus_11x5_tag24(lotteryCount);
		} else if ("0-5".equals(tag) || "1-5".equals(tag)) {
			Getlotterykind.getInstance().tosaveBonus_11x5_tag5(lotteryNums);
		} else if ("2-5".equals(tag)) {
			Getlotterykind.getInstance().tosaveBonus_11x5_tag25(lotteryCount);
		} else if ("0-6".equals(tag) || "1-6".equals(tag)) {
			Getlotterykind.getInstance().tosaveBonus_11x5_tag6(lotteryNums);
		} else if ("2-6".equals(tag)) {
			Getlotterykind.getInstance().tosaveBonus_11x5_tag26(lotteryCount);
		} else if ("0-7".equals(tag) || "1-7".equals(tag)) {
			Getlotterykind.getInstance().tosaveBonus_11x5_tag7(lotteryNums);
		} else if ("2-7".equals(tag)) {
			Getlotterykind.getInstance().tosaveBonus_11x5_tag27(lotteryCount);
		} else if ("0-8".equals(tag)) {
			Getlotterykind.getInstance().tosaveBonus_11x5_tag8(lotteryNums);
		} else if ("0-9".equals(tag) || "1-9".equals(tag) || "2-9".equals(tag)
				|| "3-9".equals(tag) || "4-9".equals(tag) || "5-9".equals(tag)) {
			Getlotterykind.getInstance().tosaveBonus_11x5_tag39(lotteryCount);

		} else if ("0-11".equals(tag) || "1-11".equals(tag)
				|| "2-11".equals(tag) || "4-11".equals(tag)
				|| "5-11".equals(tag)) {
			Getlotterykind.getInstance().tosaveBonus_11x5_tag11(lotteryCount);
		} else if ("3-11".equals(tag)) {
			Getlotterykind.getInstance().tosaveBonus_11x5_tag211(lotteryCount);
		} else if ("0-12".equals(tag) || "1-12".equals(tag)
				|| "4-12".equals(tag) || "5-12".equals(tag)) {
			Getlotterykind.getInstance().tosaveBonus_11x5_tag12(lotteryCount);
		} else if ("2-12".equals(tag)) {
			Getlotterykind.getInstance().tosaveBonus_11x5_tag212(lotteryCount);
		}
		// switch (Integer.parseInt(tag)) {
		// // 任选二
		// case 2:
		// Getlotterykind.getInstance().tosaveBonus_11x5_tag2(lotteryCount);
		//
		// break;
		// // 任选三
		// case 3:
		// Getlotterykind.getInstance().tosaveBonus_11x5_tag3(lotteryNums);
		//
		// break;
		// // 任选四
		// case 4:
		// Getlotterykind.getInstance().tosaveBonus_11x5_tag4(lotteryNums);
		// break;
		// // // 任选五
		// case 5:
		// Getlotterykind.getInstance().tosaveBonus_11x5_tag5(lotteryNums);
		// break;
		// // 任选六
		// case 6:
		// Getlotterykind.getInstance().tosaveBonus_11x5_tag6(lotteryNums);
		// break;
		// // 任选七
		// case 7:
		// Getlotterykind.getInstance().tosaveBonus_11x5_tag7(lotteryNums);
		// break;
		// // 任选八
		// case 8:
		// Getlotterykind.getInstance().tosaveBonus_11x5_tag8(lotteryNums);
		// break;
		// // 前一
		// case 1:
		// Getlotterykind.getInstance().tosaveBonus_11x5_tag1(lotteryNums);
		// break;
		// // 前二直选
		// case 39:
		// Getlotterykind.getInstance().tosaveBonus_11x5_tag39(lotteryCount);
		//
		//
		// break;
		// // 前二组选
		// case 11:
		// Getlotterykind.getInstance().tosaveBonus_11x5_tag11(lotteryCount);
		//
		// break;
		// // 前三直选(复式)
		// case 310:
		// Getlotterykind.getInstance().tosaveBonus_11x5_tag310(lotteryCount);
		//
		// break;
		// // 前三直选(单式)
		// case 10:
		// Getlotterykind.getInstance().tosaveBonus_11x5_tag310(lotteryCount);
		//
		// break;
		// // 前三组选
		// case 12:
		// Getlotterykind.getInstance().tosaveBonus_11x5_tag12(lotteryCount);
		//
		// break;
		// // -------------------------------胆码---------------------------------
		// // 任选二
		// case 22:
		// Getlotterykind.getInstance().tosaveBonus_11x5_tag22(lotteryCount);
		// break;
		// // 任选三
		// case 23:
		// Getlotterykind.getInstance().tosaveBonus_11x5_tag23(lotteryCount);
		// break;
		// // 任选四
		// case 24:
		// Getlotterykind.getInstance().tosaveBonus_11x5_tag24(lotteryCount);
		// break;
		// // 任选五
		// case 25:
		// Getlotterykind.getInstance().tosaveBonus_11x5_tag25(lotteryCount);
		// break;
		// // 任选六
		// case 26:
		// Getlotterykind.getInstance().tosaveBonus_11x5_tag26(lotteryCount);
		// break;
		// // 任选七
		// case 27:
		// Getlotterykind.getInstance().tosaveBonus_11x5_tag27(lotteryCount);
		// break;
		// // 前二组选 1170
		// case 211:
		// Getlotterykind.getInstance().tosaveBonus_11x5_tag211(lotteryCount);
		//
		// break;
		// // 前三组选
		// case 212:
		// Getlotterykind.getInstance().tosaveBonus_11x5_tag212(lotteryCount);
		//
		// break;
		// default:
		// break;
		// }

	}

	// 任选二
	public void tosaveBonus_11x5_tag2(int lotteryCount) {
		saveBonusList = new ArrayList<Integer>();

		switch (lotteryCount) {
		case 1:

			saveBonusList.add(6);
			break;
		case 3:

			saveBonusList.add(6);
			saveBonusList.add(18);
			break;
		case 6:

			saveBonusList.add(6);
			saveBonusList.add(36);
			break;
		case 10:

			saveBonusList.add(6);
			saveBonusList.add(60);
			break;
		case 36:
			saveBonusList.add(18);
			saveBonusList.add(60);

			break;
		case 45:
			saveBonusList.add(36);
			saveBonusList.add(60);

			break;
		case 55:
			saveBonusList.add(60);

			break;

		default:
			// lotteryCount= 15 21 28
			saveBonusList.add(6);// 默认存储
			saveBonusList.add(60);
			break;
		}

		RATEBean.getInstance().setBonusScopeList(saveBonusList);

	}

	// 任选三
	public void tosaveBonus_11x5_tag3(List<List<Integer>> lotteryNums2) {
		saveBonusList = new ArrayList<Integer>();
		switch (lotteryNums2.get(0).size()) {

		case 3:

			saveBonusList.add(19);
			break;
		case 4:

			saveBonusList.add(19);
			saveBonusList.add(76);

			break;
		case 10:

			saveBonusList.add(76);
			saveBonusList.add(190);
			break;
		case 11:
			saveBonusList.add(190);

			break;

		default:

			saveBonusList.add(19);
			saveBonusList.add(190);
			break;
		}

		RATEBean.getInstance().setBonusScopeList(saveBonusList);

	}

	// 任选四
	public void tosaveBonus_11x5_tag4(List<List<Integer>> lotteryNums2) {
		saveBonusList = new ArrayList<Integer>();
		switch (lotteryNums2.get(0).size()) {
		case 4:
			saveBonusList.add(78);
			break;
		case 11:

			saveBonusList.add(390);
			break;

		default:
			saveBonusList.add(78);
			saveBonusList.add(390);
			break;
		}

		RATEBean.getInstance().setBonusScopeList(saveBonusList);
	}

	// 任选五
	public void tosaveBonus_11x5_tag5(List<List<Integer>> lotteryNums2) {
		saveBonusList = new ArrayList<Integer>();
		saveBonusList.add(540);
		RATEBean.getInstance().setBonusScopeList(saveBonusList);

	}

	// 任选六
	public void tosaveBonus_11x5_tag6(List<List<Integer>> lotteryNums2) {
		saveBonusList = new ArrayList<Integer>();
		switch (lotteryNums2.get(0).size()) {
		case 6:
			saveBonusList.add(90);

			break;
		case 7:
			saveBonusList.add(180);

			break;
		case 8:
			saveBonusList.add(270);

			break;
		case 9:
			saveBonusList.add(360);

			break;
		case 10:
			saveBonusList.add(450);

			break;
		case 11:
			saveBonusList.add(540);

			break;

		default:
			saveBonusList.add(0);
			break;
		}
		RATEBean.getInstance().setBonusScopeList(saveBonusList);
	}

	// 任选七
	public void tosaveBonus_11x5_tag7(List<List<Integer>> lotteryNums2) {
		saveBonusList = new ArrayList<Integer>();
		switch (lotteryNums2.get(0).size()) {
		case 7:
			saveBonusList.add(26);
			break;
		case 8:
			saveBonusList.add(78);

			break;
		case 9:
			saveBonusList.add(156);

			break;
		case 10:
			saveBonusList.add(260);

			break;
		case 11:
			saveBonusList.add(390);

			break;

		default:
			saveBonusList.add(0);
			break;
		}

		RATEBean.getInstance().setBonusScopeList(saveBonusList);

	}

	// 任选八
	public void tosaveBonus_11x5_tag8(List<List<Integer>> lotteryNums2) {
		saveBonusList = new ArrayList<Integer>();
		switch (lotteryNums2.get(0).size()) {
		case 8:
			saveBonusList.add(9);
			break;
		case 9:
			saveBonusList.add(36);

			break;
		case 10:
			saveBonusList.add(90);

			break;
		case 11:
			saveBonusList.add(180);
			break;

		default:
			saveBonusList.add(0);
			break;
		}

		RATEBean.getInstance().setBonusScopeList(saveBonusList);
	}

	// 前一
	public void tosaveBonus_11x5_tag1(List<List<Integer>> lotteryNums2) {
		saveBonusList = new ArrayList<Integer>();
		if (lotteryNums2.get(0).size() == 0) {
			saveBonusList.add(0);
		}
		saveBonusList.add(13);
		RATEBean.getInstance().setBonusScopeList(saveBonusList);

	}

	public void tosaveBonus_11x5_tag39(int lotteryCount) {
		if (lotteryCount >= 1) {
			saveBonusList = new ArrayList<Integer>();
			saveBonusList.add(13);
			RATEBean.getInstance().setBonusScopeList(saveBonusList);
		}
	}

	public void tosaveBonus_11x5_tag11(int lotteryCount) {
		if (lotteryCount >= 1) {
			saveBonusList = new ArrayList<Integer>();
			saveBonusList.add(65);
			RATEBean.getInstance().setBonusScopeList(saveBonusList);
		}

	}

	// 前三直选
	public void tosaveBonus_11x5_tag310(int lotteryCount) {
		if (lotteryCount >= 1) {
			saveBonusList = new ArrayList<Integer>();
			saveBonusList.add(1170);
			RATEBean.getInstance().setBonusScopeList(saveBonusList);
		}
	}

	// 前三组选
	public void tosaveBonus_11x5_tag12(int lotteryCount) {
		if (lotteryCount >= 1) {
			saveBonusList = new ArrayList<Integer>();
			saveBonusList.add(195);
			RATEBean.getInstance().setBonusScopeList(saveBonusList);
		}

	}

	// 胆拖 任选二
	public void tosaveBonus_11x5_tag22(int lotteryCount) {
		saveBonusList = new ArrayList<Integer>();
		switch (lotteryCount) {
		case 1:
			saveBonusList.add(6);
			break;
		case 2:
			saveBonusList.add(6);
			saveBonusList.add(12);

			break;
		case 3:
			saveBonusList.add(6);
			saveBonusList.add(18);

			break;
		case 8:
			saveBonusList.add(12);
			saveBonusList.add(24);
			break;
		case 9:
			saveBonusList.add(18);
			saveBonusList.add(24);
			break;
		case 10:
			saveBonusList.add(24);
			break;

		default:
			// 4 5 6 7
			saveBonusList.add(6);
			saveBonusList.add(24);
			break;
		}
		RATEBean.getInstance().setBonusScopeList(saveBonusList);
	}

	// 任选三
	public void tosaveBonus_11x5_tag23(int lotteryCount) {
		saveBonusList = new ArrayList<Integer>();
		switch (lotteryCount) {
		case 1:
			saveBonusList.add(19);
			break;
		case 2:
			saveBonusList.add(19);
			saveBonusList.add(38);

			break;

		case 8:
			saveBonusList.add(38);
			saveBonusList.add(57);

			break;
		case 9:
			saveBonusList.add(57);

			break;

		default:
			// 3 4 5 6 7
			saveBonusList.add(19);
			saveBonusList.add(57);

			break;
		}

		RATEBean.getInstance().setBonusScopeList(saveBonusList);

	}

	// 胆拖 任选四
	public void tosaveBonus_11x5_tag24(int lotteryCount) {
		saveBonusList = new ArrayList<Integer>();
		switch (lotteryCount) {
		case 1:
			saveBonusList.add(78);
			break;
		case 8:
			saveBonusList.add(156);
			break;

		default:
			// 2 3 4 5 6 7
			saveBonusList.add(78);
			saveBonusList.add(156);
			break;
		}

		RATEBean.getInstance().setBonusScopeList(saveBonusList);
	}

	// 胆拖 任选五
	public void tosaveBonus_11x5_tag25(int lotteryCount) {
		if (lotteryCount >= 1) {
			saveBonusList = new ArrayList<Integer>();
			saveBonusList.add(540);
			RATEBean.getInstance().setBonusScopeList(saveBonusList);

		}

	}

	// 任选六
	// 胆拖 任选六
	public void tosaveBonus_11x5_tag26(int lotteryCount) {
		saveBonusList = new ArrayList<Integer>();
		switch (lotteryCount) {
		case 1:
			saveBonusList.add(90);
			break;
		case 2:
			saveBonusList.add(90);
			saveBonusList.add(180);
			break;
		case 3:
			saveBonusList.add(90);
			saveBonusList.add(270);

			break;
		case 4:
			saveBonusList.add(90);
			saveBonusList.add(360);

			break;
		case 5:
			saveBonusList.add(90);
			saveBonusList.add(450);
			break;
		case 6:
			saveBonusList.add(90);
			saveBonusList.add(540);
			break;

		default:
			saveBonusList.add(0);
			break;
		}
		RATEBean.getInstance().setBonusScopeList(saveBonusList);

	}

	// 任选七
	// 胆拖 任选七
	public void tosaveBonus_11x5_tag27(int lotteryCount) {
		saveBonusList = new ArrayList<Integer>();
		switch (lotteryCount) {
		case 1:
			saveBonusList.add(26);
			break;
		case 2:
			saveBonusList.add(26);
			saveBonusList.add(52);
			break;
		case 3:
			saveBonusList.add(26);
			saveBonusList.add(78);
			break;
		case 4:
			saveBonusList.add(26);
			saveBonusList.add(104);
			break;
		case 5:
			saveBonusList.add(26);
			saveBonusList.add(130);
			break;

		default:
			saveBonusList.add(0);
			break;
		}
		RATEBean.getInstance().setBonusScopeList(saveBonusList);

	}

	// 前二组选
	public void tosaveBonus_11x5_tag211(int lotteryCount) {
		if (lotteryCount >= 1) {
			saveBonusList = new ArrayList<Integer>();
			saveBonusList.add(65);
			RATEBean.getInstance().setBonusScopeList(saveBonusList);
		}

	}

	// 前二组选
	public void tosaveBonus_11x5_tag212(int lotteryCount) {
		if (lotteryCount >= 1) {
			saveBonusList = new ArrayList<Integer>();
			saveBonusList.add(195);
			RATEBean.getInstance().setBonusScopeList(saveBonusList);
		}

	}

	// ---------------------江西 时时彩 只能追号 -----------------
	/**
	 * 时时彩 ticket 对应的奖金
	 * 
	 * 
	 * @param lotteryCount
	 *            注数
	 * @param lotteryNums
	 *            选号
	 * @param tag
	 *            saletype+chidId
	 */
	public void znzh_pl119_tosaveBonus(int lotteryCount,
			List<List<Integer>> lotteryNums, String tag) {
		if ("".equals(tag) || tag == null) {
			return;
		}

		if (lotteryCount < 1) {
			return;
		}

		if ("0-1".equals(tag) || "1-1".equals(tag)) {// 一星单式\复式
			saveBonusList = new ArrayList<Integer>();
			saveBonusList.add(11);
			RATEBean.getInstance().setBonusScopeList(saveBonusList);
		} else if ("0-2".equals(tag) || "1-2".equals(tag)) {// 二星 单式 \复式
			saveBonusList = new ArrayList<Integer>();
			saveBonusList.add(116);
			RATEBean.getInstance().setBonusScopeList(saveBonusList);
		} else if ("2-2".equals(tag)) {// 大小单双
			saveBonusList = new ArrayList<Integer>();
			saveBonusList.add(4);
			RATEBean.getInstance().setBonusScopeList(saveBonusList);
		} else if ("4-2".equals(tag) || "5-2".equals(tag) || "6-2".equals(tag)
				|| "7-2".equals(tag) || "9-2".equals(tag) || "10-2".equals(tag)) {// 二星和值，组选单式，组选复式
			saveBonusList = new ArrayList<Integer>();
			saveBonusList.add(58);
			RATEBean.getInstance().setBonusScopeList(saveBonusList);
		} else if ("0-3".equals(tag) || "1-3".equals(tag)) {// 三星直选
			saveBonusList = new ArrayList<Integer>();
			saveBonusList.add(1160);
			RATEBean.getInstance().setBonusScopeList(saveBonusList);

		} else if ("2-3".equals(tag)) {// 三星-组三单式
			saveBonusList = new ArrayList<Integer>();
			saveBonusList.add(1160);
			RATEBean.getInstance().setBonusScopeList(saveBonusList);
		} else if ("3-3".equals(tag)) {// 三星-组三复式
			saveBonusList = new ArrayList<Integer>();
			saveBonusList.add(385);
			RATEBean.getInstance().setBonusScopeList(saveBonusList);
		} else if ("4-3".equals(tag) || "5-3".equals(tag) || "6-3".equals(tag)) {// 三星-组六单式
																					// 复式
			saveBonusList = new ArrayList<Integer>();
			saveBonusList.add(190);
			RATEBean.getInstance().setBonusScopeList(saveBonusList);
		} else if ("0-4".equals(tag) || "1-4".equals(tag) || "3-4".equals(tag)) {// 四星-单式
																					// 复式
																					// 复选
			tosaveBonus_ssc_tag4(lotteryNums);
		} else if ("0-5".equals(tag) || "1-5".equals(tag) || "2-5".equals(tag)) {// 五星-单式
																					// 复式
																					// 复选
			saveBonusList = new ArrayList<Integer>();
			saveBonusList.add(116000);
			RATEBean.getInstance().setBonusScopeList(saveBonusList);

		} else if ("3-5".equals(tag)) {// 五星-通选//--TODO 只返回单式奖金
			saveBonusList = new ArrayList<Integer>();
			saveBonusList.add(20460);
			RATEBean.getInstance().setBonusScopeList(saveBonusList);
		} else if ("0-6".equals(tag)) {// 任一-单式
			saveBonusList = new ArrayList<Integer>();
			saveBonusList.add(11);
			RATEBean.getInstance().setBonusScopeList(saveBonusList);
		} else if ("0-7".equals(tag)) {// 任二-单式
			saveBonusList = new ArrayList<Integer>();
			saveBonusList.add(116);
			RATEBean.getInstance().setBonusScopeList(saveBonusList);
		}

		// switch (Integer.parseInt(tag)) {
		// // 一星直选
		// case 1 :
		// saveBonusList=new ArrayList<Integer>();
		// saveBonusList.add(11);
		// RATEBean.getInstance().setBonusScopeList(saveBonusList);
		// break;
		// // 二星直选
		// case 2:
		// saveBonusList=new ArrayList<Integer>();
		// saveBonusList.add(116);
		// RATEBean.getInstance().setBonusScopeList(saveBonusList);
		// break;
		// // // 二星组选
		// case 52:
		// saveBonusList=new ArrayList<Integer>();
		// saveBonusList.add(58);
		// RATEBean.getInstance().setBonusScopeList(saveBonusList);
		// break;
		// // 三星直选
		// case 3:
		// saveBonusList=new ArrayList<Integer>();
		// saveBonusList.add(1160);
		// RATEBean.getInstance().setBonusScopeList(saveBonusList);
		// break;
		// // 三星组三单式
		// case 23:
		// saveBonusList=new ArrayList<Integer>();
		// saveBonusList.add(1160);
		// RATEBean.getInstance().setBonusScopeList(saveBonusList);
		// break;
		// // 三星组三复式
		// case 33:
		// saveBonusList=new ArrayList<Integer>();
		// saveBonusList.add(385);
		// RATEBean.getInstance().setBonusScopeList(saveBonusList);
		// break;
		//
		// // 三星组六
		// case 43:
		// saveBonusList=new ArrayList<Integer>();
		// saveBonusList.add(190);
		// RATEBean.getInstance().setBonusScopeList(saveBonusList);
		//
		// break;
		// // // 四星直选
		// case 4:
		// tosaveBonus_ssc_tag4(lotteryNums);
		//
		// break;
		// // 五星直选
		// case 5:
		// saveBonusList=new ArrayList<Integer>();
		// saveBonusList.add(116000);
		// RATEBean.getInstance().setBonusScopeList(saveBonusList);
		// break;
		// // 五星通选
		// case 35:
		// // ------TODO 只返回单注最低奖金
		// saveBonusList=new ArrayList<Integer>();
		// saveBonusList.add(20460);
		// RATEBean.getInstance().setBonusScopeList(saveBonusList);
		// break;
		// // 大小单双
		// case 22:
		// saveBonusList=new ArrayList<Integer>();
		// saveBonusList.add(4);
		// RATEBean.getInstance().setBonusScopeList(saveBonusList);
		//
		// break;
		// // 任选一
		// case 6:
		// saveBonusList=new ArrayList<Integer>();
		// saveBonusList.add(11);
		// RATEBean.getInstance().setBonusScopeList(saveBonusList);
		//
		// break;
		// // 任选二
		// case 7:
		// saveBonusList=new ArrayList<Integer>();
		// saveBonusList.add(116);
		// RATEBean.getInstance().setBonusScopeList(saveBonusList);
		// break;
		// default:
		//
		// break;
		// }
	}

	private void tosaveBonus_ssc_tag4(List<List<Integer>> lotteryNums) {
		int tag1 = lotteryNums.get(0).size();
		int tag2 = lotteryNums.get(3).size();
		// 千位上为1各位上为1.2.3.4....10
		if (1 <= tag1 && 1 == tag2 || 1 <= tag2 && 1 == tag1) {

			switch (tag1) {
			case 1:
				saveBonusList = new ArrayList<Integer>();
				saveBonusList.add(88);
				saveBonusList.add(10000);
				RATEBean.getInstance().setBonusScopeList(saveBonusList);
				break;
			case 2:
				// 千位 或者 个位 size2 =2

				saveBonusList = new ArrayList<Integer>();
				saveBonusList.add(88);
				saveBonusList.add(10088);
				RATEBean.getInstance().setBonusScopeList(saveBonusList);
				break;
			case 3:
				// 千位 或者 个位 size2 =3

				saveBonusList = new ArrayList<Integer>();
				saveBonusList.add(88);
				saveBonusList.add(10176);
				RATEBean.getInstance().setBonusScopeList(saveBonusList);
				break;
			case 4:
				// 千位 或者 个位 size2 =4
				saveBonusList = new ArrayList<Integer>();
				saveBonusList.add(88);
				saveBonusList.add(10264);
				RATEBean.getInstance().setBonusScopeList(saveBonusList);
				break;
			case 5:
				// 千位 或者 个位 size2
				saveBonusList = new ArrayList<Integer>();
				saveBonusList.add(88);
				saveBonusList.add(10352);
				RATEBean.getInstance().setBonusScopeList(saveBonusList);
				break;
			case 6:
				// 千位 或者 个位 size2

				saveBonusList = new ArrayList<Integer>();
				saveBonusList.add(88);
				saveBonusList.add(10440);
				RATEBean.getInstance().setBonusScopeList(saveBonusList);
				break;
			case 7:
				// 千位 或者 个位 size2

				saveBonusList = new ArrayList<Integer>();
				saveBonusList.add(88);
				saveBonusList.add(10528);
				RATEBean.getInstance().setBonusScopeList(saveBonusList);
				break;
			case 8:
				// 千位 或者 个位 size2

				saveBonusList = new ArrayList<Integer>();
				saveBonusList.add(88);
				saveBonusList.add(10616);
				RATEBean.getInstance().setBonusScopeList(saveBonusList);
				break;
			case 9:
				// 千位 或者 个位 size2

				saveBonusList = new ArrayList<Integer>();
				saveBonusList.add(88);
				saveBonusList.add(10704);
				RATEBean.getInstance().setBonusScopeList(saveBonusList);
				break;
			case 10:
				// 千位 或者 个位 size2
				saveBonusList = new ArrayList<Integer>();
				saveBonusList.add(88);
				saveBonusList.add(10729);
				RATEBean.getInstance().setBonusScopeList(saveBonusList);
				break;

			default:
				saveBonusList = new ArrayList<Integer>();
				saveBonusList.add(88);
				saveBonusList.add(10000);
				RATEBean.getInstance().setBonusScopeList(saveBonusList);
				break;
			}
		} else {
			// tag2 +tag1
			switch (tag1 + tag2) {
			case 12:
				saveBonusList = new ArrayList<Integer>();
				saveBonusList.add(176);
				saveBonusList.add(10880);
				RATEBean.getInstance().setBonusScopeList(saveBonusList);
				break;
			case 13:
				saveBonusList = new ArrayList<Integer>();
				saveBonusList.add(264);
				saveBonusList.add(10968);
				RATEBean.getInstance().setBonusScopeList(saveBonusList);
				break;
			case 14:
				saveBonusList = new ArrayList<Integer>();
				saveBonusList.add(352);
				saveBonusList.add(11056);
				RATEBean.getInstance().setBonusScopeList(saveBonusList);
				break;
			case 15:
				saveBonusList = new ArrayList<Integer>();
				saveBonusList.add(440);
				saveBonusList.add(11144);
				RATEBean.getInstance().setBonusScopeList(saveBonusList);
				break;
			case 16:
				saveBonusList = new ArrayList<Integer>();
				saveBonusList.add(528);
				saveBonusList.add(11232);
				RATEBean.getInstance().setBonusScopeList(saveBonusList);
				break;
			case 17:
				saveBonusList = new ArrayList<Integer>();
				saveBonusList.add(616);
				saveBonusList.add(11320);
				RATEBean.getInstance().setBonusScopeList(saveBonusList);
				break;
			case 18:
				saveBonusList = new ArrayList<Integer>();
				saveBonusList.add(704);
				saveBonusList.add(11408);
				RATEBean.getInstance().setBonusScopeList(saveBonusList);
				break;
			case 19:
				saveBonusList = new ArrayList<Integer>();
				saveBonusList.add(792);
				saveBonusList.add(11496);
				RATEBean.getInstance().setBonusScopeList(saveBonusList);
				break;
			case 20:
				saveBonusList = new ArrayList<Integer>();
				saveBonusList.add(880);
				saveBonusList.add(11584);
				RATEBean.getInstance().setBonusScopeList(saveBonusList);
				break;

			default:
				saveBonusList = new ArrayList<Integer>();
				saveBonusList.add(88);
				saveBonusList.add(10000);
				RATEBean.getInstance().setBonusScopeList(saveBonusList);
				break;
			}

		}

	}

	/**
	 * key=(lotteryid+saletype) 或者 (lotteryid+saletype+childtype)
	 * 
	 * @param key
	 * @return
	 */
	public static String get(String key) {

		if (mMap == null) {
			mMap = new HashMap<String, String>();
			// 排列三 key=lotteryid+saletype+"0"//childtype 默认为"0"
			mMap.put("100-0-0", "排列三-直选-普通");
			mMap.put("100-1-0", "排列三-直选-复式");
			mMap.put("100-2-0", "排列三-直选-和值");
			mMap.put("100-11-0", "排列三-直选-跨度");
			mMap.put("100-7-0", "排列三-直选-组合");
			mMap.put("100-8-0", "排列三-直选-组合胆拖");
			mMap.put("100-3-0", "排列三-组选三-单式");
			mMap.put("100-6-0", "排列三-组选三-和值");
			mMap.put("100-5-0", "排列三-组选三-复式");
			mMap.put("100-9-0", "排列三-组选三-胆拖");
			mMap.put("100-12-0", "排列三-组选三-跨度");
			mMap.put("100-4-0", "排列三-组选六");
			mMap.put("100-10-0", "排列三-组选六");
			mMap.put("100-13-0", "排列三-组选六");
			// 福彩3D key=lotteryid+saletype+"0"//childtype 默认为"0"
			mMap.put("116-0-0", "福彩3D-直选-普通");
			mMap.put("116-1-0", "福彩3D-直选-复式");
			mMap.put("116-2-0", "福彩3D-直选-和值");
			mMap.put("116-11-0", "福彩3D-直选-跨度");
			mMap.put("116-7-0", "福彩3D-直选-组合");
			mMap.put("116-8-0", "福彩3D-直选-组合胆拖");
			mMap.put("116-3-0", "福彩3D-组选三-单式");
			mMap.put("116-6-0", "福彩3D-组选三-和值");
			mMap.put("116-5-0", "福彩3D-组选三-复式");
			mMap.put("116-9-0", "福彩3D-组选三-胆拖");
			mMap.put("116-12-0", "福彩3D-组选三-跨度");
			mMap.put("116-4-0", "福彩3D-组选六-复式");
			mMap.put("116-10-0", "福彩3D-组选六-胆拖");
			mMap.put("116-13-0", "福彩3D-组选六-跨度");
			// 排列五 key=lotteryid+saletype+"0"//childtype 默认为"0"
			mMap.put("102-0-0", "排列五-单式");
			mMap.put("102-1-0", "排列五-复式");
			// 七星彩 key=lotteryid+saletype+"0"//childtype 默认为"0"
			mMap.put("103-0-0", "七星彩-");
			mMap.put("103-1-0", "七星彩-");
			// 七乐彩 key=lotteryid+saletype="0"//childtype 默认为"0"
			mMap.put("117-0-0", "七乐彩-单式");
			mMap.put("117-1-0", "七乐彩-复式");
			mMap.put("117-2-0", "七乐彩-胆拖");
			// 七乐彩机选
			mMap.put("117-0-", "七乐彩-单式");
			// 双色球 key=lotteryid+saletype+"0"//childtype 默认为"0"
			mMap.put("118-0-0", "双色球-");
			mMap.put("118-1-0", "双色球-");
			mMap.put("118-2-0", "双色球-胆拖");
			// 大乐透 key=lotteryid+saletype+"0"//childtype 默认为"0"
			mMap.put("106-0-0", "大乐透-");
			mMap.put("106-1-0", "大乐透-");
			mMap.put("106-2-0", "大乐透-胆拖");
			// 大乐透追加
			mMap.put("106-0-1", "大乐透-");
			mMap.put("106-1-1", "大乐透-");
			mMap.put("106-2-1", "大乐透-胆拖");
			// 江西时时彩 kay=(lotteryid+saletype+childtype)
			mMap.put("119-0-1", "时时彩-一星-单式");
			mMap.put("119-1-1", "时时彩-一星-复式");
			mMap.put("119-0-2", "时时彩-二星-单式");
			mMap.put("119-1-2", "时时彩-二星-复式");
			mMap.put("119-2-2", "时时彩-大小单双");
			mMap.put("119-4-2", "时时彩-二星-和值");
			mMap.put("119-5-2", "时时彩-二星-组选单式");
			mMap.put("119-6-2", "时时彩-二星-组选复式");
			mMap.put("119-7-2", "时时彩-二星-组选和值");
			mMap.put("119-9-2", "时时彩-二星-组选包胆");
			mMap.put("119-10-2", "时时彩-二星-复选");
			mMap.put("119-0-3", "时时彩-三星-单式");
			mMap.put("119-1-3", "时时彩-三星-复式");
			mMap.put("119-2-3", "时时彩-三星-组三单式");
			mMap.put("119-3-3", "时时彩-三星-组三复式");
			mMap.put("119-4-3", "时时彩-三星-组六单式");
			mMap.put("119-5-3", "时时彩-三星-组六复式");
			mMap.put("119-6-3", "时时彩-三星-复选");
			mMap.put("119-0-4", "时时彩-四星-单式");
			mMap.put("119-1-4", "时时彩-四星-复式");
			mMap.put("119-3-4", "时时彩-四星-复选");
			mMap.put("119-0-5", "时时彩-五星-单式");
			mMap.put("119-1-5", "时时彩-五星-复式");
			mMap.put("119-2-5", "时时彩-五星-复选");
			mMap.put("119-3-5", "时时彩-五星-通选");
			mMap.put("119-0-6", "时时彩-任一-单式");
			mMap.put("119-0-7", "时时彩-任二-单式");
			// 快三投注 kay=(lotteryid+saletype+childtype)
			mMap.put("120-0-1", "福彩快三-和值");
			mMap.put("120-0-2", "福彩快三-三同号");
			mMap.put("120-0-3", "福彩快三-三同号通选");
			mMap.put("120-0-4", "福彩快三-三连号通选");
			mMap.put("120-0-5", "福彩快三-三不同号");
			mMap.put("120-2-5", "福彩快三-三不同号");
			mMap.put("120-0-6", "福彩快三-二同号");
			mMap.put("120-0-7", "福彩快三-二同号");
			mMap.put("120-0-8", "福彩快三-二不同号");
			mMap.put("120-2-8", "福彩快三-二不同号");
			// 江西快三投注 kay=(lotteryid+saletype+childtype)
			mMap.put("123-0-1", "江苏快三-和值");
			mMap.put("123-0-2", "江苏快三-三同号");
			mMap.put("123-0-3", "江苏快三-三同号通选");
			mMap.put("123-0-4", "江苏快三-三连号通选");
			mMap.put("123-0-5", "江苏快三-三不同号");
			mMap.put("123-2-5", "江苏快三-三不同号");
			mMap.put("123-0-6", "江苏快三-二同号");
			mMap.put("123-0-7", "江苏快三-二同号");
			mMap.put("123-0-8", "江苏快三-二不同号");
			mMap.put("123-2-8", "江苏快三-二不同号");
			// 模拟快三投注 kay=(lotteryid+saletype+childtype)
			mMap.put("122-0-1", "模拟快三-和值");
			mMap.put("122-0-2", "模拟快三-三同号");
			mMap.put("122-0-3", "模拟快三-三同号通选");
			mMap.put("122-0-4", "模拟快三-三连号通选");
			mMap.put("122-0-5", "模拟快三-三不同号");
			mMap.put("122-2-5", "模拟快三-三不同号");
			mMap.put("122-0-6", "模拟快三-二同号");
			mMap.put("122-0-7", "模拟快三-二同号");
			mMap.put("122-0-8", "模拟快三-二不同号");
			mMap.put("122-2-8", "模拟快三-二不同号");

			// 11选5 kay=(lotteryid+saletype+childtype)
			mMap.put("112-0-1", "前一-");
			mMap.put("112-1-1", "前一-");

			mMap.put("112-0-2", "任选二-");
			mMap.put("112-1-2", "任选二-");
			mMap.put("112-2-2", "任选二-");

			mMap.put("112-0-3", "任选三-");
			mMap.put("112-1-3", "任选三-");
			mMap.put("112-2-3", "任选三-");

			mMap.put("112-0-4", "任选四-");
			mMap.put("112-1-4", "任选四-");
			mMap.put("112-2-4", "任选四-");

			mMap.put("112-0-5", "任选五-");
			mMap.put("112-1-5", "任选五-");
			mMap.put("112-2-5", "任选五-");

			mMap.put("112-0-6", "任选六-");
			mMap.put("112-1-6", "任选六-");
			mMap.put("112-2-6", "任选六-");

			mMap.put("112-0-7", "任选七-");
			mMap.put("112-1-7", "任选七-");
			mMap.put("112-2-7", "任选七-");

			mMap.put("112-0-8", "任选八-");
			mMap.put("112-0-9", "前二直选-");
			mMap.put("112-1-9", "前二直选-");
			mMap.put("112-3-9", "前二直选-");
			mMap.put("112-2-9", "前二直选-");
			mMap.put("112-4-9", "前二直选-");
			mMap.put("112-5-9", "前二直选-");

			mMap.put("112-0-10", "前三直选-");
			mMap.put("112-1-10", "前三直选-");
			mMap.put("112-3-10", "前三直选-");
			mMap.put("112-4-10", "前三直选-");
			mMap.put("112-5-10", "前三直选-");
			mMap.put("112-2-10", "前三直选-");

			mMap.put("112-0-11", "前二组选-");
			mMap.put("112-1-11", "前二组选-");
			mMap.put("112-3-11", "前二组选-");
			mMap.put("112-4-11", "前二组选-");
			mMap.put("112-5-11", "前二组选-");
			mMap.put("112-2-11", "前二组选-");

			mMap.put("112-0-12", "前三组选-");
			mMap.put("112-1-12", "前三组选-");
			mMap.put("112-2-12", "前三组选-");
			mMap.put("112-4-12", "前三组选-");
			mMap.put("112-5-12", "前三组选-");
			mMap.put("130-0-1", "模拟前一-");
			mMap.put("130-1-1", "模拟前一-");

			mMap.put("130-0-2", "模拟任选二-");
			mMap.put("130-1-2", "模拟任选二-");
			mMap.put("130-2-2", "模拟任选二-");

			mMap.put("130-0-3", "模拟任选三-");
			mMap.put("130-1-3", "模拟任选三-");
			mMap.put("130-2-3", "模拟任选三-");

			mMap.put("130-0-4", "模拟任选四-");
			mMap.put("130-1-4", "模拟任选四-");
			mMap.put("130-2-4", "模拟任选四-");

			mMap.put("130-0-5", "模拟任选五-");
			mMap.put("130-1-5", "模拟任选五-");
			mMap.put("130-2-5", "模拟任选五-");

			mMap.put("130-0-6", "模拟任选六-");
			mMap.put("130-1-6", "模拟任选六-");
			mMap.put("130-2-6", "模拟任选六-");

			mMap.put("130-0-7", "模拟任选七-");
			mMap.put("130-1-7", "模拟任选七-");
			mMap.put("130-2-7", "模拟任选七-");

			mMap.put("130-0-8", "模拟任选八-");
			mMap.put("130-0-9", "模拟前二直选-");
			mMap.put("130-1-9", "模拟前二直选-");
			mMap.put("130-3-9", "模拟前二直选-");
			mMap.put("130-2-9", "模拟前二直选-");
			mMap.put("130-4-9", "模拟前二直选-");
			mMap.put("130-5-9", "模拟前二直选-");

			mMap.put("130-0-10", "模拟前三直选-");
			mMap.put("130-1-10", "模拟前三直选-");
			mMap.put("130-3-10", "模拟前三直选-");
			mMap.put("130-4-10", "模拟前三直选-");
			mMap.put("130-5-10", "模拟前三直选-");
			mMap.put("130-2-10", "模拟前三直选-");

			mMap.put("130-0-11", "模拟前二组选-");
			mMap.put("130-1-11", "模拟前二组选-");
			mMap.put("130-3-11", "模拟前二组选-");
			mMap.put("130-4-11", "模拟前二组选-");
			mMap.put("130-5-11", "模拟前二组选-");
			mMap.put("130-2-11", "模拟前二组选-");

			mMap.put("130-0-12", "模拟前三组选-");
			mMap.put("130-1-12", "模拟前三组选-");
			mMap.put("130-2-12", "模拟前三组选-");
			mMap.put("130-4-12", "模拟前三组选-");
			mMap.put("130-5-12", "模拟前三组选-");
			// 江西 11选5 kay=(lotteryid+saletype+childtype)
			mMap.put("113-0-1", "前一-");
			mMap.put("113-1-1", "前一-");

			mMap.put("113-0-2", "任选二-");
			mMap.put("113-1-2", "任选二-");
			mMap.put("113-2-2", "任选二-");

			mMap.put("113-0-3", "任选三-");
			mMap.put("113-1-3", "任选三-");
			mMap.put("113-2-3", "任选三-");

			mMap.put("113-0-4", "任选四-");
			mMap.put("113-1-4", "任选四-");
			mMap.put("113-2-4", "任选四-");

			mMap.put("113-0-5", "任选五-");
			mMap.put("113-1-5", "任选五-");
			mMap.put("113-2-5", "任选五-");

			mMap.put("113-0-6", "任选六-");
			mMap.put("113-1-6", "任选六-");
			mMap.put("113-2-6", "任选六-");

			mMap.put("113-0-7", "任选七-");
			mMap.put("113-1-7", "任选七-");
			mMap.put("113-2-7", "任选七-");

			mMap.put("113-0-8", "任选八-");
			mMap.put("113-0-9", "前二直选-");
			mMap.put("113-1-9", "前二直选-");
			mMap.put("113-3-9", "前二直选-");
			mMap.put("113-2-9", "前二直选-");
			mMap.put("113-4-9", "前二直选-");
			mMap.put("113-5-9", "前二直选-");

			mMap.put("113-0-10", "前三直选-");
			mMap.put("113-1-10", "前三直选-");
			mMap.put("113-3-10", "前三直选-");
			mMap.put("113-4-10", "前三直选-");
			mMap.put("113-5-10", "前三直选-");
			mMap.put("113-2-10", "前三直选-");

			mMap.put("113-0-11", "前二组选-");
			mMap.put("113-1-11", "前二组选-");
			mMap.put("113-3-11", "前二组选-");
			mMap.put("113-4-11", "前二组选-");
			mMap.put("113-5-11", "前二组选-");
			mMap.put("113-2-11", "前二组选-");

			mMap.put("113-0-12", "前三组选-");
			mMap.put("113-1-12", "前三组选-");
			mMap.put("113-2-12", "前三组选-");
			mMap.put("113-4-12", "前三组选-");
			mMap.put("113-5-12", "前三组选-");
			// 快乐扑克 kay=(lotteryid+saletype+childtype)
			mMap.put("121-0-0", "包选-");
			mMap.put("121-0-1", "猜同花-");
			mMap.put("121-0-2", "猜同花-");
			mMap.put("121-1-2", "猜同花-");

			mMap.put("121-0-3", "猜同花顺-");
			mMap.put("121-0-4", "猜同花顺-");
			mMap.put("121-1-4", "猜同花顺-");

			mMap.put("121-0-5", "猜顺子-");
			mMap.put("121-0-6", "猜顺子-");
			mMap.put("121-1-6", "猜顺子-");

			mMap.put("121-0-7", "猜豹子-");
			mMap.put("121-0-8", "猜豹子-");
			mMap.put("121-1-8", "猜豹子-");

			mMap.put("121-0-9", "猜对子-");
			mMap.put("121-0-10", "猜对子-");
			mMap.put("121-1-10", "猜对子-");

			mMap.put("121-0-11", "任选一-");
			mMap.put("121-1-11", "任选一-");

			mMap.put("121-0-12", "任选二-");
			mMap.put("121-1-12", "任选二-");
			mMap.put("121-2-12", "任选二-");

			mMap.put("121-0-13", "任选三-");
			mMap.put("121-1-13", "任选三-");
			mMap.put("121-2-13", "任选三-");

			mMap.put("121-0-14", "任选四-");
			mMap.put("121-1-14", "任选四-");
			mMap.put("121-2-14", "任选四-");

			mMap.put("121-0-15", "任选五-");
			mMap.put("121-1-15", "任选五-");
			mMap.put("121-2-15", "任选五-");

			mMap.put("121-0-16", "任选六-");
			mMap.put("121-1-16", "任选六-");
			mMap.put("121-2-16", "任选六-");
		}

		return mMap.get(key);
	}

	/**
	 * 快乐扑克 奖金划分
	 * 
	 * @param lotteryCount
	 * @param lotteryNums
	 * @param string
	 */
	public void znzh_pl121_tosaveBonus(int lotteryCount,
			List<List<Integer>> lotteryNums, String tag) {
		if ("".equals(tag) || tag == null) {
			return;
		}
		if (lotteryCount < 1) {
			return;
		}

		if ("0-0".equals(tag)) {// 包选
			for (int i = 0; i < lotteryNums.get(0).size(); i++) {
				saveBonusList = new ArrayList<Integer>();
				switch (lotteryNums.get(0).get(i)) {

				case 0:// 对子包选
					saveBonusList.add(7);
					break;
				case 1:
					saveBonusList.add(500);
					break;
				case 2:
					saveBonusList.add(22);
					break;
				case 3:
					saveBonusList.add(33);
					break;
				case 4:
					saveBonusList.add(535);
					break;
				default:
					break;
				}
				RATEBean.getInstance().setBonusScopeList(saveBonusList);
			}
		} else if ("0-9".equals(tag)) {// 猜对子
			setBonusScopeList(lotteryNums, 88, 7);
		} else if ("0-7".equals(tag)) {// 猜豹子
			setBonusScopeList(lotteryNums, 6400, 500);
		} else if ("0-1".equals(tag)) {// 猜同花
			setBonusScopeList(lotteryNums, 90, 22);
		} else if ("0-5".equals(tag)) {// 猜顺子
			setBonusScopeList(lotteryNums, 400, 33);
		} else if ("0-3".equals(tag)) {// 猜同花顺
			setBonusScopeList(lotteryNums, 2150, 535);
		} else if ("0-11".equals(tag)) {// 任选一
			setBonusScopeList(lotteryNums, 5, 0);
		} else if ("0-12".equals(tag)) {// 任选二
			setBonusScopeList(lotteryNums, 33, 0);
		} else if ("0-13".equals(tag)) {// 任选三
			setBonusScopeList(lotteryNums, 116, 0);
		} else if ("0-14".equals(tag)) {// 任选四
			setBonusScopeList(lotteryNums, 46, 0);
		} else if ("0-15".equals(tag)) {// 任选五
			setBonusScopeList(lotteryNums, 22, 0);
		} else if ("0-16".equals(tag)) {// 任选六
			setBonusScopeList(lotteryNums, 12, 0);
		}
	}

	/**
	 * 
	 * 快乐扑克设置普通玩法对应的奖金
	 * 
	 * 选号
	 * 
	 * @param lotteryNums
	 *            奖金范围
	 * @param maxBonus
	 * @param minBonus
	 */
	private void setBonusScopeList(List<List<Integer>> lotteryNums,
			int maxBonus, int minBonus) {
		saveBonusList = new ArrayList<Integer>();
		if (2 == lotteryNums.size()) {
			if (lotteryNums.get(0).size() < 1 && 1 == lotteryNums.get(1).size()) {
				saveBonusList.add(minBonus);
			} else if (lotteryNums.get(0).size() > 0
					&& 1 == lotteryNums.get(1).size()) {
				saveBonusList.add(maxBonus);
				saveBonusList.add(minBonus);
			} else if (lotteryNums.get(0).size() > 0
					&& 1 > lotteryNums.get(1).size()) {
				saveBonusList.add(maxBonus);
			}
		} else if (1 == lotteryNums.size()) {
			if (lotteryNums.get(0).size() > 1) {
				saveBonusList.add(maxBonus);
			}
		}
		RATEBean.getInstance().setBonusScopeList(saveBonusList);
	}

}
