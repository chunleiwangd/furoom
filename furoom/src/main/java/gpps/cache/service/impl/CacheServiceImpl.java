package gpps.cache.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import gpps.cache.service.ICacheService;
import gpps.model.Borrower;
import gpps.model.Lender;
import gpps.model.Notice;
import gpps.model.Product;
import gpps.model.ProductSeries;
import gpps.service.IHelpService;
import gpps.service.ILoginService;
import gpps.service.INewsService;
import gpps.service.INoticeService;
import gpps.service.IProductSeriesService;
import gpps.service.IProductService;
import gpps.service.IStatisticsService;
import gpps.service.TotalStatistics;
@Service
public class CacheServiceImpl implements ICacheService {
	@Autowired
	IStatisticsService statisticsService;
	@Autowired
	IProductSeriesService seriesService;
	@Autowired
	INoticeService noticeService;
	@Autowired
	INewsService newsService;
	@Autowired
	IProductService productService;
	@Autowired
	IHelpService helpService;
	
	Logger logger = Logger.getLogger(CacheServiceImpl.class);
	
	private TotalStatistics totalStatistics=null;
	private List<ProductSeries> productSeries = new ArrayList<ProductSeries>();
	private Map<Integer, Map<String,Object>> notices = new HashMap<Integer, Map<String,Object>>();
	private Map<String, Map<String, Object>> news = new HashMap<String, Map<String,Object>>();
	private Map<String, List<Product>> products = new HashMap<String, List<Product>>();
	public Map<String, Map<String, Object>> helps = new HashMap<String, Map<String,Object>>();
	
	
	@PostConstruct
	public void init() {
		//构建守护线程，检查待支付Submit，到一定时间未支付成功设置为“退订”
		Thread taskThread=new Thread(){
			public void run()
			{
				logger.info("启动Cache定时更新线程");
				int i = 0;
				while(true)
				{
					i++;
					if(i>=30){
						clearAll();
						i=0;
					}else{
						clear(TOTALSTATISTICS);
						clear(PRODUCTS);
					}
					try {
						sleep(60L*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		taskThread.setName("CacheThread");
		taskThread.start();
	}
	
	
	
	
	
	@Override
	public void clear(int type){
		switch(type){
		case TOTALSTATISTICS:
			totalStatistics = null;
			break;
		case ALLSERIES:
			productSeries.clear();
			productSeries = new ArrayList<ProductSeries>();
			break;
		case ALLNOTICE:
			notices.clear();
			notices = new HashMap<Integer, Map<String,Object>>();
			break;
		case ALLNEWS:
			news.clear();
			news = new HashMap<String, Map<String,Object>>();
			break;
		case PRODUCTS:
			products.clear();
			products = new HashMap<String, List<Product>>();
			break;
		case HELP:
			helps.clear();
			helps = new HashMap<String, Map<String,Object>>();
			break;
		}
			
	}
	
	@Override
	public void clearAll(){
		totalStatistics = null;
		
		productSeries.clear();
		productSeries = new ArrayList<ProductSeries>();
		
		notices.clear();
		notices = new HashMap<Integer, Map<String,Object>>();
		
		news.clear();
		news = new HashMap<String, Map<String,Object>>();
		
		products.clear();
		products = new HashMap<String, List<Product>>();
		
		helps.clear();
		helps = new HashMap<String, Map<String,Object>>();
	}
	
	
	private void reload(){
		totalStatistics = statisticsService.getTotalStatistics();
		productSeries = seriesService.findAll();
		
		notices.clear();
		notices = new HashMap<Integer, Map<String,Object>>();
		
		Map<String, Object> ns = newsService.findAll(-1, 0, 6);
		news.put("news", ns);
		
		Map<String, Object> helps_1 = helpService.findPublicHelps(-1, 0, 10);
		Map<String, Object> helps0 = helpService.findPublicHelps(0, 0, 10);
		Map<String, Object> helps1 = helpService.findPublicHelps(1, 0, 10);
		Map<String, Object> helps2 = helpService.findPublicHelps(2, 0, 10);
		Map<String, Object> helps3 = helpService.findPublicHelps(3, 0, 10);
		helps.put("-1", helps_1);
		helps.put("0", helps0);
		helps.put("1", helps1);
		helps.put("2", helps2);
		helps.put("3", helps3);
		
		List<Product> nmpros = productService.findByStates(11, 0, 5);
		products.put("nmproduct", nmpros);
		
		List<Product> nlpros = productService.findNewLenderProductByStates(11, 0, 5);
		products.put("nlproduct", nlpros);
	}
	
	@Override
	public TotalStatistics getTotalStatistics() {
		if(totalStatistics!=null){
			return totalStatistics;
		}else{
			totalStatistics = statisticsService.getTotalStatistics();
			return totalStatistics;
		}
	}

	@Override
	public List<ProductSeries> findAllSeries() {
		if(!productSeries.isEmpty()){
			return productSeries;
		}else{
			productSeries = seriesService.findAll();
			return productSeries;
		}
	}

	@Override
	public Map<String, Object> findAllNotice() {
		int level=-1;
		int usertype = 0;
		
		HttpSession session=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		Object user=session.getAttribute(ILoginService.SESSION_ATTRIBUTENAME_USER);
		if(user==null)
		{
			level=-1;
			usertype = Notice.USEFOR_ALL;
		}
		else if(user instanceof Lender)
		{
			level=((Lender)user).getLevel();
			usertype = Notice.USEFOR_LENDER;
		}else if(user instanceof Borrower)
		{
			level=((Borrower)user).getLevel();
			usertype = Notice.USEFOR_BORROWER;
		}
		
		int key = usertype*100+level;
		if(notices.containsKey(key)){
			return notices.get(key);
		}else{
			Map<String, Object> nots = noticeService.findAll(0, 0, 6);
			notices.put(key, nots);
			return nots;
		}
	}

	@Override
	public Map<String, Object> findAllNews() {
		if(news.containsKey("news")){
			return news.get("news");
		}else{
			Map<String, Object> ns = newsService.findAll(-1, 0, 6);
			news.put("news", ns);
			return ns;
		}
	}
	
	@Override
	public Map<String, Object> findPublicHelps(int type){
		if(helps.containsKey(type+"")){
			return helps.get(type+"");
		}else{
			Map<String, Object> hps = helpService.findPublicHelps(type, 0, 10);
			helps.put(type+"", hps);
			return hps;
		}
	}

	@Override
	public List<Product> findProductByStates() {
		if(products.containsKey("nmproduct")){
			return products.get("nmproduct");
		}else{
			List<Product> pros = productService.findByStates(11, 0, 5);
			products.put("nmproduct", pros);
			return pros;
		}
	}

	@Override
	public List<Product> findNewLenderProductByStates() {
		if(products.containsKey("nlproduct")){
			return products.get("nlproduct");
		}else{
			List<Product> pros = productService.findNewLenderProductByStates(11, 0, 5);
			products.put("nlproduct", pros);
			return pros;
		}
	}

}
