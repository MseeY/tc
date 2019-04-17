package com.mitenotc.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.mitenotc.tc.ConstantValue;
import com.mitenotc.tc.GloableParams;

/**
 * 访问网络的工具
 * 
 * @author Administrator
 * 
 */
public class HttpClientUtil {
	private HttpClient client;

	// 请求
	private HttpRequest request;
	private HttpPost post;
	private HttpGet get;
	// 回复
	private HttpResponse response;
	private  final int MAXLENGTH = 102400*30; //响应数据的大小原来是102400
	private  final int BUFFERSIZE = 1024*2;  

	private static Header[] headers;
	static{
		headers=new Header[10];
//		headers[0]=new BasicHeader("appkey", ";lashfe;lwaief");
		
		headers[0] = new BasicHeader("Appkey", "12343");
		headers[1] = new BasicHeader("Udid", "");// 手机串号
		headers[2] = new BasicHeader("Os", "android");//
		headers[3] = new BasicHeader("Osversion", "");//
		headers[4] = new BasicHeader("Appversion", "");// 1.0
		headers[5] = new BasicHeader("Sourceid", "");//
		headers[6] = new BasicHeader("Ver", "");

		headers[7] = new BasicHeader("Userid", "");
		headers[8] = new BasicHeader("Usersession", "");

		headers[9] = new BasicHeader("Unique", "");
	}

	public HttpClientUtil() {
		client = new DefaultHttpClient();

		// 如果是wap方式连接的话，需要设置ip和端口信息
//		if (StringUtils.isNotBlank(GloableParams.PROXY_IP)) {
//			HttpHost host = new HttpHost(GloableParams.PROXY_IP, GloableParams.PROXY_PORT);
//			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
//		}
	}

