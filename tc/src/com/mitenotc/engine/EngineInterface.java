package com.mitenotc.engine;

import java.util.List;
import java.util.Map;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.engine.annotations.Command;
import com.mitenotc.exception.MessageException;
import com.mitenotc.exception.ProtocolException;

/**
 * 业务层,对请求的封装,实际的封装代码已经用一个 代理 来完成了.
 * @author mitenotc
 */
public interface EngineInterface {

/*---------------------------------------------------------------------------------------------------------------------------------
 * 	4.1.	用户管理（1000~1099）
	需要检查登陆状态和操作权限的协议必须先执行登陆。
	协议号范围：1000~1099
	模块名称：Mod_UsrMng
*/
	
	
	/**
	 * 用户注册
	 * @param A  推荐人
	 * @param B 密码
	 * @param C 彩名
	 * @param D 验证码
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1000(String A, String B, String C,String D) throws MessageException;
	/**
	 * 短信验证  (要把 手机号 存入到sharedpreference 和 protocol)
	 * @param A  类型：1获取验证码；2校验验证码
	 * @param B 校验业务命令号
	 * @param C 手机号或者验证码
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1001(String A, String B, String C) throws MessageException;

	/**
	 * 用户登录  (要把session 存入到sharedpreference 和 protocol)
	 * @param A 密码
	 * @return
	 */
	MessageBean getCMD_1002(String A) throws MessageException;
	/**
	 * 用户注销
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1003() throws MessageException;
	
	/**
	 * 完善信息
	 * @param A  姓名
	 * @param B  性别
	 * @param C  生日
	 * @param D  电子邮箱
	 * @param E  证件类型
	 * @param F  证件号码
	 * @param list	 A		银行名称  B	银行卡号	C	省份	D	城市
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1004(String A, String B, String C, String D, String E, String F,List<Map<String, String>> list) throws MessageException;
	
	/**
	 * 查询彩名是否可用
	 * @param A 彩名
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1005(String A) throws MessageException;
	/**
	 * 修改彩名
	 * @param A  新彩名
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1006(String A) throws MessageException;
	/**
	 * 修改密码
	 * @param A 旧密码
	 * @param B 新密码
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1007(String A,String B) throws MessageException;
	/**
	 * 查询用户基本资料
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1008() throws MessageException;
	/**
	 * 找回密码
	 * @param A  短信验证码
	 * @param B  新密码
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1009(String A,String B) throws MessageException;
	/**
	 * 删除银行卡
	 * @param A  银行名称
	 * @param B  银行名称
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1010(String A,String B) throws MessageException;
	
/*---------------------------------------------------------------------------------------------------------------------------------
4.2.	用户交易（1100~1149）
	协议号范围：1100~1149
	模块名称：Mod_Trade
*/
	
