package test;

import java.util.Iterator;
import java.util.LinkedList;

public class Q1 {

	public class Ring<E> extends LinkedList<E>{
		
		public class RingIterator ______________{

			_______________ it;
			public RingIterator() {
			}
			
			@Override
			public boolean hasNext() {
			}

			@Override
			public E next() {
			}
		} // end of inner RingIterator class
		
		@Override
		public Iterator<E> iterator(){
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
