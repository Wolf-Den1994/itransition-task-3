package com.company;
import java.io.UnsupportedEncodingException;
import java.lang.String;
import java.security.*;
import java.util.*;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import javax.crypto.Mac;
import java.util.Scanner;

public class Main {

    public static String getHash(byte[] inputBytes, String algoritm) {
        String hashValue = "";
        try{
            MessageDigest messageDigest = MessageDigest.getInstance(algoritm);
            messageDigest.update(inputBytes);
            byte[] digestedBytes = messageDigest.digest();
            hashValue = DatatypeConverter.printHexBinary(digestedBytes);
        }
        catch (Exception e) {}
        return hashValue;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {

        for (byte i = 1; i < 100; i++) {
            Scanner in = new Scanner(System.in);
            String receivedValue = in.nextLine();

            String[] arrReceivedValue = receivedValue.split("\\s+");
            for (int j = 3; j < arrReceivedValue.length; j++) {
                arrReceivedValue[j] = arrReceivedValue[j].replaceAll("[^\\w]", "");
            }
            String[] arrReceivedValueRed = Arrays.copyOfRange(arrReceivedValue, 3, arrReceivedValue.length);
            int lenArr = arrReceivedValueRed.length;
            if (lenArr == 0) {
                System.out.println("Error! The number must be greater than or equal to 3! The number must be even! No repetitionsTry again:");
                i = 1;
            }
            else
            {
                for (int j = 0; j < arrReceivedValueRed.length; j++) {
                    for (int k = 0; k < arrReceivedValue.length; k++) {
                        if (arrReceivedValueRed[j] == arrReceivedValue[k]) i = 99;
                    }
                }

                SecureRandom randomKey = new SecureRandom();
                byte key[] = new byte[16];
                randomKey.nextBytes(key);

                StringBuilder builde = new StringBuilder();
                for (byte b : key) {
                    builde.append(String.format("%02X",b));
                }
                String keyForUser = builde.toString();

                SecureRandom randomStep = new SecureRandom();
                byte  preStep = (byte) (1 + randomStep.nextInt(lenArr));
                String step = Integer. toString(preStep);

                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(key, "HmacSHA256"));
                byte[] preHmac = mac.doFinal(step.getBytes());
                StringBuilder builder = new StringBuilder();
                for (byte b : preHmac) {
                    builder.append(String.format("%02X",b));
                }
                String hmac = builder.toString();

                if (lenArr >= 3 && lenArr % 2 != 0)
                {
                    System.out.println("HMAC:");
                    System.out.println(hmac);
                    System.out.println("Available moves:");
                    for (int k = 0; k < lenArr; k++) {
                        System.out.println(k+1 + " - " + arrReceivedValueRed[k]);
                    }
                    System.out.println("0 - exit");
                    System.out.print("Enter your move:");
                    int stepHuman = in.nextInt();
                    if (stepHuman == 0)
                    {
                        System.out.println("Bye bye!");
                        i = 99;
                    }
                    else
                    {
                        System.out.println("Your move:" + arrReceivedValueRed[stepHuman - 1]);
                        System.out.println("Computer move:" + arrReceivedValueRed[preStep - 1]);

                        // arrReceivedValueRed stepHuman  preStep
                        double numberLength = Math.floor(lenArr / 2);

                        int userIndex = 0;
                        for (int m = 0; m < arrReceivedValueRed.length; m++) {
                            if (m == stepHuman - 1) userIndex = m;
                        }
                        int aiIndex = 0;
                        for (int m = 0; m < arrReceivedValueRed.length; m++) {
                            if (m == preStep - 1) aiIndex = m;
                        }

                        boolean userWin = false;
                        int flag = 0;
                        if (userIndex == aiIndex)
                        {
                            System.out.println("Draw!");
                        } else {
                            for (int k = 0; k <= numberLength; k++) {
                                if (userIndex + k >= arrReceivedValueRed.length - 1) {
                                    if (aiIndex == flag) {
                                        userWin = true;
                                    }
                                    flag++;
                                }
                                if (aiIndex == userIndex + k) {
                                    userWin = true;
                                }
                            }
                            if (userWin == true)
                            {
                                System.out.println("You lose!");
                            }
                            else
                            {
                                System.out.println("You win!");
                            }
                        }

                        System.out.println("HMAC key:");
                        System.out.println(keyForUser);
                        i = 99;
                    }
                }
                else
                {
                    System.out.println("Error! The number must be greater than or equal to 3! The number must be even! No repetitionsTry again:");
                    i = 1;
                }
            }
        }
    }
}

