package com.systemofmonitoring.login;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CheckUsersDatas {
    public CheckUsersDatas() {}

    public String makeHash(String pass) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(pass.getBytes(), 0, pass.length());
            return new BigInteger(1, md5.digest()).toString(16);

        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean check() {
        return true;
    }
}
