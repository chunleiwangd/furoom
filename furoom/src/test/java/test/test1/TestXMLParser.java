package test.test1;

import weixin.model.Message;

import com.furoom.xml.EasyObjectXMLTransformerImpl;
import com.furoom.xml.IEasyObjectXMLTransformer;

public class TestXMLParser {
	private static final IEasyObjectXMLTransformer xmlTransformer=new EasyObjectXMLTransformerImpl(); 
	public static void main(String args[]) throws Exception{
		Message mess = xmlTransformer.parse(TestXMLParser.class.getResourceAsStream("test.xml"), Message.class);
		System.out.println(mess.getMsgType());
		
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
		template = template.replaceAll("template_content", "你好啊小伙："+mess.getFromUserName());
		System.out.println(template);
	}
}
