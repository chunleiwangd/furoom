package test.presstest;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Counter {
	public AtomicInteger total = new AtomicInteger(0);
	public AtomicLong totalHanleTime = new AtomicLong(0);
	public AtomicLong totalWaitTime = new AtomicLong(0);
	public AtomicLong maxWaitTime = new AtomicLong(0);
	public AtomicLong minWaitTime = new AtomicLong(0);
	public AtomicLong maxHandleTime = new AtomicLong(0);
	public AtomicLong minHandleTime = new AtomicLong(0);
	public AtomicInteger error = new AtomicInteger(0);
	
	public AtomicLong totalsize = new AtomicLong(0);
}
