package com.egs.core.bank.config;

import org.springframework.core.codec.DecodingException;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class AESComponent {

    public final int AES_KEY_SIZE = 256;
    public final int GCM_TAG_LENGTH = 16;
    public final int GCM_IV_LENGTH = 12;
    private final byte[] IV;

    public AESComponent() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(AES_KEY_SIZE);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        IV = new byte[GCM_IV_LENGTH];
    }

    public String decrypt(String cipherText, String secret) {
        try {
            byte[] cipherTextBytes = Base64.getDecoder().decode(cipherText);
            byte[] secretBytes = Base64.getDecoder().decode(secret);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(secretBytes, "AES");
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);
            byte[] decryptedText = cipher.doFinal(cipherTextBytes);
            return new String(decryptedText);
        } catch (Exception e) {
            throw new DecodingException("Decoding failed!");
        }
    }
}
