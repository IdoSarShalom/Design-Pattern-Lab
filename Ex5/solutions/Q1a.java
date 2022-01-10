package test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Q1a<V> {
	
	public interface FunctionalInterface<V> {
		public V apply();
	}
		
	ExecutorService es;
	
	public Q1a() {
		es=Executors.newSingleThreadExecutor();
	}

	
	public void close(){
		es.shutdown();
	}
	
	// implement threadIt() method here
	
	public Future<V> threadIt(FunctionalInterface<V> fi) {
		
		return es.submit(()->{			
			return fi.apply();		
		});
		
	}
	
}
