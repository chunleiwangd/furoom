package weixin.servlet;

import gpps.inner.service.IInnerThirdPaySupportService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import weixin.model.Message;
import weixin.service.IBindService;
import weixin.service.ICentralService;
import weixin.service.IQueryForWeixinService;

import com.furoom.xml.EasyObjectXMLTransformerImpl;
import com.furoom.xml.IEasyObjectXMLTransformer;
import com.furoom.xml.XMLParseException;

@Controller
public class ConnectServlet {
	public static final String TOKEN = "furoom_gpps_chunlei";
	private static Logger log = Logger.getLogger(ConnectServlet.class);
	private static final IEasyObjectXMLTransformer xmlTransformer=new EasyObjectXMLTransformerImpl(); 
	
	@Autowired
	IBindService bindService;
	
	@Autowired
	IQueryForWeixinService queryService;
	
	@Autowired
	IInnerThirdPaySupportService supportService;
	
	@Autowired
	ICentralService centralService;
	
	@RequestMapping(value = { "/weixin/connect" })
	public void completeThirdPartyRegist(HttpServletRequest req, HttpServletResponse resp) {
		try{
			String echostr = getParam(req, "echostr");
			String signature = getParam(req, "signature");
			String timestamp = getParam(req, "timestamp");
			String nonce = getParam(req, "nonce");
			
			if(check(timestamp, nonce, signature))
			{
				handleMessage(req, resp);
//				writeMessage(resp, echostr);
			}else{
				log.error("signature is wrong! "+timestamp+":"+nonce+":"+signature);
				writeMessage(resp, "");
			}
			
			
		}catch(Exception e){
			log.error(e.getMessage());
		}
	}
	
	private void handleMessage(HttpServletRequest req, HttpServletResponse resp) throws IOException, XMLParseException, Exception{
		
		Message mess = xmlTransformer.parse(req.getInputStream(), Message.class);
		
		log.info("receive a message-------------------------------");
		log.info("from: "+mess.getFromUserName());
		
		String MsgType = mess.getMsgType();
		
		if("text".equals(MsgType)){
			handleText(mess, resp);
		}else if("image".equals(MsgType)){
			handleImage(mess, resp);
		}else if("voice".equals(MsgType)){
			handleVoice(mess, resp);
		}else if("event".equals(MsgType)){
			
			log.info(MsgType+"::"+mess.getEvent()+"::"+mess.getEventKey());
			
			if("CLICK".equals(mess.getEvent())){
				if("key_001".equals(mess.getEventKey())){
					try{
						String message = bindService.getAccountMessage(mess.getFromUserName());
						sendMessage(mess, message, resp);
					}catch(Exception e){
						sendMessage(mess, e.getMessage(),resp);
					}
				}else if("key_004".equals(mess.getEventKey())){
					try{
						bindService.getAccountMessage(mess.getFromUserName());
						String text = "查看账户详情请[<a href='"+supportService.getReturnUrl()+"/weixin_myaccount.html?userid="+mess.getFromUserName()+"'>点击这里</a>]";
						sendMessage(mess, text, resp);
					}catch(Exception e){
						sendMessage(mess, e.getMessage(),resp);
					}
					
				}else if("key_005".equals(mess.getEventKey())){
					sendMessage(mess, "<a href='"+supportService.getReturnUrl()+"/productlist.html'>点击这里</a>",resp);
				}
				else if("key_002".equals(mess.getEventKey())){
					boolean isBind = bindService.isBind(mess.getFromUserName());
					if(isBind==false)
					{
						String res = picMessage("bind.xml", mess);
						writeMessage(resp, res);
					}else{
						sendMessage(mess, "您已绑定账户，请先解绑再重新绑定！",resp);
					}
				}
				else if("key_003".equals(mess.getEventKey())){
					try{
						bindService.unbind(mess.getFromUserName());
						sendMessage(mess, "您的账户已经解除绑定",resp);
					}catch(Exception e){
						sendMessage(mess, e.getMessage(),resp);
					}
				}else{
					sendMessage(mess, "哈哈哈",resp);
				}
			}else if("subscribe".equals(mess.getEvent())){
				StringBuilder sb = new StringBuilder();
				String name = centralService.getUserInfo(mess.getFromUserName());
				sb.append("尊敬的用户"+name+"，春蕾投资为您服务，点击【<a href='")
					.append(supportService.getReturnUrl()).append("/register.html?userid=")
					.append(mess.getFromUserName()).append("'>注册账户</a>】,输入下列指令可自助查看：\n");
				
				sb.append("【help】指令帮助\n");
				sendMessage(mess, sb.toString(), resp);
			}
			else if("VIEW".equals(mess.getEvent())){
//				try{
//					bindService.getCurrentUser(mess.getFromUserName());
//					log.info(mess.getFromUserName()+"登录成功！");
//				}catch(Exception e){
//					log.error(mess.getFromUserName()+"::"+e.getMessage());
//				}
			}
		}
		
		log.info("done-------------------------------");
	}
	
