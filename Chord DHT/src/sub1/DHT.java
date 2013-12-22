package sub1;

/**
 * A simple interface used for HashTable-like storage.
 * @author Andreas
 *
 */
public interface DHT {
	public Object get(String key);
	public void put(String key, Object object);
	public void remove(String key);
}
