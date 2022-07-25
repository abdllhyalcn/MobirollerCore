package com.mobiroller.core.helpers;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AesBase64Wrapper {

    private static String IV = "kd25gbk4wdcksc49";
    private static String COMBINED_STRING = "5J4Pjqzzmf4j9NTx";
    private static String SALT = "E56634A68AD9353D";


    public String encryptAndEncode(String raw) {
        try {
            Cipher c = getCipher(Cipher.ENCRYPT_MODE);
            byte[] encryptedVal = c.doFinal(getBytes(raw));
            String s = getString(Base64.encodeBase64(encryptedVal));
            return s;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }


    public String decodeAndDecryptISO(String encrypted) throws Exception {
        byte[] decodedValue = Base64.decodeBase64(getBytes(encrypted));
        Cipher c = getCipherISO(Cipher.DECRYPT_MODE);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }

    public String decodeAndDecrypt(String encrypted) throws Exception {
        byte[] decodedValue = Base64.decodeBase64(getBytes(encrypted));
        Cipher c = getCipher(Cipher.DECRYPT_MODE);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }

    private String getString(byte[] bytes) throws UnsupportedEncodingException {
        return new String(bytes, "UTF-8");
    }

    private byte[] getBytes(String str) throws UnsupportedEncodingException {
        return str.getBytes("UTF-8");
    }

    private Cipher getCipherISO(int mode) throws Exception {
        Cipher c = Cipher.getInstance("AES/CBC/ISO10126Padding");
        byte[] iv = getBytes(IV);
        c.init(mode, generateKey(), new IvParameterSpec(iv));
        return c;
    }

    private Cipher getCipher(int mode) throws Exception {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = getBytes(IV);
        c.init(mode, generateKey(), new IvParameterSpec(iv));
        return c;
    }


    public static String decryptKeyISO(String key) {
        try {
            return new AesBase64Wrapper().decodeAndDecryptISO(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptKey(String key) {
        try {
            return new AesBase64Wrapper().decodeAndDecrypt(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decodeBase64(String key) {
        try {
            return new String(android.util.Base64.decode(key, android.util.Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private Key generateKey() throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        char[] password = COMBINED_STRING.toCharArray();
        byte[] salt = getBytes(SALT);

        KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        byte[] encoded = tmp.getEncoded();
        return new SecretKeySpec(encoded, "AES");
    }

}
