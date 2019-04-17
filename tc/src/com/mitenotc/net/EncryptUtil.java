package com.mitenotc.net;


public class EncryptUtil {
	
	private static Des2 des2;
	private static String publicKey = "5e372ae9";//专门用于 cmd 1000 和 1009 注册 和 忘记密码 接口 还没有密码的时候使用

	public static String encode(String msg){
		Des2 des2 = obtainDES();
		try {
			msg = des2.encrypt(msg);
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

//	private static String getKey() {
//		String pwd = Protocol.getInstance().getPSW();
//		if(AppUtil.isEmpty(pwd)){
//			return "";
//		}
//		String psw = DigestUtils.md5Hex(Protocol.getInstance().getUSR() + pwd).toLowerCase();
//		psw = psw.substring(0,4) + psw.substring(pwd.length()-4);
//		return psw;
//	}
	
	private static Des2 obtainDES(){
//		if(!AppUtil.isEmpty(key) && des2 != null && key.equals(getKey())){
//			return des2;
//		}
//		key = getKey();
		if(des2 == null){
			des2 = new Des2(publicKey);
		}
		return des2;// = new Des2(key);
	}
	
	public static String decode(String msg){
		Des2 des2 = obtainDES();
		try {
			msg = des2.decrypt(msg);
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
}
