package weixin.service.impl;

import gpps.service.thirdpay.IHttpClientService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import weixin.service.Const;
import weixin.service.ICentralService;

@Service
public class CentralServiceImpl implements ICentralService {
	String token = "";
	@Autowired
	IHttpClientService httpClientService;
	
	@Override
	public String getToken() throws Exception{
		
		if(token==null || token.equals("")){
			refreshToken();
		}
		return token;
	}
	
	@Override
	public String getOpenId(String code) throws Exception{
		StringBuilder url = new StringBuilder("https://api.weixin.qq.com/sns/oauth2/access_token");
		url.append("?").append("appid=").append(Const.APPID).append("&").append("secret=").append(Const.APPSECRET)
		.append("&").append("code=").append(code)
		.append("&grant_type=authorization_code");
		String res = httpClientService.post(url.toString(), new HashMap<String, String>());
		
		
		Gson gson = new Gson();
		Map returnParams=gson.fromJson(res, Map.class);
		
		
		Object openid = returnParams.get("openid");
		if(openid==null){
			throw new Exception("获取openid失败！");
		}
		
		return openid.toString();
	}
	
	
	

	@Override
	public String refreshToken() throws Exception{
		
		String url = "https://api.weixin.qq.com/cgi-bin/token";
		Map<String, String> params = new HashMap<String, String>();
		params.put("grant_type", "client_credential");
		params.put("appid", Const.APPID);
		params.put("secret", Const.APPSECRET);
		String res = httpClientService.post(url, params);
		
		Gson gson = new Gson();
		Map returnParams=gson.fromJson(res, Map.class);
		
		Object token = returnParams.get("access_token");
		if(token==null){
			throw new Exception("获取TOKEN失败！");
		}
		this.token = token.toString();
		return token.toString();
	}
	
	@Override
	public String getUserInfo(String openId) throws Exception{
		String token = getToken();
		StringBuilder url = new StringBuilder("https://api.weixin.qq.com/cgi-bin/user/info?access_token=").append(token).append("&openid=").append(openId).append("&lang=zh_CN");
		try{
			Gson gson = new Gson();
			//自动、需要审核转账(Action=2  NeedAudit=null)，只包含一个json，记录了具体的转账信息
			String body = httpClientService.post(url.toString(), new HashMap<String, String>());
			Map returnParams=gson.fromJson(body, Map.class);
			
			Object nickname = returnParams.get("nickname");
			
			if(nickname!=null){
				return nickname.toString();
			}
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
		
		throw new Exception("用户尚未关注，拉取不到用户信息");
	}

}
