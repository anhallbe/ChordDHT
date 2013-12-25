package sub1;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The implementation of a node/peer in the distributed hash table.
 * Interfaces for usage and node-to-node communication are implemented.
 * @author Andreas
 * @param <E> is the type of value that is stored in the table.
 *
 */
public class NodeImpl<E> extends UnicastRemoteObject implements Node<E>, DHT<E> {

	private static final long serialVersionUID = 7837010474371220959L;
	
	private String name;
	private String key;
	private Node<E> successor;
	private Node<E> predecessor;
	private HashMap<String, E> storage = new HashMap<>();
	private Map<String, Node<E>> fingers = new LinkedHashMap<>();
	
	private static final int N = 1048576;
	public static final int DEFAULT_PORT = 1099;
	
	/**
	 * Constructor to initiate a single node.
	 * @param name
	 * @throws RemoteException
	 */
	public NodeImpl(String name) throws RemoteException {
		this.name = name;
		key = Key.generate(name, N);
		//System.out.println("Generated key (N=" + N + ": " + key);
		successor = this;
		predecessor = this;
		Registry registry;
		try {
			registry = LocateRegistry.createRegistry(DEFAULT_PORT);
//			System.out.println("Create Registry");
		} catch (Exception e) {
			registry = LocateRegistry.getRegistry(DEFAULT_PORT);
//			System.out.println("Get registry.");
		}
		registry.rebind(name, this);
	}
	
	/**
	 * Constructor used to join another node (can be remote).
	 * @param name
	 * @param other
	 * @throws RemoteException
	 */
	public NodeImpl(String name, Node<E> other) throws RemoteException {
		this(name);
		join(other);
	}
	
	/**
	 * Constructor used to join a Node on a remote network.
	 * @param name
	 * @param host
	 * @param port
	 * @param ohterName
	 * @throws RemoteException
	 * @throws NotBoundException 
	 */
	@SuppressWarnings("unchecked")
	public NodeImpl(String name, String host, int port, String ohterName) throws RemoteException, NotBoundException {
		this(name);
		Registry registry = LocateRegistry.getRegistry(host, port);
		Node<E> other = (Node<E>) registry.lookup(ohterName);
		join(other);
	}
	
	@Override
	public void join(Node<E> other) {
		try {
		boolean joined = false;
			while(!joined) {
				Node<E> pred = other.getPredecessor();
				String otherKey = other.getKey();
				String predKey = pred.getKey();
				
				if(Key.between(key, predKey, otherKey)) {
					pred.setSuccessor(this);
					other.setPredecessor(this);
					setSuccessor(other);
					setPredecessor(pred);
					joined = true;
				} else
					other = other.getSuccessor();
			}
			updateRouting();
		} catch(RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void leave() {
		try {
			successor.setPredecessor(predecessor);
			predecessor.setSuccessor(successor);
			successor = this;
			predecessor = this;
			updateRouting();
		} catch(RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public String getKey() throws RemoteException {
		return key;
	}

	@Override
	public Node<E> getSuccessor() throws RemoteException {
		return successor;
	}

	@Override
	public Node<E> getPredecessor() throws RemoteException {
		return predecessor;
	}

	@Override
	public void setSuccessor(Node<E> succ) throws RemoteException {
		successor = succ;
	}

	@Override
	public void setPredecessor(Node<E> pred) throws RemoteException {
		predecessor = pred;
	}

	@Override
	public void probe(String key, int count) throws RemoteException {
		if(this.key.equals(key) && count > 0) {
			System.out.println("Probe returned after " + count + " hops.");
		} else {
			System.out.println(name + ": Forwarding probe to " + successor);
			successor.probe(key, count+1);
		}
	}

	@Override
	public Node<E> lookup(String key) throws RemoteException {
		String predKey = predecessor.getKey();
		if(Key.between(key, predKey, getKey()))
			return this;
		else if(fingers.keySet().size() < 3) {
			return successor.lookup(key);
		}
		else {
			String[] keys = {};
			keys = fingers.keySet().toArray(keys);
			for(int i=0; i<(keys.length-1); i++) {
				String currentKey = keys[i];
				String nextKey = keys[i+1];
				if(Key.between(key, currentKey, nextKey)) {
					Node<E> currentNode = fingers.get(currentKey);
					Node<E> node = currentNode.getSuccessor();
					return node.lookup(key);
				}
			}
			return fingers.get(keys[keys.length-1]).getSuccessor().lookup(key);
		}
		
	}
	
	@Override
	public E getStored(String key) throws RemoteException {
		return storage.get(key);
	}
	
	@Override
	public void addStored(String key, E value) throws RemoteException {
		storage.put(key, value);
	}
	
	@Override
	public void removeStored(String key) throws RemoteException {
		storage.remove(key);
	}

	@Override
	public E get(String key) {
		try {
			String k = Key.generate(key, N);
			Node<E> node = lookup(k);
			System.out.println("get " + node.getStored(k) + " from " + node);
			return node.getStored(k);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void put(String key, E object) {
		try {
			String k = Key.generate(key, N);
			Node<E> node = lookup(k);
			node.addStored(k, object);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void remove(String key) {
		try {
			String k = Key.generate(key, N);
			Node<E> node = lookup(k);
			node.removeStored(k);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<String> listAll() {
		Node<E> currentNode = this;
		ArrayList<String> all = new ArrayList<>();
		try {
			do {
				for(E element : currentNode.getValues())
					all.add(element.toString());
				currentNode = currentNode.getSuccessor();
			} while(currentNode != this);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return all;
	}

	@Override
	public Collection<E> getValues() throws RemoteException {
		return storage.values();
	}

	/**
	 * Get a list of all nodes in the network (Warning: May take some time, and consume a lot of resources!).
	 * @return a list of all nodes.
	 */
	private List<Node<E>> allNodes() {
		ArrayList<Node<E>> nodes = new ArrayList<>();
		try {
			Node<E> current = this;
			do {
				nodes.add(current);
				current = current.getSuccessor();
			} while(current != this);
		} catch(RemoteException e){
			System.err.println("Error finding all nodes!");
		}
		Collections.sort(nodes, new Comparator<Node<E>>() {
			@Override
			public int compare(Node<E> n1, Node<E> n2) {
				try {
					String key1 = n1.getKey();
					String key2 = n2.getKey();
					int i1 = Integer.parseInt(key1, 2);
					int i2 = Integer.parseInt(key2, 2);
					
					if(i1 > i2)
						return 1;
					if(i1 < i2)
						return -1;
					else
						return 0;
				}catch(RemoteException e) {
					return 0;
				}
			}
		});
		return nodes;
	}
	
	public void updateFingers(List<Node<E>> nodes) {
		Map<String, Node<E>> fingers = new LinkedHashMap<>();
		fingers.put(key, this);
		try {
			int myIndex = nodes.indexOf(this);
			
			for(int i=1; i<nodes.size(); i = i*2) {
				int nodeIndex = (myIndex + i) % nodes.size();
				Node<E> n = nodes.get(nodeIndex);
				fingers.put(n.getKey(), n);
			}
		} catch(RemoteException e) {
			e.printStackTrace();
		}
		this.fingers = fingers;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof NodeImpl<?>)
			try {
				return key.equals(((NodeImpl<?>) other).getKey());
			} catch (RemoteException e) {
				e.printStackTrace();
				return false;
			}
		else
			return false;
	}

	@Override
	public void updateRouting() {
		try {
			List<Node<E>> nodes = allNodes();
			for(Node<E> n : nodes)
				n.updateFingers(nodes);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
