package gpps.cache.service;

import java.util.List;
import java.util.Map;

import gpps.model.Product;
import gpps.model.ProductSeries;
import gpps.service.TotalStatistics;

public interface ICacheService {
	public static final int TOTALSTATISTICS = 0;
	public static final int ALLSERIES = 1;
	public static final int ALLNOTICE = 2;
	public static final int ALLNEWS = 3;
	public static final int PRODUCTS = 4;
	public static final int HELP = 5;
	
	
	
	
	public TotalStatistics getTotalStatistics();
	public List<ProductSeries> findAllSeries();
	public Map<String,Object> findAllNotice();
	public Map<String,Object> findAllNews();
	
	public List<Product> findProductByStates();
	public List<Product> findNewLenderProductByStates();
	
	public Map<String, Object> findPublicHelps(int type);
	
	public void clear(int type);
	public void clearAll();
}
