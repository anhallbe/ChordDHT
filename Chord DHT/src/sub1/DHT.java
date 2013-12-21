package sub1;

public interface DHT {
	public Object get(String key);
	public void put(String key, Object object);
	public void remove(String key);
}
