package com.nikhil.Functions;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import android.util.Base64;

    public class AES {
    //Sample Code
    public static void main(String[] args)  throws Exception{
        String msg = "123456";
        String keyStr = "abcdef";
        String ivStr = "ABCDEF";

        byte[] msg_byte = msg.getBytes(StandardCharsets.UTF_8);
        System.out.println("Before Encrypt: " + msg);

        byte[] ans = AES.encrypt(ivStr, keyStr, msg.getBytes());
        System.out.println("After Encrypt: " + new String(ans, StandardCharsets.UTF_8));

        String ansBase64 = AES.encryptStrAndToBase64(ivStr, keyStr, msg);
        System.out.println("After Encrypt & To Base64: " + ansBase64);

        byte[] deans = AES.decrypt(ivStr, keyStr, ans);
        System.out.println("After Decrypt: " + new String(deans, StandardCharsets.UTF_8));

        String deansBase64 = AES.decryptStrAndFromBase64(ivStr, keyStr, ansBase64);
        System.out.println("After Decrypt & From Base64: " + deansBase64);}

        public static byte[] encrypt(String ivStr, String keyStr, byte[] bytes) throws Exception{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(ivStr.getBytes());
            byte[] ivBytes = md.digest();

            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(keyStr.getBytes());
            byte[] keyBytes = sha.digest();

            return encrypt(ivBytes, keyBytes, bytes);
        }

        static byte[] encrypt(byte[] ivBytes, byte[] keyBytes, byte[] bytes) throws Exception{
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
            return cipher.doFinal(bytes);
        }

        public static byte[] decrypt(String ivStr, String keyStr, byte[] bytes) throws Exception{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(ivStr.getBytes());
            byte[] ivBytes = md.digest();

            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(keyStr.getBytes());
            byte[] keyBytes = sha.digest();

            return decrypt(ivBytes, keyBytes, bytes);
        }

        static byte[] decrypt(byte[] ivBytes, byte[] keyBytes, byte[] bytes)  throws Exception{
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
            return cipher.doFinal(bytes);
        }

        public static String encryptStrAndToBase64(String ivStr, String keyStr, String enStr) throws Exception{
            byte[] bytes = encrypt(keyStr, keyStr, enStr.getBytes(StandardCharsets.UTF_8));
            return new String(Base64.encode(bytes ,Base64.DEFAULT), StandardCharsets.UTF_8);
        }

        public static String decryptStrAndFromBase64(String ivStr, String keyStr, String deStr) throws Exception{
            byte[] bytes = decrypt(keyStr, keyStr, Base64.decode(deStr.getBytes(StandardCharsets.UTF_8),Base64.DEFAULT));

            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

