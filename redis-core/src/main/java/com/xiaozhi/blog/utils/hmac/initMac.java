package com.xiaozhi.blog.utils.hmac;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import com.xiaozhi.blog.utils.MD5;

/**
 * This program demonstrates how to generate a secret-key object for
 * HMAC-MD5, and initialize an HMAC-MD5 object with it.
 */

public class initMac {

    public static void main(String[] args) throws Exception {

        // Generate secret key for HMAC-MD5
        KeyGenerator kg = KeyGenerator.getInstance("HmacMD5");
        SecretKey sk = kg.generateKey();


        // Get instance of Mac object implementing HMAC-MD5, and
        // initialize it with the above secret key
        Mac mac = Mac.getInstance("HmacMD5");
        mac.init(sk);
        byte[] result = mac.doFinal("Hi There".getBytes());
        System.out.println(MD5.byte2hex(result));
    }



    public byte[] hmacMD5(byte[] key, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacMD5");
        mac.init(new SecretKeySpec(key, "HmacMD5"));
        mac.update(data);
        return mac.doFinal();
    }





}