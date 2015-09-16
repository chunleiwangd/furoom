package test.testService;

import gpps.service.ITempDebtService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class TestTempDebtReturn {
	static String SPRINGCONFIGPATH="/src/main/webapp/WEB-INF/spring/root-context.xml";
	protected static ApplicationContext context =new FileSystemXmlApplicationContext(SPRINGCONFIGPATH);
	static ITempDebtService tempDebtService = context.getBean(ITempDebtService.class);
	public static void main(String args[]) throws Exception{
		try{
			tempDebtService.returnMoney(13402, 13400);
			tempDebtService.returnMoney(13402, 13400);
			tempDebtService.returnMoney(13402, 13400);
			tempDebtService.returnMoney(13402, 13400);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.exit(0);
	}
}
