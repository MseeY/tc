package com.mitenotc.test;

import org.json.JSONArray;
import org.json.JSONObject;

import android.test.AndroidTestCase;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.engine.BaseEngine;
import com.mitenotc.engine.EngineProxy;
import com.mitenotc.exception.MessageException;
import com.mitenotc.net.MessageJson;
import com.mitenotc.net.Protocol;
import com.mitenotc.utils.FormatUtil;
import com.mitenotc.utils.LogUtil;

public class EngineTest extends AndroidTestCase{

	private String SND = "5A83EE032C60B7C3C960E069101DF929";
	//正确
	public void testCMD_1000(){
		try {
			// 调用 业务层 获取数据
			MessageBean message = EngineProxy.proxy.getCMD_1000("189888888", "passwrod1000", "彩名a","123456");
			print(1000,message);
		} catch (MessageException e) {// 当 业务中返回的数据 A 不是 0 是 B中的错误信息 封装在 异常的message 中,调用 e.getMessage 获取. //当然,也可以调用messageBean.getB 来获取
			e.printStackTrace();
			String message = e.getMessage();
			LogUtil.info(EngineTest.class,"e.getmessage = "+e.getMessage());
		}
	}

	private void print(int cmd,MessageBean message) {
		String json = new Gson().toJson(message);
		LogUtil.info(EngineTest.class,"test "+cmd+" = "+json);
	}
	//正确
	public void testCMD1001() throws MessageException{
		MessageBean message = EngineProxy.proxy.getCMD_1001("1", "1001", "18058259434");
		print(1001, message);
	}
	public void testCMD1001_verify() throws MessageException{
		MessageBean message = EngineProxy.proxy.getCMD_1001("2", "1001", "123456");
		print(1001, message);
	}
	//异常:message 能够解析,但是 message 没法解析到 messageBean 中
	public void testCMD1002() throws MessageException{
		MessageBean message = EngineProxy.proxy.getCMD_1002("passwrod1000");
		print(1002, message);
	}
	//正确
	public void testCMD1003() throws MessageException{
		MessageBean message = EngineProxy.proxy.getCMD_1003();
		print(1003, message);
	}
	//正确
	public void testCMD1004() throws MessageException{
		Protocol.getInstance().setSND("DF6BB8487259F47EDD8DB9BA61889BF5");
		MessageBean message = EngineProxy.proxy.getCMD_1004(null, null, null, null, null, null, null);
		print(1004, message);
	}
	//异常:返回信息中没有msg//1005 服务端还未完成
	public void testCMD1005() throws MessageException{
		Protocol.getInstance().setSND("9106568BD0C7D0BC1F1C73E244B22FE8");
		MessageBean message = EngineProxy.proxy.getCMD_1005("彩名泰彩");
		print(1005, message);
	}
	
	public void testCMD1200() throws MessageException{
		MessageBean message = EngineProxy.proxy.getCMD_1200("");
		print(1200, message);
	}
	public void testCMD1201() throws MessageException{
		MessageBean message = EngineProxy.proxy.getCMD_1201("",null,"1","2");
		print(1201, message);
	}
	
	public void testCMD_1200() throws Exception{
		BaseEngine.getCMD(1200,null);
	} 
	
	
	//*******************************新协议的测试*******************************
	public void testCMD__1000() throws Exception{
		MessageJson msg = new MessageJson();
		msg.put("B", "passwrod1000");
		msg.put("C", "彩名a");
		msg.put("D", "123456");
		MessageBean message = BaseEngine.getCMD(1000,msg);
		print(1201, message);
	}
	//jsonresult = {"MSG":{"A":"0","B":"操作成功","C":"彩名a"},"HEADER":{"CMD":"1000","VER":"100"}}

	public void testCMD__1001() throws Exception{
		MessageJson msg = new MessageJson();
		msg.put("A", "1");
		msg.put("B", "1001");
		msg.put("C", "13866667777");
		MessageBean message = BaseEngine.getCMD(1001,msg);
	}
	//jsonresult = {"MSG":{"A":"0","B":"操作成功","C":"2014-01-10 12:45:20"},"HEADER":{"CMD":"1001","VER":"100"}}
	
