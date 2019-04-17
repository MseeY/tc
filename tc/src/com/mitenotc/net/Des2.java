package com.mitenotc.net;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
*
* @author Administrator
*/
public class Des2 {

  private byte[] desKey;  
 
  /*private static char[] base64EncodeChars = new char[]{
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
      'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
      'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
      'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
      'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
      'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
      'w', 'x', 'y', 'z', '0', '1', '2', '3',
      '4', '5', '6', '7', '8', '9', '+', '/'};
private static byte[] base64DecodeChars = new byte[]{
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
      52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
      -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
      15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
      -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
      41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1};
*//**
* 加密
*
* @param data 明文的字节数组
* @return 密文字符串
*//*
public static String encode(byte[] data) {
  StringBuffer sb = new StringBuffer();
  int len = data.length;
  int i = 0;
  int b1, b2, b3;
  while (i < len) {
      b1 = data[i++] & 0xff;
      if (i == len) {
          sb.append(base64EncodeChars[b1 >>> 2]);
          sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
          sb.append("==");
          break;
      }
      b2 = data[i++] & 0xff;
      if (i == len) {
          sb.append(base64EncodeChars[b1 >>> 2]);
          sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
          sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
          sb.append("=");
          break;
      }
      b3 = data[i++] & 0xff;
      sb.append(base64EncodeChars[b1 >>> 2]);
      sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
      sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
      sb.append(base64EncodeChars[b3 & 0x3f]);
  }
  return sb.toString();
}
*//**
* 解密
*
* @param str 密文
* @return 明文的字节数组
* @throws UnsupportedEncodingException
*//*
public static byte[] decode(String str) throws UnsupportedEncodingException {
  StringBuffer sb = new StringBuffer();
  byte[] data = str.getBytes("US-ASCII");
  int len = data.length;
  int i = 0;
  int b1, b2, b3, b4;
  while (i < len) {
       b1 
      do {
          b1 = base64DecodeChars[data[i++]];
      } while (i < len && b1 == -1);
      if (b1 == -1) break;
       b2 
      do {
          b2 = base64DecodeChars
                  [data[i++]];
      } while (i < len && b2 == -1);
      if (b2 == -1) break;
      sb.append((char) ((b1 << 2) | ((b2 & 0x30) >>> 4)));
       b3 
      do {
          b3 = data[i++];
          if (b3 == 61) return sb.toString().getBytes("iso8859-1");
          b3 = base64DecodeChars[b3];
      } while (i < len && b3 == -1);
      if (b3 == -1) break;
      sb.append((char) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));
       b4 
      do {
          b4 = data[i++];
          if (b4 == 61) return sb.toString().getBytes("iso8859-1");
          b4 = base64DecodeChars[b4];
      } while (i < len && b4 == -1);
      if (b4 == -1) break;
      sb.append((char) (((b3 & 0x03) << 6) | b4));
  }
  return sb.toString().getBytes("iso8859-1");
} */
  
   public Des2(String desKey) {  
       this.desKey = desKey.getBytes();  
   }  
 
