package test.presstest;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.dom4j.dtd.ElementDecl;

public class PressTest {
	public static void main(String args[]) throws Exception{
		ExecutorService executor = Executors.newFixedThreadPool(75);
		String url = "http://127.0.0.1:8080/furoom/com.furoom.remote.batch.IEasyBatchService";
		String param = "{_i_:0,id:\"1438098021458_1_id\",method:\"batchCall_1\",params:[[{_t_:\"com.furoom.remote.batch.SingleRequest\",_i_:1,service:\"gpps.cache.service.ICacheService\",method:\"findAllSeries\",args:null},{_t_:\"com.furoom.remote.batch.SingleRequest\",_i_:2,service:\"gpps.cache.service.ICacheService\",method:\"getTotalStatistics\",args:null},{_t_:\"com.furoom.remote.batch.SingleRequest\",_i_:3,service:\"gpps.cache.service.ICacheService\",method:\"findAllNotice\",args:null},{_t_:\"com.furoom.remote.batch.SingleRequest\",_i_:4,service:\"gpps.cache.service.ICacheService\",method:\"findAllNews\",args:null},{_t_:\"com.furoom.remote.batch.SingleRequest\",_i_:5,service:\"gpps.cache.service.ICacheService\",method:\"findProductByStates\",args:null},{_t_:\"com.furoom.remote.batch.SingleRequest\",_i_:6,service:\"gpps.cache.service.ICacheService\",method:\"findNewLenderProductByStates\",args:null}]]}";
		String param2 = "{_i_:0,id:\"1438105996173_1_id\",method:\"batchCall_1\",params:[[{_t_:\"com.furoom.remote.batch.SingleRequest\",_i_:1,service:\"gpps.cache.service.ICacheService\",method:\"findAllSeries\",args:null},{_t_:\"com.furoom.remote.batch.SingleRequest\",_i_:2,service:\"gpps.cache.service.ICacheService\",method:\"findPublicHelps\",args:[1]},{_t_:\"com.furoom.remote.batch.SingleRequest\",_i_:3,service:\"gpps.service.IProductService\",method:\"find\",args:[3]},{_t_:\"com.furoom.remote.batch.SingleRequest\",_i_:4,service:\"gpps.service.IGovermentOrderService\",method:\"findGovermentOrderByProduct\",args:[3]},{_t_:\"com.furoom.remote.batch.SingleRequest\",_i_:5,service:\"gpps.service.IBorrowerService\",method:\"findByProductId\",args:[3]},{_t_:\"com.furoom.remote.batch.SingleRequest\",_i_:6,service:\"gpps.service.ISubmitService\",method:\"findPayedSubmitsByProduct\",args:[3,0,5]},{_t_:\"com.furoom.remote.batch.SingleRequest\",_i_:7,service:\"gpps.service.IPayBackService\",method:\"findAll\",args:[3]},{_t_:\"com.furoom.remote.batch.SingleRequest\",_i_:8,service:\"gpps.service.ISubscribeService\",method:\"countSubscribe\",args:[3,-1]}]]}";
		Counter counter = new Counter();
		long starttime = System.currentTimeMillis();
		for(int i=0; i<5000; i++){
			if(i%2==0){
				executor.execute(new Visitor(counter, url, param,i));
			}else{
				executor.execute(new Visitor(counter, url, param2,i));
			}
//			int sleepi = (new Random()).nextInt(50);
//				try{
//					Thread.sleep(sleepi);
//				}catch(Exception e){
//					
//				}
		}
		while(true){
			if(counter.total.intValue()>=4900){
				
				long endtime = System.currentTimeMillis();
				
				
				System.out.println("执行完毕！");
				System.out.println("在"+(endtime-starttime)+"毫秒内");
				System.out.println("总线程数："+counter.total.intValue());
				System.out.println("错误数："+counter.error.intValue());
				System.out.println("累计等待调度时间："+counter.totalWaitTime.longValue()+"ms");
				System.out.println("平均待调度时间："+counter.totalWaitTime.longValue()/(counter.total.intValue()-counter.error.intValue())+"ms");
				System.out.println("最大待调度时间："+counter.maxWaitTime.longValue()+"ms");
				System.out.println("最小待调度时间："+counter.minWaitTime.longValue()+"ms");
				System.out.println("累计执行时间："+counter.totalHanleTime.longValue()+"ms");
				System.out.println("平均执行时间："+counter.totalHanleTime.longValue()/(counter.total.intValue()-counter.error.intValue())+"ms");
				System.out.println("最大执行时间："+counter.maxHandleTime.longValue()+"ms");
				System.out.println("最小执行时间："+counter.minHandleTime.longValue()+"ms");
				
				System.out.println("总共传输："+counter.totalsize.longValue()+"B");
				executor.shutdown();
				break;
			}
			try{
				Thread.sleep(100);
			}catch(Exception e){
				
			}
		}
	}
}
