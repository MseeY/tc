package com.mitenotc.bean;

import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.View;
/**
 * @author mitenotc: chenkui
 * 对服务端返回的消息的封装
 */
public class MessageBean {
	private String A;
	private String A1;
	private String B;
	private String B1;
	protected String C;
	protected String C0;
	protected String C1;
	private String D;
	private String E;
	private String E1;
	private String E2;
	private String E3;
	private String E4;
	private String E5;
	private String E6;
	private String E7;
	private String E8;
	private String E9;
	private String E10;
	private String E11;
	private String E12;
	private String F;
	private String F0;
	private String G;
	private String H;
	private String H0;
	private String I;
	private String J;
	private String K;
	private String L;
	private String M;
	private String N;
	private String O;
	private String P;
	private String Q;
	private String U;
	private String V;
	private String G3;
	private MessageBean T;
	private MessageBean X;
	private String W;
	private String S;
	private String R;
	private List<MessageBean> LIST;
	private List<MessageBean> LIST1;
	private List<MessageBean> LISTVALUE;
	private String VER;
	private String SND;
	private String CMD;
	private String RN;
	
	private String FROM;
	private int position;//用于 MessageBean 在集合中时 排序,确定位置
	/**竞彩用来标记设担 协议数据不会出现此字段**/
	private boolean ISD=false;
	/**竞彩用来记录该场所选胜平负3,1,0**/
	private int CH_NUM=0;
	
	public boolean isISD() {
		return ISD;
	}
	public void setISD(boolean iSD) {
		ISD = iSD;
	}
	public int getCH_NUM() {
		return CH_NUM;
	}
	public void setCH_NUM(int cH_NUM) {
		CH_NUM = cH_NUM;
	}
	public String getSND() {
		return SND;
	}
	public void setSND(String sND) {
		SND = sND;
	}
	public String getVER() {
		return VER;
	}
	public void setVER(String vER) {
		VER = vER;
	}
	public String getA() {
		return A;
	}
	public void setA(String a) {
		A = a;
	}
	public String getB() {
		return B;
	}
	public void setB(String b) {
		B = b;
	}
	public String getC() {
		return C;
	}
	public void setC(String c) {
		C = c;
	}
	public String getD() {
		return D;
	}
	public void setD(String d) {
		D = d;
	}
	public String getE() {
		return E;
	}
	public void setE(String e) {
		E = e;
	}
	public String getF() {
		return F;
	}
	public void setF(String f) {
		F = f;
	}
	public String getG() {
		return G;
	}
	public void setG(String g) {
		G = g;
	}
	public String getH() {
		return H;
	}
	public void setH(String h) {
		H = h;
	}
	public String getI() {
		return I;
	}
	public void setI(String i) {
		I = i;
	}
	public String getJ() {
		return J;
	}
	public void setJ(String j) {
		J = j;
	}
	public List<MessageBean> getLIST() {
		return LIST;
	}
	public void setLIST(List<MessageBean> lIST) {
		LIST = lIST;
	}
	public String getK() {
		return K;
	}
	public void setK(String k) {
		K = k;
	}
	public String getL() {
		return L;
	}
	public void setL(String l) {
		L = l;
	}
	public String getM() {
		return M;
	}
	public void setM(String m) {
		M = m;
	}
	public String getO() {
		return O;
	}
	public void setO(String o) {
		O = o;
	}
	public String getP() {
		return P;
	}
	public void setP(String p) {
		P = p;
	}
	public String getQ() {
		return Q;
	}
	public void setQ(String q) {
		Q = q;
	}
	public String getN() {
		return N;
	}
	public void setN(String n) {
		N = n;
	}
	
	public String getU() {
		return U;
	}
	public void setU(String u) {
		U = u;
	}
	public String getV() {
		return V;
	}
	public void setV(String v) {
		V = v;
	}
	public String getR() {
		return R;
	}
	public void setR(String r) {
		R = r;
	}
	
	public MessageBean getT() {
		return T;
	}
	public void setT(MessageBean t) {
		T = t;
	}
	public String getW() {
		return W;
	}
	public void setW(String w) {
		W = w;
	}
	public String getS() {
		return S;
	}
	public void setS(String s) {
		S = s;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getB1() {
		return B1;
	}
	public void setB1(String b1) {
		B1 = b1;
	}
	public String getF0() {
		return F0;
	}
	public void setF0(String f0) {
		F0 = f0;
	}
	public String getH0() {
		return H0;
	}
	public void setH0(String h0) {
		H0 = h0;
	}
	public List<MessageBean> getLIST1() {
		return LIST1;
	}
	public void setLIST1(List<MessageBean> lIST1) {
		LIST1 = lIST1;
	}
	public String getA1() {
		return A1;
	}
	public void setA1(String a1) {
		A1 = a1;
	}
	public String getC1() {
		return C1;
	}
	public void setC1(String c1) {
		C1 = c1;
	}
	public String getCMD() {
		return CMD;
	}
	public void setCMD(String cMD) {
		CMD = cMD;
	}
	public String getE1() {
		return E1;
	}
	public void setE1(String e1) {
		E1 = e1;
	}
	public String getE2() {
		return E2;
	}
	public void setE2(String e2) {
		E2 = e2;
	}
	public String getE3() {
		return E3;
	}
	public void setE3(String e3) {
		E3 = e3;
	}
	public String getE4() {
		return E4;
	}
	public void setE4(String e4) {
		E4 = e4;
	}
	public String getFROM() {
		return FROM;
	}
	public void setFROM(String fROM) {
		FROM = fROM;
	}
	public String getC0() {
		return C0;
	}
	public void setC0(String c0) {
		C0 = c0;
	}
	public List<MessageBean> getLISTVALUE() {
		return LISTVALUE;
	}
	public void setLISTVALUE(List<MessageBean> lISTVALUE) {
		LISTVALUE = lISTVALUE;
	}
	public String getE5() {
		return E5;
	}
	public void setE5(String e5) {
		E5 = e5;
	}
	public String getE6() {
		return E6;
	}
	public void setE6(String e6) {
		E6 = e6;
	}
	public String getE7() {
		return E7;
	}
	public String getE8() {
		return E8;
	}
	public String getE9() {
		return E9;
	}
	public String getE10() {
		return E10;
	}
	public String getE11() {
		return E11;
	}
	public String getE12() {
		return E12;
	}
	public MessageBean getX() {
		return X;
	}
	public void setX(MessageBean x) {
		X = x;
	}
	public String getRN() {
		return RN;
	}
	public void setRN(String rN) {
		RN = rN;
	}
	public String getG3() {
		return G3;
	}
	public void setG3(String g3) {
		G3 = g3;
	}
}
