package sub1;

import java.rmi.RemoteException;
import java.util.Scanner;

public class Test {
	
	public Test() throws RemoteException {
		DHT dht = new NodeImpl("a");
		new NodeImpl("b", (Node)dht);
		System.out.println("Interface: ");
		System.out.println("put key value");
		System.out.println("get key");
		System.out.println("remove key");
		System.out.println("bench n");
		Scanner scan = new Scanner(System.in);
		String input = scan.nextLine();
		while(!input.equals("exit")) {
			String[] inputs = input.split(" ");
			switch(inputs[0]) {
			case "put":
				String key = inputs[1];
				String value = inputs[2];
				dht.put(key, value);
				break;
			case "get":
				key = inputs[1];
				System.out.println("Got from dht: " + dht.get(key));
				break;
			case "remove":
				key = inputs[1];
				dht.remove(key);
				System.out.println("Removed " + key);
				break;
			case "bench":
				Benchmark.bench(Integer.parseInt(inputs[1]));
				break;
			default:
				System.out.println("Usage:");
				System.out.println("put key value");
				System.out.println("get key");
				System.out.println("remove key");
				System.out.println("bench");
			}
			input = scan.nextLine();
		}
		scan.close();
	}
	
	public static void main(String[] args) {
		try {
			new Test();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
