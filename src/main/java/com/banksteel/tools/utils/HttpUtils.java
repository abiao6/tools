package com.banksteel.tools.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {
	private final static Logger log = LoggerFactory.getLogger(HttpUtils.class);
	public final static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36";
	@SuppressWarnings("deprecation")
	private final static CloseableHttpClient httpClient = HttpClients.custom().setUserAgent(USER_AGENT).setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY).setSocketTimeout(5*1000).setConnectTimeout(5*1000).build()).build();
	
	static final String latestPriceUrl = "http://hq.sinajs.cn/list={0}";
	public static String getTheLatestPrice(String code){
		String requestUrl = MessageFormat.format(latestPriceUrl, code);
		String responseContent = HttpUtils.getRequestData(requestUrl);
		log.debug("###:::"+responseContent);
		String data[] = responseContent.split("=")[1].split(",");
		String inc = new BigDecimal(data[3]).subtract(new BigDecimal(data[1])).toString();
		return data[3] + "/" + data[1] + "/" + data[2] + "/" + inc + "/"+new BigDecimal(inc).multiply(new BigDecimal(100)).divide(new BigDecimal(data[1]), RoundingMode.HALF_UP).toString()+"%";
	}

	public static String getRequestData(String urlStr) {
		String responseContent = null;
		
		HttpGet httpGet = null;
		CloseableHttpResponse httpResponse = null;
		try {
			URL url = new URL(urlStr);
			URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
			httpGet = new HttpGet(uri);//HTTP Get请求(POST雷同)
			
			try {
				httpResponse = httpClient.execute(httpGet);
				httpResponse.setHeader("Content-Type", "text/html; charset=utf-8");
				int rescode = httpResponse.getStatusLine().getStatusCode();
				if(rescode == HttpStatus.SC_OK){
					HttpEntity entity = httpResponse.getEntity();
			        if (entity != null) {
			        	responseContent = EntityUtils.toString(entity);
			        }
				} else {
					log.error(urlStr + " : status code is " + rescode);
				}
			} catch (ClientProtocolException e) {
				log.error(e.getMessage(), e);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			} finally {
				if(httpResponse != null){
					try {
						httpResponse.close();
					} catch (IOException e) {
						log.error(e.getMessage(), e);
					}
				}
			}
		} catch (MalformedURLException e) {
			log.error(e.getMessage(), e);
		} catch (URISyntaxException e) {
			log.error(e.getMessage(), e);
		}
		return responseContent;
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(getTheLatestPrice("sh600363"));
		System.out.println(getTheLatestPrice("sz002078"));
		System.out.println(getTheLatestPrice("sh600108"));
	}
}
