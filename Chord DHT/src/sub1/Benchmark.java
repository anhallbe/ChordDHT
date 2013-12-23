package sub1;

import java.rmi.RemoteException;

public class Benchmark {
	public static void bench(int n) {
		try {
			long start, end;
			DHT dht = new NodeImpl("table");
			System.out.println("Creating " + n + " nodes.");
			start = System.currentTimeMillis();
			for(int i=0; i<n; i++) {
				new NodeImpl("table"+i, (Node) dht);
			}
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
			
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
