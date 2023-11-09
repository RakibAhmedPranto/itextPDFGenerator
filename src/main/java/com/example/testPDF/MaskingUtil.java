package com.example.testPDF;

public class MaskingUtil {
    public static String starMask(String cardNumber) {
        return cardNumber.substring(0, 6) + "******" + cardNumber.subSequence(12, 16);
    }

    public static String starMaskTransactionDescription(String cardNumber) {
        return "****" + cardNumber.subSequence(12, 16);
    }

    public static String xMasKWithSpace(String cardNumber){
        return cardNumber.substring(0, 4) + " XXXX XXXX " + cardNumber.substring(12);
    }

    public static String xMasKWithOutSpace(String cardNumber){
        return cardNumber.substring(0, 4) + "XXXXXXXX" + cardNumber.substring(12);
    }

    public static String xMaskAccountNo(String accNumber) {
        if (accNumber == null)
            return null;

        accNumber = accNumber.trim();

        if (accNumber.length() != 16 && accNumber.length() != 13)
            throw new IllegalArgumentException(accNumber + " is not a 13/16 digit number");

        if (accNumber.length() == 16)
            return accNumber.substring(0, 5) + "XXXXXX" + accNumber.substring(13);

        return accNumber.substring(0, 4) + "XXXXX" + accNumber.substring(11);
    }

    public static String xMaskCifNo(String cifNumber) {
        if (cifNumber == null)
            return null;

        cifNumber = cifNumber.trim();

        return cifNumber.substring(0, 2) + "XXX" + cifNumber.substring(4);
    }

    public static String starMaskAccountNo(String accNumber) {
        if (accNumber == null)
            return null;

        accNumber = accNumber.trim();

        if (accNumber.length() != 16 && accNumber.length() != 13)
            throw new IllegalArgumentException(accNumber + " is not a 13/16 digit number");

        if (accNumber.length() == 16)
            return accNumber.substring(0, 5) + "***" + accNumber.substring(13);

        return accNumber.substring(0, 4) + "**" + accNumber.substring(11);
    }
}
