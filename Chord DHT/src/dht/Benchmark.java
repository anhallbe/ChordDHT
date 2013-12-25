package dht;

import java.rmi.RemoteException;

public class Benchmark {
	@SuppressWarnings("unchecked")
	public static void bench(int n) {
		try {
			long start, end;
			DHT<String> dht = new NodeImpl<>("table");
			System.out.println("Creating " + n + " nodes.");
			start = System.currentTimeMillis();
			for(int i=0; i<n; i++) {
				new NodeImpl<>("table"+i, (Node<String>) dht);
			}
			end = System.currentTimeMillis();
			System.out.println("Time: " + (int)(end-start));
			
			System.out.println("Updating routing tables...");
			start = System.currentTimeMillis();
			dht.updateRouting();
			end = System.currentTimeMillis();
			System.out.println("Time: " + (int)(end-start));
			
			System.out.println("Adding " + n + " values.");
			start = System.currentTimeMillis();
			for(int i=0; i<n; i++) {
				dht.put("key"+i, "some object lalalalala");
			}
			end = System.currentTimeMillis();
			System.out.println("Time: " + (int)(end-start));
			
			System.out.println("Fetching " + n + " values.");
			start = System.currentTimeMillis();
			for(int i=0; i<n; i++) {
				dht.get("key"+i);
			}
			end = System.currentTimeMillis();
			System.out.println("Time: " + (int)(end-start));
			
			
			System.out.println("Listing " + n + " values.");
			start = System.currentTimeMillis();
//			for(String s : dht.listAll())
//				System.out.print(s);
			dht.listAll();
			end = System.currentTimeMillis();
			System.out.println("Time: " + (int)(end-start));
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
