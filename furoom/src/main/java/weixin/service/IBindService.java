package weixin.service;

import java.util.Map;


public interface IBindService {
	public static final long MESSAGEVALIDATECODEEXPIRETIME=20*60*1000;//短信验证码有效时间:20分钟
	public static final long MESSAGEVALIDATECODEINTERVAL=5*60*1000;//获取短信验证码间隔时间:5分钟
	public static final String SESSION_ATTRIBUTENAME_MESSAGEVALIDATECODESENDTIME="messageValidateCodeSendTime";//短信验证码发送时间在Session中的KEY常量，value为long类型
	public static final String SESSION_ATTRIBUTENAME_MESSAGEVALIDATECODE="messageValidateCode";//短信验证码在Session中的KEY常量
	public static final String SESSION_ATTRIBUTENAME_GRAPHVALIDATECODE="graphValidateCode";//图形验证码在Session中的KEY常量
	public static final String PASSWORDSEED="PASSWORDSEED";
	
	public boolean isBind(String userid);
	
	public void bind(String username, String password, String userid, String validate) throws Exception;
	public void unbind(String userid);
	
	public String getAccountMessage(String userid) throws Exception;
	
	public Map<String, Object> getCurrentUser(String userid) throws Exception;
}