	public void testCMD__1001_2() throws Exception{
		MessageJson msg = new MessageJson();
		msg.put("A", "2");
		msg.put("B", "1001");
		msg.put("C", "123456");
		MessageBean message = BaseEngine.getCMD(1001,msg);
	}
	// jsonresult = {"MSG":{"A":"0","B":"操作成功","C":""},"HEADER":{"CMD":"1001","VER":"100"}}

	
	public void testCMD__1002() throws Exception{
		MessageJson msg = new MessageJson();
		msg.put("A", "123");
		MessageBean message = BaseEngine.getCMD(1002,msg);
		////System.out.println("SND = "+ message.getD());
	}
	// jsonresult = {"MSG":{"D":"1A37C741D82C95765A65D7B969D2256C","E":"0","F":"0","A":"0","B":"操作成功","C":"彩名a"},"HEADER":{"CMD":"1002","VER":"100"}}
	public void testCMD__1003() throws Exception{
		MessageJson msg = new MessageJson();
		MessageBean message = BaseEngine.getCMD(1003,null);
	}
	//01-10 04:01:56.342: I/System.out(2858): jsonresult = {"MSG":{"A":"0","B":"操作成功"},"HEADER":{"CMD":"1003","VER":"100"}}
	
	
	public void testCMD__1004() throws Exception{
		Protocol.getInstance().setSND("764675628B64E45C782251141670F807");
		MessageJson msg = new MessageJson();
		msg.put("A", "姓名");
		msg.put("B", "男");//**身份证和性别要进行数据的转换
		msg.put("C", "2013-01-01");
		msg.put("D", "781492604@qq.com");
		msg.put("E", "1");//**身份证和性别要进行数据的转换
		msg.put("F", "330724199003125631");
		JSONArray jsonArray = new JSONArray();
		JSONObject bankJson = new JSONObject();
		bankJson.put("A", "银行名称");
		bankJson.put("B", "1234567890");
		bankJson.put("C", "北京");
		bankJson.put("D", "北京");
		jsonArray.put(bankJson);
		msg.put("LIST", jsonArray);
		MessageBean message = BaseEngine.getCMD(1004,msg);
	}
// jsonresult = {"MSG":{"A":"0","B":"操作成功"},"HEADER":{"CMD":"1004","VER":"100"}}

	public void testCMD__1005() throws Exception{
		MessageJson msg = new MessageJson();
		msg.put("A", "aa");
		MessageBean message = BaseEngine.getCMD(1005,msg);
	}
	// jsonresult = {"MSG":{"A":"0","B":"操作成功"},"HEADER":{"CMD":"1005","VER":"100"}}

	public void testCMD__1006() throws Exception{
		Protocol.getInstance().setSND("96DB35291DC03C55DEE0E081683A15FC");
		MessageJson msg = new MessageJson();
		msg.put("A", "aaa");
		MessageBean message = BaseEngine.getCMD(1006,msg);
	}
	//jsonresult = {"MSG":{"A":"0","B":"操作成功"},"HEADER":{"CMD":"1006","VER":"100"}}
	
	public void testCMD__1007() throws Exception{
		Protocol.getInstance().setSND("96DB35291DC03C55DEE0E081683A15FC");
		MessageJson msg = new MessageJson();
		msg.put("A", "passwrod1000");
		msg.put("B", "passwrod1000_1");
		MessageBean message = BaseEngine.getCMD(1007,msg);
	}
	// jsonresult = {"MSG":{"A":"0","B":"操作成功"},"HEADER":{"CMD":"1007","VER":"100"}}

	public void testCMD__1008() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		MessageBean message = BaseEngine.getCMD(1008,msg);
	}
	
	//1009 要先获取验证码,然后才是修改密码
	public void testCMD__1001_9() throws Exception{
		MessageJson msg = new MessageJson();
		msg.put("A", "1");
		msg.put("B", "1009");
		msg.put("C", "18058259434");
		MessageBean message = BaseEngine.getCMD(1001,msg);
	}
	public void testCMD__1009() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "123456");
		msg.put("B", "passwrod1000");
		MessageBean message = BaseEngine.getCMD(1009,msg);
	}
	//jsonresult = {"MSG":{"A":"0","B":"操作成功"},"HEADER":{"CMD":"1009","VER":"100"}}

	public void testCMD__1010() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "银行名称");
		msg.put("B", "1234567890");
		MessageBean message = BaseEngine.getCMD(1010,msg);
	}
	//jsonresult = {"MSG":{"A":"0","B":"操作成功"},"HEADER":{"CMD":"1010","VER":"100"}}
	
	
