package dht;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Benchmark {
	@SuppressWarnings("unchecked")
	public static void bench(int n) {
		try {
			long start, end;
			DHT<String> dht = new NodeImpl<>("table");
			
			System.out.println("Creating " + n + " nodes.");
			start = System.currentTimeMillis();
			List<DHT<String>> nodes = createNodes(n, (Node<String>) dht);
			end = System.currentTimeMillis();
			System.out.println("Time: " + (int)(end-start));
			
			System.out.println("Putting " + n + " values.");
			start = System.currentTimeMillis();
			List<String> keys = benchPut(nodes);
			end = System.currentTimeMillis();
			System.out.println("Time: " + (int)(end-start));
			
			System.out.println("Getting " + keys.size() + " random values from DHT");
			start = System.currentTimeMillis();
			benchGet(nodes, keys);
			end = System.currentTimeMillis();
			System.out.println("Time: " + (int)(end-start));
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private static List<DHT<String>> createNodes(int n, final Node<String> dht) throws RemoteException {
		final List<DHT<String>> nodes = new CopyOnWriteArrayList<>();
		for(int i=0; i<n; i++) {
			final int r = i;
			new Thread() {
				public void run() {					
					try {
						nodes.add(new NodeImpl<>("node"+r, dht));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
		return nodes;
	}
	
	private static List<String> benchPut(List<DHT<String>> dhts) {
		Random rand = new Random();
		final List<String> keys = new CopyOnWriteArrayList<>();
		for(final DHT<String> dht : dhts) {
			final String key = "key" + rand.nextInt();
			new Thread(){
				public void run() {					
					dht.put(key, "v");
					keys.add(key);
				}
			}.start();
		}
		return keys;
	}
	
	private static void benchGet(List<DHT<String>> dhts, List<String> keys) {
		Random rand = new Random();
		for(final DHT<String> dht : dhts) {
			final String key = keys.get(rand.nextInt(keys.size()));
			new Thread() {
				public void run() {					
					dht.get(key);
				}
			}.start();
		}
	}
}
