package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

// 受け取った文字列の暗号化・引数がなければ、空の文字列を返す
public class EncryptUtil {
    public static String getPasswordEncrypt(String plain_p, String salt) {
        String ret = "";

        if(plain_p != null && !plain_p.equals("")) {
            byte[] bytes;
            String password = plain_p + salt;
            try{
                bytes = MessageDigest.getInstance("SHA-256").digest(password.getBytes());
                ret = DatatypeConverter.printHexBinary(bytes);
            } catch(NoSuchAlgorithmException ex) {}
        }
        return ret;
    }

}
