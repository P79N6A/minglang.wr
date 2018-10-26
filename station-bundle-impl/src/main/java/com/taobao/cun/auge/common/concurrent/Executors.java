package com.taobao.cun.auge.common.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.AtomicLongMap;

/**
 * 重新实现Executors中的部分方法，增加自定义DefaultThreadFactory
 * 
 * @author chengyu.zhoucy
 *
 */
public class Executors {
	/**
	 * 固定线程数的ExecutorService
	 * 
	 * @param nThreads
	 * @param threadName
	 * @return
	 */
	public static ExecutorService newFixedThreadPool(int nThreads, String threadName) {
		return new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), 
                new DefaultThreadFactory(threadName));
	}
	
	static class DefaultThreadFactory implements ThreadFactory {
		
        private final static AtomicLongMap<String> ATOMICLONGMAP = AtomicLongMap.create();
        
        private String threadName;
        
        public DefaultThreadFactory(String threadName) {
        	this.threadName = threadName;
        }
        
        @Override
        public Thread newThread(final Runnable r) {
            return new Thread(r, threadName + ATOMICLONGMAP.getAndIncrement(threadName));
        }
    }
}
