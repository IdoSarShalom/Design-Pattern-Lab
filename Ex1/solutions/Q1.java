package test;

import java.util.Iterator;
import java.util.LinkedList;

public class Q1 {

	public class Ring<E> extends LinkedList<E>{

		public class RingIterator implements Iterator<E>{
			
			private Iterator<E> it;
			
			public RingIterator() {
				Q1.Ring.RingIterator.this.it = Q1.Ring.this.listIterator();
			}
			
			@Override
			public boolean hasNext() {
				return true; // RingIterator always has next node 
			}

			@Override
			public E next() {
				if(it.hasNext())
					return it.next();
				// End of list, return the curser to the first element
				Q1.Ring.RingIterator.this.it = Q1.Ring.this.listIterator(); 
				return it.next();
			}

		} // end of inner RingIterator class
		
		@Override
		public Iterator<E> iterator(){
			return new RingIterator();
		}
	
	}
	
	public void APItest(){
		Ring<String> r=new Ring<>();
		r.add("a");
		r.add("b");
		r.add("c");
		r.add("d");
		Iterator<String> it=r.iterator();
		for(int i=0;i<r.size()+2;i++)
			System.out.print(it.next()+",");
		System.out.println(); // a,b,c,d,a,b,
	}
	
}
