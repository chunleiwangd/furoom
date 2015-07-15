package test.dao;

import gpps.dao.IBindingDao;
import gpps.model.Binding;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class BindingTest {
	static String SPRINGCONFIGPATH="/src/main/webapp/WEB-INF/spring/root-context.xml";
	protected static ApplicationContext context =new FileSystemXmlApplicationContext(SPRINGCONFIGPATH);
	static IBindingDao bindingDao = context.getBean(IBindingDao.class);
	public static void main(String args[]) throws Exception{
//		Binding binding = new Binding();
//		binding.setBtype(Binding.TYPE_OPENID);
//		binding.setCreatetime(System.currentTimeMillis());
//		binding.setExpiredtime(System.currentTimeMillis()+2L*30*24*3600*1000);
//		binding.setState(Binding.STATE_VALID);
//		binding.setTvalue("sadflajsdflksajdlfakjsdf");
//		binding.setUserid(12200);
//		bindingDao.create(binding);
		bindingDao.changeState(1, Binding.STATE_INVALID);
		System.exit(0);
	}
}
