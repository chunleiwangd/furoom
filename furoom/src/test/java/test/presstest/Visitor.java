package test.presstest;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

public class Visitor implements Runnable {
	Counter counter = null;
	String url = null;
	String param = null;
	long createtime = 0;
	int index=0;
	public Visitor(Counter counter, String url, String param, int index){
		this.counter = counter;
		this.url = url;
		this.param = param;
		this.index = index;
		createtime = System.currentTimeMillis();
	}	
	@Override
	public void run() {
		
		try{
		long runstarttime = System.currentTimeMillis();
		long waittime = runstarttime-createtime;
		counter.totalWaitTime.addAndGet(waittime);
		if(waittime>counter.maxWaitTime.longValue()){
			counter.maxWaitTime.set(waittime);
		}
		if(waittime<counter.minWaitTime.longValue()){
			counter.minWaitTime.set(waittime);
		}
		
		int size = sendPost(url, param, index);
		counter.totalsize.addAndGet(size);
		
		if(counter.total.intValue()%100==0)
		System.out.println("线程"+index+"执行完毕,完成的总线程数："+counter.total.intValue());
		long runendtime = System.currentTimeMillis();
		long runtime = runendtime-runstarttime;
		counter.totalHanleTime.addAndGet(runtime);
		if(runtime>counter.maxHandleTime.longValue()){
			counter.maxHandleTime.set(runtime);
		}
		if(runtime<counter.minHandleTime.longValue()){
			counter.minHandleTime.set(runtime);
		}
		}catch(Throwable e){
			counter.error.addAndGet(1);
		}finally{
			counter.total.addAndGet(1);
		}
		
	}
	

	    /**
	     * 向指定 URL 发送POST方法的请求
	     * 
	     * @param url
	     *            发送请求的 URL
	     * @param param
	     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	     * @return 所代表远程资源的响应结果
	     */
	    public static int sendPost(String url, String param, int index) throws Throwable{
	        PrintWriter out = null;
	        BufferedReader in = null;
	        String result = "";
	        try {
	            URL realUrl = new URL(url);
	            // 打开和URL之间的连接
	            URLConnection conn = realUrl.openConnection();
	            // 设置通用的请求属性
	            conn.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	            conn.setRequestProperty("connection", "Keep-Alive");
	            conn.setRequestProperty("Content-Type", "x-application/fr:ws-json-http");
	            conn.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            // 发送POST请求必须设置如下两行
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            // 获取URLConnection对象对应的输出流
	            out = new PrintWriter(conn.getOutputStream());
	            // 发送请求参数
	            out.print(param);
	            // flush输出流的缓冲
	            out.flush();
	            
	            
	            InputStream ins = conn.getInputStream();
	            ByteArrayOutputStream baout = new ByteArrayOutputStream();
	            byte[] buffer = new byte[1024];
	            int i = 0;
	            while((i=ins.read(buffer))!=-1){
	            	baout.write(buffer, 0, i);
	            }
	            baout.flush();
	            int size = baout.size();
	            return size;
	            
	            // 定义BufferedReader输入流来读取URL的响应
//	            GZIPInputStream gzipIn = new GZIPInputStream(conn.getInputStream());
//	            ByteArrayOutputStream baout = new ByteArrayOutputStream();
//	            byte[] buffer = new byte[1024];
//	            int i = 0;
//	            while((i=gzipIn.read(buffer))!=-1){
//	            	baout.write(buffer, 0, i);
//	            }
//	            baout.flush();
//	            result = baout.toString("utf-8");
//	            
//	            gzipIn.close();
//	            baout.close();
	           
	        } catch (Throwable e) {
	        	System.out.println(e.getMessage());
	            throw e;
	        }
	        //使用finally块来关闭输出流、输入流
	        finally{
	            try{
	                if(out!=null){
	                    out.close();
	                }
	                if(in!=null){
	                    in.close();
	                }
	            }
	            catch(IOException ex){
	                ex.printStackTrace();
	            }
	        }
//	        return result;
	    }
	    
	    public static void main(String args[]) throws Throwable{
	    	int size = sendPost("http://123.57.6.110/furoom/com.furoom.remote.batch.IEasyBatchService", "{_i_:0,id:\"1438098021458_1_id\",method:\"batchCall_1\",params:[[{_t_:\"com.furoom.remote.batch.SingleRequest\",_i_:1,service:\"gpps.cache.service.ICacheService\",method:\"findAllSeries\",args:null},{_t_:\"com.furoom.remote.batch.SingleRequest\",_i_:2,service:\"gpps.cache.service.ICacheService\",method:\"getTotalStatistics\",args:null},{_t_:\"com.furoom.remote.batch.SingleRequest\",_i_:3,service:\"gpps.cache.service.ICacheService\",method:\"findAllNotice\",args:null},{_t_:\"com.furoom.remote.batch.SingleRequest\",_i_:4,service:\"gpps.cache.service.ICacheService\",method:\"findAllNews\",args:null},{_t_:\"com.furoom.remote.batch.SingleRequest\",_i_:5,service:\"gpps.cache.service.ICacheService\",method:\"findProductByStates\",args:null},{_t_:\"com.furoom.remote.batch.SingleRequest\",_i_:6,service:\"gpps.cache.service.ICacheService\",method:\"findNewLenderProductByStates\",args:null}]]}",1);
	    	System.out.println(size);
	    }

}