  /* public byte[] desEncrypt(byte[] plainText) throws Exception {  
       SecureRandom sr = new SecureRandom();  
       byte rawKeyData[] = desKey;  
       DESKeySpec dks = new DESKeySpec(rawKeyData);  
       SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
       SecretKey key = keyFactory.generateSecret(dks);  
       Cipher cipher = Cipher.getInstance("DES");  
       cipher.init(Cipher.ENCRYPT_MODE, key, sr);  
       byte data[] = plainText;  
       byte encryptedData[] = cipher.doFinal(data);  
       return encryptedData;  
   }  
 
   public byte[] desDecrypt(byte[] encryptText) throws Exception {  
       SecureRandom sr = new SecureRandom();  
       byte rawKeyData[] = desKey;  
       DESKeySpec dks = new DESKeySpec(rawKeyData);  
       SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
       SecretKey key = keyFactory.generateSecret(dks);  
       Cipher cipher = Cipher.getInstance("DES");  
       cipher.init(Cipher.DECRYPT_MODE, key, sr);  
       byte encryptedData[] = encryptText;  
       byte decryptedData[] = cipher.doFinal(encryptedData);  
       return decryptedData;  
   }  
 
   public String encrypt(String input) throws Exception {  
       return encode(desEncrypt(input.getBytes()));  
   }  
 
   public String decrypt(String input) throws Exception {  
       byte[] result = decode(input);  
       return new String(desDecrypt(result));  
   }  */
   
   
   
   
   
   
   
   
   private static char[] base64EncodeChars = new char[]{
       'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
       'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
       'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
       'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
       'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
       'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
       'w', 'x', 'y', 'z', '0', '1', '2', '3',
       '4', '5', '6', '7', '8', '9', '+', '/'};
private static byte[] base64DecodeChars = new byte[]{
       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
       52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
       -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
       15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
       -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
       41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1};
/**
* 加密
*
* @param data 明文的字节数组
* @return 密文字符串
*/
public static String encode(byte[] data) {
   StringBuffer sb = new StringBuffer();
   int len = data.length;
   int i = 0;
   int b1, b2, b3;
   while (i < len) {
       b1 = data[i++] & 0xff;
       if (i == len) {
           sb.append(base64EncodeChars[b1 >>> 2]);
           sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
           sb.append("==");
           break;
       }
       b2 = data[i++] & 0xff;
       if (i == len) {
           sb.append(base64EncodeChars[b1 >>> 2]);
           sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
           sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
           sb.append("=");
           break;
       }
       b3 = data[i++] & 0xff;
       sb.append(base64EncodeChars[b1 >>> 2]);
       sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
       sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
       sb.append(base64EncodeChars[b3 & 0x3f]);
   }
   return sb.toString();
}
/**
* 解密
*
* @param str 密文
* @return 明文的字节数组
* @throws UnsupportedEncodingException
*/
public static byte[] decode(String str) throws UnsupportedEncodingException {
   StringBuffer sb = new StringBuffer();
   byte[] data = str.getBytes("US-ASCII");
   int len = data.length;
   int i = 0;
   int b1, b2, b3, b4;
   while (i < len) {
       /* b1 */
       do {
           b1 = base64DecodeChars[data[i++]];
       } while (i < len && b1 == -1);
       if (b1 == -1) break;
       /* b2 */
       do {
           b2 = base64DecodeChars
                   [data[i++]];
       } while (i < len && b2 == -1);
       if (b2 == -1) break;
       sb.append((char) ((b1 << 2) | ((b2 & 0x30) >>> 4)));
       /* b3 */
       do {
           b3 = data[i++];
           if (b3 == 61) return sb.toString().getBytes("iso8859-1");
           b3 = base64DecodeChars[b3];
       } while (i < len && b3 == -1);
       if (b3 == -1) break;
       sb.append((char) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));
       /* b4 */
       do {
           b4 = data[i++];
           if (b4 == 61) return sb.toString().getBytes("iso8859-1");
           b4 = base64DecodeChars[b4];
       } while (i < len && b4 == -1);
       if (b4 == -1) break;
       sb.append((char) (((b3 & 0x03) << 6) | b4));
   }
   return sb.toString().getBytes("iso8859-1");
}  
public byte[] desEncrypt(byte[] plainText) throws Exception {  
   SecureRandom sr = new SecureRandom();  
   byte rawKeyData[] = desKey;  
   DESKeySpec dks = new DESKeySpec(rawKeyData);  
   SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
   SecretKey key = keyFactory.generateSecret(dks);  
   Cipher cipher = Cipher.getInstance("DES");  
   cipher.init(Cipher.ENCRYPT_MODE, key, sr);  
   byte data[] = plainText;  
   byte encryptedData[] = cipher.doFinal(data);  
   return encryptedData;  
}  

public byte[] desDecrypt(byte[] encryptText) throws Exception {  
   SecureRandom sr = new SecureRandom();  
   byte rawKeyData[] = desKey;  
   DESKeySpec dks = new DESKeySpec(rawKeyData);  
   SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
   SecretKey key = keyFactory.generateSecret(dks);  
   Cipher cipher = Cipher.getInstance("DES");  
   cipher.init(Cipher.DECRYPT_MODE, key, sr);  
   byte encryptedData[] = encryptText;  
   byte decryptedData[] = cipher.doFinal(encryptedData);  
   return decryptedData;  
}  

public String encrypt(String input) throws Exception {  
   //return base64Encode(desEncrypt(input.getBytes()));  
   return encode(desEncrypt(input.getBytes()));  
}  

public String decrypt(String input) throws Exception {  
   //byte[] result = base64Decode(input);  
   byte[] result = decode(input);  
   return new String(desDecrypt(result));  
}  

/*public static String base64Encode(byte[] s) {  
   if (s == null)  
       return null;  
   BASE64Encoder b = new sun.misc.BASE64Encoder();  
   return b.encode(s);  
}  

public static byte[] base64Decode(String s) throws IOException {  
   if (s == null)  
       return null;  
   BASE64Decoder decoder = new BASE64Decoder();  
   byte[] b = decoder.decodeBuffer(s);  
   return b;  
}  */

 /*  
   public static String base64Encode(byte[] s) {  
       if (s == null)  
           return null;  
       BASE64Encoder b = new sun.misc.BASE64Encoder();  
       return b.encode(s);  
   }  
 
   public static byte[] base64Decode(String s) throws IOException {  
       if (s == null)  
           return null;  
       BASE64Decoder decoder = new BASE64Decoder();  
       byte[] b = decoder.decodeBuffer(s);  
       return b;  
   }  
 /*  public static void main(String[] args) throws Exception {  
       String key = "12345678";  
       String input = "加密测试";  
       JavaApplication2 crypt = new JavaApplication2(key);  
//       ////System.out.println("Encode:" + crypt.encrypt(input));  
//       ////System.out.println("Decode:" + crypt.decrypt(crypt.encrypt(input)));  
   }*/

}