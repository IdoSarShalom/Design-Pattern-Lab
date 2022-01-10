package test;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.io.*;
import java.lang.Thread;

public class MainTrain {

	public static void main(String[] args) {

		// ## Mission 1 - Test ##

//		IOSearcher ios = new IOSearcher();
//		Result res = ios.search("and", "C:\\Users\\Ido\\eclipse-workspace\\DP_Lab1\\src\\test\\main_folder");
//		String str = res.getQuery();
//		Map<String, Set<String>> hashMap = res.getAnswer();
//
//		System.out.println("The query string: " + str);
//
//		for (String key : hashMap.keySet()) {
//			Set<String> value = hashMap.get(key);
//			System.out.println(key + " " + value.toString());
//		}

		// ## Mission 2 - Test ##

//		ParallelIOSearcher pio = new ParallelIOSearcher();
//		
//		String query = "and", rootPath = "C:\\Users\\Ido\\eclipse-workspace\\DP_Lab1\\src\\test\\main_folder" ; 
//				
//		Result res = pio.search(query, rootPath);
//		Map<String, Set<String>> hashMap1 = res.getAnswer();
//		String str = res.getQuery();
//		
//		System.out.println("The query string: " + str);
//
//		for (String key : hashMap1.keySet()) {
//			Set<String> value = hashMap1.get(key);
//			System.out.println(key + " " + value.toString());
//		}
//		
//		System.out.println("\n");
//		
//		query = "boy" ; 
//		res = pio.search(query, rootPath);
//		Map<String, Set<String>> hashMap2 = res.getAnswer();
//
//		str = res.getQuery();
//		System.out.println("The query string: " + str);
//
//		
//		for (String key : hashMap2.keySet()) {
//			Set<String> value = hashMap2.get(key);
//			System.out.println(key + " " + value.toString());
//		}
//		
//		
//		System.out.println("\n");
//		
//		for (String key : hashMap1.keySet()) {
//			Set<String> value = hashMap1.get(key);
//			System.out.println(key + " " + value.toString());
//		}

		// ## Mission 3 - Test ##

//		CacheIOSearcher ts = new CacheIOSearcher(new IOSearcher());
//
//		Result res = ts.search("and", "C:\\Users\\Ido\\eclipse-workspace\\DP_Lab1\\src\\test\\main_folder");
//
//		System.out.println(CacheIOSearcher.calls);
//
//		res = ts.search("and", "C:\\Users\\Ido\\eclipse-workspace\\DP_Lab1\\src\\test\\main_folder");
//
//		System.out.println(CacheIOSearcher.calls);
//
//		res = ts.search("boy", "C:\\Users\\Ido\\eclipse-workspace\\DP_Lab1\\src\\test\\main_folder");
//
//		System.out.println(CacheIOSearcher.calls);
//
//		Set<Result> s = ts.getCachedResults();
//
//		for (Result resElement : s) {
//			// Implementing for loop
//			System.out.println("The query string: " + resElement.getQuery());
//			Map<String, Set<String>> hashMap = resElement.getAnswer();
//			
//			for (String key : hashMap.keySet()) {
//				Set<String> value = hashMap.get(key);
//				System.out.println(key + " " + value.toString());
//			}
//		}
//
//	Result arrRes[] = s.toArray();
//
//	Iterator itr = s.iterator();while(itr.hasNext())
//	{
//
//		System.out.println(itr.next());
//
//	}

//		String str = res.getQuery();
//		Map<String, Set<String>> hashMap = res.getAnswer();
//
//		System.out.println("The query string: " + str);
//
//		for (String key : hashMap.keySet()) {
//			Set<String> value = hashMap.get(key);
//			System.out.println(key + " " + value.toString());
//		}

	}

}