	/**
	 * 账户查询
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1100() throws MessageException;
	/**
	 * 账户充值
	 * @param A  充值渠道代码
	 * @param B  充值金额  调用者必须要进行判断 必须大于0
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1101(String A,String B) throws MessageException;
	/**
	 * 积分兑换红包
	 * @param A	要兑换的积分分值
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1102(String A) throws MessageException;

	/**
	 * 查询交易明细:
	 * 快捷查询条件是自当天开始，往前查询的天数，1天只查询当日交易 etc..，0表示不适用该条件，该域不为0的时候，开始日期和结束日期无效。
	 * @param A  快捷查询(天数)
	 * @param B  开始日期
	 * @param C  结束日期
	 * @param D  交易类型
	 * @param E  当前页码
	 * @param F  每页行数
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1103(String A,String B,String C,String D,String E,String F) throws MessageException;
	
	/**
	 * 查询可购红包
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1104() throws MessageException;
	
	/**
	 * 现金购买红包
	 * @param A  支付渠道代码
	 * @param B  支付金额(购买价格*购买数量，前台展示，后台校验)
	 * @param C  红包编号
	 * @param D  购买数量
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1105(String A,String B,String C,String D) throws MessageException;
	/**
	 * 用户提交提现申请
	 * @param A  银行名称
	 * @param B  银行账号
	 * @param C  提现金额
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1106(String A,String B,String C) throws MessageException;
	/**
	 * 付款:
	 * 通过本系统的可用金额进行付款。
	 * @param A  订单流水号
	 * @param B  红包账户付款金额
	 * @param C  现金账户付款金额
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1107(String A,String B,String C) throws MessageException;
	
	
/*	---------------------------------------------------------------------------------------------------------------------------------
 * 4.3.	彩票投注（1200~1299）
	协议号范围：1200~1299
	模块名称：Mod_Lty
	    使用频繁的协议，如查询期信息、开奖公告、对阵信息、号码遗漏等，后台要加载到缓存中，以便快速响应，并减少数据库访问。
*/
	/**
	 * 查询期信息
	 * @param A  玩法代码(可为空)
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1200(String A) throws MessageException;
	/**
	 * 查询开奖公告:
	 * 玩法代码域为空时，返回所有玩法(竞彩除外)最近一期的开奖公告，此时期数域无效，需要特别注意的是，输入参数玩法代码域不为空的时候，输出列表里面A和B域不存在，以便节省流量；期数值自最后一个开奖期开始往前的多少期，此域为空时返回最近一期的开奖公告。
	 * @param A	玩法代码
	 * @param B	期数
	 * @param C	当前页码
	 * @param D	每页行数
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1201(String A,String B,String C,String D) throws MessageException;
	/**
	 * 查询开奖公告明细:
	 * @paramA	玩法代码
	* @paramB	期数
	* @paramC	当前页码
	* @paramD	每页行数
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1202(String A,String B,String C,String D) throws MessageException;
	/**
	 * 查询足彩对阵信息
	 * @param A  玩法代码
	 * @param B  期号
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1203(String A,String B) throws MessageException;
	
	/**
	 * 查询号码遗漏值(11选5)
	 * @param A  玩法代码
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1204(String A) throws MessageException;
	/**
	 * 购彩下单:
	 * 子玩法代码和销售方式域不同的玩法可能不一样
	 * @param A  购彩时间戳(防止协议劫持)
	 * @param B  玩法代码(竞彩玩法见附录)
	 * @param C  购彩总金额
	 * @param D  中奖后是否停止追号(0不停止；1停止)
	 * @param E  追号期数
	 * @param F  追号详情(高频才有)
	 * @param list  A	  期号	B	子玩法代码	C	销售方式	D	投注号码	E	倍数	F	注数	G	金额
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1205(String A,String B,String C,String D,String E,String F,List<Map<String,String>> list) throws MessageException;
	/**
	 * 查询订单记录:
	 * 按时间倒序排列。
	 * @param A  玩法代码(为空则查询所有)
	 * @param B  查询类别(1全部；2未支付；3中奖；4待开奖；5追号；6失败订单)
	 * @param C  当前页码
	 * @param D  每页行数
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1206(String A,String B,String C,String D) throws MessageException;
	/**
	 * 查询订单购彩详情
	 * @param A
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1207(String A) throws MessageException;
	
	/**
	 * 终止/取消追号:
	 * 期号为空时终止整个追号，不为空时取消某一期的追号。
	 * @param A
	 * @param B
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1208(String A,String B) throws MessageException;
	/**
	 * 查询战绩/等级榜:  
	 * 相同等级或者战绩的，按注册时间先后排列
	 * @param A  查询方式(1查战绩；2查等级)
	 * @param B
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1209(String A) throws MessageException;
	/**
	 * 彩票订单再购:
	 * 用户查询订单以后，可以再次购买，后台生成新的订单。
	 * @param A  购彩时间戳(防止协议劫持)
	 * @param B  玩法代码(竞彩玩法见附录)
	 * @param C  期号
	 * @param D  旧的订单流水号
	 * @param E  倍数(单子的倍数)
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1210(String A,String B,String C,String D,String E) throws MessageException;
	/**
	 * 取消未支付订单
	 * @param A  订单流水号
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1211(String A) throws MessageException;
	
/*	---------------------------------------------------------------------------------------------------------------------------------
 * 4.4.	辅助功能（1300~1349）
	协议号范围：1300~1349
	模块名称：Mod_Assit*/
	/**
	 * 查询信息推送设置:
	 * 开奖号码推送为字符串，0代表不推送，1推送；第1位是大乐透，第2位是双色球。如：10，代表推送大乐透不推送双色球。购彩提醒是指在开奖日提醒用户封期前购买彩票，高频不提醒。
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1300() throws MessageException;
	

	/**
	 * 设置信息推送参数
	 * @param A  开奖号码推送
	 * @param B  中奖动画(0不显示动画；1显示动画)
	 * @param C  购彩提醒(0不提醒；1提醒)
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1301(String A,String B,String C) throws MessageException;
	/**
	 * 查询用户个性化设置:
	 * 前台自行定义各参数名称和值，后台直接保存参数串，此参数串为一个JSON串。
	 * @param A  类型(1系统参数；2用户参数)
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1302(String A) throws MessageException;
	/**
	 * 设置用户个性化参数
	 * @param A  类型(1系统参数；2用户参数)
	 * @param B  参数串
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1303(String A,String B) throws MessageException;
	/**
	 * 查询购彩参数
	 * @param A
	 * @param B
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1304(String A,String B) throws MessageException;
	/**
	 * 设置购彩参数
	 * @param A  追号款是否冻结(1冻结；0不冻结)
	 * @param B
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1305(String A,String B) throws MessageException;
	
	/*---------------------------------------------------------------------------------------------------------------------------------
	 * 4.5.	信息查询（1350~1399）
	协议号范围：1350~1399
	模块名称：Mod_QryMsg*/

	/**
	 * 获取专家推荐信息/彩票新闻列表
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1350() throws MessageException;
	/**
	 * 获取信息详细内容
	 * @param A 信息编号
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1351(String A) throws MessageException;
	/**
	 * 获取推送信息:
	 * 信息推送时，前台轮询，如果后台有信息需要推送，就返回对应的信息码，信息码4位，0000表示没有信息，其他情况，前台要通过本协议获取推送的信息内容。
	 * @param A  信息码
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1352(String A) throws MessageException;
	
	/**
	 * 在线咨询-客户
	 * @param A  会话ID
	 * @param B  咨询内容
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1353(String A,String B) throws MessageException;
	/**
	 * 在线咨询-客服
	 * 不传会话ID的情况下，是获取用户咨询列表。
	 * @param A  会话ID
	 * @param B  咨询内容
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1354(String A,String B) throws MessageException;
	/**
	 * 软件更新检查
	 * @return
	 * @throws MessageException
	 */
	MessageBean getCMD_1355() throws MessageException;
}
