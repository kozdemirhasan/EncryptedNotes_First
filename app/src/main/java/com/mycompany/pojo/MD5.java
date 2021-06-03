package com.mycompany.pojo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Casper on 17.01.2018.
 */

public class MD5 {

    public static String md5Sifrele(String parola) {

        try {
            MessageDigest messageDigestNesnesi = MessageDigest.getInstance("MD5");
            messageDigestNesnesi.update(parola.getBytes());
            byte messageDigestDizisi[] = messageDigestNesnesi.digest();
         //   StringBuffer sb16 = new StringBuffer();
            StringBuffer sb32 = new StringBuffer();
            for (int i = 0; i < messageDigestDizisi.length; i++) {
             //   sb16.append(Integer.toString((messageDigestDizisi[i] & 0xff) + 0x100, 16).substring(1));
                sb32.append(Integer.toString((messageDigestDizisi[i] & 0xff) + 0x100, 32));
            }
          //  System.out.println("Parolanın Şifrelenmiş Hali:(16) " + sb16.toString());
          //  System.out.println("Parolanın Şifrelenmiş Hali:(32) " + sb32.toString());
            return sb32.toString();

        } catch (NoSuchAlgorithmException ex) {
            System.err.println(ex);
            return  null;
        }

    }
}

