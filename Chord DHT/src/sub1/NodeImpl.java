package sub1;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class NodeImpl extends UnicastRemoteObject implements Node, DHT {

	private static final long serialVersionUID = 7837010474371220959L;
	
	private String name;
	private String key;
	private Node successor;
	private Node predecessor;
	private HashMap<String, Object> storage = new HashMap<>();
	
	private final static int N = 10;
	
	public NodeImpl(String name) throws RemoteException {
		this.name = name;
		key = Key.generate(name, N);
		successor = this;
		predecessor = this;
//		System.out.println(name + ": My key is " + key);
	}
	
	public NodeImpl(String name, Node other) throws RemoteException {
		this(name);
		join(other);
	}
	
	private void join(Node other) throws RemoteException {
		boolean joined = false;
		while(!joined) {
			Node pred = other.getPredecessor();
			String otherKey = other.getKey();
			String predKey = pred.getKey();
			
			if(Key.between(key, predKey, otherKey)) {
				pred.setSuccessor(this);
				other.setPredecessor(this);
				setSuccessor(other);
				setPredecessor(pred);
				joined = true;
//				System.out.println(name + ": Joined in between " + pred + " and " + other);
			} else
				other = other.getSuccessor();
		}
	}
	
	private void leave() throws RemoteException {
		successor.setPredecessor(predecessor);
		predecessor.setSuccessor(successor);
		successor = this;
		predecessor = this;
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
	public Node getSuccessor() throws RemoteException {
		return successor;
	}

	@Override
	public Node getPredecessor() throws RemoteException {
		return predecessor;
	}

	@Override
	public void setSuccessor(Node succ) throws RemoteException {
		successor = succ;
	}

	@Override
	public void setPredecessor(Node pred) throws RemoteException {
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
	public Node lookup(String key) throws RemoteException {
		String predKey = predecessor.getKey();
		if(Key.between(key, predKey, this.key)) {
			return this;
		} else {
			return successor.lookup(key);
		}
	}
	
	@Override
	public Object getStored(String key) throws RemoteException {
		return storage.get(key);
	}
	
	@Override
	public void addStored(String key, Object value) throws RemoteException {
		storage.put(key, value);
	}
	
	@Override
	public void removeStored(String key) throws RemoteException {
		storage.remove(key);
	}

	@Override
	public Object get(String key) {
		try {
			String k = Key.generate(key, N);
			Node node = lookup(k);
//			System.out.println(name + ": " + node + " have that.");
			return node.getStored(k);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void put(String key, Object object) {
		try {
			String k = Key.generate(key, N);
			Node node = lookup(k);
//			System.out.println(name + ": " + node + " wants that.");
			node.addStored(k, object);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void remove(String key) {
		try {
			String k = Key.generate(key, N);
			Node node = lookup(k);
//			System.out.println(name + ": " + node + " had that.");
			node.removeStored(k);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
