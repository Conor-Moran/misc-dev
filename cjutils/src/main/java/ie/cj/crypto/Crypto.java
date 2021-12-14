package ie.cj.crypto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class Crypto {
    public static void main(String[] args) throws Exception {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        SecretKey aesKey1 = keygen.generateKey();
        SecretKey aesKey2 = keygen.generateKey();

        Cipher aesCipher1;
        Cipher aesCipher2;

        // Create the cipher
        aesCipher1 = Cipher.getInstance("AES/ECB/PKCS5Padding");
        aesCipher2 = Cipher.getInstance("AES/ECB/PKCS5Padding");

        // Initialize the cipher for encryption
        aesCipher1.init(Cipher.ENCRYPT_MODE, aesKey1);
        aesCipher2.init(Cipher.ENCRYPT_MODE, aesKey2);

        // Our cleartext
        byte[] cleartext = "This is just an example".getBytes();

        // Encrypt the cleartext
        byte[] ciphertext1 = aesCipher1.doFinal(cleartext);
        byte[] ciphertext2 = aesCipher2.doFinal(ciphertext1);

        System.out.println(new String(ciphertext2));
        // Initialize the same cipher for decryption
        aesCipher1.init(Cipher.DECRYPT_MODE, aesKey1);
        aesCipher2.init(Cipher.DECRYPT_MODE, aesKey2);

        // Decrypt the ciphertext
        byte[] tmp = aesCipher2.doFinal(ciphertext2);
        tmp  = aesCipher1.doFinal(tmp);
        System.out.println(new String(tmp));
    }


}
