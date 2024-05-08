package com.scp.view.module.api.robot;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ApiEncryptUtil {

    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    public static final String DES_ENCRYPTION_SCHEME = "DES";
    public static final String SUB_ENCRYPTION_KEY = "6b1fa0b8-b06c-4f5f-8071-685e2242";
    public static final String DEFAULT_ENCRYPTION_KEY = "237c09f4-cfbb-4346-b920-dda59615";

    private KeySpec keySpec;
    private SecretKeyFactory keyFactory;
    private Cipher cipher;

    private static final String UNICODE_FORMAT = "UTF8";

    public ApiEncryptUtil() throws EncryptionException {
        this(DESEDE_ENCRYPTION_SCHEME, SUB_ENCRYPTION_KEY + DEFAULT_ENCRYPTION_KEY);
    }

    public ApiEncryptUtil(String encryptionScheme, String encryptionKey) throws EncryptionException {

        if (encryptionKey == null)
            throw new IllegalArgumentException("encryption key was null");
        if (encryptionKey.trim().length() < 24)
            throw new IllegalArgumentException("encryption key was less than 24 characters");

        try {
            byte[] keyAsBytes = encryptionKey.getBytes(UNICODE_FORMAT);
            keySpec = new DESedeKeySpec(keyAsBytes);

            if (encryptionScheme.equals(DESEDE_ENCRYPTION_SCHEME)) {
                keySpec = new DESedeKeySpec(keyAsBytes);
            } else if (encryptionScheme.equals(DES_ENCRYPTION_SCHEME)) {
                keySpec = new DESKeySpec(keyAsBytes);
            } else {
                throw new IllegalArgumentException("Encryption scheme not supported: " + encryptionScheme);
            }

            keyFactory = SecretKeyFactory.getInstance(encryptionScheme);
            cipher = Cipher.getInstance(encryptionScheme);

        } catch (InvalidKeyException e) {
            throw new EncryptionException(e);
        } catch (UnsupportedEncodingException e) {
            throw new EncryptionException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionException(e);
        } catch (NoSuchPaddingException e) {
            throw new EncryptionException(e);
        }

    }

    public String encrypt(String unencryptedString) throws EncryptionException {
        if (unencryptedString == null || unencryptedString.trim().length() == 0)
            throw new IllegalArgumentException("unencrypted string was null or empty");

        try {
            SecretKey key = keyFactory.generateSecret(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //byte[] cleartext = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] cleartext = unencryptedString.getBytes();
            byte[] ciphertext = cipher.doFinal(cleartext);

            BASE64Encoder base64encoder = new BASE64Encoder();
            String res=base64encoder.encode(ciphertext);
            return res.replace(System.getProperty("line.separator"),"");
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    public String decrypt(String encryptedString) throws EncryptionException {
        if (encryptedString == null || encryptedString.trim().length() <= 0)
            throw new IllegalArgumentException("encrypted string was null or empty");

        try {
            SecretKey key = keyFactory.generateSecret(keySpec);
            cipher.init(Cipher.DECRYPT_MODE, key);
            BASE64Decoder base64decoder = new BASE64Decoder();
            byte[] cleartext = base64decoder.decodeBuffer(encryptedString);

            byte[] ciphertext = cipher.doFinal(cleartext);
            //String a = a.getBytes(ciphertext,"UTF-8");
            return new String(ciphertext);
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    private static String bytes2String(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            stringBuffer.append((char) bytes[i]);
        }
        return stringBuffer.toString();
    }

    public static class EncryptionException extends Exception {
        public EncryptionException(Throwable t) {
            super(t);
        }
    }

    /**
     * @param args
     * @throws EncryptionException
     */
    public static void main(String[] args) throws EncryptionException {
        //test
        System.out.println("--->test encrypt and decrypt...");
        ApiEncryptUtil sysUtils = new ApiEncryptUtil();
        String originalStr="待加密码的用户名或者密码~!@#$%^&*()_+{}|:<>?,./;'[]`012ABC\"\\";
        String encryptStr=null;
        String descryptStr=null;

        System.out.println("original string:["+originalStr+"]");
        //加密
        encryptStr=sysUtils.encrypt(originalStr);
        System.out.println("encrypt  string:["+encryptStr+"]");

        //解密
        descryptStr=sysUtils.decrypt(encryptStr);
        System.out.println("decrypt  string:["+descryptStr+"]");


        System.out.println("--->test end.");

    }

}
