package com.tks.gwa.utils;

public class LuhnAlgorithm {

    public static boolean validateCreditCardNumber(String str)  {
        boolean check = false;
        int[] ints = new int[str.length()];
        try{
            for (int i = 0; i < str.length(); i++) {
                ints[i] = Integer.parseInt(str.substring(i, i + 1));
            }
            for (int i = ints.length - 2; i >= 0; i = i - 2) {
                int j = ints[i];
                j = j * 2;
                if (j > 9) {
                    j = j % 10 + 1;
                }
                ints[i] = j;
            }
            int sum = 0;
            for (int i = 0; i < ints.length; i++) {
                sum += ints[i];
            }
            if (sum % 10 == 0) {
                System.out.println(str + " is a valid credit card number");
                check = true;
            } else {
                System.out.println(str + " is an invalid credit card number");
                check = false;
            }
        } catch (NumberFormatException e){
            check = false;
            return check;
        }

        return check;
    }
}
