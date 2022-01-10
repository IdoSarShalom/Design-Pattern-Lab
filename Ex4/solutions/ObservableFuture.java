package test;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Future;

public class ObservableFuture<V> extends Observable implements Observer{
	
	private V value; 
	private Future<V> f; 
	
	public ObservableFuture(Future<V> f) {
		this.f = f;
	}
	
	public V get(){
		return this.value;
	}
	
	public void set(V v) {
		if(this.value != v) {
			this.value = v; 			
			this.setChanged();
			this.notifyObservers();	
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		ObservableFuture of1 = (ObservableFuture)o;
		this.set((V) of1.get()); //waits for get() to return 
	}	
}