	private void handleContent(String content, Message mess, HttpServletResponse resp){
		String res = null;
		if(content.contains("账户") || content.contains("查询")){
			try{
				String message = bindService.getAccountMessage(mess.getFromUserName());
				sendMessage(mess, message, resp);
			}catch(Exception e){
				sendMessage(mess, e.getMessage(),resp);
			}
		}else if(content.contains("投资") || content.contains("图文")){
			res = picMessage("response.xml", mess);
			writeMessage(resp, res);
		}else if(content.equals("help")){
			StringBuilder sb = new StringBuilder();
			sb.append("【账户】查看我的账户信息").append("\n");
			sb.append("【投资】查看可投资信息").append("\n");
			sb.append("【1】查看我的投资").append("\n");
			sb.append("【2】查看我的已回款").append("\n");
			sb.append("【3】查看我的待回款").append("\n");
			sb.append("【4】关于我们").append("\n");
			sendMessage(mess, sb.toString(), resp);
		}
		else if(content.equals("1")){
			try{
				String message = queryService.getMySubmit(mess.getFromUserName());
				sendMessage(mess, message, resp);
			}catch(Exception e){
				sendMessage(mess, e.getMessage(),resp);
			}
		}else if(content.equals("2")){
			try{
				String message = queryService.getMyPayBack(mess.getFromUserName());
				sendMessage(mess, message, resp);
			}catch(Exception e){
				sendMessage(mess, e.getMessage(),resp);
			}
		}else if(content.equals("3")){
			try{
				String message = queryService.getMyPayBackTo(mess.getFromUserName());
				sendMessage(mess, message, resp);
			}catch(Exception e){
				sendMessage(mess, e.getMessage(),resp);
			}
		}else if(content.equals("4")){
			sendMessage(mess, "北京春蕾投资管理有限责任公司\n联系电话： 13811188888\n邮箱： wd123@qq.com", resp);
		}
		else{
			res = justTalk(mess, content);
			writeMessage(resp, res);
		}
	}
	
	
	private void handleText(Message mess, HttpServletResponse resp){
		String content = mess.getContent();
		handleContent(content, mess, resp);
	}
	
	private void handleImage(Message mess, HttpServletResponse resp){
		String res = justTalk(mess, "图片信息");
		writeMessage(resp, res);
	}
	
	private void handleVoice(Message mess, HttpServletResponse resp){
		String Recognition = mess.getRecognition();
		Recognition.replaceAll("一", "1");
		Recognition.replaceAll("二", "2");
		Recognition.replaceAll("三", "3");
		Recognition.replaceAll("四", "4");
		Recognition.replaceAll("五", "5");
		Recognition.replaceAll("六", "6");
		Recognition.replaceAll("七", "7");
		Recognition.replaceAll("八", "8");
		Recognition.replaceAll("九", "9");
		Recognition.replaceAll("十", "10");
		handleContent(Recognition, mess, resp);
	}
	
	
	
	
	