//	*****************4.2.	用户交易（1100~1149）***************

	public void testCMD__1100() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		MessageBean message = BaseEngine.getCMD(1100,msg);
	}//测试 通过
	public void testCMD__1101() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "0");
		msg.put("B", "1000");
		MessageBean message = BaseEngine.getCMD(1101,msg);
	}//测试通过
	public void testCMD__1102() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "100");
		MessageBean message = BaseEngine.getCMD(1102,msg);
	}//测试通过
	
	public void testCMD__1103() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "1");
		msg.put("D", "0");
		msg.put("E", "1");
		msg.put("F", "20");
		MessageBean message = BaseEngine.getCMD(1103,msg);
	}//测试通过
	
	public void testCMD__1104() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		MessageBean message = BaseEngine.getCMD(1104,msg);
	}
	public void testCMD__1105() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "0");
		msg.put("B", "100");
		msg.put("C", "201401090004");
		msg.put("D", "1");
		MessageBean message = BaseEngine.getCMD(1105,msg);
	}
	//jsonresult = {"MSG":{"A":"0","B":"操作成功","C":"201401111520241634"},"HEADER":{"CMD":"1105","VER":"100"}}

	public void testCMD__1106() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "中国银行");
		msg.put("B", "1000003243241");
		msg.put("C", "100");
		MessageBean message = BaseEngine.getCMD(1106,msg);
	}//测试通过
	
	public void testCMD__1107() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "12345678");
		msg.put("B", "100");
		msg.put("C", "100");
		MessageBean message = BaseEngine.getCMD(1107,msg);
	}
	// jsonresult = {"MSG":{"LIST":[{"A":"分红账户","B":"3870"},{"A":"奖金账户","B":"3000"},{"A":"现金账户","B":"2899"},{"A":"红包账户","B":"678"}],"A":"0","B":"操作成功","C":"2899"},"HEADER":{"CMD":"1107","VER":"100"}}

//	*****************************4.3.	彩票投注（1200~1299）************************
	//测试 0k
	public void testCMD__1200() throws Exception{
		MessageJson msg = new MessageJson();
//		msg.put("A", "112");
		MessageBean message = BaseEngine.getCMD(1200,msg);
	}
	
	public void testCMD__1201() throws Exception{
//		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "118");
		msg.put("C", "20");
		MessageBean message = BaseEngine.getCMD(1201,msg);
		print(1201, message);
	}//测试ok
	
	 
	public void testCMD__1202() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "106");
		msg.put("B", "14003");
		msg.put("C", "3");
		MessageBean message = BaseEngine.getCMD(1202,msg);
		print(1201, message);
	}// 测试0k
	
	// 1203 足彩 目前还没有数据
	public void testCMD__1203() throws Exception{
		MessageJson msg = new MessageJson();
		msg.put("A", "108");
		msg.put("B", "14003");
		MessageBean message = BaseEngine.getCMD(1203,msg);
	}
	public void testCMD__1204() throws Exception{
		MessageJson msg = new MessageJson();
		msg.put("A", "112");
		MessageBean message = BaseEngine.getCMD(1204,msg);
		////System.out.println("c = "+message.getC());
	}//测试通过
//	{"A":"2014\/1\/11 16:31:59","B":"106","C":"14005","D":"200","E":"1","F":"1","LIST":[{"A":"0","B":"0","C":"0102030405|0102","D":"1","E":"1","F":"200"}]}
	//多期参数错误
	public void testCMD__1205() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", FormatUtil.timeFormat(System.currentTimeMillis()));
		msg.put("B", "106");
		msg.put("C", "14006");
		msg.put("D", "1000");
		msg.put("E", "1");
		msg.put("F", "5");
		JSONArray jsonArray = new JSONArray();
		MessageJson bean = new MessageJson();
		bean.put("A", "0");
		bean.put("B", "0");
		bean.put("C", "0102030405|0607");
		bean.put("D", "1");
		bean.put("E", "1");
		bean.put("F", "200");
		jsonArray.put(bean);
		msg.put("LIST", jsonArray);
		MessageBean message = BaseEngine.getCMD(1205,msg);
	}
	
	public void testCMD__1206() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "118");
		msg.put("B", "1");
		msg.put("C", "1");
		msg.put("D", "1");
		MessageBean message = BaseEngine.getCMD(1206,msg);
	}//1206 ok
