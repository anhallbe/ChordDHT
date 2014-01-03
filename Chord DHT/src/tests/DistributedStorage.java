package tests;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import dht.DHT;
import dht.Node;
import dht.NodeImpl;

/**
 * This class presents a user interface that lets the user put/get/list/remove entries in a DHT.
 * @author Andreas
 *
 */
public class DistributedStorage {
	
	/**
	 * Reference to a Distributed Hash Table.
	 */
	private DHT<String> dht;
	
	/**
	 * Constructor used to create a local network of nodes.
	 * @param name
	 */
	@SuppressWarnings("unchecked")
	public DistributedStorage(String name) {
		try {
			dht = new NodeImpl<>(name);
			new NodeImpl<>("a", (Node<String>)dht);
			new NodeImpl<>("b", (Node<String>)dht);
			new NodeImpl<>("c", (Node<String>)dht);
			new NodeImpl<>("d", (Node<String>)dht);
			userInterface();
		} catch (RemoteException e) {
			System.err.println("Shit's not working, yo. (RemoteException)");
//			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor used to join an existing DHT.
	 * @param name - The local name of this table/node.
	 * @param rName - Remote name of another table (RMI registry name)
	 * @param host - Remote host of the table.
	 * @param port - Port used for RMI connection.
	 */
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
	
	/**
	 * A simple console-based UI to perform some operations on the DHT.
	 */
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
					Benchmark.bench(Integer.parseInt(inputs[1]), Integer.parseInt(inputs[2]));
					break;
				case "benchc":
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
					System.out.println("bench nodes values");
					System.out.println("benchc nodes");
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
	
	/**
	 * Create/join a DHT and let the user interact with it.
	 * @param args - A single argument [name] will create a local dht. Arguments [localname, remotename, remotehost, remoteport] is used to connect to an existing dht.
	 */
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
