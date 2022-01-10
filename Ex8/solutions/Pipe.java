package test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Pipe<T> implements Stoppable{

	// implement add(), filter(), map() , forEach()
	
	private BlockingQueue<T> q;
	private Thread mainLoop;
	private volatile boolean stop; 
	public Consumer<T> cons;
	private Pipe<T> next;
	
	public Pipe() {
		this.stop = false;
		this.q = new ArrayBlockingQueue<>(100);
		this.mainLoop = new Thread(()->{
			while(!stop) {
				
				try {
					
					T t = this.q.take(); 
					if(this.cons != null)
						cons.accept(t);
					
				}catch(InterruptedException e) {}
						
			}
			
		});
		this.mainLoop.start();
	}
	
	public void add(T t) {
		try {
			this.q.put(t);
		} catch (InterruptedException e) {}
	}
	
	// Do not tuch ! 
	public Pipe<T> filter(Predicate<T> p){
		this.next = new Pipe<>();
		this.cons=(T t)->{
			if(p.test(t))
				this.next.add(t);	
		};
		return next;
	}
	
	// and stop
	@Override
	public void stop() {
		
		this.stop = true; 
		this.mainLoop.interrupt();
		if(this.next != null)
			this.next.stop();

	}

	public void forEach(Consumer<T> c) {		
		this.cons= c;
	}

	@SuppressWarnings("unchecked")
	public <R> Pipe<R> map(Function<T, R> func) {

		Pipe<R> r = new Pipe<>();
		
		this.cons=(t)->{
				r.add(func.apply(t));	
		};
		this.next = (Pipe<T>) r;
		
		return r;
	}
}
