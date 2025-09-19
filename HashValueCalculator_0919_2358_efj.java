// 代码生成时间: 2025-09-19 23:58:15
package com.example.hashvaluecalculator;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * Main class for the hash value calculator utility.
 */
@QuarkusMain
public class HashValueCalculator implements QuarkusApplication {

    @Override
    public int run(String... args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a string to calculate its hash value: ");
        String input = scanner.nextLine();
        String hashValue = calculateHash(input);
        System.out.println("The hash value of the input is: " + hashValue);
        return 0;
    }

    /**
     * Calculates the hash value of the given string using SHA-256.
     *
     * @param input The input string to calculate the hash for.
     * @return The hash value as a hexadecimal string.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     */
    public static String calculateHash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder(2 * hashBytes.length);
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Main method for standalone execution.
     */
    public static void main(String... args) {
        HashValueCalculator calculator = new HashValueCalculator();
        calculator.run(args);
    }
}
