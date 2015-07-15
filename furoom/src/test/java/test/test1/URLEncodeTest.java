package test.test1;

import java.net.URLEncoder;

import org.apache.http.client.utils.URLEncodedUtils;

public class URLEncodeTest {
	public static void main(String args[]) throws Exception{
		String url = URLEncoder.encode("http://www.zhengcaidai.com/weixin_bind.html");
		System.out.println(url);
	}
}
