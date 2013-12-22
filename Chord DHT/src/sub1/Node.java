package sub1;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface to let Nodes interact with each other.
 * @author Andreas
 *
 */
public interface Node extends Remote {
	
	public String getKey() throws RemoteException;
	public Node getSuccessor() throws RemoteException;
	public Node getPredecessor() throws RemoteException;
	public void setSuccessor(Node succ) throws RemoteException;
	public void setPredecessor(Node pred) throws RemoteException;
	public void probe(String key, int count) throws RemoteException;
	public Node lookup(String key) throws RemoteException;
	public Object getStored(String key) throws RemoteException;
	public void addStored(String key, Object value) throws RemoteException;
	public void removeStored(String key) throws RemoteException;
}
