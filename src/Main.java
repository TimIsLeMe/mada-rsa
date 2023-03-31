import java.math.BigInteger;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        RSA rsa = new RSA();
        rsa.setPrivateKey();
        String decryptedMessage = rsa.decrypt(String.valueOf(rsa.readFile("chiffre.txt")));
        rsa.writeFile("text-d.txt", decryptedMessage);
    }
}