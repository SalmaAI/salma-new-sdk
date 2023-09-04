package ai.mawdoo3.salma.utils.security;



import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;

import ai.mawdoo3.salma.R;


public final class RSASecurityWithSymmetric {

    private static final String RSA_ALGORITHM = "RSA";
    private static final String RSA_TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private static final String AES_ALGORITHM = "AES";
    private final Context context;
    private Cipher encryptCipher;
    private Cipher encryptKeyCipher;
    private boolean encryptOperationInitiated;
    private static KeyGenerator keyGen;

    public RSASecurityWithSymmetric(Context context){
        this.context = context;
        init();
    }

    public void init() {
        try {
            encryptCipher = Cipher.getInstance(RSA_TRANSFORMATION);
            encryptCipher.init(Cipher.ENCRYPT_MODE, getPublicKey(), new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT));
            encryptKeyCipher = Cipher.getInstance(AES_ALGORITHM);

        } catch (Exception e) {
            System.exit(0);
        }
        encryptOperationInitiated = true;
    }

    private PublicKey getPublicKey() throws Exception {
        InputStream in = context.getAssets().open("encrypt_payload_cert.pem");
        //InputStream in = this.getClass().getClassLoader().getResourceAsStream(file);
        int size = in.available();
        byte[] buffer = new byte[size];
        in.read(buffer);
        String key = new String(buffer);
        //String key = new String(readBytesFromFile(keyPath), Charset.defaultCharset());
        String publicKeyPEM = key.replace("-----BEGIN PUBLIC KEY-----", "").replaceAll(System.lineSeparator(), "").replace("-----END PUBLIC KEY-----", "");
        byte[] encoded = Base64.decodeBase64(publicKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return keyFactory.generatePublic(keySpec);
    }

    public String encryptKey(byte[] key) throws IllegalBlockSizeException, BadPaddingException {
        byte[] keyEncrypted = encryptCipher.doFinal(key);
        return Base64.encodeBase64String(keyEncrypted).replaceAll(System.lineSeparator(), "");
    }

    public String encrypt(String message, byte[] key) throws IllegalBlockSizeException, BadPaddingException, IllegalAccessException, InvalidKeyException {
        checkInit();
        SecretKey originalKey = new SecretKeySpec(key, 0, key.length, "AES");
        encryptKeyCipher.init(Cipher.ENCRYPT_MODE, originalKey);
        byte[] cipherText = encryptKeyCipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(cipherText).replaceAll(System.lineSeparator(), "");
    }

    private void checkInit() throws IllegalAccessException {
        if (!encryptOperationInitiated)
            throw new IllegalAccessException("Encryption is not initiated");
    }

    public byte[] getEncodedKey() throws NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
        if (null == keyGen) {
            keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
        }
        Key key = keyGen.generateKey();
        return key.getEncoded();
    }
}
