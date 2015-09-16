package test.view;

import java.util.Date;
import java.util.List;

import gpps.dao.IBorrowerDao;
import gpps.dao.IGovermentOrderDao;
import gpps.dao.IPayBackDao;
import gpps.dao.IProductDao;
import gpps.dao.IProductSeriesDao;
import gpps.model.Borrower;
import gpps.model.GovermentOrder;
import gpps.model.PayBack;
import gpps.model.Product;
import gpps.model.ProductSeries;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ViewPayback {
	static String SPRINGCONFIGPATH="/src/main/webapp/WEB-INF/spring/root-context.xml";
	protected static ApplicationContext context =new FileSystemXmlApplicationContext(SPRINGCONFIGPATH);
	static IPayBackDao paybackDao = context.getBean(IPayBackDao.class);
	static IProductDao productDao = context.getBean(IProductDao.class);
	static IGovermentOrderDao orderDao = context.getBean(IGovermentOrderDao.class);
	static IProductSeriesDao seriesDao = context.getBean(IProductSeriesDao.class);
	static IBorrowerDao borrowerDao = context.getBean(IBorrowerDao.class);
	public static void main(String args[]) throws Exception{
		List<PayBack> pbs = paybackDao.findByTimeAndState(1438358400879L, 1441036799739L, PayBack.STATE_FINISHREPAY);
		for(PayBack pb : pbs){
			Product product = productDao.find(pb.getProductId());
			GovermentOrder order = orderDao.find(product.getGovermentorderId());
			ProductSeries series = seriesDao.find(product.getProductseriesId());
			Borrower borrower = borrowerDao.find(order.getBorrowerId());
			Date date = new Date(pb.getRealtime());
			System.out.println(order.getFormalName()+"\t\t"+order.getTitle()+"["+series.getTitle()+"]"+"\t\t"+borrower.getCompanyName()+"\t\t"+pb.getChiefAmount().intValue()+"\t\t"+pb.getInterest().intValue()+"\t\t"+date.toLocaleString());
		}
		System.exit(0);
	}
}
