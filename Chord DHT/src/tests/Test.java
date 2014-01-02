package tests;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import dht.DHT;
import dht.Node;
import dht.NodeImpl;

public class Test {
	
	@SuppressWarnings("unchecked")
	public Test() throws RemoteException, NotBoundException {
		DHT<String> dht = new NodeImpl<>("a");
		dht.put("asd", "asd");
		dht.put("asd1", "asd");
		dht.put("asd2", "asd");
		dht.put("asd3", "asd");
		new NodeImpl<String>("b", (Node<String>)dht);
		new NodeImpl<String>("c", (Node<String>)dht);
		new NodeImpl<String>("d", (Node<String>)dht);
		new NodeImpl<String>("e", (Node<String>)dht);
		new NodeImpl<String>("f", (Node<String>)dht);
		new NodeImpl<String>("g", (Node<String>)dht);
		new NodeImpl<String>("h", (Node<String>)dht);
		new NodeImpl<String>("i", (Node<String>)dht);
		new NodeImpl<String>("j", (Node<String>)dht);
		new NodeImpl<String>("k", (Node<String>)dht);
		new NodeImpl<String>("l", (Node<String>)dht);
		new NodeImpl<String>("m", (Node<String>)dht);
		new NodeImpl<String>("n", (Node<String>)dht);
		new NodeImpl<String>("o", (Node<String>)dht);
		new NodeImpl<String>("p", (Node<String>)dht);
		dht.updateRouting();
		
//		DHT dht = new NodeImpl("c", "192.168.2.2", 1099, "a");
		System.out.println("Interface: ");
		System.out.println("put key value");
		System.out.println("get key");
		System.out.println("remove key");
		System.out.println("bench n");
		System.out.println("list");
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
				Benchmark.benchConcurrency(Integer.parseInt(inputs[1]));
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
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
}