// jsonresult = {"MSG":{"D":3,"LIST":[{"D":"2014-01-06 17:13","E":"1200","F":"4","A":"qc1401061713506009","B":"112","C":"14010642","H":"3","B1":"十一运"},{"D":"2014-01-06 17:02","E":"400","F":"4","A":"qc1401061702506004","B":"112","C":"14010641","H":"3","B1":"十一运"},{"D":"2014-01-06 17:02","E":"400","F":"4","A":"qc1401061702506005","B":"112","C":"14010641","H":"3","B1":"十一运"}],"E":1,"A":"0","B":"操作成功","C":1},"HEADER":{"CMD":"1206","VER":"100"}}

	public void testCMD__1207() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "qc1401061713506009");
		msg.put("C", "1");
		msg.put("D", "20");
		MessageBean message = BaseEngine.getCMD(1207,msg);
		////System.out.println("list1 = "+message.getLIST().get(0));
	}//测试通过
	
	public void testCMD__1208() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "qc1401061713506009");
		MessageBean message = BaseEngine.getCMD(1208,msg);
	}//
	public void testCMD__1209() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "2");
		MessageBean message = BaseEngine.getCMD(1209,msg);
	}//1209 没有 List 输出//战绩目前没用数据,等级测试通过
	public void testCMD__1210() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A",FormatUtil.timeFormat(System.currentTimeMillis()));
		msg.put("B","14006");
		msg.put("C","qc1401111654506038");
		MessageBean message = BaseEngine.getCMD(1210,msg);
	}//测试通过
	//jsonresult = {"MSG":{"D":10447,"A":"0","B":"操作成功","C":"qc1401131455506065"},"HEADER":{"CMD":"1210","VER":"100"}}

	public void testCMD__1211() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A","qc1401131447506064");
		MessageBean message = BaseEngine.getCMD(1211,msg);
	}//测试通过
	
	//****************************辅助功能 接口****************************
	public void testCMD__1300() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "");
		MessageBean message = BaseEngine.getCMD(1300,msg);
	}
	//jsonresult = {"MSG":{"D":"0","E":"0","A":"0","B":"操作成功","C":"00"},"HEADER":{"CMD":"1300","VER":"100"}}

	public void testCMD__1301() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "01");
		msg.put("B", "0");
		msg.put("C", "0");
		MessageBean message = BaseEngine.getCMD(1301,msg);
	}
	// jsonresult = {"MSG":{"A":"0","B":"操作成功"},"HEADER":{"CMD":"1301","VER":"100"}}

	//先设1203的设置,然后是1302的查询
	public void testCMD__1302() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "2");
		MessageBean message = BaseEngine.getCMD(1302,msg);
	}
	// jsonresult = {"MSG":{"A":"0","B":"操作成功","C":"用户个性化设置"},"HEADER":{"CMD":"1302","VER":"100"}}

	public void testCMD__1303() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "2");
		msg.put("B", "用户个性化设置");
		MessageBean message = BaseEngine.getCMD(1303,msg);
	}
	//jsonresult = {"MSG":{"A":"0","B":"操作成功"},"HEADER":{"CMD":"1303","VER":"100"}}

	public void testCMD__1304() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "");
		MessageBean message = BaseEngine.getCMD(1304,msg);
	}
	//jsonresult = {"MSG":{"A":"0","B":"操作成功","C":"0"},"HEADER":{"CMD":"1304","VER":"100"}}

	public void testCMD__1305() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "0");
		MessageBean message = BaseEngine.getCMD(1305,msg);
	}
	// jsonresult = {"MSG":{"A":"0","B":"操作成功"},"HEADER":{"CMD":"1305","VER":"100"}}

	//*********************4.5.	信息查询（1350~1399）*********************
	public void testCMD__1350() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "");
		MessageBean message = BaseEngine.getCMD(1350,msg);
	}//测试 通过
	public void testCMD__1351() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "1234567");
		MessageBean message = BaseEngine.getCMD(1351,msg);
	}
	//1352 消息推送 目前使用极光推送
	public void testCMD__1353() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		msg.put("A", "201401111013241609");
		msg.put("B", "用户咨询");
		MessageBean message = BaseEngine.getCMD(1353,msg);
	}
	//1354前端不用
	public void testCMD__1355() throws Exception{
		Protocol.getInstance().setSND(SND);
		MessageJson msg = new MessageJson();
		MessageBean message = BaseEngine.getCMD(1355,msg);
	}//测试成功
	//1356前端不用
	//1357用于提示 用户 是否有客服在线
	public void testCMD__1357() throws Exception{
		MessageJson msg = new MessageJson();
		MessageBean message = BaseEngine.getCMD(1357,msg);
	}
	// jsonresult = {"MSG":{"LIST":[{"A":"1002"}],"A":"0","B":"操作成功"},"HEADER":{"CMD":"1357","VER":"100"}}

}
