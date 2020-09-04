package com.imooc.o2o.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.imooc.o2o.util.wechat.MyX509TrustManager;
@Component
public class GetShortAddressUsingWechatApiUtil {
	private static String appId;

	private static String appSecret;

	@Value("${wechat.appid}")
	public void setAppId(String appId) {
		GetShortAddressUsingWechatApiUtil.appId = appId;
	}

	@Value("${wechat.appsecret}")
	public void setAppSecret(String appSecret) {
		GetShortAddressUsingWechatApiUtil.appSecret = appSecret;
	}

	public static String wxLongUrl2Short(String longUrl) throws Exception {
		String uri = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=ACCESS_TOKEN";
		String accessToken = getAccessToken();
		String url = uri.replace("ACCESS_TOKEN", accessToken);
		String param = "{\"action\":\"long2short\"," + "\"long_url\":\"" + longUrl + "\"}";
		// 调用接口创建菜单
		JSONObject jsonObject = httpRequest(url, "POST", param);
		System.out.println(jsonObject);
		if (null != jsonObject) {
			return jsonObject.getString("short_url");
		} else {
			return null;
		}

	}

	public static String getAccessToken() {
		String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId
				+ "&secret=" + appSecret;
		System.out.println("accessTokenUrl ----> " + accessTokenUrl);
		JSONObject res = httpRequest(accessTokenUrl, "GET", null);
		System.out.println("res ------> " + res);
		return res.getString("access_token");
	}

	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		HttpsURLConnection httpUrlConn = null;
		OutputStream outputStream = null;
		InputStream inputStream = null;

		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		// 创建SSLContext对象，并使用我们指定的信任管理器初始化
		TrustManager[] tm = { new MyX509TrustManager() };
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			jsonObject = JSONObject.parseObject(buffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			inputStream = null;
			if (httpUrlConn != null) {
				httpUrlConn.disconnect();
			}
		}
		return jsonObject;
	}
}
