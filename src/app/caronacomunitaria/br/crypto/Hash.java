package app.caronacomunitaria.br.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
	public static String gerarHash(byte[] s, String algoritmo) {

		MessageDigest md;

		try {
			md = MessageDigest.getInstance(algoritmo);
			md.update(s);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return new String(md.digest());
	}
}
