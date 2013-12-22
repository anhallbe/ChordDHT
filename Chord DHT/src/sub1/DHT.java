package sub1;

/**
 * A simple interface used for HashTable-like storage.
 * @author Andreas
 *
 */
public interface DHT {
	/**
	 * Get an object that is mapped to the key.
	 * @param key
	 * @return
	 */
	public Object get(String key);
	
	/**
	 * Put a new object in the DHT, the key can be any string.
	 * @param key
	 * @param object
	 */
	public void put(String key, Object object);
	
	/**
	 * Remove a key/object mapping if it exists.
	 * @param key
	 */
	public void remove(String key);
	
	/**
	 * Leave the current network (if any) and join other.
	 * @param other
	 */
	public void join(Node other);
	
	/**
	 * Leave the current network.
	 */
	public void leave();
}