	/**
	 * 发送xml文件到服务器端
	 */
	public InputStream sendXml(String uri, String xml) {
		post = new HttpPost(uri);

		// 设置连接参数
		// HttpParams httpParams = new BasicHttpParams();//
		// httpParams = new BasicHttpParams();
		// HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
		// HttpConnectionParams.setSoTimeout(httpParams, 8000);
		// post.setParams(httpParams);

		try {
			StringEntity entity = new StringEntity(xml, ConstantValue.CHARSET);
			post.setEntity(entity);
			response = client.execute(post);
			// 判断是否成功处理
			if (response.getStatusLine().getStatusCode() == 200)
				return response.getEntity().getContent();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 处理post请求
	 * 
	 * @param uri
	 * @param params
	 * @return
	 */
	public String sendPost(String uri, Map<String, String> params) {
		post = new HttpPost(uri);
		
		post.setHeaders(headers);

		// 设置连接参数
		HttpParams httpParams = new BasicHttpParams();//
		httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
		HttpConnectionParams.setSoTimeout(httpParams, 8000);
		post.setParams(httpParams);

		// 设置参数
		if (params != null && params.size() > 0) {
			// BasicNameValuePair pair=new BasicNameValuePair(name, value)
			//
			// List<? extends NameValuePair> parameters;
			// UrlEncodedFormEntity entity=new UrlEncodedFormEntity(parameters, ConstantValue.CHARSET);

			/**
			 * username 用户名 xiaowen@redbaby.com.cn password 密码 123456
			 */
			List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			for (Map.Entry<String, String> item : params.entrySet()) {
				BasicNameValuePair pair = new BasicNameValuePair(item.getKey(), item.getValue());
				parameters.add(pair);
			}
			try {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, ConstantValue.CHARSET);
				//http://localhost:8080/ECServiceHM21/help?version=0
				post.setEntity(entity);

				response = client.execute(post);
				////System.out.println("code = "+response.getStatusLine().getStatusCode());
				// 判断是否成功处理
				if (response.getStatusLine().getStatusCode() == 200) {

					return EntityUtils.toString(response.getEntity(), ConstantValue.CHARSET);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;

	} 
	
	
	public String sendProtocol(String uri, String xml){
		post = new HttpPost(uri);
		// 设置连接参数		
//		 第一个针对连接建立后，但是没有收到response的超时时间，测试时可将server simulator收到request后等一段时间后再回response。
//		 出错信息：
//		java.net.SocketTimeoutException: Read timed out
//		第二个针对连接建立的超时时间，测试时可将目的IP地址设为不存在的IP地址。
//		 出错信息：
//		org.apache.commons.httpclient.ConnectTimeoutException: The host did not accept the connection within timeout of 8000 ms
		 HttpParams httpParams = new BasicHttpParams();//
		 HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		 HttpConnectionParams.setSoTimeout(httpParams, 30000);
		 post.addHeader("Accept-Encoding", "gzip, deflate");		 
		 post.setParams(httpParams);
		try {
			 if(xml.length()>1000)
			 {
				post.addHeader("Content-Encoding", "gzip, deflate");
				ByteArrayEntity entity = new ByteArrayEntity(this.gzip(xml.getBytes("UTF-8")));
				post.setEntity(entity);
			 }else
			 {
					StringEntity entity = new StringEntity(xml,ConstantValue.CHARSET);
					post.setEntity(entity);				 
			 }
			response = client.execute(post);
			// 判断是否成功处理
			if (response.getStatusLine().getStatusCode() == 200)
			{
				Header header = response.getFirstHeader("Content-Encoding");
				if(header != null && header.getValue().equals("gzip"))
				{
					byte[] resultstream = EntityUtils.toByteArray(response.getEntity());
					resultstream =   this.unGZip(resultstream);
					return new String(resultstream,"UTF-8");					
				}
				else
					return EntityUtils.toString(response.getEntity(), ConstantValue.CHARSET);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}
	public byte[] gzip(byte[] bContent)
	{
	    byte[] data = new byte[MAXLENGTH];  
	    try  
	    {  
	    	ByteArrayOutputStream  out = new ByteArrayOutputStream();  
	    	GZIPOutputStream  gzip = new GZIPOutputStream (out);  
	        //DataInputStream objIn = new DataInputStream(pIn);
	    	gzip.write(bContent);
	    	gzip.close();
	        return out.toByteArray();  

	    }  
	    catch (Exception e)  
	    {  
	        e.printStackTrace();  
	    }
	    return null;		
	}
	public  byte[] zip(byte[] bContent) 
	{  

	    byte[] b = null;  
	    try  
	    {  
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
	        ZipOutputStream zip = new ZipOutputStream(bos);  
	        ZipEntry entry = new ZipEntry("zip");  
	        entry.setSize(bContent.length);  
	        zip.putNextEntry(entry);  
	        zip.write(bContent);  
	        zip.closeEntry();  
	        zip.close();  
	        b = bos.toByteArray();
	        bos.close();  
	    }  
	    catch (Exception ex)  
	    {  
	        ex.printStackTrace();  
	    }  
	    return b;  
	} 
	public  byte[] unZip(byte[] bContent)
	{
	    byte[] b = null;
	    try  
	    {  
	        ByteArrayInputStream bis = new ByteArrayInputStream(bContent);  
	        ZipInputStream zip = new ZipInputStream(bis);  
	        while (zip.getNextEntry() != null)  
	        {  
	            byte[] buf = new byte[1024];  
	            int num = -1;  
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	            while ((num = zip.read(buf, 0, buf.length)) != -1)  
	            {  
	                baos.write(buf, 0, num);  
	            }  
	            b = baos.toByteArray();  
	            baos.flush();  
	            baos.close();  
	        }  
	        zip.close();  
	        bis.close();  
	    }  
	    catch (Exception ex)  
	    {  
	        ex.printStackTrace();  
	    }  
	    return b;  
	}  
	public  byte[] unGZip(byte[] bContent)  
	{  

	    byte[] data = new byte[MAXLENGTH];  
	    try  
	    {  
	        ByteArrayInputStream in = new ByteArrayInputStream(bContent);  
	        GZIPInputStream pIn = new GZIPInputStream(in);  
	        DataInputStream objIn = new DataInputStream(pIn);  

	        int len = 0;  
	        int count = 0;  
	        while ((count = objIn.read(data, len, len + BUFFERSIZE)) != -1)  
	        {  
	            len = len + count;  
	        }  

	        byte[] trueData = new byte[len];  
	        System.arraycopy(data, 0, trueData, 0, len);  

	        objIn.close();  
	        pIn.close();  
	        in.close();  

	        return trueData;  

	    }  
	    catch (Exception e)  
	    {  
	        e.printStackTrace();  
	    }
	    return null;
	} 	
}
