package dht;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class DistributedStorage {
	
	private DHT<String> dht;
	
	public DistributedStorage(String name) {
		try {
			dht = new NodeImpl<>(name);
			userInterface();
		} catch (RemoteException e) {
			System.err.println("Shit's not working, yo. (RemoteException)");
//			e.printStackTrace();
		}
	}
	
	public DistributedStorage(String name, String rName, String host, int port) {
		try {
			dht = new NodeImpl<>(name, host, port, rName);
			userInterface();
		} catch (RemoteException e) {
			System.err.println("RemoteException");
//			e.printStackTrace();
		} catch (NotBoundException e) {
			System.err.println("RMI Registry error.");
//			e.printStackTrace();
		}
	}
	
	private void userInterface() {
		Scanner scan = new Scanner(System.in);
		String input = scan.nextLine();
		while(!input.equals("exit")) {
			try {
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
				case "list":
					System.out.println("Items:");
					for(String s : dht.listAll())
						System.out.println(s);
					break;
				default:
					System.out.println("Usage:");
					System.out.println("put key value");
					System.out.println("get key");
					System.out.println("remove key");
					System.out.println("bench");
					System.out.println("list");
					System.out.println("exit");
				}
			} catch (Exception e) {
				System.out.println("Something went wrong.. a bad input?");
			}
			input = scan.nextLine();
		}
		dht.leave();
		scan.close();
	}
	
	public static void main(String[] args) {
		if(args.length == 1) {
			String name = args[0];
			new DistributedStorage(name);
		}
		else if(args.length == 4) {
			String name = args[0];
			String rName = args[1];
			String rHost = args[2];
			int port = Integer.parseInt(args[3]);
			new DistributedStorage(name, rName, rHost, port);
		}
		else
			System.out.println("Expected arguments: [localname] or [localname, remotename, remotehost, remoteport]");
	}
}
