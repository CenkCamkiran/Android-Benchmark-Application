package com.example.androidbenchmarkapp;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    private String data = "John Doe";
    private String encryptedValue;
    private String decryptedValue;

    public double start, finish, elapsedTime;

    protected double encrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        start = System.nanoTime();

        SecretKeySpec key = generateKey(data);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encodedValue = cipher.doFinal(data.getBytes());
        encryptedValue = Base64.encodeToString(encodedValue, Base64.DEFAULT);

        finish = System.nanoTime();
        elapsedTime = (finish - start) / 1000000;

        Log.d("encrypted", encryptedValue);

        return elapsedTime;
    }

    protected double decrypt() throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        start = System.nanoTime();

        SecretKeySpec key = generateKey(data);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.decode(encryptedValue, Base64.DEFAULT);
        byte[] decValue = cipher.doFinal(decodedValue);
        decryptedValue = new String(decValue);

        finish = System.nanoTime();

        elapsedTime = (finish - start) / 1000000;
        Log.d("decrypted", decryptedValue);

        return elapsedTime;
    }

    private SecretKeySpec generateKey(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = data.getBytes("UTF-8");
        messageDigest.update(bytes, 0, bytes.length);
        byte[] key = messageDigest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

        return secretKeySpec;
    }

}
