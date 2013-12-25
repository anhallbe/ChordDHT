package sub1;

import java.rmi.RemoteException;

public class TestRouting {
	
	@SuppressWarnings("unchecked")
	public TestRouting() throws RemoteException {
		DHT<String> dht = new NodeImpl<>("n1");
		new NodeImpl<String>("s2", (Node<String>) dht);
		new NodeImpl<String>("s3", (Node<String>) dht);
		new NodeImpl<String>("s4", (Node<String>) dht);
		new NodeImpl<String>("s5", (Node<String>) dht);
		new NodeImpl<String>("s6", (Node<String>) dht);
		new NodeImpl<String>("s7", (Node<String>) dht);
		new NodeImpl<String>("s8", (Node<String>) dht);
		new NodeImpl<String>("s9", (Node<String>) dht);
		new NodeImpl<String>("s10", (Node<String>) dht);
		new NodeImpl<String>("s11", (Node<String>) dht);
		new NodeImpl<String>("s12", (Node<String>) dht);
		new NodeImpl<String>("s13", (Node<String>) dht);
		new NodeImpl<String>("s14", (Node<String>) dht);
		new NodeImpl<String>("s15", (Node<String>) dht);
		new NodeImpl<String>("s16", (Node<String>) dht);
		new NodeImpl<String>("s17", (Node<String>) dht);
		new NodeImpl<String>("ss2", (Node<String>) dht);
		new NodeImpl<String>("s3s", (Node<String>) dht);
		new NodeImpl<String>("s4s", (Node<String>) dht);
		new NodeImpl<String>("ss5", (Node<String>) dht);
		new NodeImpl<String>("ss6", (Node<String>) dht);
		new NodeImpl<String>("ss7", (Node<String>) dht);
		new NodeImpl<String>("ss8", (Node<String>) dht);
		new NodeImpl<String>("ss9", (Node<String>) dht);
		new NodeImpl<String>("ss10", (Node<String>) dht);
		new NodeImpl<String>("ss11", (Node<String>) dht);
		new NodeImpl<String>("ss12", (Node<String>) dht);
		new NodeImpl<String>("ss13", (Node<String>) dht);
		new NodeImpl<String>("ss14", (Node<String>) dht);
		new NodeImpl<String>("ss15", (Node<String>) dht);
		new NodeImpl<String>("ss16", (Node<String>) dht);
		new NodeImpl<String>("ss17", (Node<String>) dht);
		
		dht.updateRouting();
		
	}
	
	public static void main(String[] args) {
		try {
			new TestRouting();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
