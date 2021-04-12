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

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        for (;;) {
            Scanner in = new Scanner(System.in);
            int lenArr = args.length;
            if (lenArr == 0 || lenArr < 3 || lenArr % 2 == 0) {
                System.out.println("Error! The number must be greater than or equal to 3! The number must be even! No repetitionsTry again:");
                break;
            }
            else
            {
                int flagRepiat = 0;
                for (int j = 0; j < args.length; j++) {
                    for (int k = j + 1; k < args.length; k++) {
                        if (args[j].equals(args[k])) flagRepiat = 1;
                    }
                }
                if (flagRepiat == 1) {
                    System.out.println("Error! The number must be greater than or equal to 3! The number must be even! No repetitionsTry again:");
                    break;
                }
                else
                {
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

                    System.out.println("HMAC:");
                    System.out.println(hmac);
                    System.out.println("Available moves:");
                    for (int k = 0; k < lenArr; k++) {
                        System.out.println(k+1 + " - " + args[k]);
                    }
                    System.out.println("0 - exit");
                    System.out.print("Enter your move:");
                    int stepHuman = in.nextInt();
                    if (stepHuman == 0)
                    {
                        System.out.println("Bye bye!");
                        break;
                    }
                    else
                    {
                        System.out.println("Your move:" + args[stepHuman - 1]);
                        System.out.println("Computer move:" + args[preStep - 1]);

                        double numberLength = Math.floor(lenArr / 2);

                        int userIndex = 0;
                        for (int m = 0; m < args.length; m++) {
                            if (m == stepHuman - 1) userIndex = m;
                        }
                        int aiIndex = 0;
                        for (int m = 0; m < args.length; m++) {
                            if (m == preStep - 1) aiIndex = m;
                        }

                        boolean userWin = false;
                        int flag = 0;
                        if (userIndex == aiIndex)
                        {
                            System.out.println("Draw!");
                        } else {
                            for (int k = 0; k <= numberLength; k++) {
                                if (userIndex + k >= args.length - 1) {
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
                        System.out.println(keyForUser + "\n");
                    }
                }
            }
        }
    }
}

