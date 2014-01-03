package dht;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Provides some static methods to generate/compare keys.
 * @author Andreas
 *
 */
public class Key {
	
	/**
	 * Generate a key based on the given name and key-space. Depending on the key-space, this key may or may not be unique.
	 * @param name
	 * @param space
	 * @return
	 */
	public static String generate(String name, int space) {
		String sha1 = sha1(name);
		int characters = (int) (Math.log(space)/Math.log(2));
		characters = Math.min(characters, sha1.length());
//		System.out.println("sha1.length() = " + sha1.length() + ", sha1=" + sha1 + " characters=" + characters);
		return sha1.substring(sha1.length()-characters-1, sha1.length());
//		return sha1.substring(0, characters);
	}
	
	/**
	 * Decide whether the key k is between f and t in the ring.
	 * @param k - Key
	 * @param f - From
	 * @param t - To
	 * @return true if k is in the interval (f, t]
	 */
	public static boolean between(String k, String f, String t) {
		float key = Float.parseFloat(k);
		float from = Float.parseFloat(f);
		float to = Float.parseFloat(t);
		
		if(from > to) {
			return key > from || key <= to;
		}else if(from < to)
			return key > from && key <= to;
		else
			return true;		
	}
	
	/**
	 * Generate a SHA1-hash of the String s.
	 * @param s
	 * @return a String representing the SHA1-hash in base 2.
	 */
	private static String sha1(String s) {
		String sha1 = null;
		try {
			MessageDigest mDigest = MessageDigest.getInstance("SHA1");
			byte[] result = mDigest.digest(s.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < result.length; i++) {
				sb.append(Integer.toString(result[i], 2).substring(1));
			}
			sha1 = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sha1;
	}
}
