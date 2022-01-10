package test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Q1b {
	
	private BlockingQueue<Runnable> q = new LinkedBlockingQueue<Runnable>();
	
	private volatile boolean stop = false;
	
	public Q1b() {
		
		new Thread(()->{
			while(!stop) {
				try {
					// take() blocks, so no busy waiting 
					q.take().run();
				} catch (InterruptedException e) {}			
			}
			
		}).start();
		
		
	}
	
	public void push(Runnable r){
		q.add(r);
	}
	
	public void close(){
		// add to the dispachQueue a terminator runnable 
		q.add(()->{
			this.stop = true;
		});
	
	}
}