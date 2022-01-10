package test;

import java.util.Iterator;


import test.Q1.Ring;


public class MainTrain {

	public static void main(String[] args) {		
		test2a();
		test2b();
		System.out.println("done");
	}


	private static void test2a() { 
		Q1 q=new Q1();
		Ring<Integer> r=q.new Ring<>();
		r.add(1);
		r.add(2);
		r.add(3);
		r.add(4);
		Iterator<Integer> it=r.iterator();
		int sum=0;
		for(int i=0;i<r.size()+2;i++)
			sum+=it.next();
		if(sum!=13)
			System.out.println("Q1 wrong implementation of the Iterator (-50)");
	}
	
	private static void test2b() { 
		Q1 q=new Q1();
		Ring<Integer> r=q.new Ring<>();
		r.add(1);
		r.add(2);
		r.add(3);
		r.add(4);
		Iterator<Integer> it=r.iterator();
		int sum=0;
		for(int i=0;i<r.size()*5;i++)
			sum+=it.next();
		if(sum!=50)
			System.out.println("Q1 wrong implementation of the Iterator (-50)");
	}


}
