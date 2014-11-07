package com.brogrammers.agora.helper;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * We use an md5 hash function to generate the unique ids of the questions, and answers 
 * to retrieve the correct question/answer. This can be applied to any other data object.
 *  
 * More info about md5 hashing can be found here: (http://en.wikipedia.org/wiki/MD5)
 * @author Group02
 *
 */
public class md5 {
	static public Long hash(String s) {
		try {
			// make hash
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			
			return ByteBuffer.wrap(digest.digest()).getLong();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return 0L;
	}
}