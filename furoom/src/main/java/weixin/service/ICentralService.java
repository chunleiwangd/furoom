package weixin.service;

public interface ICentralService {
	public String getToken() throws Exception;
	public String refreshToken() throws Exception;
	public String getOpenId(String code) throws Exception;
	public String getUserInfo(String openId) throws Exception;
}
