package test.test1;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import weixin.service.IBindService;
import weixin.service.ICentralService;

public class Test {
	static String SPRINGCONFIGPATH="/src/main/webapp/WEB-INF/spring/root-context.xml";
	protected static ApplicationContext context =new FileSystemXmlApplicationContext(SPRINGCONFIGPATH);
	static ICentralService central = context.getBean(ICentralService.class);
	static IBindService bindService = context.getBean(IBindService.class);
	public static void main(String args[]) throws Exception{
		System.out.println(central.getToken());
		
		System.exit(0);
//		Map<String, Object> res = bindService.getCurrentUser("oL_gTswxLjhXj30coE4-3ALIjGHw");
//		
//		System.out.println(res.get("usertype"));
	}
}
