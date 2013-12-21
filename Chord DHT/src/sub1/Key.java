package sub1;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Key {
	
	public static String generate(String name, int space) {
		String sha1 = sha1(name);
		return sha1.substring(0, space);
	}
	
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
