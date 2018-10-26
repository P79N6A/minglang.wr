package com.taobao.cun.auge.common.concurrent;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import org.junit.Test;

public class ExecutorsTest {
	@Test
	public void testA() throws IOException {
		ExecutorService ex = Executors.newFixedThreadPool(2, "test");
		
		ex.execute(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("hello1");
				try {
					Thread.sleep(100000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		ex.execute(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("hello2");
				try {
					Thread.sleep(100000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		System.in.read();
	}
}
