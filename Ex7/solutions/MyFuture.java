package test;

import java.util.function.Consumer;
import java.util.function.Function;

public class MyFuture<V> {
	private V v;
	private Runnable r;
//	private volatile boolean start; 
	
	public MyFuture() {}
	
	// implement set()
	public void set(V v){
		this.v = v;	
		r.run();
	}
	
	// implement thenDo()
	public <R> MyFuture<R> thenDo(Function<V, R> func){
		
		MyFuture<R> mfr = new MyFuture<>();  
		this.r = ()->mfr.set(func.apply(this.v));
		return mfr;
		
	}
	
	// implement finallyDo()
	public void finallyDo(Consumer<V> c) {
		this.r = ()->c.accept(this.v);
	}
	
}