	private String queryAccount(Message mess){
		String template = "<xml>"+
				"<ToUserName><![CDATA[template_toUser]]></ToUserName>"+
				"<FromUserName><![CDATA[template_fromUser]]></FromUserName>"+
				"<CreateTime>template_createTime</CreateTime>"+
				"<MsgType><![CDATA[text]]></MsgType>"+
				"<Content><![CDATA[template_content]]></Content>"+
				"</xml>";
		template = template.replaceAll("template_toUser", mess.getFromUserName());
		template = template.replaceAll("template_fromUser", mess.getToUserName());
		template = template.replaceAll("template_createTime", System.currentTimeMillis()/1000+"");
		template = template.replaceAll("template_content", "你好啊小伙："+mess.getFromUserName()+"\n你的账户总额:20000元\n已投资:10000元\n可用金额:10000元");
		return template;
	}
	
	private void sendMessage(Message mess, String content, HttpServletResponse resp){
		String template = "<xml>"+
				"<ToUserName><![CDATA[template_toUser]]></ToUserName>"+
				"<FromUserName><![CDATA[template_fromUser]]></FromUserName>"+
				"<CreateTime>template_createTime</CreateTime>"+
				"<MsgType><![CDATA[text]]></MsgType>"+
				"<Content><![CDATA[template_content]]></Content>"+
				"</xml>";
		template = template.replaceAll("template_toUser", mess.getFromUserName());
		template = template.replaceAll("template_fromUser", mess.getToUserName());
		template = template.replaceAll("template_createTime", System.currentTimeMillis()/1000+"");
		template = template.replaceAll("template_content", content);
		writeMessage(resp, template);
	}
	
	
	private String justTalk(Message mess, String content){
		String template = "<xml>"+
				"<ToUserName><![CDATA[template_toUser]]></ToUserName>"+
				"<FromUserName><![CDATA[template_fromUser]]></FromUserName>"+
				"<CreateTime>template_createTime</CreateTime>"+
				"<MsgType><![CDATA[text]]></MsgType>"+
				"<Content><![CDATA[template_content]]></Content>"+
				"</xml>";
		template = template.replaceAll("template_toUser", mess.getFromUserName());
		template = template.replaceAll("template_fromUser", mess.getToUserName());
		template = template.replaceAll("template_createTime", System.currentTimeMillis()/1000+"");
		template = template.replaceAll("template_content", "你好啊小伙："+mess.getFromUserName()+",你发的消息是:"+content);
		return template;
	}
	
	
	private String picMessage(String file, Message mess){
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(ConnectServlet.class.getResourceAsStream(file)));
			String temp = br.readLine();
			StringBuilder sb = new StringBuilder();
			while(temp!=null){
				sb.append(temp);
				temp = br.readLine();
			}
			String template = sb.toString();
			template = template.replaceAll("template_toUser", mess.getFromUserName());
			template = template.replaceAll("template_fromUser", mess.getToUserName());
			template = template.replaceAll("template_createTime", System.currentTimeMillis()/1000+"");
			return template;
		}catch(Exception e){
			return "";
		}
	}
	
	
	
	
	@RequestMapping(value = { "/weixin/message" })
	public void getMessage(HttpServletRequest req, HttpServletResponse resp) {
		
		try{
			BufferedReader br = req.getReader();
			String temp = br.readLine();
			StringBuilder sb = new StringBuilder();
			while(temp!=null){
				sb.append(temp).append("\n");
				temp=br.readLine();
			}
			writeMessage(resp, sb.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	
	private String getParam(HttpServletRequest req, String key) throws UnsupportedEncodingException{
		req.setCharacterEncoding("UTF-8");
		Map m = req.getParameterMap();
		Object object = m.get(key);
		String value = object==null?"":((String[])object)[0];
		return value;
	}
	
	private boolean check(String timestamp, String nonce, String signature){
		String[] arr = new String[3];
		arr[0] = timestamp;
		arr[1] = nonce;
		arr[2] = TOKEN;
		Arrays.sort(arr);
		String temp = arr[0]+arr[1]+arr[2];
		String sha1 = DigestUtils.sha1Hex(temp);
		if(sha1.equals(signature)){
			return true;
		}else{
			return false;
		}
	}
	
	
	private void writeMessage(HttpServletResponse resp, String message)
	{
		resp.setCharacterEncoding("UTF-8");
		resp.setStatus(200);
		PrintWriter writer = null;
		try {
			writer = resp.getWriter();
			writer.write(message);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}
	}
}
