package org.example.szs.common.utils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AES256Util {
	private final SecretKeySpec secretKeySpec;
	private final Cipher cipher;

	public AES256Util(@Value("${app.secret-key}") String key) throws Exception {
		byte[] keyBytes = Arrays.copyOf(key.getBytes(StandardCharsets.UTF_8), 32);
		secretKeySpec = new SecretKeySpec(keyBytes, "AES");
		cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	}

	public String encrypt(String plainText) throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
	}

	public String decrypt(String cipherText) throws Exception {
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
		return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
	}
}
