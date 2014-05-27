package app.caronacomunitaria.br.crypto;

import java.security.MessageDigest;

public class Hash {
	public static String gerarHash(String s, String algoritmo) {

		MessageDigest md;

		try {
			md = MessageDigest.getInstance(algoritmo);
			byte[] hash = md.digest(s.getBytes("UTF-8"));
			StringBuilder hexString = new StringBuilder(2);
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			
			return hexString.toString();
			
			
		} catch (Exception e) {
			return null;
		}
		
	}
}
