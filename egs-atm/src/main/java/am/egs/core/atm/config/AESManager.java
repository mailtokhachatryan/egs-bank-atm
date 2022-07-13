package am.egs.core.atm.config;

import org.springframework.core.codec.EncodingException;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class AESManager {

    public final int AES_KEY_SIZE = 256;
    public final int GCM_TAG_LENGTH = 16;
    public final int GCM_IV_LENGTH = 12;
    private final byte[] IV;

    public AESManager() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(AES_KEY_SIZE);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        IV = new byte[GCM_IV_LENGTH];
    }

    public String encrypt(String plaintext, String secret) {
        try {
            byte[] secretBytes = Base64.getDecoder().decode(secret);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(secretBytes, "AES");
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);
            byte[] cipherText = cipher.doFinal(plaintext.getBytes());
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            throw new EncodingException("Encoding failed!");
        }
    }
}
