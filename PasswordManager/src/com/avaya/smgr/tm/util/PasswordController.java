package com.avaya.smgr.tm.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class PasswordController {
    private final String numbers = "1234567890";
    private final String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
    private final String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String specialChars = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
    private final String[] passwordIndexOptions = {numbers, lowerAlphabet, upperAlphabet, specialChars};

    // generates a secure password of desired length.
    public String generatePassword(int length){
        Random random = new Random();
        String password = "";
        for (int i = length; i > 0; i--){
            int optionIndex = random.nextInt(passwordIndexOptions.length);
            String option = passwordIndexOptions[optionIndex];
            password += option.charAt(random.nextInt(option.length()));
        }
        return password;
    }


    //hashing method used to hash a password. Made for the master password for access to password manager.
    //master password should only be stored in database after hashed.
    // requires salt which can be get from the getSalt() method.
    public String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        String hashedPassword = "";
        try {
            MessageDigest hash = MessageDigest.getInstance("SHA-256");
            hash.update(salt);
            byte[] bytes = hash.digest(password.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (byte aByte : bytes) {
                stringBuilder.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            hashedPassword = stringBuilder.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }

    //generate a random salt to be used with hashing method
    public byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
}
